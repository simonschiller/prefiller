package io.github.simonschiller.prefiller.sample.customer;

import androidx.room.Dao;
import androidx.room.Query;

import java.util.List;

@Dao
public interface CustomerDao {

    @Query("SELECT * FROM customers")
    List<Customer> getAll();

    @Query("SELECT * FROM adults")
    List<AdultCustomer> getAdults();
}
