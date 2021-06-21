package edu.uw.cp520.scg.net.cmd;

import edu.uw.cp520.scg.net.server.CommandProcessor;

/**
 * The superclass of all Command objects, implements the command role in the
 * Command design pattern.
 *
 * @param <T> the target type, the type the command operates on
 *
 * @author Russ Moul
 */
@SuppressWarnings("serial")
public abstract class AbstractCommand<T> implements Command<T> {
    /** The CommandProcessor that will execute this command. */
    private transient CommandProcessor receiver;

    /** The target of the command. */
    private T target;

    /**
     * Construct an AbstractCommand without a target; called from subclasses.
     */
    public AbstractCommand() {
    }

    /**
     * Construct an AbstractCommand with a target; called from subclasses.
     *
     * @param target the target
     */
    public AbstractCommand(final T target) {
        this.target = target;
    }

    /**
     * Gets the CommandProcessor receiver for this Command.
     *
     * @return the receiver for this Command.
     */
    @Override
    public final CommandProcessor getReceiver() {
        return receiver;
    }

    /**
     * Set the CommandProcessor that will execute this Command.
     *
     * @param receiver the receiver for this Command.
     */
    @Override
    public final void setReceiver(final CommandProcessor receiver) {
        this.receiver = receiver;
    }

    /**
     * Return the target of this Command.
     *
     * @return the target.
     */
    @Override
    public final T getTarget() {
        return target;
    }

    /**
     * A string representation of this command.
     *
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return this.getClass().getSimpleName() + ", target " + target;
    }
}
