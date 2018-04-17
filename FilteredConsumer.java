import java.io.IOException;
import java.io.Writer;
import java.io.PrintWriter;
import java.util.function.Predicate;

/**
 * FilteredConsumer Task
 * 
 * Consumes objects from a given buffer that satisfy a given predicate.
 */
public class FilteredConsumer<E> implements Runnable
{
    private Channel<E> chan;
    private PrintWriter output;
    private Predicate<E> predicate;

    public FilteredConsumer(Channel<E> chan, Writer output, Predicate<E> predicate)
    {
        this.chan = chan;
        this.output = new PrintWriter(output);
        this.predicate = predicate;
    }

    public void run()
    {
        try
        {
            while (true)
            {
                E i = chan.takeIf(predicate);

                if (i != null)
                {
                    System.out.println(i.toString());
                    output.println(i.toString());
                }
                else
                {
                    break;
                }
            }
        }
        catch (InterruptedException e)
        {
            //Cancel task if an interrupt occurs
            return;
        }
    }
}
