package com.mudxx.sso.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mudxx.sso.system.pojo.SSOUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SSOUserMapper extends BaseMapper<SSOUser> {
    /**
     * 基于用户名查询用户信息
     * @param username
     * @return
     */
    @Select("select id,username,password,status " +
            " from sso_user " +
            " where username=#{username}")
    SSOUser selectUserByUsername(String username);

    /**
     * 基于用户id查询用户权限,涉及到的表有:
     * 1)sso_user_role(用户角色关系表,可以在此表中基于用户id找到用户角色)
     * 2)sso_role_menu(角色菜单关系表,可以基于角色id找到菜单id)
     * 3)sso_menu(菜单表,菜单为资源的外在表现形式,在此表中可以基于菜单id找到权限标识>
     * 基于如上三张表获取用户权限,有什么解决方案?
     * 1)方案1:三次单表查询
     * 2)方案2:嵌套查询
     * 3)方案3:多表查询
     * @param userId
     * @return
     */
    @Select("select distinct m.permission " +
            "from sso_user_role ur join  sso_role_menu rm " +
            "      on ur.role_id=rm.role_id " +
            "      join sso_menu m " +
            "      on rm.menu_id=m.id " +
            "where ur.user_id=#{userId}")
    List<String> selectUserPermissions(Long userId);


}
