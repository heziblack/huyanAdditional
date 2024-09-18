package icu.heziblack.miraiplugin.chahuyunAdditionalItem.command

import cn.chahuyun.authorize.match.CustomPattern
import net.mamoe.mirai.event.Event
import net.mamoe.mirai.event.events.GroupMessageEvent
import net.mamoe.mirai.message.data.content
import kotlin.reflect.KClass

class CustomCheck:CustomPattern{
    /**
     * 自定义过滤函数还在写
     * TODO
     * */
    override fun custom(event: Event): Boolean {
        if (event !is GroupMessageEvent) return false
        return event.message.content == "测试"
    }
}