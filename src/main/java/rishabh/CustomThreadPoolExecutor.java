package rishabh;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

public final class CustomThreadPoolExecutor {
    public BlockingDeque<Runnable> workerQueue;
    Thread[] workerThreads;
    int poolSize;
    private final AtomicBoolean isInterrupted = new AtomicBoolean(false);

    public CustomThreadPoolExecutor(int poolSize) {
        this.poolSize = poolSize;
        workerQueue = new LinkedBlockingDeque<>();
        workerThreads = new Thread[poolSize];
        startWorkerThreads();
    }

    public void startWorkerThreads() {

        for (int i = 0; i < poolSize; i++) {
            workerThreads[i] = new WorkerThread("Thread " + i, isInterrupted, workerQueue);
            workerThreads[i].start();
        }
    }

    public void shutDownPool() {
        isInterrupted.set(true);
    }

    public void submit(Runnable runnableTask) throws InterruptedException {
        RunnableFuture<?> runnableFuture = new FutureTask<>(runnableTask, null);
        workerQueue.put(runnableFuture);
    }

    public <T> Future<T> submit(Callable<T> callableTask) throws InterruptedException {
        RunnableFuture<T> callableFuture = new FutureTask<>(callableTask);
        workerQueue.put(callableFuture);
        return callableFuture;
    }

    public static void main(String[] args) throws InterruptedException, ExecutionException, TimeoutException {
        CustomThreadPoolExecutor executor = new CustomThreadPoolExecutor(5);

        executor.submit(() -> System.out.println("First Task"));
        Future<Integer> future = executor.submit(() -> 10);
        System.out.println(future.get(5, TimeUnit.MILLISECONDS));

        executor.shutDownPool();

    }
}


