
### ThreadPoolExecutor

implements Runnable 的run方法可以多次调用，相当于一个普通方法的调用；
Thread 的start()方法不能多次调用，原理为系统新开一个线程去执行runnabl的run方法，当重复调用的时候会发生IllegalThreadStateException异常；

>构造函数如下：

```
public ThreadPoolExecutor(int corePoolSize, //保留的线程池大小
                              int maximumPoolSize,//
                              long keepAliveTime,
                              TimeUnit unit,
                              BlockingQueue<Runnable> workQueue) {
        this(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue,
             Executors.defaultThreadFactory(), defaultHandler);
    }

```

* corePoolSize 保留的线程池大小；
* maximumPoolSize 线程池最大容量；
* keepAliveTime 线程池维护线程所允许的空闲时间
* unit 线程池维护线程所允许的空闲时间的单位，枚举
* workQueue 线程池所使用的缓冲队列
* threadFactory 创建Thrad的工厂，默认为DefaultThreadFactory；
* handler 线程池对拒绝任务的处理策略，默认值ThreadPoolExecutor.AbortPolicy()；

**workQueue ：** 

* ArrayBlockingQueue：　　基于数组结构的有界队列，此队列按FIFO原则对任务进行排序。如果队列满了还有任务进来，则调用拒绝策略。
* LinkedBlockingQueue：　 基于链表结构的无界队列，此队列按FIFO原则对任务进行排序。因为它是无界的，根本不会满，所以采用此队列后线程池将忽略拒绝策略（handler）参数；同时还将忽略最大线程数（maximumPoolSize）等参数。
* SynchronousQueue：　　 直接将任务提交给线程而不是将它加入到队列，实际上此队列是空的。每个插入的操作必须等到另一个调用移除的操作；如果新任务来了线程池没有任何可用线程处理的话，则调用拒绝策略。其实要是把maximumPoolSize设置成无界

**handler**：

* AbortPolicy：拒绝任务，抛出RejectedExecutionException异常。默认值。如果不try catch的话，程序会阻塞;
* CallerRunsPolicy 重试添加当前的任务，他会自动重复调用execute()方法;
* DiscardOldestPolicy 抛弃旧的任务;
* DiscardPolicy 抛弃当前的任务;
 
 使用时为了不丢弃任务，建议线程队列使用LinkedBlockingQueue，





	






