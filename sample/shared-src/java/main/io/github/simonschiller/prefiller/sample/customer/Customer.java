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
