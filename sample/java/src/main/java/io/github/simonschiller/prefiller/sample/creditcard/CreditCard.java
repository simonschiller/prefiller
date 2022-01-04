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
