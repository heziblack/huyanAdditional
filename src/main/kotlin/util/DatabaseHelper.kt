package icu.heziblack.miraiplugin.chahuyunAdditionalItem.util


import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.transaction
import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardCopyOption
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.io.path.Path
import kotlin.io.path.exists


object DatabaseHelper {
    private val fileName = "test2.db3"
    private var fileLocation: File = Path(fileName).toFile()
    private val db by lazy {
        Database.connect("jdbc:sqlite:${fileLocation.path}","org.sqlite.JDBC")
    }
    init {
        transaction(db){

        }
    }
    fun dbUrl():String{
        return db.url
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
                    val to = File("${fileName}-${timeStamp}.backup")
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

    fun setLocation(folder:File){
        fileLocation = File(folder, fileName)
    }
}