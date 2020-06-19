package io.github.simonschiller.prefiller.sample.creditcard;

import androidx.room.Dao;
import androidx.room.Query;

import java.util.List;

@Dao
public interface CreditCardDao {

    @Query("SELECT * FROM creditcards")
    List<CreditCard> getAll();
}
