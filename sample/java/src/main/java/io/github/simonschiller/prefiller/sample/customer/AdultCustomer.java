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
