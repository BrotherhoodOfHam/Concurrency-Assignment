import java.util.Random;

/**
 * Thread producer class
 */
class Producer implements Runnable
{
    private final int RAND_RANGE = 1000000;

    private BoundedBuffer<Integer> buffer;
    private Random rand;
    private int dataSize;

    /**
     * Construct a producer object
     * @param buffer output buffer
     * @param dataSize size of data to be produced
     */
    public Producer(BoundedBuffer<Integer> buffer, int dataSize)
    {
        this.buffer = buffer;
        this.rand = new Random();
        this.dataSize = dataSize;
    }

    public void run()
    {
        try
        {
            for (int i = 0; i < this.dataSize; i++)
            {
                this.buffer.put(this.rand.nextInt(RAND_RANGE));
            }

            //Signals consumers to exit
            this.buffer.put(-1);
        }
        catch (InterruptedException e)
        {
            //Cancel task if an interrupt occurs
            return;
        }
    }
}