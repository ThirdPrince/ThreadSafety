package threadsafety

import kotlinx.coroutines.*
import kotlinx.coroutines.internal.synchronized
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlin.concurrent.thread
import kotlin.system.measureTimeMillis

var count = 0
suspend fun main() {

    //lockWithMutex()
    count = 0
    lockWithSyn()



}

suspend fun lockWithMutex() {
    val mutex = Mutex()
    val deferredList = mutableListOf<Deferred<Int>>()
    val timeCost = measureTimeMillis {
        coroutineScope {
            List(1000) {
                val result = async {
                    mutex.withLock {
                        increment()
                    }

                }
                deferredList.add(result)
            }
            deferredList.awaitAll()
        }

    }
    println("lockWithMute-->count -->$count")
    println("lockWithMutex--> timeCost-->$timeCost")
}



@OptIn(InternalCoroutinesApi::class)
suspend fun lockWithSyn(){
    val deferredList = mutableListOf<Deferred<Int>>()
    val lock = Any()
    val timeCost = measureTimeMillis {
            coroutineScope {
                List(1000) {
                val result = async {
                   // synchronized(lock){
                        increment2(lock)
                //    }


                }
                deferredList.add(result)
            }
            deferredList.awaitAll()
            }

    }
    println("lockWithSyn-->count -->$count")
    println("lockWithSyn-->timeCost-->$timeCost")
}
suspend fun increment() = withContext(Dispatchers.IO){
    delay(20)
    count++
}


suspend fun increment2(lock:Any)= withContext(Dispatchers.IO){
    delay(20)
    count++

}