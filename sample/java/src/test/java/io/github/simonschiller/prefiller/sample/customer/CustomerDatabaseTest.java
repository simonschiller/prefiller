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

package io.github.simonschiller.prefiller.sample.customer;

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

import io.github.simonschiller.prefiller.sample.creditcard.CreditCard;

@Config(sdk = 28)
@RunWith(AndroidJUnit4.class)
public class CustomerDatabaseTest {
    private CustomerDatabase database;

    @Before
    public void setup() {
        Context context = ApplicationProvider.getApplicationContext();
        database = Room.databaseBuilder(context, CustomerDatabase.class, "customers.db")
                .allowMainThreadQueries()
                .createFromAsset("customers.db")
                .build();
    }

    @After
    public void teardown() {
        database.close();
    }

    @Test
    public void customersAreInsertedCorrectly() {
        List<Customer> customers = database.customerDao().getAll();
        assertThat(customers).containsExactly(
                new Customer(1, "Mikael Burke", 38),
                new Customer(2, "Ayana Clarke", 12),
                new Customer(3, "Malachy Wall", 24)
        );
    }

    @Test
    public void creditCardsAreInsertedCorrectly() {
        List<CreditCard> creditCards = database.creditCardDao().getAll();
        assertThat(creditCards).containsExactly(
                new CreditCard(1, 1, "4098 6178 7375 6825"),
                new CreditCard(2, 3, "4003 8097 1909 0394")
        );
    }

    @Test
    public void viewsAreCreatedCorrectly() {
        List<AdultCustomer> adults = database.customerDao().getAdults();
        assertThat(adults).containsExactly(
                new AdultCustomer(1, "Mikael Burke", 38),
                new AdultCustomer(3, "Malachy Wall", 24)
        );
    }
}
