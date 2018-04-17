import java.io.IOException;
import java.io.Writer;
import java.io.PrintWriter;
import java.util.function.Predicate;

/**
 * Filtered Consumer Task
 * 
 * Consumes objects from a given channel that satisfy a given predicate.
 */
public class FilteredConsumer<E> implements Runnable
{
    private Channel<E> chan;
    private PrintWriter output;
    private Predicate<E> predicate;

    /**
     * Construct a consumer task object
     * 
     * @param chan the input channel
     * @param output the output target
     * @param predicate the output condition
     */
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
                E i = chan.readIf(predicate);

                //If item is null
                if (i == null)
                {
                    //Terminate consumption
                    break;
                }
                
                output.println(i);
            }

            //Close writer
            output.close();
        }
        catch (InterruptedException e)
        {
            //Cancel task if an interrupt occurs
            return;
        }
    }
}
