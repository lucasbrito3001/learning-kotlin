package com.lucasdbrito.application.domain

import java.util.UUID

sealed class ResultPayTransaction<out T> {
    data class Success<out T>(val value: T) : ResultPayTransaction<T>()
    data class DuplicatedTransaction(
        val name: String = "DUPLICATED_TRANSACTION",
        val errorMessage: String = "Already exists a transaction with this id in the wallet"
    ) :
        ResultPayTransaction<Nothing>()

    data class InsufficientBalance(
        val name: String = "INSUFFICIENT_BALANCE",
        val errorMessage: String = "The wallet don't have enough balance to complete this transaction"
    ) :
        ResultPayTransaction<Nothing>()
}

sealed class ResultAddBalance<out T> {
    data class Success<out T>(val value: T) : ResultAddBalance<T>()

    data class InvalidValue(
        val name: String = "INVALID_VALUE",
        val errorMessage: String = "Can't add negative values to balance"
    ) :
        ResultAddBalance<Nothing>()
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

    fun addBalance(value: Float): ResultAddBalance<Unit> {
        if (value < 0) return ResultAddBalance.InvalidValue()

        this.balance += value
        return ResultAddBalance.Success(Unit)
    }

    fun payTransaction(transaction: Transaction): ResultPayTransaction<Unit> {
        val duplicatedTransaction = transactions.find { it.id == transaction.id }

        if (duplicatedTransaction != null) return ResultPayTransaction.DuplicatedTransaction()

        this.transactions.add(transaction)

        if (this.balance < transaction.value) {
            transaction.cancel()
            return ResultPayTransaction.InsufficientBalance()
        }

        transaction.approve()
        this.balance -= transaction.value

        return ResultPayTransaction.Success(Unit)
    }
}