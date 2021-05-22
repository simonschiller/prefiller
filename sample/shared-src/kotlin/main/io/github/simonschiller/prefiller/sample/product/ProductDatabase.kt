package io.github.simonschiller.prefiller.sample.product

import androidx.room.Database
import androidx.room.RoomDatabase
import io.github.simonschiller.prefiller.sample.order.Order
import io.github.simonschiller.prefiller.sample.order.OrderDao

@Database(entities = [Product::class, Order::class], version = 3)
abstract class ProductDatabase : RoomDatabase() {
    abstract fun productDao(): ProductDao
    abstract fun orderDao(): OrderDao
}
