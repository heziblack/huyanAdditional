package icu.heziblack.miraiplugin.chahuyunAdditionalItem

import cn.chahuyun.authorize.EventComponent
import cn.chahuyun.authorize.MessageAuthorize
import cn.chahuyun.authorize.constant.PermConstant
import cn.chahuyun.authorize.entity.User
import cn.chahuyun.authorize.utils.MessageUtil.sendMessageQuery
import icu.heziblack.miraiplugin.chahuyunAdditionalItem.manager.PlayerPropManager
import icu.heziblack.miraiplugin.chahuyunAdditionalItem.util.DatabaseHelper
import icu.heziblack.miraiplugin.chahuyunAdditionalItem.util.TextUtil as tu
import cn.chahuyun.authorize.utils.PermUtil as pu
import net.mamoe.mirai.event.events.GroupMessageEvent
import cn.chahuyun.economy.utils.EconomyUtil as eu

const val TestGroup = "test-group"
@EventComponent
class TestManager {
    @MessageAuthorize(
        text = ["开启 测试"],
        userPermissions = [PermConstant.OWNER],
    )
    suspend fun testOn(event: GroupMessageEvent) {
        val groupUser = User.group(event.group.id)
        if (pu.checkUserHasPerm(groupUser, TestGroup)){
            event.sendMessageQuery(tu.addPermAlreadyHas("测试"))
            return
        }
        if(pu.addUserToPermGroupByName(groupUser,"测试群")){
            event.sendMessageQuery(tu.addPermSucceed("测试"))
        }else{
            event.sendMessageQuery(tu.addPermFail("测试"))
        }
    }

    @MessageAuthorize(
        text = ["关闭 测试"],
        userPermissions = [PermConstant.OWNER],
    )
    suspend fun testOff(event: GroupMessageEvent) {
        val groupUser = User.group(event.group.id)
        if (pu.checkUserHasPerm(groupUser, TestGroup)){
            event.sendMessageQuery(tu.removePermAlreadyLost("测试"))
            return
        }
        val pg = pu.talkPermGroupByName("测试群")
        pg.users.remove(groupUser)
        pg.save()
        event.sendMessageQuery(tu.removePermSucceed("测试"))
    }

    @MessageAuthorize(text = ["测试"],
        groupPermissions = [TestGroup])
    suspend fun test2(event: GroupMessageEvent) {
        val p = DatabaseHelper.talkPlayer(event.sender)
        val sb = StringBuilder("当前属性如下：\n")
        sb.append("""
            |HP:${p.hp}/${p.hpLimit}
            |FD:${p.food}/${p.foodLimit}
            |EN:${p.energy}/${p.energyLimit}
        """.trimMargin())
        event.sendMessageQuery(sb.toString())
    }
}

