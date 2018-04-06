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
        FileWriter fwriter = new FileWriter("numbers");

        try
        {
            ArrayBlockingQueue queue = new ArrayBlockingQueue<Integer>(30);
            Producer pr = new Producer(queue, 10000);    
            Consumer co = new Consumer(queue, fwriter);

            System.out.println("start");
    
            Thread t1 = new Thread(co);
            //t0.start();
            t1.start();
    
            pr.run();
    
            //t0.join();
            t1.join();
            System.out.println("done");
        }
        finally
        {
            fwriter.close();
        }
    }
}
