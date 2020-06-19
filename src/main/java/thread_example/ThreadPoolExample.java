package thread_example;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ThreadPoolExample {

    public static class Runner implements Runnable {
        private int id;

        Runner(int id) {
            this.id = id;
        }

        @Override
        public void run() {
            System.out.println("Starting " + id);

            //code for processing files, here we will use Thread.sleep for that
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.out.println("Completed " + id);
        }
    }

    public static void main(String[] args) {
        //Created a thread pool of 2 threads --> ThreadPool is like having n number of workers in factory
        ExecutorService executor = Executors.newFixedThreadPool(2);

        //Executing 10 tasks --> task will be divided into above n threads
        for (int i=0; i<10; i++) {
            executor.submit(new Runner(i));
        }

        //shutdown() --> stop accepting new tasks and shut itself down when all the tasks have finished.
        executor.shutdown();

        System.out.println("All tasks Submitted");

        //Waittime = 1 hour, for all tasks to be completed by this time
        try {
            executor.awaitTermination(1, TimeUnit.HOURS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("All tasks completed.");
    }
}
