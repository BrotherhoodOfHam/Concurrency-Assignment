import java.io.IOException;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.BlockingQueue;

/**
 * Thread producer class
 */
class Producer implements Runnable
{
    private final int RAND_RANGE = 1000000;

    private BlockingQueue<Integer> queue;
    private Random rand;
    private int dataSize;

    /**
     * Construct a producer object
     * @param q output queue
     * @param dataSize size of data to be produced
     */
    public Producer(BlockingQueue<Integer> q, int dataSize)
    {
        this.queue = q;
        this.rand = new Random();
        this.dataSize = dataSize;
    }

    public void run()
    {
        try
        {
            for (int i = 0; i < this.dataSize; i++)
            {
                //this.queue.put(i + 1);
                this.queue.put(this.rand.nextInt(RAND_RANGE));
            }

            //Signals consumers to stop consuming
            this.queue.put(-1);
        }
        catch (InterruptedException e)
        {
            //Cancel task if an interrupt occurs
            return;
        }
    }
}