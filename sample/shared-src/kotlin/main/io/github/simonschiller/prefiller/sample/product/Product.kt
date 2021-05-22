package io.github.simonschiller.prefiller.sample.product

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "products")
data class Product(
    @PrimaryKey(autoGenerate = true) val id: Long,
    val name: String,
    val description: String
)
