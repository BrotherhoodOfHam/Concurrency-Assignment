import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Predicate;
import java.util.ArrayList;

/**
 *  Program main class
 * 
 *  Can be invoked with optional command-line arguments:
 *      {number-count} {channel-capacity} {task-count}
 *            n                m               k     
 */
public class Main
{
    /**
     * Entry point
     */
    public static void main(String[] args) throws InterruptedException, IOException
    {
        //n, m, k
        int numberGenCount  = getIntArg(args, 0, 1000);
        int channelCapacity = getIntArg(args, 1, 4);
        int taskCount       = getIntArg(args, 2, 2);

        //Print parameters
        System.out.println("n = " + numberGenCount);
        System.out.println("m = " + channelCapacity);
        System.out.println("k = " + taskCount);

        FileWriter evenWriter = new FileWriter("even-numbers");
        FileWriter oddWriter = new FileWriter("odd-numbers");

        Channel chan = new Channel<Integer>(channelCapacity);

        Predicate<Integer> isEven = item -> { return item % 2 == 0; };
        Predicate<Integer> isOdd = isEven.negate();

        RandomNumberGenerator pr = new RandomNumberGenerator(chan, numberGenCount);

        ExecutorService executor = Executors.newFixedThreadPool(taskCount);
        //Start consumers
        executor.submit(new FilteredConsumer<Integer>(chan, evenWriter, isEven));
        executor.submit(new FilteredConsumer<Integer>(chan, oddWriter, isOdd));

        //k
        for (int i = 3; i <= taskCount; i++)
        {
            //Lambda functions expect local variables to be immutable - so copy i
            final int mult = i;
            //Predicate that tests if a value is a multiple of i
            Predicate<Integer> multipleOfI = item -> { return item % mult == 0; };

            FileWriter output = new FileWriter("multiples-of-" + i);

            executor.submit(new FilteredConsumer(chan, output, multipleOfI));
        }

        System.out.println("running...");

        //Start producer task
        pr.run();
        //Wait for consumers to finish
        executor.shutdown();
        
        System.out.println("done.");
    }
    
    /**
     * Helper method:
     * 
     * Get an integer argument at the given index
     * 
     * If the given index is out of range, return the default
     */
    private static int getIntArg(String[] args, int index, int def)
    {
        try
        {
            if (index < args.length && index >= 0)
            {
                return Integer.parseUnsignedInt(args[index]);
            }
        }
        catch(NumberFormatException e)
        {
            System.err.println("Invalid integer argument at " + index);
        }

        return def;
    }
}
