package cn.cuiot.dmp.system.infrastructure.persistence.dao;

import cn.cuiot.dmp.system.infrastructure.entity.MenuEntity;
import cn.cuiot.dmp.system.infrastructure.entity.dto.MenuByOrgTypeIdResDto;
import cn.cuiot.dmp.system.infrastructure.entity.dto.MenuQuery;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author wangyh
 * @className MenuDao
 * @description
 * @date 2020-08-31 19:40:07
 */
@Mapper
public interface MenuDao {

	/**
	 * 按照角色Id查询菜单
	 *
	 * @param roleId
	 * @return
	 */
	List<MenuEntity> selectMenuListByRoleId(@Param("roleId") String roleId);

	/**
	 * 查询角色菜单
	 * @param roleId
	 * @return
	 */
	List<MenuEntity> getAllRoleMenu(@Param("roleId") String roleId);

	/**
	 * 查询企业菜单
	 */
	List<MenuEntity> selectMenuByOrgId(@Param("orgId") Long orgId);

	/**
	 * 查询企业类型菜单
	 */
	List<MenuEntity> selectMenuByOrgTypeId(@Param("orgTypeId") Long orgTypeId);

	/**
	 * 查询菜单
	 *
	 * @param menuName
	 * @return
	 */
	List<MenuEntity> selectMenu(String menuName);

	/**
	 * 修改菜单
	 *
	 * @param menuEntity
	 * @return
	 */
	int updateMenu(MenuEntity menuEntity);

	/**
	 * 删除菜单
	 *
	 * @param id
	 * @return
	 */
	int deleteMenuById(@Param("id") Long id);

	/**
	 * 添加一级菜单
	 *
	 * @param entity
	 * @return
	 */
	int insertMenu(MenuEntity entity);

	/**
	 * 自动id
	 *
	 * @param parentId
	 * @return
	 */
	List<Long> getMenuIdByParentId(Long parentId);

	/**
	 * 通过typeId获取菜单
	 * @param orgTypeId
	 * @return
	 */
    List<MenuByOrgTypeIdResDto> getMenuByOrgTypeId(@Param("orgTypeId") String orgTypeId);


	List<MenuEntity> queryForList(@Param("param") MenuQuery query);


	List<MenuEntity> selectChildList(@Param("parentId") Long parentId);

	MenuEntity getById(@Param("id") Long id);
}
