package icu.heziblack.miraiplugin.chahuyunAdditionalItem

import cn.chahuyun.authorize.EventComponent
import cn.chahuyun.authorize.MessageAuthorize
import cn.chahuyun.authorize.utils.MessageUtil.sendMessageQuery
import net.mamoe.mirai.event.events.GroupMessageEvent
import cn.chahuyun.economy.utils.EconomyUtil as eu
import net.mamoe.mirai.event.events.MessageEvent

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
    @MessageAuthorize(text = ["测试"], groupPermissions = ["123"])
    suspend fun chet(event: MessageEvent) {
        if (event !is GroupMessageEvent) return
        val ac = eu.getMoneyByUser(event.sender)
        event.sendMessageQuery("成功:${ac}")
    }
}

