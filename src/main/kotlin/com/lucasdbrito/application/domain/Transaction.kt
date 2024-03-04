package com.lucasdbrito.application.domain

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
            return Transaction(uuid, value, type, TransactionStatuses.PENDING)
        }

        fun instance(id: UUID, value: Float, type: TransactionTypes, status: TransactionStatuses): Transaction {
            return Transaction(id, value, type, status)
        }
    }

    fun approve() {
        this.status = TransactionStatuses.APPROVED
    }

    fun cancel() {
        this.status = TransactionStatuses.CANCELED
    }
}