package icu.heziblack.miraiplugin.chahuyunAdditionalItem.manager

import cn.chahuyun.authorize.entity.Perm
import cn.chahuyun.authorize.entity.PermGroup

/**自定义的权限列表
 *
 * 格式为 '权限组' -Perm( '权限码' ， '权限注释' ）*/
val CUSTOM_PERMS = mutableMapOf<String,Perm>(
    Pair("测试组", Perm("test-group","测试权限-群")),
)
