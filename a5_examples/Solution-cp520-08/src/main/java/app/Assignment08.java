package app;

import java.util.ArrayList;
import java.util.List;

import edu.uw.cp520.scg.domain.ClientAccount;
import edu.uw.cp520.scg.domain.Consultant;
import edu.uw.cp520.scg.domain.TimeCard;

import edu.uw.cp520.scg.net.client.InvoiceClient;
import edu.uw.ext.util.ListFactory;

/**
 * The client application for Assignment08, create an InvoiceClient
 * instance and use it to send the various commands to the server, and disconnects,
 * and then on a separate connection sends the shutdown command.
 *
 * @author Russ Moul
 */
public final class Assignment08 {
    /** Localhost. */
    private static final String LOCALHOST = "127.0.0.1";
    /**
     * Prevent instantiation.
     */
    private Assignment08() {
    }

    /**
     * Instantiates an InvoiceClient, provides it with a set of timecards to
     * send the server and starts it running, and then send the shutdown command.
     *
     * @param args Command line parameters, not used
     * @throws Exception if anything goes awry
     */
    public static void main(final String[] args) throws Exception {
        final List<ClientAccount> accounts = new ArrayList<ClientAccount>();
        final List<Consultant> consultants = new ArrayList<Consultant>();
        final List<TimeCard> timeCards = new ArrayList<TimeCard>();
        ListFactory.populateLists(accounts, consultants, timeCards);

        final InvoiceClient netClient = new InvoiceClient(LOCALHOST,
                                                    Assignment08Server.DEFAULT_PORT, timeCards);
        netClient.run();

        // Sent quit command
        InvoiceClient.sendShutdown(LOCALHOST, Assignment08Server.DEFAULT_PORT);
    }

}
