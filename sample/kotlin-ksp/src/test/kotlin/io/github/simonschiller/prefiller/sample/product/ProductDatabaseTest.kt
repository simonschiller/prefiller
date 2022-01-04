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

package io.github.simonschiller.prefiller.sample.product

import android.app.Application
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import io.github.simonschiller.prefiller.sample.order.Order
import org.junit.After
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
        val products = database.productDao().getAll()
        assertThat(products).containsExactly(
            Product(1, "Thermosiphon", "Some description for the thermosiphon"),
            Product(2, "Pump", "Some description for the pump"),
            Product(3, "Heater", "Some description for the heater"),
        )
    }

    @Test
    fun `Orders are inserted correctly`() {
        val orders = database.orderDao().search("M*")
        assertThat(orders).containsExactly(
            Order("Mustard"),
            Order("Mayonnaise"),
        )
    }
}
