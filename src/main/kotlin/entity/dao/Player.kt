package icu.heziblack.miraiplugin.chahuyunAdditionalItem.entity.dao

import icu.heziblack.miraiplugin.chahuyunAdditionalItem.entity.table.Players
import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID


// 需要一个机制对玩家属性进行刷新和判定
class Player(id:EntityID<ULong>):Entity<ULong>(id) {
    companion object: EntityClass<ULong,Player>(Players)

    var hp by Players.hp
    var hpLimit by Players.hpLimit
    var food by Players.food
    var foodLimit by Players.foodLimit
    var energy by Players.energy
    var energyLimit by Players.energyLimit

    var sickness by Players.sickness
    var happiness by Players.happiness
    var luck by Players.luck

    var remake by Players.remake
    var eat by Players.eat
    var rob by Players.rob
    var craft by Players.craft

    var onRemake by Players.onRemake
}