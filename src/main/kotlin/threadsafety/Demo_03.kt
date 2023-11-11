package threadsafety

import kotlinx.coroutines.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.util.concurrent.Executors
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import kotlin.system.measureTimeMillis

/**
 * 本质上是串行执行
 * 不存在并发
 */
fun main() = runBlocking {

    val jobs = mutableListOf<Job>()
    val timeCost = measureTimeMillis {
        val job = launch(Dispatchers.Default) {
            repeat(1000) {
                delay(100)
                countVar++
            }
        }
        jobs.add(job)
        jobs.forEach {
            it.join()
        }
    }
    log("timeCost =$timeCost")
    log("count =$countVar")
}

