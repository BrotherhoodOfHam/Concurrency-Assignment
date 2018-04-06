import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.ArrayBlockingQueue;

/**
 *  Program main class
 */
public class Main
{
    public static void main(String[] args) throws InterruptedException, IOException
    {
        FileWriter evenWriter = new FileWriter("even-numbers");
        FileWriter oddWriter = new FileWriter("odd-numbers");

        try
        {
            //ArrayBlockingQueue queue = new ArrayBlockingQueue<Integer>(5);
            BoundedBuffer buffer = new BoundedBuffer<Integer>(50);

            Producer pr = new Producer(buffer, 10000);    
            Consumer c0 = new Consumer(buffer, evenWriter);
            Consumer c1 = new Consumer(buffer, oddWriter);

            System.out.println("start");
    
            Thread t1 = new Thread(c0);
            //t0.start();
            t1.start();
    
            pr.run();
    
            //t0.join();
            t1.join();
            System.out.println("done");
        }
        finally
        {
            evenWriter.close();
            oddWriter.close();
        }
    }
}
