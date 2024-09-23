package icu.heziblack.miraiplugin.chahuyunAdditionalItem.util

import cn.chahuyun.economy.manager.UserManager
import icu.heziblack.miraiplugin.chahuyunAdditionalItem.entity.dao.Player
import icu.heziblack.miraiplugin.chahuyunAdditionalItem.entity.dao.PlayerUpdate
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
import java.time.Duration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalUnit
import kotlin.io.path.Path


object DatabaseHelper {
    private val fileName = "save.db3"
    private var fileLocation: File = Path(fileName).toFile()
    private const val FOOD_DEVALUE:Double = 0.35

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
            val p = talkPlayer(id)
            val update = playerTimestamp(p)?: createPlayerTimestamp(p)
            val d = Duration.between(getTimeFromUpdate(update.timestamp),LocalDateTime.now())
            if (d < standDuration){
                // TODO 由于时间间隔小于更新时间,不对玩家做更新
            }else{
                updatePlayer(p,d)
                talkUpdate(p)
            }
        }
    }
    /**更新玩家数据，并返回更新后的对象
     *
     * 长方法*/
    private fun updatePlayer(player: Player,duration: Duration):Player{
//         TODO 根据玩家时间间隔扣除数据
        val minutes = duration.toMinutes() //分钟数
        val counter = minutes / 5
        transaction(getDatabase()) {
            // TODO 扣血逻辑需要再打磨一下
            var left = counter
            for (i in 1..counter) {
                player.food -= 0.35
                left -= 1
                if (player.food <= 0) {
                    player.food = 0.0
                    player.hp -= 0.4
                    player.sickness -= 1.0
                    break
                }
            }
            if (left > 0) {
                for (i in 1..left) {
                    player.hp -= 0.4
                    player.sickness -= 1.0
                    // TODO 还未做完
                }
            }
        }
        // 返回更新数据后的玩家对象
        return talkPlayer(player.id.value)
    }
    /**使用递归来更新玩家数据
     *
     * 调用此方法前必须保证[Player]和[PlayerUpdate]数据存在
     *
     * @param playerID 玩家ID，用于查询玩家对象，每次递归计算通过其重新获取玩家
     * @param leftTime 剩余循环次数 */
    private fun newUpdate(playerID:ULong, leftTime:Long){
        // 若次数耗尽结束递归
        if (leftTime<=0) return

        val result =  transaction(getDatabase()) {
            // 重新查询玩家数据，以防万一
            val newPlayer = Player.findById(playerID)?: talkPlayer(playerID)
            // 检查、修改玩家食物属性
            if (newPlayer.food > 0.0){
                if (newPlayer.food > FOOD_DEVALUE){
                    newPlayer.food -= FOOD_DEVALUE
                }else{
                    newPlayer.food = 0.0
                }
            }else{
                // 若饱食度<=0.0，开始扣除心情、生命、能量
                if (newPlayer.hp > 0.0){
                    // 根据玩家心情值决定要扣除的HP值
                    val decValue = hpDecReferHappy(newPlayer.happiness)
                    when(newPlayer.hp){
                        in (- 1.0 .. decValue) ->{
                            TODO()
                        }
                        else -> {
                            newPlayer.hp -= decValue
                        }
                    }
                }else{
                    // HP都没了，重开
                    newPlayer.onRemake = true
                }
            }

            // 当生命值归零，将玩家reMake标志置True
            // TODO
            newPlayer
        }
        // 若生命值没有耗尽继续递归
        if (!result.onRemake) newUpdate(playerID,leftTime-1)
    }

    /**根据[happiness]分段决定扣除的hp值*/
    private fun hpDecReferHappy(happiness:Double):Double{
        return when(happiness){
            in (0.0 .. 0.3) -> {
                TODO()
            }
            in (0.3 .. 0.5) -> {
                TODO()
            }
            in (0.5 .. 0.8) -> {
                TODO()
            }
            in (0.8 .. 1.0) -> {
                TODO()
            }
            else ->{
                TODO()
            }
        }
    }

    /**对数据库进行初始化*/
    private fun initDatabase(){
        transaction(getDatabase()) {
            SchemaUtils.createMissingTablesAndColumns(Players,PlayerUpdates)
        }
    }
    /**获取玩家时间戳*/
    private fun playerTimestamp(player: Player):PlayerUpdate?{
        return transaction(getDatabase()) {
            PlayerUpdate.findById(player.id)
        }
    }
    /**创建玩家时间戳*/
    private fun createPlayerTimestamp(player: Player):PlayerUpdate{
        return transaction(getDatabase()) {
            PlayerUpdate.new(id = player.id.value){
                this.timestamp = LocalDateTime.now().format(timestampFormatter)
            }
        }
    }
    /**更新间隔颗粒度*/
    private val standDuration = Duration.ofMinutes(5L)
    /**日期格式化为字符*/
    private val timestampFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyMMddhhmmss")
    /**从字符串获取时间*/
    private fun getTimeFromUpdate(timestamp:String):LocalDateTime{
        return LocalDateTime.parse(timestamp, timestampFormatter)
    }
    /**获取创建玩家时间戳*/
    private fun talkUpdate(player: Player):PlayerUpdate{
        return playerTimestamp(player)?: createPlayerTimestamp(player)
    }

    private fun makePlayerUpdate(player: Player){
        transaction(getDatabase()){
            talkUpdate(player).timestamp = LocalDateTime.now().format(timestampFormatter)
        }
    }


}