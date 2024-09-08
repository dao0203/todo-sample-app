package com.example.testing.turbine

import android.util.Log
import app.cash.turbine.ReceiveTurbine
import org.junit.Assert

suspend fun <T> ReceiveTurbine<T>.gatherAsList(): List<T> {
    val list = mutableListOf<T>()
    try {
        while (true) {
            val item = awaitItem()
            list.add(item)
        }
    } catch (e: AssertionError) {
        Log.d("TurbineExt", "gatherAsList: $e")
        return list
    } catch (e: Throwable) {
        Log.d("TurbineExt", "gatherAsList: $e")
        throw e
    }
}

fun <T> List<T>.assertContainsExactly(vararg elements: T) {
    if (size != elements.size) {
        Assert.fail("Expected ${elements.toList()} but was $this")
    }

    this.forEachIndexed { index, item ->
        Assert.assertEquals(elements[index], item)
    }
}