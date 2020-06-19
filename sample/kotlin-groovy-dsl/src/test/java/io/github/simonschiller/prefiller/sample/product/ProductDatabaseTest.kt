package io.github.simonschiller.prefiller.sample.product

import android.app.Application
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@Config(sdk = [28])
@RunWith(AndroidJUnit4::class)
class ProductDatabaseTest {
    private lateinit var database: ProductDatabase

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Application>()
        database = Room.databaseBuilder(context, ProductDatabase::class.java, "products.db")
            .allowMainThreadQueries()
            .createFromAsset("products.db")
            .build()
    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun `Products are inserted correctly`() {
        val thermosiphon = Product(1, "Thermosiphon", "Some description for the thermosiphon")
        val pump = Product(2, "Pump", "Some description for the pump")
        val heater = Product(3, "Heater", "Some description for the heater")

        val products = database.productDao().getAll()
        assertEquals(listOf(thermosiphon, pump, heater), products)
    }
}
