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

import androidx.room.Database;
import androidx.room.RoomDatabase;

import io.github.simonschiller.prefiller.sample.creditcard.CreditCard;
import io.github.simonschiller.prefiller.sample.creditcard.CreditCardDao;

@Database(entities = {CreditCard.class, Customer.class}, views = AdultCustomer.class, version = 1)
public abstract class CustomerDatabase extends RoomDatabase {
    public abstract CreditCardDao creditCardDao();
    public abstract CustomerDao customerDao();
}
