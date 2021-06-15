package io.github.simonschiller.prefiller.sample.product

import androidx.room.Dao
import androidx.room.Query

@Dao
interface ProductDao {

    @Query("SELECT * FROM products")
    fun getAll(): List<Product>
}
