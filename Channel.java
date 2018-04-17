import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Predicate;

/**
 * Channel class
 * 
 * Provides a mechanism for inter-thread communication
 * assuming a producer -> multi-consumer relationship
 */
public class Channel<E>
{
    private Object[] items; //fixed size item buffer
    private int count = 0;  //number of items in the buffer
    private int read = 0;   //read pointer - position of next item to read
    private int write = 0;  //write pointer - position of next item to write

    //Synchronization primitives
    private Lock itemLock;
    private Condition notFull;
    private Condition notEmpty;

    /*
        Helper methods
    */

    private boolean isEmpty()
    {
        return count == 0;
    }

    private boolean isFull()
    {
        return count == items.length;
    }

    private void incRead()
    {
        count--;
        read = (read + 1) % items.length;
    }

    private void incWrite()
    {
        count++;
        write = (write + 1) % items.length;
    }

    /**
     * Construct a channel object with an internal fixed size buffer with the specified capacity
     */
    public Channel(int capacity)
    {
        items = new Object[capacity];
        itemLock = new ReentrantLock();
        notFull = itemLock.newCondition();
        notEmpty = itemLock.newCondition();
    }

    /**
     * Write an item into the channel
     * 
     * If the buffer is full, method blocks until space becomes available
     */
    public void write(E e) throws InterruptedException
    {
        //Attempt to aquire access to the buffer
        itemLock.lock();

        try
        {
            //Wait until there is space in the buffer
            while(isFull()) notFull.await();

            //Write value into buffer + increment the write pointer
            items[write] = e;
            incWrite();

            //Signal there are available items in the buffer
            notEmpty.signalAll();
        }
        finally
        {
            itemLock.unlock();
        }
    }

    /**
     * Read an item from the channel
     * 
     * If empty, method blocks until an item is added
     */
    public E read() throws InterruptedException
    {
        //Attempt to aquire access to the buffer
        itemLock.lock();

        try
        {
            //Wait until there are items in the buffer
            while(isEmpty()) notEmpty.await();

            //Read item from buffer
            E i = (E)items[read];

            //Consume
            if (i != null)
            {
                items[read] = null;
                count--;
                incRead();
            }

            //Signal there is empty space in the buffer
            notFull.signalAll();

            return i;
        }
        finally
        {
            itemLock.unlock();
        }
    }

    /**
     * Read an item from the channel but only if it satisfies the given predicate,
     * 
     * the method blocks until an item is available and satisfies the condition
     * 
     * @see Channel.read
     */
    public E readIf(Predicate<E> pred) throws InterruptedException
    {
        //While the predicate isn't satisfied
        //Try and consume an item from the channel
        while(true)
        {
            //Attempt to aquire access to the buffer
            itemLock.lock();

            try
            {
                //Wait until there are items in the buffer
                while(isEmpty()) notEmpty.await();

                //Read next item in buffer
                E i = (E)items[read];

                try
                {
                    //If null the channel is closed
                    if (i == null)
                        return null;
                    
                    //If the item satisfies the predicate
                    if (pred.test(i))
                    {
                        //Consume
                        items[read] = null;
                        incRead();
                        return i;
                    }
                }
                finally
                {
                    //Signal any waiting threads
                    notFull.signalAll();
                }
            }
            finally
            {
                itemLock.unlock();
            }
        }
    }

    /**
     * Closes the channel, causes all read operations to return null
     */
    void close() throws InterruptedException
    {
       this.write(null); 
    }

    /**
     * Returns true if the channel has been closed
     */
    boolean isClosed() throws InterruptedException
    {
        itemLock.lock();

        try
        {
            return items[read] == null;
        }
        finally
        {
            itemLock.unlock();
        }
    }
}
