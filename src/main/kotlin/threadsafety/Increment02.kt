package threadsafety

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlin.math.log

suspend fun main() {
    val count = 0
    val result = count + List(1000) {
        GlobalScope.async {
            delay(5000)
            1
        }
    }.sumOf {
        it.await()
    }
    println("count -->$count")
    println("result-->$result")
}



