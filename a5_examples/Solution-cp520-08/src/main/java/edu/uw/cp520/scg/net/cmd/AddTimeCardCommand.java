package edu.uw.cp520.scg.net.cmd;

import edu.uw.cp520.scg.domain.TimeCard;

/**
 * Command that adds a TimeCard to the server's list of TimeCards,
 * target type is TimeCards.
 *
 * @author Russ Moul
 */
public final class AddTimeCardCommand extends AbstractCommand<TimeCard> {
    /** Serial version id. */
	private static final long serialVersionUID = -1756936222794293427L;

	/**
     * Construct an AddTimeCardCommand with a target.
     *
     * @param target the target of this Command.
     */
    public AddTimeCardCommand(final TimeCard target) {
        super(target);
    }

    /**
     * Execute this command by calling receiver.execute(this).
     */
    @Override
    public void execute() {
        getReceiver().execute(this);
    }
}
