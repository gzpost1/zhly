package cn.cuiot.dmp.system.application.service.initialize;

import cn.cuiot.dmp.base.infrastructure.dto.companyinit.CommonOptionSettingSyncDTO;
import cn.cuiot.dmp.base.infrastructure.dto.companyinit.CommonOptionSyncDTO;
import cn.cuiot.dmp.base.infrastructure.dto.companyinit.SyncCompanyDTO;
import cn.cuiot.dmp.base.application.service.DataSyncService;
import cn.cuiot.dmp.base.infrastructure.utils.RedisUtil;
import cn.cuiot.dmp.common.utils.Const;
import cn.cuiot.dmp.common.utils.JsonUtil;
import cn.cuiot.dmp.common.utils.TreeUtil;
import cn.cuiot.dmp.system.application.constant.FormConfigConstant;
import cn.cuiot.dmp.base.infrastructure.dto.companyinit.SyncCompanyRelationDTO;
import cn.cuiot.dmp.system.application.param.vo.FormConfigTypeTreeNodeVO;
import cn.cuiot.dmp.system.infrastructure.entity.*;
import cn.cuiot.dmp.system.infrastructure.persistence.mapper.FormConfigMapper;
import cn.cuiot.dmp.system.infrastructure.persistence.mapper.FormConfigTypeMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

import static cn.cuiot.dmp.base.infrastructure.dto.companyinit.SyncCompanyCacheConstant.*;

/**
 * 同步企业表单配置
 *
 * @Author: zc
 * @Date: 2024-11-06
 */
@Slf4j
@Component
public class FormConfigSyncService extends DataSyncService<FormConfigTypeEntity> {

    @Resource
    private FormConfigTypeMapper formConfigTypeMapper;
    @Resource
    private FormConfigMapper formConfigMapper;
    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private RedisUtil redisUtil;

    @Override
    public List<FormConfigTypeEntity> fetchData(Long sourceCompanyId) {
        List<FormConfigTypeEntity> list = formConfigTypeMapper.selectList(
                new LambdaQueryWrapper<FormConfigTypeEntity>()
                        .eq(FormConfigTypeEntity::getCompanyId, sourceCompanyId));
        if (CollectionUtils.isEmpty(list)) {
            log.error("企业初始化【表单配置】异常，模板企业不存在数据");
        }
        return list;
    }

    @Override
    public List<SyncCompanyRelationDTO<FormConfigTypeEntity>> preprocessData(List<FormConfigTypeEntity> data, Long targetCompanyId) {

        Map<Long, FormConfigTypeTreeNodeVO> map = buildFormMap(data);

        return data.stream().map(item -> {
            FormConfigTypeTreeNodeVO nodeVO = map.get(item.getId());
            FormConfigTypeEntity entity = new FormConfigTypeEntity();
            entity.setId(Long.parseLong(nodeVO.getId()));
            entity.setParentId(Long.parseLong(nodeVO.getParentId()));
            entity.setName(item.getName());
            entity.setLevelType(item.getLevelType());
            entity.setCompanyId(targetCompanyId);
            entity.setPathName(item.getPathName());
            entity.setInitFlag(item.getInitFlag());
            entity.setDeletedFlag(item.getDeletedFlag());

            return new SyncCompanyRelationDTO<>(entity, item.getId());
        }).collect(Collectors.toList());
    }

    @Override
    public void saveData(List<SyncCompanyRelationDTO<FormConfigTypeEntity>> data, Long targetCompanyId) {
        // 表单配置-表单管理分类表
        data.forEach(item -> formConfigTypeMapper.insert(item.getEntity()));
    }

    /**
     * 构造表单新老数据对照
     */
    public static Map<Long, FormConfigTypeTreeNodeVO> buildFormMap(List<FormConfigTypeEntity> data) {
        List<FormConfigTypeTreeNodeVO> formConfigTypeTreeNodeVOList = data.stream()
                .map(parent -> new FormConfigTypeTreeNodeVO(
                        parent.getId().toString(), parent.getParentId().toString(),
                        parent.getName(), parent.getLevelType(), parent.getCompanyId()))
                .collect(Collectors.toList());
        List<FormConfigTypeTreeNodeVO> tree = TreeUtil.makeTree(formConfigTypeTreeNodeVOList);
        Map<Long, FormConfigTypeTreeNodeVO> map = Maps.newHashMap();
        updateIds(tree, null, map);
        return map;
    }

    /**
     * 递归修改表单id
     */
    private static void updateIds(List<FormConfigTypeTreeNodeVO> nodes, String parentId, Map<Long, FormConfigTypeTreeNodeVO> map) {
        for (FormConfigTypeTreeNodeVO node : nodes) {
            // 更新当前节点的 ID 和 Parent ID
            Long oldId = Long.parseLong(node.getId());
            String newId = String.valueOf(IdWorker.getId());
            node.setId(newId);
            if (StringUtils.isNotBlank(parentId)) {
                node.setParentId(parentId);
            }

            // 添加到 map 中，并清空子节点引用
            map.put(oldId, new FormConfigTypeTreeNodeVO(newId, node.getParentId(), node.getName(), node.getLevelType(), node.getCompanyId()));

            // 递归更新子节点
            if (CollectionUtils.isNotEmpty(node.getChildren())) {
                updateIds(node.getChildren(), newId, map);
            }
        }
    }

    @Override
    public void syncAssociatedData(List<SyncCompanyRelationDTO<FormConfigTypeEntity>> targetData, SyncCompanyDTO dto) {
        Long sourceCompanyId = dto.getSourceCompanyId();
        Long targetCompanyId = dto.getTargetCompanyId();

        List<FormConfigEntity> formConfigEntities = formConfigMapper.selectList(
                new LambdaQueryWrapper<FormConfigEntity>()
                        .eq(FormConfigEntity::getCompanyId, sourceCompanyId));

        if (CollectionUtils.isNotEmpty(formConfigEntities)) {
            // 用于设置缓存值
            List<SyncCompanyRelationDTO<FormConfigEntity>> formConfigList = Lists.newArrayList();
            // 需要缓存的数据
            List<SyncCompanyRelationDTO<FormConfigDetailEntity>> formConfigDetailList = Lists.newArrayList();

            Map<Long, FormConfigTypeEntity> map = targetData.stream().collect(Collectors.toMap(SyncCompanyRelationDTO::getOldId, SyncCompanyRelationDTO::getEntity));
            formConfigEntities.forEach(item -> {
                Long oldId = item.getId();
                FormConfigEntity configEntity = new FormConfigEntity();
                configEntity.setId(IdWorker.getId());
                configEntity.setName(item.getName());
                configEntity.setCompanyId(targetCompanyId);
                configEntity.setTypeId(map.get(item.getTypeId()).getId());
                configEntity.setStatus(item.getStatus());
                configEntity.setDeletedFlag(item.getDeletedFlag());

                // 保存表单配置-表单管理表
                formConfigMapper.insert(configEntity);

                // 保存表单
                FormConfigDetailEntity mongoDetail = mongoTemplate.findById(item.getId(), FormConfigDetailEntity.class, FormConfigConstant.FORM_CONFIG_COLLECTION);
                if (Objects.nonNull(mongoDetail)) {
                    Long oldDetailId = mongoDetail.getId();
                    FormConfigDetailEntity formConfigDetailEntity = new FormConfigDetailEntity();
                    formConfigDetailEntity.setFormConfigDetail(handleJson(mongoDetail.getFormConfigDetail(), targetCompanyId));
                    formConfigDetailEntity.setId(configEntity.getId());
                    mongoTemplate.save(formConfigDetailEntity, FormConfigConstant.FORM_CONFIG_COLLECTION);

                    formConfigDetailList.add(new SyncCompanyRelationDTO<>(formConfigDetailEntity, oldDetailId));
                }
                formConfigList.add(new SyncCompanyRelationDTO<>(configEntity, oldId));
            });

            // 设置缓存数据
            if (CollectionUtils.isNotEmpty(formConfigList)) {
                redisUtil.set(COMPANY_INITIALIZE + targetCompanyId + ":" + FORM_CONFIG, JsonUtil.writeValueAsString(formConfigList), Const.ONE_DAY_SECOND);
            }
            // 设置缓存数据
            if (CollectionUtils.isNotEmpty(formConfigDetailList)) {
                redisUtil.set(COMPANY_INITIALIZE + targetCompanyId + ":" + FORM_CONFIG_DETAIL, JsonUtil.writeValueAsString(formConfigDetailList), Const.ONE_DAY_SECOND);
            }
        }
    }

    /**
     * 处理json数据
     *
     * @return String
     * @Param formConfigDetail 表单数据
     */
    private String handleJson(String formConfigDetail, Long targetCompanyId) {
        ObjectMapper mapper = new ObjectMapper();
        // 目标 name 值
        List<String> targetNames = Arrays.asList("CommonOptionsSelect", "CommonOptionsSelectMultiple");
        JsonNode root;
        try {
            root = mapper.readTree(formConfigDetail);
        } catch (JsonProcessingException e) {
            log.error("企业初始化【表单配置】异常，表单对象转换失败");
            return formConfigDetail;
        }

        if (Objects.nonNull(root)) {
            Map<Long, CommonOptionSettingSyncDTO> optionSettingMap = getCommonOptionSettingMap(targetCompanyId);
            Map<Long, CommonOptionSyncDTO> optionMap = getCommonOptionMap(targetCompanyId);
            // 修改 options 中的 id
            modifyOptionsIdsByName(root, targetNames, optionSettingMap, optionMap);
        }

        // 将修改后的 JSON 转回字符串
        try {
            return mapper.writeValueAsString(root);
        } catch (JsonProcessingException e) {
            log.error("将修改后的表单数据转换为字符串失败", e);
            return formConfigDetail;
        }
    }

    private static void modifyOptionsIdsByName(JsonNode node, List<String> targetNames, Map<Long, CommonOptionSettingSyncDTO> optionSettingMap,
                                               Map<Long, CommonOptionSyncDTO> optionMap) {
        if (node == null) {
            return;
        }

        if (node.isObject()) {
            handleObjectNode((ObjectNode) node, targetNames, optionSettingMap, optionMap);
        } else if (node.isArray()) {
            // 递归检查数组中的每个元素
            node.forEach(item -> modifyOptionsIdsByName(item, targetNames, optionSettingMap, optionMap));
        }
    }

    private static void handleObjectNode(ObjectNode objectNode, List<String> targetNames, Map<Long, CommonOptionSettingSyncDTO> optionSettingMap,
                                         Map<Long, CommonOptionSyncDTO> optionMap) {
        if (objectNode.has("name") && targetNames.contains(objectNode.get("name").asText())) {
            JsonNode propsNode = objectNode.get("props");
            if (propsNode != null && propsNode.isObject()) {
                ObjectNode propsObject = (ObjectNode) propsNode;
                modifyTypeId(propsObject, optionMap);
                modifyOptions(propsObject, optionSettingMap);
                modifyAnswers(propsObject, optionSettingMap);
            }
        }

        // 递归处理对象的其他字段
        objectNode.fields().forEachRemaining(entry -> modifyOptionsIdsByName(entry.getValue(), targetNames, optionSettingMap, optionMap));
    }

    private static void modifyTypeId(ObjectNode propsObject, Map<Long, CommonOptionSyncDTO> optionMap) {
        if (propsObject.has("typeId")) {
            long typeId = propsObject.get("typeId").asLong();
            // 通过 Optional 处理空值，并将结果设置回 typeId
            String value = Optional.ofNullable(optionMap.get(typeId))
                    .map(CommonOptionSyncDTO::getId)
                    .map(String::valueOf)
                    .orElse("");
            propsObject.put("typeId", value);
        }
    }

    private static void modifyOptions(ObjectNode propsObject, Map<Long, CommonOptionSettingSyncDTO> optionSettingMap) {
        if (propsObject.has("options") && propsObject.get("options").isArray()) {
            ArrayNode optionsArray = (ArrayNode) propsObject.get("options");
            // 创建新的 ArrayNode，用于存储修改后的选项
            ArrayNode filteredArray = optionsArray.arrayNode();

            for (JsonNode option : optionsArray) {
                if (option.has("id") && option.isObject()) {
                    ObjectNode optionObject = (ObjectNode) option;
                    long id = optionObject.get("id").asLong();
                    // 检查 id 是否在 optionSettingMap 中
                    if (optionSettingMap.containsKey(id)) {
                        CommonOptionSettingSyncDTO dto = optionSettingMap.get(id);
                        if (dto != null) {
                            // 替换 id 值
                            optionObject.put("id", String.valueOf(dto.getId()));
                        }
                    }
                    // 将修改后的 option 添加到过滤后的数组中
                    filteredArray.add(optionObject.deepCopy());  // 使用 deepCopy 以确保不影响原始数组
                }
            }
            // 将过滤后的数组设置回 propsObject 的 options 字段
            propsObject.set("options", filteredArray);
        }
    }

    private static void modifyAnswers(ObjectNode propsObject, Map<Long, CommonOptionSettingSyncDTO> optionSettingMap) {
        if (propsObject.has("answer") && propsObject.get("answer").isArray()) {
            ArrayNode answerArray = (ArrayNode) propsObject.get("answer");
            // 新建一个 ArrayNode 用于存储非空字符串的元素
            ArrayNode filteredArray = answerArray.arrayNode();
            for (int i = 0; i < answerArray.size(); i++) {
                long answer = answerArray.get(i).asLong();
                if (optionSettingMap.containsKey(answer)) {
                    CommonOptionSettingSyncDTO dto = optionSettingMap.get(answer);
                    if (Objects.nonNull(dto)) {
                        filteredArray.add(dto.getId() + "");
                    }
                }
            }
            // 将非空元素的 filteredArray 设置回 propsObject 的 answer 字段
            propsObject.set("answer", filteredArray);
        }
    }

    /**
     * 获取选项值信息缓存数据
     *
     * @return Map
     * @Param targetCompanyId 企业id
     */
    private Map<Long, CommonOptionSettingSyncDTO> getCommonOptionSettingMap(Long targetCompanyId) {
        String redisJson = redisUtil.get(COMPANY_INITIALIZE + targetCompanyId + ":" + COMMON_OPTION_SETTING);

        if (StringUtils.isNotBlank(redisJson)) {
            List<SyncCompanyRelationDTO<CommonOptionSettingSyncDTO>> beans = JsonUtil.readValue(redisJson,
                    new TypeReference<List<SyncCompanyRelationDTO<CommonOptionSettingSyncDTO>>>() {
                    });
            if (CollectionUtils.isNotEmpty(beans)) {
                return beans.stream().collect(Collectors.toMap(SyncCompanyRelationDTO::getOldId, SyncCompanyRelationDTO::getEntity));
            }
        }
        return Maps.newHashMap();
    }

    /**
     * 获取选项类型信息缓存数据
     *
     * @return Map
     * @Param targetCompanyId 企业id
     */
    private Map<Long, CommonOptionSyncDTO> getCommonOptionMap(Long targetCompanyId) {
        String redisJson = redisUtil.get(COMPANY_INITIALIZE + targetCompanyId + ":" + COMMON_OPTION);

        if (StringUtils.isNotBlank(redisJson)) {
            List<SyncCompanyRelationDTO<CommonOptionSyncDTO>> beans = JsonUtil.readValue(redisJson,
                    new TypeReference<List<SyncCompanyRelationDTO<CommonOptionSyncDTO>>>() {
                    });
            if (CollectionUtils.isNotEmpty(beans)) {
                return beans.stream().collect(Collectors.toMap(SyncCompanyRelationDTO::getOldId, SyncCompanyRelationDTO::getEntity));
            }
        }
        return Maps.newHashMap();
    }
}
