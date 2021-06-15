package io.github.simonschiller.prefiller.sample.order

import androidx.room.Entity
import androidx.room.Fts4

@Fts4
@Entity(tableName = "orders")
data class Order(
    val description: String
)
