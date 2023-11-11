package threadsafety

import kotlinx.coroutines.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.util.concurrent.Executors
import java.util.concurrent.atomic.AtomicInteger
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import kotlin.system.measureTimeMillis

/**
 * CAS 乐观锁
 */
fun main() = runBlocking {
    val atomicInteger = AtomicInteger()
    val jobs = mutableListOf<Job>()
    val timeCost = measureTimeMillis {
        repeat(100) {
            val job = launch(Dispatchers.Default) {
                delay(100)
                atomicInteger.incrementAndGet()
            }
            jobs.add(job)
        }
        jobs.forEach {
            it.join()
        }
    }
    log("timeCost =$timeCost")
    log("count =${atomicInteger.get()}")

}

