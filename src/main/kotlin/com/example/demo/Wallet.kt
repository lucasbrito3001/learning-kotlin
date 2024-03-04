package com.example.demo

import java.time.LocalDate
import java.time.Period
import java.util.Date
import java.util.UUID

sealed class PayTransactionOutput<out T> {
    data class Success<out T>(val value: T) : PayTransactionOutput<T>()
    data class DuplicatedTransaction(
        val name: String = "DUPLICATED_TRANSACTION",
        val errorMessage: String = "Already exists a transaction with this id in the wallet"
    ) :
        PayTransactionOutput<Nothing>()

    data class InsufficientBalance(
        val name: String = "INSUFFICIENT_BALANCE",
        val errorMessage: String = "The wallet don't have enough balance to complete this transaction"
    ) :
        PayTransactionOutput<Nothing>()
}

sealed class GetTransactionsOutput<out T> {
    data class Success<out T>(val value: T) : GetTransactionsOutput<T>()
    data class InvalidDateRange(
        val name: String = "INVALID_DATE_RANGE",
        val errorMessage: String = "The start date must be before the end date"
    ) :
        GetTransactionsOutput<Nothing>()
    data class VeryLargeDateRange(
        val name: String = "VERY_LARGE_DATE_RANGE",
        val errorMessage: String = "The date range must be less than or equal 90 days"
    ) :
        GetTransactionsOutput<Nothing>()
}

class Wallet private constructor(
    var id: UUID,
    balance: Float,
    transactions: MutableSet<Transaction>?
) : Domain() {
    var balance: Float = balance
        private set
    var transactions: MutableSet<Transaction>? = transactions
        private set

    companion object Factory {
        fun create(initialBalance: Float): Wallet {
            val uuid = generateUUID()
            val wallet = Wallet(uuid, initialBalance, mutableSetOf())

            return wallet
        }

        fun instance(id: UUID, balance: Float, transactions: MutableSet<Transaction>): Wallet {
            val wallet = Wallet(id, balance, transactions)

            return wallet
        }
    }

    fun addBalance(value: Float) {
        this.balance += value
    }

    fun payTransaction(transaction: Transaction): PayTransactionOutput<Unit> {
        val duplicatedTransaction = transactions?.find { it.id == transaction.id }

        if (duplicatedTransaction != null) return PayTransactionOutput.DuplicatedTransaction()

        this.transactions?.add(transaction)

        if (this.balance < transaction.value) {
            transaction.cancel()
            return PayTransactionOutput.InsufficientBalance()
        }

        transaction.approve()
        this.balance -= transaction.value

        return PayTransactionOutput.Success(Unit)
    }

    fun getTransactions(startDate: LocalDate, endDate: LocalDate): GetTransactionsOutput<Unit> {
        if(endDate < startDate) return GetTransactionsOutput.InvalidDateRange()

        val diffDatesPeriod: Period = Period.between(startDate, endDate)

        val diffDates: Double = diffDatesPeriod.months * 30.44 + diffDatesPeriod.days

        if(diffDates >= 90) return GetTransactionsOutput.VeryLargeDateRange()

        return GetTransactionsOutput.Success(Unit)
    }
}