package com.lucasdbrito.application.domain

import org.junit.jupiter.api.Test
import java.util.UUID
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class TransactionTest {
    @Test
    fun shouldCreateTransaction() {
        val value = 10f
        val type = TransactionTypes.PIX
        val transaction = Transaction.create(value, type)

        assertNotNull(transaction.id)
        assert(transaction.value == value)
        assert(transaction.type == type)
        assert(transaction.status == TransactionStatuses.PENDING)
    }

    @Test
    fun shouldInstanceExistentTransaction() {
        val uuid = UUID.randomUUID()
        val value = 10f
        val type = TransactionTypes.PIX
        val status = TransactionStatuses.APPROVED

        val transaction = Transaction.instance(uuid, value, type, status)

        assert(transaction.id == uuid)
        assert(transaction.value == value)
        assert(transaction.type == type)
        assert(transaction.status == status)
    }

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