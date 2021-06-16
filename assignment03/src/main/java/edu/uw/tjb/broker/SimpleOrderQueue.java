package edu.uw.tjb.broker;

import edu.uw.ext.framework.order.*;
import edu.uw.ext.framework.broker.OrderQueue;

import java.util.Comparator;
import java.util.Optional;
import java.util.TreeSet;
import java.util.function.BiPredicate;
import java.util.function.Consumer;

/**
 * A simple OrderQueue implementation backed by a TreeSet.
 * An ordered set with no duplicates.
 *
 * @param <T>
 */
public class SimpleOrderQueue <T, E extends Order> implements OrderQueue<T, E> {


    /*
    *
    * T threshold
    * Consumer
    *
    * */
    private T threshold;
    private Consumer<E> consumer;
    private BiPredicate<T, E> filter;

    /*
    * Internally, for every element, the values are compared and sorted in ascending order.
    * We need to keep a note that duplicate elements are not allowed and all the duplicate
    * elements are ignored
    * */
    private TreeSet<E> queue;


    /**
     * Constructor.
     * ?
     * @param threshold
     * @param filter
     */
    public SimpleOrderQueue(T threshold, BiPredicate<T, E> filter)	{
        this(threshold, filter, Comparator.naturalOrder());
    }

    /**
     * Constructor.
     *
     * @param threshold
     * @param filter
     * @param cmp
     */
    public SimpleOrderQueue(T threshold, BiPredicate<T, E> filter, Comparator<E> cmp) {
        this.threshold = threshold;
        this.filter = filter;
        queue = new TreeSet<>(cmp);
    }


    /**
     * Adds the specified order to the queue.
     *
     * @param order
     */
    @Override
    public void enqueue(E order) {
        queue.add(order);
    }

    /**
     * Removes the highest dispatchable order in the queue.
     *
     * @return
     */
    @Override
    public Optional<E> dequeue() {
        //Needs to determine if it's dispatchable?
        if( queue.size() > 0 ) {
            E queueEntryToDispatch = queue.first();
            //return Optional<queueEntryToDispatch>;
            return Optional.of(queueEntryToDispatch);
        }
        else
            return Optional.empty();
    }

    /**
     * Registers the consumer to be used during order processing.
     *
     * @param consumer
     */
    @Override
    public void setConsumer(Consumer<E> consumer) {
        this.consumer = consumer;

    }

    /**
     * Adjusts the threshold and dispatches orders.
     *
     * @param threshold
     */
    @Override
    public void setThreshold(T threshold) {
        this.threshold = threshold;
    }

    /**
     * Obtains the current threshold value.
     *
     * @return
     */
    @Override
    public T getThreshold() {
        return this.threshold;
    }

}
