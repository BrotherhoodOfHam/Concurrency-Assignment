import java.util.concurrent.ArrayBlockingQueue;

/**
 *  Program main class
 */
public class Main
{
    public static void main(String[] args) throws InterruptedException
    {
        ArrayBlockingQueue queue = new ArrayBlockingQueue<Integer>(100);
        Producer pr = new Producer(queue, 100);
        Consumer co = new Consumer(queue, System.out);

        System.out.println("start");

        Thread t0 = new Thread(pr);
        Thread t1 = new Thread(co);
        t0.start();
        t1.start();

        t0.join();
        System.out.println("done");
    }
}
