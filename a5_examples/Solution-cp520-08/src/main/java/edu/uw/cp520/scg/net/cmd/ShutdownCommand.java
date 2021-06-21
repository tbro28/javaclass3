package edu.uw.cp520.scg.net.cmd;

/**
 * This Command will cause the CommandProcessor to shutdown the server, this
 * command has no target, so target type is Void.
 *
 * @author Russ Moul
 */
public final class ShutdownCommand extends AbstractCommand<Void> {
    /** Serial version id. */
	private static final long serialVersionUID = -7671502565041615667L;

	/**
     * Construct an ShutdownCommand.
     */
    public ShutdownCommand() {
        super();
    }

    /**
     * The method called by the receiver. This method must be implemented by
     * subclasses to send a reference to themselves to the receiver by calling
     * receiver.execute(this).
     */
    @Override
    public void execute() {
        getReceiver().execute(this);
    }
}
