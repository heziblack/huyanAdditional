package icu.heziblack.miraiplugin.chahuyunAdditionalItem.entity.table

//import kotlinx.datetime.LocalDateTime
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.and
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


object Players: IdTable<ULong>() {
    override val id: Column<EntityID<ULong>> = ulong("player_id").entityId()
    // PlayerBaseProps
    val hp = double("hp").default(DefaultBasePropValue)
    val hpLimit = double("hp_l").default(DefaultBasePropValue)
    val food = double("food").default(DefaultBasePropValue)
    val foodLimit = double("food_l").default(DefaultBasePropValue)
    val energy = double("energy").default(DefaultBasePropValue)
    val energyLimit = double("energy_l").default(DefaultBasePropValue)
    // PlayerAdvProps
    val sickness = double("sickness").default(DefaultAdvPropValue)
    val happiness = double("happiness").default(DefaultAdvPropValue)
    val luck = double("luck").default(DefaultAdvPropValue)
    // PlayerRecords
    val remake = uinteger("remake").default(DefaultRecordValue)
    val eat = uinteger("eat").default(DefaultRecordValue)
    val rob = uinteger("rob").default(DefaultRecordValue)
    val craft = uinteger("craft").default(DefaultRecordValue)
    // flag
    val onRemake = bool("on_remake").default(false)
    val timestamp = long("ts").default(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyMMddHHmmss")).toLong())
}