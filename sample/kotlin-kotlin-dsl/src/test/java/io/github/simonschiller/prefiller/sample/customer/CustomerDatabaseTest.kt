package io.github.simonschiller.prefiller.sample.customer

import android.app.Application
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import io.github.simonschiller.prefiller.sample.creditcard.CreditCard
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import java.util.*

@Config(sdk = [28])
@RunWith(AndroidJUnit4::class)
class CustomerDatabaseTest {
    private lateinit var database: CustomerDatabase

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Application>()
        database = Room.databaseBuilder(context, CustomerDatabase::class.java, "customers.db")
            .allowMainThreadQueries()
            .createFromAsset("customers.db")
            .build()
    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun `Customers are inserted correctly`() {
        val mikael = Customer(1, "Mikael Burke", 38)
        val ayana = Customer(2, "Ayana Clarke", 12)
        val malachy = Customer(3, "Malachy Wall", 24)

        val customers = database.customerDao().getAll()
        assertEquals(listOf(mikael, ayana, malachy), customers)
    }

    @Test
    fun `Credit cards are inserted correctly`() {
        val mikaelsCreditCard = CreditCard(1, 1, "4098 6178 7375 6825")
        val malachysCreditCard = CreditCard(2, 3, "4003 8097 1909 0394")

        val creditCards = database.creditCardDao().getAll()
        assertEquals(listOf(mikaelsCreditCard, malachysCreditCard), creditCards)
    }

    @Test
    fun `Views are created correctly`() {
        val mikael = AdultCustomer(1, "Mikael Burke", 38)
        val malachy = AdultCustomer(3, "Malachy Wall", 24)

        val adults = database.customerDao().getAdults()
        assertEquals(listOf(mikael, malachy), adults)
    }
}
