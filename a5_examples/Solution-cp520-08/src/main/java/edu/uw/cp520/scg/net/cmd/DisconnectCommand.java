package edu.uw.cp520.scg.net.cmd;

/**
 * The command to disconnect, this command has no target, so target type is Void.
 *
 * @author Russ Moul
 */
public final class DisconnectCommand extends AbstractCommand<Void> {
    /** Serial version id. */
	private static final long serialVersionUID = 9076692752230584371L;

	/**
     * Construct an DisconnectCommand.
     */
    public DisconnectCommand() {
        super();
    }

    /**
     * Execute this Command by calling receiver.execute(this).
     */
    @Override
    public void execute() {
        getReceiver().execute(this);
    }
}
