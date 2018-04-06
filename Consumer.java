import java.io.IOException;
import java.io.Writer;
import java.io.PrintWriter;
import java.util.function.*;

/**
 * Thread consumer class
 */
public class Consumer implements Runnable
{
    private BoundedBuffer<Integer> buffer;
    private PrintWriter output;
    private Predicate<Integer> predicate;

    public Consumer(BoundedBuffer<Integer> buffer, Writer output, Predicate<Integer> predicate)
    {
        this.buffer = buffer;
        this.output = new PrintWriter(output);
        this.predicate = predicate;
    }

    public void run()
    {
        try
        {
            while (true)
            {
                Integer i = buffer.takeIf(predicate);

                if (i == null)
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
