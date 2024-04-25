package cn.cuiot.dmp.system.application.service.impl;

import cn.cuiot.dmp.common.constant.ResultCode;
import cn.cuiot.dmp.common.exception.BusinessException;
import cn.cuiot.dmp.domain.types.id.UserId;
import cn.cuiot.dmp.system.application.service.INoticeService;
import cn.cuiot.dmp.system.infrastructure.entity.Notice;
import cn.cuiot.dmp.system.infrastructure.entity.NoticeUserAccess;
import cn.cuiot.dmp.base.infrastructure.dto.DepartmentDto;
import cn.cuiot.dmp.system.infrastructure.entity.dto.NoticeDto;
import cn.cuiot.dmp.system.infrastructure.entity.dto.NoticeUserAccessDto;
import cn.cuiot.dmp.system.infrastructure.persistence.dao.DepartmentDao;
import cn.cuiot.dmp.system.infrastructure.persistence.dao.NoticeDao;
import cn.cuiot.dmp.system.infrastructure.persistence.dao.NoticeUserAccessDao;
import cn.cuiot.dmp.system.user_manage.domain.entity.User;
import cn.cuiot.dmp.system.user_manage.query.UserCommonQuery;
import cn.cuiot.dmp.system.user_manage.repository.UserRepository;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * 公告信息Service业务层处理
 * 
 * @author zhangxp207
 * @date 2022-08-05
 */
@Service
public class NoticeServiceImpl implements INoticeService
{
    public static final String DATE_PATTERN_YYYY_MM_DD_HH_MM = "yyyy-MM-dd HH:mm";
    @Resource
    private NoticeDao noticeDao;
    @Resource
    private DepartmentDao departmentDao;
    @Resource
    private NoticeUserAccessDao noticeUserAccessDao;

    @Resource
    private UserRepository userRepository;

    /**
     * 查询公告信息列表
     * 
     * @param notice 公告信息
     * @return 公告信息
     */
    @Override
    public List<Notice> selectNoticeList(Notice notice)
    {
        List<Notice> noticeList = noticeDao.selectNoticeList(notice);
        if (!noticeList.isEmpty()){
            List<Notice> notices = new ArrayList<>();
            Notice nt = noticeList.get(0);
            //将日期精确到小时，显示到分钟
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:");
            String startTime = dtf.format(nt.getStartTime());
            String substring = startTime.substring(0, 14);
            String newStartTime = substring+"00";
            nt.setStartTime(LocalDateTime.parse(newStartTime, DateTimeFormatter.ofPattern(DATE_PATTERN_YYYY_MM_DD_HH_MM)));
            String endTime = dtf.format(nt.getEndTime());
            String newEndTime = endTime.substring(0, 14)+"00";
            nt.setEndTime(LocalDateTime.parse(newEndTime, DateTimeFormatter.ofPattern(DATE_PATTERN_YYYY_MM_DD_HH_MM)));
            notices.add(noticeList.get(0));
            return notices;
        }else {
            return noticeList;
        }
    }


    /**
     * 新增公告信息
     * 
     * @param noticeDto 公告信息
     * @return 结果
     */
    @Override
    public int insertNotice(NoticeDto noticeDto)
    {

        // 获取用户组织
        DepartmentDto dept = departmentDao.getPathByUser(noticeDto.getUserId());
        String deptTreePath = Optional.ofNullable(dept).map(DepartmentDto::getPath).orElse(null);
        if (StringUtils.isEmpty(deptTreePath)) {
            throw new BusinessException(ResultCode.INNER_ERROR);
        }
        if (noticeDto.getStartTime().isAfter(noticeDto.getEndTime()) || LocalDateTime.now().isAfter(noticeDto.getEndTime())) {
            throw new BusinessException(ResultCode.PARAM_NOT_COMPLIANT);
        }
        Notice notice = new Notice();
        BeanUtils.copyProperties(noticeDto, notice);
        notice.setDeptTreePath(deptTreePath);
        notice.setCreateTime(LocalDateTime.now());
        notice.setCreateBy(noticeDto.getUserId());
        notice.setDeptId(String.valueOf(dept.getId()));
        notice.setStatus(1);
        return noticeDao.insertNotice(notice);
    }

    /**
     * 修改公告信息
     * 
     * @param noticeDto 公告信息
     * @return 结果
     */
    @Override
    public int updateNotice(NoticeDto noticeDto)
    {
        Notice notice = new Notice();
        BeanUtils.copyProperties(noticeDto,notice);
        notice.setUpdateBy(noticeDto.getUserId());
        notice.setUpdateTime(LocalDateTime.now());
        notice.setStatus(0);
        return noticeDao.updateNotice(notice);
    }


    @Override
    public int insertNoticeUserAccess(NoticeUserAccessDto noticeUserAccessDto, String userId) {
        NoticeUserAccess noticeUserAccess = new NoticeUserAccess();
        BeanUtils.copyProperties(noticeUserAccessDto,noticeUserAccess);
        noticeUserAccess.setUserId(userId);
        List<NoticeUserAccess> noticeUserAccesses = noticeUserAccessDao.selectNoticeUserAccessList(noticeUserAccess);
        if (!noticeUserAccesses.isEmpty()){
            return 1;
        }
        User user = userRepository.find(new UserId(userId));
        if (user.getPhoneNumber() == null || Objects.equals(user.getPhoneNumber().decrypt(), "")){
            NoticeUserAccess notice = new NoticeUserAccess();
            notice.setUserId(userId);
            notice.setNoticeId(noticeUserAccessDto.getNoticeId());
            notice.setCreateTime(LocalDateTime.now());
            notice.setCreateBy(userId);
            return noticeUserAccessDao.insertNoticeUserAccess(notice);
        }
        //查询小程序端手机号下多少用户
        List<User> userList = userRepository.commonQuery(UserCommonQuery.builder().phoneNumber(user.getPhoneNumber()).build());
        userList.forEach(userDataEntity -> {
            NoticeUserAccess notice = new NoticeUserAccess();
            notice.setUserId(userDataEntity.getId().getStrValue());
            notice.setNoticeId(noticeUserAccessDto.getNoticeId());
            notice.setCreateTime(LocalDateTime.now());
            notice.setCreateBy(userId);
            noticeUserAccessDao.insertNoticeUserAccess(notice);
        });
        return 1;
    }

    @Override
    public List<Notice> remindNotice(NoticeUserAccess noticeUserAccess, String userId) {
        Notice notice = new Notice();
        notice.setStatus(1);
        List<Notice> noticeList = noticeDao.selectNoticeList(notice);
        if (noticeList.isEmpty()){
            return new ArrayList<>();
        }
        Notice nt = noticeList.get(0);
        noticeUserAccess.setNoticeId(String.valueOf(nt.getId()));
        noticeUserAccess.setUserId(userId);
        List<NoticeUserAccess> noticeUserAccesses = noticeUserAccessDao.selectNoticeUserAccessList(noticeUserAccess);
        if (noticeUserAccesses.isEmpty()) {
            //将日期精确到小时，显示到分钟，过期时间按照小时计算
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:");
            String startTime = dtf.format(nt.getStartTime());
            String newStartTime = startTime.substring(0, 14)+"00";
            nt.setStartTime(LocalDateTime.parse(newStartTime, DateTimeFormatter.ofPattern(DATE_PATTERN_YYYY_MM_DD_HH_MM)));
            String endTime = dtf.format(nt.getEndTime());
            String newEndTime = endTime.substring(0, 14)+"00";
            nt.setEndTime(LocalDateTime.parse(newEndTime, DateTimeFormatter.ofPattern(DATE_PATTERN_YYYY_MM_DD_HH_MM)));
            String format = LocalDateTime.now().format(DateTimeFormatter.ofPattern(DATE_PATTERN_YYYY_MM_DD_HH_MM));
            LocalDateTime nowTime = LocalDateTime.parse(format, DateTimeFormatter.ofPattern(DATE_PATTERN_YYYY_MM_DD_HH_MM));
            if (nowTime.isAfter(nt.getEndTime()) || nowTime.isBefore(nt.getStartTime())) {
                return new ArrayList<>();
            } else {
                return noticeList;
            }
        } else {
            return new ArrayList<>();
        }
    }
}
