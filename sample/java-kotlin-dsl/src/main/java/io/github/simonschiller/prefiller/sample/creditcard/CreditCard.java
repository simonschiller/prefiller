package io.github.simonschiller.prefiller.sample.creditcard;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.util.Locale;
import java.util.Objects;

import io.github.simonschiller.prefiller.sample.customer.Customer;

@Entity(tableName = "creditcards", indices = @Index("customerId"), foreignKeys = {
        @ForeignKey(entity = Customer.class, parentColumns = "id", childColumns = "customerId")
})
public class CreditCard {

    @PrimaryKey(autoGenerate = true)
    private final long id;
    private final long customerId;

    @NonNull
    private final String cardNumber;

    public CreditCard(long id, long customerId, @NonNull String cardNumber) {
        this.id = id;
        this.customerId = customerId;
        this.cardNumber = cardNumber;
    }

    public long getId() {
        return id;
    }

    public long getCustomerId() {
        return customerId;
    }

    @NonNull
    public String getCardNumber() {
        return cardNumber;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (other == null || getClass() != other.getClass()) return false;
        CreditCard paymentInfo = (CreditCard) other;
        return id == paymentInfo.id && customerId == paymentInfo.customerId && cardNumber.equals(paymentInfo.cardNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, cardNumber);
    }

    @NonNull
    @Override
    public String toString() {
        return String.format(Locale.US, "PaymentInfo(id=%d, customerId=%d, cardNumber=%s)", id, customerId, cardNumber);
    }
}
