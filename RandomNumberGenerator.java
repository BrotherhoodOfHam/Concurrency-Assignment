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

    public void run()
    {
        try
        {
            for (int i = 0; i < this.numberCount; i++)
            {
                this.chan.put(this.rand.nextInt(Integer.MAX_VALUE));
            }

            this.chan.close();
        }
        catch (InterruptedException e)
        {
            //Cancel task if an interrupt occurs
            return;
        }
    }
}