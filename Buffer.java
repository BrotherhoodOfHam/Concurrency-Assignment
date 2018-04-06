/**
 * Buffer interface
 * 
 * Behaves like a queue of unknown size
 */
interface Buffer<E>
{
    /**
     *  Put a value into the buffer
     * 
     * @param e a value to be put into the buffer
     */
    public void put(E e) throws InterruptedException;

    /**
     * Take a value from the buffer
     * 
     * @return A value from the buffer or null if no more values can be taken
     */
    public E take() throws InterruptedException;
}