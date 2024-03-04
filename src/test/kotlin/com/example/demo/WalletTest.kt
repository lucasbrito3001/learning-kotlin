package com.example.demo

import org.junit.jupiter.api.Test
import java.time.LocalDate
import kotlin.test.assertEquals

class WalletTest {
    @Test
    fun shouldAddBalanceToWallet() {
        val wallet = Wallet.create(110f)

        wallet.addBalance(100f)

        assertEquals(210f, wallet.balance)
    }

    @Test
    fun shouldGetInsufficientBalanceWhenPayTransaction() {
        val wallet = Wallet.create(10f)
        val transaction1 = Transaction.create(100f, TransactionTypes.PIX)

        val result = wallet.payTransaction(transaction1)

        assertEquals(PayTransactionOutput.InsufficientBalance(), result)
    }

    @Test
    fun shouldGetDuplicatedTransactionWhenPayTransaction() {
        val wallet = Wallet.create(110f)
        val transaction1 = Transaction.create(100f, TransactionTypes.PIX)

        wallet.payTransaction(transaction1)
        val result = wallet.payTransaction(transaction1)

        assertEquals(PayTransactionOutput.DuplicatedTransaction(), result)
    }

    @Test
    fun shouldPayTransactionSuccessfully() {
        val wallet = Wallet.create(110f)
        val transaction1 = Transaction.create(100f, TransactionTypes.PIX)

        val result = wallet.payTransaction(transaction1)

        assertEquals(PayTransactionOutput.Success(Unit), result)
        assertEquals(1, wallet.transactions?.size)
    }

    @Test
    fun shouldReturnInvalidDateRangeErrorWhenGetTransactions() {
        val wallet = Wallet.create(100f)

        val today = LocalDate.now()
        val yesterday = LocalDate.now().minusDays(1)

        val result = wallet.getTransactions(today, yesterday)

        assertEquals(GetTransactionsOutput.InvalidDateRange(), result)
    }

    @Test
    fun shouldReturnVeryLargeDateRangeErrorWhenGetTransactions() {
        val wallet = Wallet.create(100f)

        val today = LocalDate.now()
        val over90DaysAgo = LocalDate.now().minusDays(95)

        val result = wallet.getTransactions(over90DaysAgo, today)

        assertEquals(GetTransactionsOutput.VeryLargeDateRange(), result)
    }

    @Test
    fun shouldReturnWalletTransactionsSuccessfully() {
        val tomorrow = LocalDate.now().plusDays(1)
        val yesterday = LocalDate.now().minusDays(1)

        val wallet = Wallet.create(100f)
        val transaction = Transaction.create(100f, TransactionTypes.PIX)

        wallet.payTransaction(transaction)

        val result = wallet.getTransactions(yesterday, tomorrow)

        assertEquals(GetTransactionsOutput.Success(Unit), result)
    }
}