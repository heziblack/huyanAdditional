package icu.heziblack.miraiplugin.chahuyunAdditionalItem.service

import icu.heziblack.miraiplugin.chahuyunAdditionalItem.util.DatabaseHelper
import kotlinx.coroutines.Runnable
import net.mamoe.mirai.console.plugin.Plugin
import net.mamoe.mirai.contact.User
import net.mamoe.mirai.utils.MiraiLogger

/**管理玩家属性的服务
 *
 * 目前的想法是将玩家数据先读取一部分到内存，必要的时候进行存储
 *
 * 暂定行为
 *
 * 废弃*/
class PlayerPropsManageService(private val logger:MiraiLogger):Runnable {
    /** 就绪标志 */
    private var ready = false
    /** 更新操作 */
    override fun run() {
        try{
            if (ready) {
                logger.info("onReady do update")
                DatabaseHelper.updatePlayer(1220541730U)
                logger.info("update done")
            } else {
                logger.warning("service not ready. can not update player.\nplease shutdown console and load again")
            }
        }catch (e:Exception){
            stop() //
        }
    }
    /**使任务起效*/
    fun ready(){
        ready = true
    }
    /**使任务失能*/
    fun stop(){
        ready = false
    }
    /**刷新玩家数据*/
    private fun updateUser(user: User){
        val player = DatabaseHelper.talkPlayer(user)
        // TODO: 需要写点逻辑上的东西然后再弄
    }

}