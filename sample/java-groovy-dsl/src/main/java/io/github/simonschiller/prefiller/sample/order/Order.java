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
