import java.io.IOException;
import java.io.PrintStream;
import java.util.concurrent.BlockingQueue;

/**
 * Thread consumer class
 */
public class Consumer implements Runnable
{
    private BlockingQueue<Integer> queue;
    private PrintStream output;

    public Consumer(BlockingQueue<Integer> queue, PrintStream output)
    {
        this.queue = queue;
        this.output = output;
    }

    public void run()
    {
        while (!queue.isEmpty())
        {
            int i = 0;

            try
            {
                i = queue.take().intValue();
            }
            catch (InterruptedException e)
            {
                System.err.println(e.getMessage());
            }

            output.println(i);
        }
    }
}
