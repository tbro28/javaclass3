package edu.uw.tjb.concurrentbroker;

import edu.uw.ext.framework.broker.OrderQueue;
import edu.uw.ext.framework.order.Order;

import java.util.Comparator;
import java.util.Optional;
import java.util.TreeSet;
import java.util.function.BiPredicate;
import java.util.function.Consumer;

/**
 * A simple OrderQueue implementation backed by a TreeSet.
 *
 * @param <T> the dispatch threshold type
 * @param <E> the type of order contained in the queue
 *
 * @author Russ Moul
 */
public final class ExecutorOrderQueue<T, E extends Order>
                                         implements OrderQueue<T, E>, Runnable {
    /** The queue data structure */
    private TreeSet<E> queue;

    /** The current threshold. */
    private T threshold;

    /** The BiPredicate used to determine if an order is dispatchable */
    private BiPredicate<T, E> filter;

    /** Consumer used to process dispatchable elements */
    private Consumer<E> consumer;
    

    /**
     * Constructor.
     *
     * @param threshold the initial threshold
     * @param filter the dispatch filter used to control dispatching from this
     *               queue
     * @param cmp Comparator to be used for ordering
     */
    public ExecutorOrderQueue(final T threshold,
                              final BiPredicate<T, E> filter,
                              final Comparator<E> cmp) {
        queue = new TreeSet<>(cmp);
        this.threshold = threshold;
        this.filter = filter;
    }

    /**
     * Constructor.
     *
     * @param threshold the initial threshold
     * @param filter the dispatch filter used to control dispatching from this
     *               queue
     */
    public ExecutorOrderQueue(final T threshold, final BiPredicate<T, E> filter) {
    	this(threshold, filter, Comparator.naturalOrder());
    }

    /**
     * Adds the specified order to the queue.  Subsequent to adding the order
     * dispatches any dispatchable orders.
     *
     * @param order the order to be added to the queue
     */
    @Override
    public void enqueue(final E order) {
        if (queue.add(order)) {
            dispatchOrders();
        }
    }

    /**
     * Removes the highest dispatchable order in the queue. If there are orders
     * in the queue but they do not meet the dispatch threshold order will not
     * be removed and null will be returned.
     *
     * @return the first dispatchable order in the queue, or null if there are no
     *         dispatchable orders in the queue
     */
    @Override
    public Optional<E> dequeue() {
    	E dispatchable = null;

        if (!queue.isEmpty()) {
        	dispatchable = queue.first();
            if (filter.test(threshold, dispatchable)) {
                queue.remove(dispatchable);
            } else {
            	dispatchable = null;
            }
        }
        return Optional.<E>ofNullable(dispatchable);
    }

    /**
     * Executes the callback for each dispatchable element.  Each dispatchable
     * element is in turn removed from the queue and passed to the callback.  If
     * no callback is registered the order is simply removed from the queue.
     */
    private void dispatchOrders() {
    	Optional<E> opt;
    	while ((opt = dequeue()).isPresent()) {
            if (consumer != null) {
                consumer.accept(opt.get());
            }
        }
    }

    /**
     * Registers the consumer to be used during order processing.
     *
     * @param consumer the consumer to be registered
     */
    @Override
    public void setConsumer(final Consumer<E> consumer) {
        this.consumer = consumer;
    }
    
    /**
     * Adjusts the threshold and dispatches orders.
     *
     * @param threshold - the new threshold
     */
    @Override
    public final void setThreshold(final T threshold) {
        this.threshold = threshold;
        dispatchOrders();
    }
    
    /**
     * Obtains the current threshold value.
     *
     * @return the current threshold
     */
    @Override
   public final T getThreshold() {
        return threshold;
    }

    /**
     * When an object implementing interface {@code Runnable} is used
     * to create a thread, starting the thread causes the object's
     * {@code run} method to be called in that separately executing
     * thread.
     * <p>
     * The general contract of the method {@code run} is that it may
     * take any action whatsoever.
     *
     * @see Thread#run()
     */
    @Override
    public void run() {

    }
}

