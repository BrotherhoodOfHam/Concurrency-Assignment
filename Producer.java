import java.io.IOException;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.BlockingQueue;

/**
 * Thread producer class
 */
class Producer implements Runnable
{
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
        for (int i = 0; i < this.dataSize; i++)
        {
            try
            {
                this.queue.put(this.rand.nextInt(1000));
            }
            catch (InterruptedException e)
            {
                System.err.println(e.getMessage());
            }
        }
    }
}