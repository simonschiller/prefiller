package io.github.simonschiller.prefiller.sample.order

import androidx.room.Dao
import androidx.room.Query

@Dao
interface OrderDao {

    @Query("SELECT * FROM orders WHERE description MATCH :term")
    fun search(term: String): List<Order>
}
