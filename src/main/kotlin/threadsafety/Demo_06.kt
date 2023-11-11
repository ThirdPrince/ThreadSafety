package threadsafety

import kotlinx.coroutines.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.util.concurrent.Executors
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import kotlin.system.measureTimeMillis

/**
 * 避免修改外部状态
 */
fun main() = runBlocking {

    val count = 0
    val timeCost = measureTimeMillis {
        val result = count + List(1000) {
            GlobalScope.async {
                delay(100)
                1
            }
        }.sumOf {
            it.await()
        }
        log("result -->$result")
    }

    log("timeCost -->$timeCost")


}

