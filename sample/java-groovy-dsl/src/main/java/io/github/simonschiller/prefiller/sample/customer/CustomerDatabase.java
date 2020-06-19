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
