package edu.uw.cp520.scg.net.cmd;

import java.time.LocalDate;

/**
 * The command to create invoices for a specified month,
 * target type is LocalDate.
 *
 * @author Russ Moul
 */
public final class CreateInvoicesCommand extends AbstractCommand<LocalDate> {
    /** Serial version id. */
	private static final long serialVersionUID = -8343847118028874472L;

	/**
     * Construct a CreateInvoicesCommand with a target month, which should be
     * obtained by getting the desired month constant from LocalDate.
     *
     * @param target the target month.
     */
    public CreateInvoicesCommand(final LocalDate target) {
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
