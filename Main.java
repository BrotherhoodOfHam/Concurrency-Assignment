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
            BoundedBuffer buffer = new BoundedBuffer<Integer>(5);

            Predicate<Integer> isEven = item -> { return item % 2 == 0; };
            Predicate<Integer> isOdd = isEven.negate();

            RandomNumberGenerator pr = new RandomNumberGenerator(buffer, 10000);
            
            Thread t0 = new Thread(new Filter<Integer>(buffer, evenWriter, isEven));
            Thread t1 = new Thread(new Filter<Integer>(buffer, oddWriter, isOdd));

            System.out.println("start");

            t0.start();
            t1.start();

            pr.run();

            t0.join();
            t1.join();

            System.out.println("done: " + buffer.getSize());
        }
        finally
        {
            evenWriter.close();
            oddWriter.close();
        }
    }
}
