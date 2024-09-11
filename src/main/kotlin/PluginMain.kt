package icu.heziblack.miraiplugin.chahuyunAdditionalItem

import cn.chahuyun.authorize.PermissionServer
import cn.chahuyun.authorize.entity.Perm
import cn.chahuyun.authorize.utils.PermUtil
import cn.chahuyun.hibernateplus.Configuration
import cn.chahuyun.hibernateplus.DriveType
import cn.chahuyun.hibernateplus.HibernatePlusService
import icu.heziblack.miraiplugin.chahuyunAdditionalItem.manager.CUSTOM_PERMS
import icu.heziblack.miraiplugin.chahuyunAdditionalItem.util.DatabaseHelper
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescription
import net.mamoe.mirai.console.plugin.jvm.KotlinPlugin
import kotlin.io.path.Path


//import cn


/**
 * 使用 kotlin 版请把
 * `src/main/resources/META-INF.services/net.mamoe.mirai.console.plugin.jvm.JvmPlugin`
 * 文件内容改成 `org.example.mirai.plugin.PluginMain` 也就是当前主类全类名
 *
 * 使用 kotlin 可以把 java 源集删除不会对项目有影响
 *
 * 在 `settings.gradle.kts` 里改构建的插件名称、依赖库和插件版本
 *
 * 在该示例下的 [JvmPluginDescription] 修改插件名称，id和版本，etc
 *
 * 可以使用 `src/test/kotlin/RunMirai.kt` 在 ide 里直接调试，
 * 不用复制到 mirai-console-loader 或其他启动器中调试
 */

object PluginMain : KotlinPlugin(
    JvmPluginDescription(
        id = "icu.heziblack.chahuyunAdditionalItem",
        name = "hyc-addition",
        version = "0.0.1-dev"
    ) {
        author("heziBlack")
        info(
            """
            meme
        """.trimIndent()
        )
        dependsOn("cn.chahuyun.HuYanEconomy",">=1.4.10",false)
        dependsOn("cn.chahuyun.HuYanAuthorize",">=1.1.5",false) // 使用依赖  版本需要大于等于 1.1.5 没有依赖将启动失败
        // author 和 info 可以删除.
    }
) {
    override fun onEnable() {
        logger.debug("====初始化鉴权====")
        registerCustomPerm()
        logger.debug("==初始化鉴权完成==")

        val configuration: Configuration = HibernatePlusService.createConfiguration(this::class.java)
        val dataFilePath = Path(this.dataFolderPath.toString(),"data.db3")
        configuration.driveType = DriveType.SQLITE
        configuration.address = dataFilePath.toString()
        configuration.isAutoReconnect = true
        configuration.user = "root"
        configuration.password = "123456"
        HibernatePlusService.loadingService(configuration)
        logger.info("附加数据存储位置:${dataFilePath}")

        //测试创建连接数据库
        DatabaseHelper.setLocation(dataFolder)
        logger.debug(DatabaseHelper.dbUrl())
    }

    /**初始化鉴权并注册自定义权限*/
    private fun registerCustomPerm(){
        PermissionServer.init(this, "icu.heziblack.miraiplugin.chahuyunAdditionalItem")
        for (custom in CUSTOM_PERMS){
            PermissionServer.registerPermCode(this, custom.value)
            // 从数据库之中获取刚刚注册的权限实体
            val p = PermUtil.takePerm(custom.value.code!!)
            // 注册获取权限组
            val pg = PermUtil.talkPermGroupByName(custom.key)
            // 将权限绑定到权限组
            PermUtil.addPermToPermGroupByPermGroup(p,pg)
        }
    }
}
