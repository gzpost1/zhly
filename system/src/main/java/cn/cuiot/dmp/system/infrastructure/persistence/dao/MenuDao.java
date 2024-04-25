package cn.cuiot.dmp.system.infrastructure.persistence.dao;

import cn.cuiot.dmp.system.infrastructure.entity.MenuEntity;
import cn.cuiot.dmp.system.infrastructure.entity.dto.GetMenuRootByOrgTypeIdResDto;
import cn.cuiot.dmp.base.infrastructure.dto.MenuDTO;
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
	 * 按照用户Id查询菜单
	 *
	 * @param roleId
	 * @return
	 */
	List<MenuEntity> selectMenuListByRoleId(String roleId);

	/**
	 * 根据菜单类型查询菜单id集合
	 *
	 * @param type
	 * @return
	 */
	List<MenuDTO> getListReadAllMenu(String type);

	/**
	 * 查询角色菜单
	 * @param roleId
	 * @param orgTypeId
	 * @return
	 */
	List<MenuEntity> getAllRoleMenu(@Param("roleId") String roleId, @Param("orgTypeId") Integer orgTypeId);

	/**
	 * 查询菜单
	 *
	 * @param description
	 * @return
	 */
	List<MenuEntity> selectMenu(String description);

	/**
	 * 获取父级菜单选项
	 *
	 * @param parentMenuId
	 * @return
	 */
	List<MenuEntity> getParentMenu(@Param("parentMenuId") String parentMenuId);

	/**
	 * 修改菜单
	 *
	 * @param menuEntity
	 * @return
	 */
	int updateMenu(@Param("menuEntity") MenuEntity menuEntity);

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
	 * @param parentMenuId
	 * @return
	 */
	List<Integer> getMenuIdByParentMenuId(int parentMenuId);

	/**
	 * 自动id
	 *
	 * @param parentMenuId
	 * @return
	 */
	List<String> getMenuIdByParentMenuIdString(String parentMenuId);

	/**
	 * 查重
	 *
	 * @param componentUri
	 * @return
	 */
	int countComponentUri(String componentUri);

	/**
	 * 菜单名称
	 *
	 * @param menuName
	 * @param menuType
	 * @return
	 */
	int countMenuName(@Param("menuName") String menuName,@Param("menuType") Integer menuType);

	/**
	 * 菜单描述
	 *
	 * @param description
	 * @param menuType
	 * @return
	 */
	int countDescription(@Param("description") String description, @Param("menuType") Integer menuType);

	/**
	 * 父级菜单
	 *
	 * @param parentMenuId
	 * @return
	 */
	int countParentMenu(Integer parentMenuId);

	/**
	 * 菜单id
	 *
	 * @param parentMenuId
	 * @return
	 */
	int countMenuId(Integer parentMenuId);

	/**
	 * 父级菜单id
	 *
	 * @param parentMenuId
	 * @param menuType
	 * @return
	 */
	int countParentMenuId(@Param("parentMenuId") Integer parentMenuId, @Param("menuType") Integer menuType);

	/**
	 * 查询用户在某账户下的切换账户菜单
	 *
	 * @param userId
	 * @param orgId
	 * @param changeOrgPermisstionCode
	 * @return
	 */
	MenuEntity getChangeOrgMenu(@Param("userId") String userId,
                                @Param("orgId") String orgId,
                                @Param("permissionCode") String changeOrgPermisstionCode);

	/**
	 * 查询超级账户一级菜单
	 * @param parentPermit
	 * @param orgType
	 * @return
	 */
	List<MenuDTO> getSuperMenuOne(@Param("parentMenuId") String parentPermit, @Param("moduleType") Integer orgType);

	/**
	 * 查询所有webUrl权限
	 * @return
	 */
	List<MenuDTO> selectAllWebUrl();

	/**
	 * 通过typeId获取菜单
	 * @param orgTypeId
	 * @return
	 */
    List<GetMenuRootByOrgTypeIdResDto> getMenuRootByOrgTypeId(@Param("orgTypeId")String orgTypeId);
}
