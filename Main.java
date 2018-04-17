import java.io.FileWriter;
import java.io.IOException;
import java.util.function.Predicate;

/**
 *  Program main class
 */
public class Main
{
    public static void main(String[] args) throws InterruptedException, IOException
    {
        //Number of numbers to produce - n,m
        int numberGenCount = 1000;
        int channelSize = 4;

        //Optional command line options
        try
        {
            for (String a : args)
                System.out.println(a);

            if (args.length > 1)
            {
                numberGenCount = Integer.parseUnsignedInt(args[0]); 
                channelSize = Integer.parseUnsignedInt(args[1]);
            }
        }
        catch(NumberFormatException e)
        {
            System.err.println("Unknown argument");
            System.exit(-1);
        }

        //Print parameters
        System.out.println("n = " + numberGenCount);
        System.out.println("m = " + channelSize);

        FileWriter evenWriter = new FileWriter("even-numbers");
        FileWriter oddWriter = new FileWriter("odd-numbers");

        try
        {
            Channel chan = new Channel<Integer>(channelSize);

            Predicate<Integer> isEven = item -> { return item % 2 == 0; };
            Predicate<Integer> isOdd = isEven.negate();

            RandomNumberGenerator pr = new RandomNumberGenerator(chan, numberGenCount);
            
            Thread t0 = new Thread(new FilteredConsumer<Integer>(chan, evenWriter, isEven));
            Thread t1 = new Thread(new FilteredConsumer<Integer>(chan, oddWriter, isOdd));

            System.out.println("start");

            t0.start();
            t1.start();

            pr.run();

            t0.join();
            t1.join();

            System.out.println("done");
        }
        finally
        {
            evenWriter.close();
            oddWriter.close();
        }
    }
}
