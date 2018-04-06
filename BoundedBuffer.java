import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Predicate;

/**
 * Bounded buffer class
 * 
 * Allows communication between threads
 */
public class BoundedBuffer<E> implements Buffer<E>
{
    private Object[] items; //fixed size item buffer
    private int count = 0;  //number of items in the buffer
    private int read = 0;   //read pointer - position of next item to read
    private int write = 0;  //write pointer - position of next item to write

    private Lock itemLock;
    private Condition notFull;
    private Condition notEmpty;

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
        read = (read + 1) % items.length;
    }

    private void incWrite()
    {
        write = (write + 1) % items.length;
    }

    public int getSize()
    {
        return this.count;
    }

    public BoundedBuffer(int capacity)
    {
        items = new Object[capacity];
        itemLock = new ReentrantLock();
        notFull = itemLock.newCondition();
        notEmpty = itemLock.newCondition();
    }

    @Override
    public void put(E e) throws InterruptedException
    {
        //Attempt to aquire access to the buffer
        itemLock.lock();

        try
        {
            //Wait until there is space in the buffer
            while(isFull())
                notFull.await();

            //Write value into buffer + increment the write pointer
            items[write] = e;
            count++;
            incWrite();

            //Signal there are available items in the buffer
            notEmpty.signalAll();
        }
        finally
        {
            itemLock.unlock();
        }
    }

    public E takeIf(Predicate<E> pred) throws InterruptedException
    {
        while(true)
        {
            //Attempt to aquire access to the buffer
            itemLock.lock();

            try
            {
                //Wait until there are items in the buffer
                while(isEmpty())
                    notEmpty.await();

                //Read item from buffer + increment read pointer
                E i = (E)items[read];

                //If the item satisfies the predicate
                if (i != null && !pred.test(i))
                {
                    //Signal any waiting threads
                    notFull.signalAll();

                    Thread.yield();

                    continue;
                }
                
                //Consume the item and increment the read pointer
                items[read] = null;
                count--;
                incRead();

                //Signal there is empty space in the buffer
                notFull.signalAll();

                return i;
            }
            finally
            {
                itemLock.unlock();
            }
        }
    }

    @Override
    public E take() throws InterruptedException
    {
        //Attempt to aquire access to the buffer
        itemLock.lock();

        try
        {
            //Wait until there are items in the buffer
            while(isEmpty()) notEmpty.await();

            //Read item from buffer + increment read pointer
            E i = (E)items[read];
            items[read] = null;
            count--;
            incRead();

            //Signal there is empty space in the buffer
            notFull.signalAll();

            return i;
        }
        finally
        {
            itemLock.unlock();
        }
    }
}
