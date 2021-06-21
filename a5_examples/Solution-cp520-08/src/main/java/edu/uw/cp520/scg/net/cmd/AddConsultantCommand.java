package edu.uw.cp520.scg.net.cmd;

import edu.uw.cp520.scg.domain.Consultant;

/**
 * The command to add a Consultant to a list maintained by the server,
 * target type is Consultant.
 *
 * @author Russ Moul
 */
public final class AddConsultantCommand extends AbstractCommand<Consultant> {
    /** Serial version id. */
	private static final long serialVersionUID = 1043616836812016530L;

	/**
     * Construct an AddConsultantCommand with a target.
     *
     * @param target The target of this Command.
     */
    public AddConsultantCommand(final Consultant target) {
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
