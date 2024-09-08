package entity

import jakarta.persistence.*;
//import lombok.*;
//import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;



@Entity(name = "TitleInfo")
@Table
class TestEntity(
    private var name:String? = null
):Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private var id:Int? = null
    override fun toString(): String {
        return "${this.name?:"未命名"}(${this.id?:-1})"
    }
}