package com.avion.alarmmanager.util

import java.util.concurrent.atomic.AtomicInteger

object RandomIntUtil {
    private val seed = AtomicInteger()

    fun getRandomInteger() = seed.getAndIncrement() + System.currentTimeMillis().toInt()
}