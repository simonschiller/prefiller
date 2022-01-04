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

import android.content.Context;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.annotation.Config;

import java.util.Arrays;
import java.util.List;

import io.github.simonschiller.prefiller.sample.order.Order;

import static org.junit.Assert.assertEquals;

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
        Product thermosiphon = new Product(1, "Thermosiphon", "Some description for the thermosiphon");
        Product pump = new Product(2, "Pump", "Some description for the pump");
        Product heater = new Product(3, "Heater", "Some description for the heater");

        List<Product> products = database.productDao().getAll();
        assertEquals(Arrays.asList(thermosiphon, pump, heater), products);
    }

    @Test
    public void ordersAreInsertedCorrectly() {
        Order mustard = new Order("Mustard");
        Order mayonnaise = new Order("Mayonnaise");

        List<Order> orders = database.orderDao().search("M*");
        assertEquals(Arrays.asList(mustard, mayonnaise), orders);
    }
}
