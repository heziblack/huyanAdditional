package icu.heziblack.miraiplugin.chahuyunAdditionalItem

import cn.chahuyun.authorize.EventComponent
import cn.chahuyun.authorize.MessageAuthorize
import cn.chahuyun.authorize.constant.AuthPerm
import cn.chahuyun.authorize.constant.MessageMatchingEnum
import cn.chahuyun.authorize.utils.MessageUtil.sendMessageQuery
import cn.chahuyun.authorize.utils.UserUtil
import cn.chahuyun.economy.utils.ShareUtils
import icu.heziblack.miraiplugin.chahuyunAdditionalItem.command.CustomCheck
import icu.heziblack.miraiplugin.chahuyunAdditionalItem.util.DatabaseHelper
import kotlinx.coroutines.delay
import icu.heziblack.miraiplugin.chahuyunAdditionalItem.util.TextUtil as tu
import cn.chahuyun.authorize.utils.PermUtil as pu
import net.mamoe.mirai.event.events.GroupMessageEvent
import java.time.LocalDateTime
import kotlin.time.Duration

/**相关权限*/
@EventComponent
class TestManager {
    companion object{
        const val TEST_GROUP = "test-group"
    }
    @MessageAuthorize(
        text = ["开启 测试"],
        userPermissions = [AuthPerm.OWNER],
    )
    suspend fun testOn(event: GroupMessageEvent) {
        val groupUser = UserUtil.group(event.group.id)
        if (pu.checkUserHasPerm(groupUser, TEST_GROUP)){
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
        userPermissions = [AuthPerm.OWNER],
    )
    suspend fun testOff(event: GroupMessageEvent) {
        val groupUser = UserUtil.group(event.group.id)
        if (!pu.checkUserHasPerm(groupUser, TEST_GROUP)){
            event.sendMessageQuery(tu.removePermAlreadyLost("测试"))
            return
        }
        val pg = pu.talkPermGroupByName("测试群")
        pg.users.remove(groupUser)
        pg.save()
        event.sendMessageQuery(tu.removePermSucceed("测试"))
    }

    @MessageAuthorize(
//        text = ["测试"],
        groupPermissions = [TEST_GROUP, AuthPerm.OWNER, AuthPerm.ADMIN],
        custom = CustomCheck::class,
        messageMatching = MessageMatchingEnum.CUSTOM,
        )
    suspend fun test2(event: GroupMessageEvent) {
//        val p = DatabaseHelper.talkPlayer(event.sender)
//        val np = DatabaseHelper.updatePlayer(p.id.value)
//        val sb = StringBuilder("Before -> after\n")
//        sb.append("""
//            ${p.timestamp}->${np.timestamp}
//            ${p.food}->${np.food}
//        """.trimIndent())
//        event.sendMessageQuery(sb.toString())
        DatabaseHelper.testTimestampUpdate(event.sender.id)
//        event.sendMessageQuery("成功")
    }

    @MessageAuthorize(
        text = ["sjc"],
        groupPermissions = [TEST_GROUP, AuthPerm.OWNER, AuthPerm.ADMIN],
    )
    suspend fun test3(event: GroupMessageEvent) {
        val p = DatabaseHelper.talkPlayer(event.sender)
        val sb = StringBuilder("here is nothing to show")
        event.sendMessageQuery(sb.toString())
    }

}

