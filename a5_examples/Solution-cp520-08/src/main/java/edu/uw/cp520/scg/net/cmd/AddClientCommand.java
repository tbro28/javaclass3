package edu.uw.cp520.scg.net.cmd;

import edu.uw.cp520.scg.domain.ClientAccount;

/**
 * The command to add a ClientAccount to a list maintained by the server,
 * target type is ClientAccount.
 *
 * @author Russ Moul
 */
public final class AddClientCommand extends AbstractCommand<ClientAccount> {
    /** Serial version id. */
	private static final long serialVersionUID = -5917364448228198875L;

	/**
     * Construct an AddClientCommand with a target.
     *
     * @param target The target of this Command.
     */
    public AddClientCommand(final ClientAccount target) {
        super(target);
    }

    /**
     * Execute this Command by calling receiver.execute(this).
     */
    @Override
    public void execute() {
        getReceiver().execute(this);
    }
}
