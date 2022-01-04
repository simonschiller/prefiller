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

import androidx.annotation.NonNull;
import androidx.room.DatabaseView;

import java.util.Locale;
import java.util.Objects;

@DatabaseView(value = "SELECT * FROM customers WHERE age >= 18", viewName = "adults")
public class AdultCustomer {
    private final long id;

    @NonNull
    private final String name;
    private final int age;

    public AdultCustomer(long id, @NonNull String name, int age) {
        this.id = id;
        this.name = name;
        this.age = age;
    }

    public long getId() {
        return id;
    }

    @NonNull
    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (other == null || getClass() != other.getClass()) return false;
        AdultCustomer customer = (AdultCustomer) other;
        return id == customer.id && name.equals(customer.name) && age == customer.age;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, age);
    }

    @NonNull
    @Override
    public String toString() {
        return String.format(Locale.US, "AdultCustomer(id=%d, name=%s, age=%d)", id, name, age);
    }
}
