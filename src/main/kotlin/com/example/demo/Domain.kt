package com.example.demo

import java.util.UUID

open class Domain {
    companion object Factory {
        fun generateUUID(): UUID {
            return UUID.randomUUID()
        }
    }
}