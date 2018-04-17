import java.util.Random;

/**
 * Random Number Generator Task
 */
class RandomNumberGenerator implements Runnable
{
    private Channel<Integer> chan;
    private Random rand;
    private int numberCount;

    /**
     * Construct a RNG object
     * @param chan output channel
     * @param numberCount number of numbers to generate
     */
    public RandomNumberGenerator(Channel<Integer> chan, int numberCount)
    {
        this.chan = chan;
        this.rand = new Random();
        this.numberCount = numberCount;
    }

    @Override
    public void run()
    {
        try
        {
            //Generate up to numberCount random numbers
            for (int i = 0; i < this.numberCount; i++)
            {
                //Write random number into the channel
                int val = this.rand.nextInt(Integer.MAX_VALUE);
                
                //System.out.println(val);

                this.chan.write(val);
            }

            //Close channel - forces consumers to exit
            this.chan.close();
        }
        catch (InterruptedException e)
        {
            //Cancel task if an interrupt occurs
            return;
        }
    }
}