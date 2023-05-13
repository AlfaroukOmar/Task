package com.alfarouk.task.Util

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.paging.LoadState
import com.alfarouk.task.util.Resource
import com.alfarouk.task.util.Status
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException
@Throws(InterruptedException::class)
fun <T> LiveData<Resource<T>>.getOrAwaitSecondValue(
    time: Long = 20,
    timeUnit: TimeUnit = TimeUnit.SECONDS,
    afterObserve: () -> Unit = {}
): Resource<T> {
    var data: Resource<T>? = null
    var valueCounter = 0
    val latch = CountDownLatch(1)
    val observer = object : Observer<Resource<T>> {
        override fun onChanged(o: Resource<T>?) {
            if (valueCounter == 1) {
                data = o
                latch.countDown()
                this@getOrAwaitSecondValue.removeObserver(this)
            } else {
                valueCounter++
            }
        }
    }
    this.observeForever(observer)
    try {
        afterObserve.invoke()

        // Don't wait indefinitely if the LiveData is not set.
        if (!latch.await(time, timeUnit)) {
            throw TimeoutException("LiveData value was never set.")
        }
    } finally {
        this.removeObserver(observer)
    }
    @Suppress("UNCHECKED_CAST")
    return data as Resource<T>
}

