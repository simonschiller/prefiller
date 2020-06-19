package io.github.simonschiller.prefiller.sample.product;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = Product.class, version = 2)
public abstract class ProductDatabase extends RoomDatabase {
    public abstract ProductDao productDao();
}
