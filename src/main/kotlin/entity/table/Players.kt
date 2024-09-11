package icu.heziblack.miraiplugin.chahuyunAdditionalItem.entity.table

import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.Column

const val DefaultBasePropValue:Double = 100.0
const val DefaultAdvPropValue:Double = 1.0
const val DefaultRecordValue:UInt = 0u
object Players: IdTable<UInt>() {
    override val id: Column<EntityID<UInt>> = uinteger("player_id").entityId()
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
}