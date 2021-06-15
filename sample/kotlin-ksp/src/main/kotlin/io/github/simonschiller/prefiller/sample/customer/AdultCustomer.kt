package io.github.simonschiller.prefiller.sample.customer

import androidx.room.DatabaseView

@DatabaseView("SELECT * FROM customers WHERE age >= 18", viewName = "adults")
data class AdultCustomer(
    val id: Long,
    val name: String,
    val age: Int
)
