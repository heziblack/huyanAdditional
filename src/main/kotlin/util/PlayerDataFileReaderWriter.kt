package icu.heziblack.miraiplugin.chahuyunAdditionalItem.util

import java.io.File
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**用于玩家数据的文本读写*/
class PlayerDataFileReaderWriter(dataDir: File) {
    val baseDir:File = File(dataDir,"players")
    init {
        if (!baseDir.exists()){
            baseDir.mkdirs()
        }else{
            if (baseDir.isFile){
                baseDir.delete()
            }
        }
    }
    /**写入时间戳*/
    fun timestampWrite(id:String){
        val target:File = File(baseDir,"${id}.txt")
        if (!target.exists()){
            target.createNewFile()
        }
        target.writeText(now())
    }
    /**读取时间戳*/
    fun timestampRead(id: String):String{
        val target:File = File(baseDir,"${id}.txt")
        return if (!target.exists()){
            timestampWrite(id)
            now()
        }else{
            target.readText()
        }
    }
    /**获取当前时间字符串，Pattern: yyMMddHHmmss*/
    private fun now():String{
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyMMddHHmmss"))
    }

}