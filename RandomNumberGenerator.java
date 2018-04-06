import java.util.Random;

/**
 * Random Number Generator Task
 */
class RandomNumberGenerator implements Runnable
{
    private BoundedBuffer<Integer> buffer;
    private Random rand;
    private int numberCount;

    /**
     * Construct a RNG object
     * @param buffer output buffer
     * @param numberCount number of numbers to generate
     */
    public RandomNumberGenerator(BoundedBuffer<Integer> buffer, int numberCount)
    {
        this.buffer = buffer;
        this.rand = new Random();
        this.numberCount = numberCount;
    }

    public void run()
    {
        try
        {
            for (int i = 0; i < this.numberCount; i++)
            {
                this.buffer.put(this.rand.nextInt(Integer.MAX_VALUE));
            }

            //Signals consumers to exit - todo: fix this
            this.buffer.put(null);
            this.buffer.put(null);
        }
        catch (InterruptedException e)
        {
            //Cancel task if an interrupt occurs
            return;
        }
    }
}