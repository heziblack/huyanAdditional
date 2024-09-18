package icu.heziblack.miraiplugin.chahuyunAdditionalItem.entity.dao

import icu.heziblack.miraiplugin.chahuyunAdditionalItem.entity.table.PlayerUpdates
import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID

/**保存玩家更新时间戳*/
class PlayerUpdate(id: EntityID<ULong>): Entity<ULong>(id) {
    companion object:EntityClass<ULong,PlayerUpdate>(PlayerUpdates)

    var timestamp by PlayerUpdates.timestamp
}