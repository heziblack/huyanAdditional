package icu.heziblack.miraiplugin.chahuyunAdditionalItem.manager

import cn.chahuyun.authorize.EventComponent
import icu.heziblack.miraiplugin.chahuyunAdditionalItem.entity.dao.Player
import net.mamoe.mirai.contact.User
import icu.heziblack.miraiplugin.chahuyunAdditionalItem.util.DatabaseHelper as dHelper

@EventComponent
class PlayerPropManager {
    /**获取或创建Player*/
    fun talkPlayer(user: User): Player {
        return dHelper.talkPlayer(user)
    }
}