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

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Locale;
import java.util.Objects;

@Entity(tableName = "products")
public class Product {

    @PrimaryKey(autoGenerate = true)
    private final long id;

    @NonNull
    private final String name;

    @NonNull
    private final String description;

    public Product(long id, @NonNull String name, @NonNull String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public long getId() {
        return id;
    }

    @NonNull
    public String getName() {
        return name;
    }

    @NonNull
    public String getDescription() {
        return description;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (other == null || getClass() != other.getClass()) return false;
        Product product = (Product) other;
        return id == product.id && name.equals(product.name) && description.equals(product.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description);
    }

    @NonNull
    @Override
    public String toString() {
        return String.format(Locale.US, "Product(id=%d, name=%s, description=%s)", id, name, description);
    }
}
