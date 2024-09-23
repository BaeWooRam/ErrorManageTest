package com.test.errormanager

import kotlin.random.Random

object Error {
    fun executeException() {
        val randomSeed = Random.nextInt(0, 5)
        val msg = "recordException $randomSeed"
        val exception = when (randomSeed) {
            0 -> NullPointerException(msg)
            1 -> ArrayIndexOutOfBoundsException(msg)
            2 -> SecurityException(msg)
            3 -> RuntimeException(msg)
            else -> ClassNotFoundException(msg)
        }
        throw exception
    }
}