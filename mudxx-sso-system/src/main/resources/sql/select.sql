#方案1:单表查询
# 基于用户id查询用户对应的角色id (查询出的角色id可能是多个)
select role_id from tb_user_roles where user_id=1;
# 基于角色id查询用户对应的菜单id
select menu_id from tb_role_menus where role_id in (1);
# 基于菜单id查询菜单权限标识
select permission from tb_menus where id in (1,2,3);

#方案2:多表嵌套查询

select permission
from tb_menus
where id in (select menu_id
             from tb_role_menus
             where role_id in (select role_id
                               from tb_user_roles
                               where user_id=1));

#方案3:多表关联查询

select distinct m.permission
from tb_user_roles ur join  tb_role_menus rm
                            on ur.role_id=rm.role_id
                      join tb_menus m
                           on rm.menu_id=m.id
where ur.user_id=1
