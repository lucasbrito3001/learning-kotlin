package com.lucasdbrito.application.domain

import java.util.UUID

open class Domain {
    companion object Factory {
        fun generateUUID(): UUID {
            return UUID.randomUUID()
        }
    }
}