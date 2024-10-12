package icu.heziblack.miraiplugin.chahuyunAdditionalItem.util

import cn.chahuyun.economy.manager.UserManager
import icu.heziblack.miraiplugin.chahuyunAdditionalItem.entity.dao.Player
import icu.heziblack.miraiplugin.chahuyunAdditionalItem.entity.dao.PlayerUpdate
import icu.heziblack.miraiplugin.chahuyunAdditionalItem.entity.table.PlayerUpdates
import icu.heziblack.miraiplugin.chahuyunAdditionalItem.entity.table.Players
import net.mamoe.mirai.contact.User
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import java.io.File
import java.nio.file.Files
import java.nio.file.StandardCopyOption
import java.time.Duration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.io.path.Path


object DatabaseHelper {
    private val fileName = "save.db3"
    private var fileLocation: File = Path(fileName).toFile()
    private const val FOOD_DEVALUE:Double = 0.35
    /**获取数据库连接*/
    fun dbUrl():String{
        return getDatabase().url
    }
    /**备份数据库文件*/
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
    /**默认数据库*/
    private val defaultDB:Database
        get() = Database.connect("jdbc:sqlite:${fileLocation.path}","org.sqlite.JDBC")
    /**查询创建玩家，通过用户对象*/
    fun talkPlayer(user: User): Player {
        val userInfo = UserManager.getUserInfo(user)
        val uid = userInfo.qq
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
    fun updatePlayer(id:ULong):Player{
        return transaction(getDatabase()){
            val p = talkPlayer(id)
            val update = p.timestamp
            val d = Duration.between(getTimeFromTimestamp(update), LocalDateTime.now())
            if (d >= standDuration) {
                println("超时")
                recursionUpdate(p.id.value,d.toMinutes()/5)
                p.timestamp = now()
                talkPlayer(p.id.value)
            } else {
                println("未超时")
                p
            }
        }
    }
    /**使用递归来更新玩家数据
     *
     * 调用此方法前必须保证[Player]和[PlayerUpdate]数据存在
     *
     * @param playerID 玩家ID，用于查询玩家对象，每次递归计算通过其重新获取玩家
     * @param leftTime 剩余循环次数 */
    private fun recursionUpdate(playerID:ULong, leftTime:Long){
        // 若次数耗尽结束递归
        if (leftTime<=0) return
        val result =  transaction(getDatabase()) {
            // 重新查询玩家数据，以防万一
            val newPlayer = talkPlayer(playerID)
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
                    formatPlayerHappiness(newPlayer)
                    newPlayer.happiness *= 0.9 // 心情值为原来的0.9
                    val decValue = hpDecReferHappy(newPlayer.happiness)
                    when(newPlayer.hp){
                        in (- 1.0 .. decValue) ->{
                            // 玩家生命值不够扣除
                            newPlayer.hp = 0.0
                            newPlayer.onRemake = true
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
            newPlayer
        }
        // 若生命值没有耗尽继续递归
        if (!result.onRemake) recursionUpdate(playerID,leftTime-1)
    }
    /***/
    private fun cycleUpdate(playerID: ULong, counter:Long){
        transaction(defaultDB) {
            for (c in (0..counter)){
                // TODO '看情况写不写吧'
            }
        }
    }
    /**根据[happiness]分段决定扣除的hp值
     *
     * 未来或许会修改*/
    private fun hpDecReferHappy(happiness:Double):Double{
        return when(happiness){
            in (0.0 .. 0.3) -> {
                0.8
            }
            in (0.3 .. 0.5) -> {
                0.6
            }
            in (0.5 .. 0.8) -> {
                0.4
            }
            in (0.8 .. 1.0) -> {
                0.2
            }
            else ->{
                0.2
            }
        }
    }
    /**规则化玩家心情,使其值保持在  0~1  */
    private fun formatPlayerHappiness(player: Player){
        if(player.happiness in (0.0 .. 1.0)) return
        transaction(getDatabase()){
            if (player.happiness>1.0){
                player.happiness = 1.0
            }else{
                player.happiness = 0.001
            }
        }
    }
    /**对数据库进行初始化*/
    private fun initDatabase(){
        transaction(getDatabase()) {
            SchemaUtils.createMissingTablesAndColumns(Players,PlayerUpdates)
//            fixPlayerData()
        }
    }
    /**获取玩家时间戳*/
    private fun playerTimestamp(player: Player):PlayerUpdate?{
        return transaction(getDatabase()) {
            PlayerUpdate.findById(player.id)
        }
    }
    /**更新间隔颗粒度*/
    private val standDuration = Duration.ofMinutes(5L)
    /**日期格式化为字符*/
    private val timestampFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyMMddHHmmss")
    /**从时间戳获取时间*/
    private fun getTimeFromTimestamp(timestamp:Long):LocalDateTime{
        return LocalDateTime.parse(timestamp.toString(), timestampFormatter)
    }
    /**获取当前时间时间戳字符串*/
    private fun now():Long{
        return LocalDateTime.now().format(timestampFormatter).toLong()
    }
    /**将带年份的字符串年份去掉*/
    fun shorter(timestamp: String):String{
        return timestamp.substring(2)
    }
    /**将带年份的时间戳年份去掉*/
    fun shorter(timestamp:Long):String{
        return shorter(timestamp.toString())
    }
    /**补全玩家数据*/
    fun fixPlayerData(){
        transaction(getDatabase()) {
            Players.update {

            }
        }
    }
    fun testTimestampUpdate(id:Long){
        transaction(defaultDB){
            val p = talkPlayer(id)
            p.timestamp = now()
        }
    }
}