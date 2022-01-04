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
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.util.Locale;
import java.util.Objects;

@Entity(tableName = "customers", indices = @Index(value = "name", unique = true))
public class Customer {

    @PrimaryKey(autoGenerate = true)
    private final long id;

    @NonNull
    private final String name;
    private final int age;

    public Customer(long id, @NonNull String name, int age) {
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
        Customer customer = (Customer) other;
        return id == customer.id && name.equals(customer.name) && age == customer.age;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, age);
    }

    @NonNull
    @Override
    public String toString() {
        return String.format(Locale.US, "Customer(id=%d, name=%s, age=%d)", id, name, age);
    }
}
