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

package io.github.simonschiller.prefiller.sample.product;

import static com.google.common.truth.Truth.assertThat;

import android.content.Context;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.annotation.Config;

import java.util.List;

import io.github.simonschiller.prefiller.sample.order.Order;

@Config(sdk = 28)
@RunWith(AndroidJUnit4.class)
public class ProductDatabaseTest {
    private ProductDatabase database;

    @Before
    public void setup() {
        Context context = ApplicationProvider.getApplicationContext();
        database = Room.databaseBuilder(context, ProductDatabase.class, "products.db")
                .allowMainThreadQueries()
                .createFromAsset("products.db")
                .build();
    }

    @After
    public void teardown() {
        database.close();
    }

    @Test
    public void productsAreInsertedCorrectly() {
        List<Product> products = database.productDao().getAll();
        assertThat(products).containsExactly(
                new Product(1, "Thermosiphon", "Some description for the thermosiphon"),
                new Product(2, "Pump", "Some description for the pump"),
                new Product(3, "Heater", "Some description for the heater")
        );
    }

    @Test
    public void ordersAreInsertedCorrectly() {
        List<Order> orders = database.orderDao().search("M*");
        assertThat(orders).containsExactly(
                new Order("Mustard"),
                new Order("Mayonnaise")
        );
    }
}
