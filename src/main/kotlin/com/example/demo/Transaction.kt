package com.example.demo

import java.util.UUID

enum class TransactionTypes { PIX, TED, DOC }

enum class TransactionStatuses { PENDING, APPROVED, CANCELED }

class Transaction private constructor(
    var id: UUID,
    var value: Float,
    var type: TransactionTypes,
    var status: TransactionStatuses
) : Domain() {
    companion object Factory {
        fun create(value: Float, type: TransactionTypes): Transaction {
            val uuid = generateUUID()
            val transaction = Transaction(uuid, value, type, TransactionStatuses.PENDING)

            return transaction
        }

        fun instance(id: UUID, value: Float, type: TransactionTypes, status: TransactionStatuses): Transaction {
            val transaction = Transaction(id, value, type, TransactionStatuses.PENDING)

            return transaction
        }
    }

    fun approve() {
        this.status = TransactionStatuses.APPROVED
    }

    fun cancel() {
        this.status = TransactionStatuses.CANCELED
    }
}