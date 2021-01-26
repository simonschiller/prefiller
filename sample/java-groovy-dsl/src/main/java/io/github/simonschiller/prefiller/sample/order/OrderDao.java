package io.github.simonschiller.prefiller.sample.order;

import androidx.annotation.NonNull;
import androidx.room.Dao;
import androidx.room.Query;

import java.util.List;

@Dao
public interface OrderDao {

    @Query("SELECT * FROM orders WHERE description MATCH :term")
    List<Order> search(@NonNull String term);
}
