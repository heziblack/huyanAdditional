package icu.heziblack.miraiplugin.chahuyunAdditionalItem

import cn.chahuyun.authorize.EventComponent
import cn.chahuyun.authorize.MessageAuthorize
import cn.chahuyun.authorize.constant.PermConstant
import cn.chahuyun.authorize.constant.PermissionMatchingEnum
import cn.chahuyun.authorize.utils.MessageUtil.sendMessageQuery
import net.mamoe.mirai.event.events.GroupMessageEvent
import cn.chahuyun.economy.utils.EconomyUtil as eu
import net.mamoe.mirai.event.events.MessageEvent


const val PC = "123"
@EventComponent
class Addition {
    @MessageAuthorize(
        text = ["添加测试权限"]
    )
    suspend fun test(event: MessageEvent) {
//        if (event !is GroupMessageEvent) return
//        val ac = eu.getMoneyByUser(event.sender)
//        event.sendMessageQuery("成功:${ac}")
    }
    @MessageAuthorize(text = ["测试"],
        userPermissions = [PermConstant.OWNER],
//        groupPermissionsMatching = PermissionMatchingEnum.AND,
        groupPermissions = [PC])
    suspend fun chet(event: GroupMessageEvent) {
        if (event !is GroupMessageEvent) return
        val ac = eu.getMoneyByUser(event.sender)
        event.sendMessageQuery("成功:${ac}")
    }
}

