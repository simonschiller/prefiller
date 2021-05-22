package io.github.simonschiller.prefiller.sample.customer

import androidx.room.Dao
import androidx.room.Query

@Dao
interface CustomerDao {

    @Query("SELECT * FROM customers")
    fun getAll(): List<Customer>

    @Query("SELECT * FROM adults")
    fun getAdults(): List<AdultCustomer>
}
