package io.github.simonschiller.prefiller.sample.product;

import android.content.Context;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.annotation.Config;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

@Config(sdk = 28)
@RunWith(AndroidJUnit4.class)
public class ProductDatabaseTest {
    private ProductDatabase database;

    @Before
    public void setup() {
        Context context = ApplicationProvider.getApplicationContext();
        database = Room.databaseBuilder(context, ProductDatabase.class, "products.db")
                .allowMainThreadQueries()
                .createFromAsset("products.db")
                .build();
    }

    @After
    public void teardown() {
        database.close();
    }

    @Test
    public void productsAreInsertedCorrectly() {
        Product thermosiphon = new Product(1, "Thermosiphon", "Some description for the thermosiphon");
        Product pump = new Product(2, "Pump", "Some description for the pump");
        Product heater = new Product(3, "Heater", "Some description for the heater");

        List<Product> products = database.productDao().getAll();
        assertEquals(Arrays.asList(thermosiphon, pump, heater), products);
    }
}
