
# 并发编程：Kotlin Coroutines 并发安全
Kotlin 协程作为一种强大的工具，提供了简洁而高效的方式来处理并发任务。然而，随着多个协程同时操作共享数据，开发者们面临着竞态条件和数据竞争的挑战。
本demo 将深入研究 Kotlin 协程中的并发安全性问题，介绍解决方案和最佳实践，帮助读者在异步的世界中游刃有余。
先看几个例子：

1，
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
count 的结果是多少？

2，

fun main() = runBlocking{

    val jobs = mutableListOf<Job>()
    val mutex = Mutex()
    val timeCost = measureTimeMillis {
        repeat(1000){
            val job = launch(Dispatchers.Default) {
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
这个count结果呢？

3，

fun main() = runBlocking {

    val jobs = mutableListOf<Job>()
    val timeCost = measureTimeMillis {
        val job = launch(Dispatchers.Default) {
            repeat(100) {
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

这个呢？

4，
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

第一个例子：count结果是1000,因为没有发生线程切换，并不会出现并发安全，所以答案是1000。
第二个例子：count的结果肯定小于1000，因为多线程访问，会出现并发安全问题。需要同步。
第三个例子：count结果是1000,和第一个例子一样，因为没有发生线程切换，并不会出现并发安全，所以答案是1000。
第四个例子：count结果是1000，在等待的结果中+1，并不会出现并发安全，所以答案是1000。

5,并发获取User的例子，记录获取成功的count

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

这个count 就不需要额外的同步，想想这是为什么？



## 结论
协程并发安全问题，实际上多数时候我们并不需要这么做，我们完全可以想办法因可变状态的共享而引发的安全问题，总而言之，如非必须，则避免访问外部可变状态；
如无必要，则避免使用可变状态。


















