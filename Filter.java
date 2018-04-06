import java.io.IOException;
import java.io.Writer;
import java.io.PrintWriter;
import java.util.function.Predicate;

/**
 * Filter Task
 * 
 * Consumes objects from a given buffer that satisfy a given predicate.
 */
public class Filter<E> implements Runnable
{
    private BoundedBuffer<E> buffer;
    private PrintWriter output;
    private Predicate<E> predicate;

    public Filter(BoundedBuffer<E> buffer, Writer output, Predicate<E> predicate)
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
                E i = buffer.takeIf(predicate);

                if (i == null)
                {
                    break;
                }

                System.out.println(i.toString());
                output.println(i.toString());
            }
        }
        catch (InterruptedException e)
        {
            //Cancel task if an interrupt occurs
            return;
        }
    }
}
