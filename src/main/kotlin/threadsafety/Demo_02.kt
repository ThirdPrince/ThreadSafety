package threadsafety

import kotlinx.coroutines.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.util.concurrent.Executors
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import kotlin.system.measureTimeMillis


/**
 * 多线程调度器
 * 需要配合mutex
 */
fun main() = runBlocking{

    val jobs = mutableListOf<Job>()
    val mutex = Mutex()
    val timeCost = measureTimeMillis {
        repeat(1000){
            val job = launch(Dispatchers.Default) {
                delay(100)
                mutex.withLock{
                    countVar++
                }

            }
            jobs.add(job)
        }
        jobs.forEach{
            it.join()
        }
    }
    log("timeCost =$timeCost")
    log("count =$countVar")
}

