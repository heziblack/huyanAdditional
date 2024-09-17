package icu.heziblack.miraiplugin.chahuyunAdditionalItem.util

import cn.chahuyun.economy.manager.UserManager
import icu.heziblack.miraiplugin.chahuyunAdditionalItem.entity.dao.Player
import icu.heziblack.miraiplugin.chahuyunAdditionalItem.entity.table.PlayerUpdates
import icu.heziblack.miraiplugin.chahuyunAdditionalItem.entity.table.Players
import net.mamoe.mirai.contact.User
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import java.io.File
import java.nio.file.Files
import java.nio.file.StandardCopyOption
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.io.path.Path


object DatabaseHelper {
    private val fileName = "save.db3"
    private var fileLocation: File = Path(fileName).toFile()

    /**获取数据库连接*/
    fun dbUrl():String{
        return getDatabase().url
    }
    /**
     * 备份数据库文件
     * */
    fun backupDataFile():Boolean{
        try{
            if (fileLocation.exists()) {
                if (fileLocation.isFile) {
                    val from = fileLocation
                    val timeStamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yy-MM-dd--hhmmss"))
                    val folder = from.parentFile
                    val to = File(folder,"${fileName}-${timeStamp}.backup")
                    Files.copy(from.toPath(),to.toPath(),StandardCopyOption.REPLACE_EXISTING)
                }
            }else{
                return false
            }
            return true
        }catch (e:Exception){
            throw e
        }
    }
    /**设置数据库文件位置*/
    fun setLocation(folder:File){
        fileLocation = File(folder, fileName)
        initDatabase()
    }
    /**获取数据库连接位置*/
    private fun getDatabase():Database{
        return Database.connect("jdbc:sqlite:${fileLocation.path}","org.sqlite.JDBC")
    }
    /**查询创建玩家，通过用户对象*/
    fun talkPlayer(user: User): Player {
        val userInfo = UserManager.getUserInfo(user)
        val uid = userInfo.qq.toULong()
        return talkPlayer(uid)
    }
    /**查询创建玩家，通过ULong*/
    fun talkPlayer(userID:ULong):Player{
        return transaction(getDatabase()) {
            Player.findById(userID)?:Player.new(userID){}
        }
    }
    /**查询创建玩家，通过Long*/
    fun talkPlayer(playerID:Long):Player{
        return talkPlayer(playerID.toULong())
    }
    /**更新玩家数据*/
    fun updatePlayer(id:ULong){
        transaction(getDatabase()){
            Player.findByIdAndUpdate(id){
                it.hp -= 50.0
                // TODO 没有做完仍在测试
            }
        }
    }
    /**对数据库进行初始化*/
    private fun initDatabase(){
        transaction(getDatabase()) {
            SchemaUtils.createMissingTablesAndColumns(Players,PlayerUpdates)
        }
    }
    /**获取玩家数据操作时间*/
    fun playerTimestamp(uid:Long):LocalDateTime{
        transaction(getDatabase()){
            TODO()
        }
    }

}