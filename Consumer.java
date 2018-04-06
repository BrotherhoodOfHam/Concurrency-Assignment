import java.io.IOException;
import java.io.Writer;
import java.io.PrintWriter;
import java.util.concurrent.BlockingQueue;

/**
 * Thread consumer class
 */
public class Consumer implements Runnable
{
    private BlockingQueue<Integer> queue;
    private PrintWriter output;

    public Consumer(BlockingQueue<Integer> queue, Writer output)
    {
        this.queue = queue;
        this.output = new PrintWriter(output);
    }

    public void run()
    {
        try
        {
            while (true)
            {
                int i = queue.take().intValue();

                if (i < 0)
                {
                    break;
                }

                System.out.println(i);
                output.println(i);
            }
        }
        catch (InterruptedException e)
        {
            //Cancel task if an interrupt occurs
            return;
        }
    }
}
