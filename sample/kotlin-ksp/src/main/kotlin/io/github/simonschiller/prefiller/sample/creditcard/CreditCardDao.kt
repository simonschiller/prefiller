package io.github.simonschiller.prefiller.sample.creditcard

import androidx.room.Dao
import androidx.room.Query

@Dao
interface CreditCardDao {

    @Query("SELECT * FROM creditcards")
    fun getAll(): List<CreditCard>
}
