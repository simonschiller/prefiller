package io.github.simonschiller.prefiller.sample.product;

import androidx.room.Dao;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ProductDao {

    @Query("SELECT * FROM products")
    List<Product> getAll();
}
