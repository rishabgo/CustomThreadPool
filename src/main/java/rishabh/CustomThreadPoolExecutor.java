package rishabh;

import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

public class CustomThreadPoolExecutor {
    public BlockingDeque<Runnable> workerQueue;
    Thread[] workerThreads;
    int poolSize;

    private volatile boolean isInterrupted;

    public CustomThreadPoolExecutor(int poolSize) {
        this.poolSize = poolSize;
        workerQueue = new LinkedBlockingDeque<>();
        workerThreads = new Thread[poolSize];
        startWorkerThreads();
    }

    public void startWorkerThreads() {

        for (int i = 0; i < poolSize; i++) {
            workerThreads[i] = new WorkerThread("Thread " + i);
            workerThreads[i].start();
        }
    }

    public void shutDownPool() {
        isInterrupted = true;
    }

    public void submit(Runnable runnableTask) throws InterruptedException {
        workerQueue.put(runnableTask);
    }

    class WorkerThread extends Thread {
        public WorkerThread(String name) {
            super(name);
        }

        public void run() {
            System.out.println(Thread.currentThread().getName() + " started");
            while (!isInterrupted) {
                try {
                    Runnable runnableTask = workerQueue.take();
                    runnableTask.run();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            System.out.println(Thread.currentThread().getName() + " stopped");
        }
    }

    public static void main(String[] args) throws InterruptedException {
        CustomThreadPoolExecutor executor = new CustomThreadPoolExecutor(5);

        executor.submit(() -> System.out.println("First Task"));
        executor.submit(() -> System.out.println("Second Task"));

        executor.shutDownPool();
    }
}


