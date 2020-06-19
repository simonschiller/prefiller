package io.github.simonschiller.prefiller.sample.creditcard

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import io.github.simonschiller.prefiller.sample.customer.Customer

@Entity(tableName = "creditcards", indices = [Index("customerId")], foreignKeys = [
    ForeignKey(entity = Customer::class, parentColumns = ["id"], childColumns = ["customerId"])
])
data class CreditCard(
    @PrimaryKey(autoGenerate = true) val id: Long,
    val customerId: Long,
    val cardNumber: String
)
