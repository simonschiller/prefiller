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

package io.github.simonschiller.prefiller.sample.order;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Fts4;

import java.util.Locale;
import java.util.Objects;

@Fts4
@Entity(tableName = "orders")
public class Order {

    @NonNull
    private final String description;

    public Order(@NonNull String description) {
        this.description = description;
    }

    @NonNull
    public String getDescription() {
        return description;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (other == null || getClass() != other.getClass()) return false;
        Order order = (Order) other;
        return description.equals(order.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(description);
    }

    @NonNull
    @Override
    public String toString() {
        return String.format(Locale.US, "Order(description=%s)", description);
    }
}
