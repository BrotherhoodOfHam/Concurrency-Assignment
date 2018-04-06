import java.io.IOException;
import java.io.Writer;
import java.io.PrintWriter;

/**
 * Thread consumer class
 */
public class Consumer implements Runnable
{
    private BoundedBuffer<Integer> buffer;
    private PrintWriter output;

    public Consumer(BoundedBuffer<Integer> buffer, Writer output)
    {
        this.buffer = buffer;
        this.output = new PrintWriter(output);
    }

    public void run()
    {
        try
        {
            while (true)
            {
                int i = buffer.take().intValue();

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
