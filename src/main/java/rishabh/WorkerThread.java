package rishabh;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

class WorkerThread extends Thread {
    private final BlockingQueue<Runnable> workerQueue;
    private final AtomicBoolean isInterrupted;

    public WorkerThread(String threadName, AtomicBoolean isInterrupted, BlockingQueue<Runnable> workerQueue) {
        super(threadName);
        this.workerQueue = workerQueue;
        this.isInterrupted = isInterrupted;
    }

    public void run() {
        System.out.println(Thread.currentThread().getName() + " started");
        while (!isInterrupted.get()) {
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
