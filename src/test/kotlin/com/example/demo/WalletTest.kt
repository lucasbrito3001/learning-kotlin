package com.example.demo

import com.example.demo.Result
import org.junit.jupiter.api.Test
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

        assertEquals(Result.InsufficientBalance(), result)
    }

    @Test
    fun shouldGetDuplicatedTransactionWhenPayTransaction() {
        val wallet = Wallet.create(110f)
        val transaction1 = Transaction.create(100f, TransactionTypes.PIX)

        wallet.payTransaction(transaction1)
        val result = wallet.payTransaction(transaction1)

        assertEquals(Result.DuplicatedTransaction(), result)
    }

    @Test
    fun shouldPayTransactionSuccessfully() {
        val wallet = Wallet.create(110f)
        val transaction1 = Transaction.create(100f, TransactionTypes.PIX)

        val result = wallet.payTransaction(transaction1)

        assertEquals(Result.Success(Unit), result)
        assertEquals(1, wallet.transactions.size)
    }
}