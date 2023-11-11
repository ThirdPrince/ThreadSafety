package threadsafety

import kotlinx.coroutines.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.util.concurrent.Executors
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import kotlin.system.measureTimeMillis

/**
 * synchronized
 * 比较重
 */
fun main() = runBlocking {

    val jobs = mutableListOf<Job>()
    val lock = Any()
    val timeCost = measureTimeMillis {
        repeat(100) {
            val job = launch(Dispatchers.Default) {
                delay(100)
                synchronized(lock) {
                    countVar++
                }
            }
            jobs.add(job)
        }

        jobs.forEach {
            it.join()
        }
    }
    log("timeCost =$timeCost")
    log("count =$countVar")

}

