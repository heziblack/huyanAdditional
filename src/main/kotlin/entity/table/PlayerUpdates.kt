package icu.heziblack.miraiplugin.chahuyunAdditionalItem.entity.table

import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.ReferenceOption

object PlayerUpdates: IdTable<ULong>() {
    override val id: Column<EntityID<ULong>> = reference("p_id",Players.id, onDelete = ReferenceOption.CASCADE)
    val timestamp = integer("timestamp")
}