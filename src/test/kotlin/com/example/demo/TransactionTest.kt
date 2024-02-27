package com.example.demo

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class TransactionTest {
    @Test
    fun shouldApproveTransaction() {
        val transaction = Transaction.create(100f, TransactionTypes.PIX)

        transaction.approve()

        assertEquals(TransactionStatuses.APPROVED, transaction.status)
    }

    @Test
    fun shouldCancelTransaction() {
        val transaction = Transaction.create(100f, TransactionTypes.PIX)

        transaction.cancel()

        assertEquals(TransactionStatuses.CANCELED, transaction.status)
    }
}