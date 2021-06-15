package io.github.simonschiller.prefiller.sample.customer

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "customers", indices = [Index("name", unique = true)])
data class Customer(
    @PrimaryKey(autoGenerate = true) val id: Long,
    val name: String,
    val age: Int
)
