package com.example.demo

import java.util.UUID

sealed class Result<out T> {
    data class Success<out T>(val value: T) : Result<T>()
    data class DuplicatedTransaction(
        val name: String = "DUPLICATED_TRANSACTION",
        val errorMessage: String = "Already exists a transaction with this id in the wallet"
    ) :
        Result<Nothing>()

    data class InsufficientBalance(
        val name: String = "INSUFFICIENT_BALANCE",
        val errorMessage: String = "The wallet don't have enough balance to complete this transaction"
    ) :
        Result<Nothing>()
}

class Wallet private constructor(
    var id: UUID,
    balance: Float,
    transactions: MutableSet<Transaction>
) : Domain() {
    var balance: Float = balance
        private set
    var transactions: MutableSet<Transaction> = transactions
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

    fun payTransaction(transaction: Transaction): Result<Unit> {
        val duplicatedTransaction = transactions.find { it.id == transaction.id }

        if (duplicatedTransaction != null) return Result.DuplicatedTransaction()

        this.transactions.add(transaction)

        if (this.balance < transaction.value) {
            transaction.cancel()
            return Result.InsufficientBalance()
        }

        transaction.approve()
        this.balance -= transaction.value

        return Result.Success(Unit)
    }
}