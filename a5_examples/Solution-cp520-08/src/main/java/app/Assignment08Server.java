package app;

import java.util.ArrayList;
import java.util.List;

import edu.uw.cp520.scg.domain.ClientAccount;
import edu.uw.cp520.scg.domain.Consultant;
import edu.uw.cp520.scg.domain.TimeCard;

import edu.uw.cp520.scg.net.server.InvoiceServer;
import edu.uw.ext.util.ListFactory;

/**
 * The server application for assignment 08, create an InvoiceServer
 * instance and starts it.
 *
 * @author Russ Moul
 */
public final class Assignment08Server {
    /** The port for the server to listen on. */
    public static final int DEFAULT_PORT = 10888;

    /**
     * Prevent instantiation.
     */
    private Assignment08Server() {
    }

    /**
     * Instantiates an InvoiceServer,initializes its account and consultant lists
     * and starts it.
     *
     * @param args Command line parameters.
     *
     * @throws Exception if the server raises any exceptions
     */
    public static void main(final String[] args) throws Exception {
        final List<ClientAccount> accounts = new ArrayList<ClientAccount>();
        final List<Consultant> consultants = new ArrayList<Consultant>();
        final List<TimeCard> timeCards = new ArrayList<TimeCard>();
        ListFactory.populateLists(accounts, consultants, timeCards);

        final InvoiceServer server = new InvoiceServer(DEFAULT_PORT,
                                                       accounts, consultants,
                                                       "target/server");
        server.run();
    }
}
