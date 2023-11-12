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

     val startTime = System.currentTimeMillis()
     val userIds: MutableList<Int> = ArrayList()
     for (i in 1..1000) {
          userIds.add(i)
     }
     var count = userIds.size
     val map: MutableMap<Int, User> = HashMap()
     val deferredResults = userIds.map { userId ->
          async {
               val user = getUserAsync(userId)
               //log("userId-->$userId :::: user --->  $user")
               map[userId] = user
               map
          }
     }


     // 获取每个 async 任务的结果
     val results = deferredResults.map { deferred ->
          count--
          log("count  $count")
          deferred.await()
     }
     val costTime = (System.currentTimeMillis() - startTime) / 1000
     log("count -> $count")
     log("costTime-->$costTime")
     log("user size -> ${results.size}")

}

/**
 * 异步同步化
 */
suspend fun getUserAsync(userId: Int): User = suspendCoroutine { continuation ->
     ClientManager.getUser(userId) {
          continuation.resume(it)
     }
}

