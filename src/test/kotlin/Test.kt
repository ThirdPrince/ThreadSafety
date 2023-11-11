import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import threadsafety.log
import kotlin.system.measureTimeMillis

@Test
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