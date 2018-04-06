import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.function.Predicate;

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
            BoundedBuffer buffer = new BoundedBuffer<Integer>(10);

            Predicate<Integer> isEven = item -> { return item % 2 == 0; };
            Predicate<Integer> isOdd = isEven.negate();

            Producer pr = new Producer(buffer, 100000);    
            Consumer c0 = new Consumer(buffer, evenWriter, isEven);
            Consumer c1 = new Consumer(buffer, oddWriter, isOdd);

            System.out.println("start");

            Thread t0 = new Thread(c0);
            Thread t1 = new Thread(c1);
            t0.start();
            t1.start();

            pr.run();

            t0.join();
            t1.join();
            System.out.println("done");
            System.out.println(buffer.getSize());
        }
        finally
        {
            evenWriter.close();
            oddWriter.close();
        }
    }
}
