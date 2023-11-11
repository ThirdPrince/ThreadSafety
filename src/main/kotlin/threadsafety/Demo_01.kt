package threadsafety

import kotlinx.coroutines.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.util.concurrent.Executors
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import kotlin.system.measureTimeMillis



/**
 * 启动100个协程
 * 单线程调度器（main）
 * 线程安全
 */
var countVar = 0
fun main() = runBlocking{

    val jobs = mutableListOf<Job>()
    val timeCost = measureTimeMillis {
        repeat(1000){
            val job = launch {
                delay(100)
                countVar++

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

