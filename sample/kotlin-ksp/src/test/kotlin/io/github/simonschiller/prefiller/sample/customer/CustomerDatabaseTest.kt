/*
 * Copyright 2020 Simon Schiller
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.simonschiller.prefiller.sample.customer

import android.app.Application
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import io.github.simonschiller.prefiller.sample.creditcard.CreditCard
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

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
        val customers = database.customerDao().getAll()
        assertThat(customers).containsExactly(
            Customer(1, "Mikael Burke", 38),
            Customer(2, "Ayana Clarke", 12),
            Customer(3, "Malachy Wall", 24),
        )
    }

    @Test
    fun `Credit cards are inserted correctly`() {
        val creditCards = database.creditCardDao().getAll()
        assertThat(creditCards).containsExactly(
            CreditCard(1, 1, "4098 6178 7375 6825"),
            CreditCard(2, 3, "4003 8097 1909 0394"),
        )
    }

    @Test
    fun `Views are created correctly`() {
        val adults = database.customerDao().getAdults()
        assertThat(adults).containsExactly(
            AdultCustomer(1, "Mikael Burke", 38),
            AdultCustomer(3, "Malachy Wall", 24),
        )
    }
}
