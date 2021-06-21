package edu.uw.cp520.scg.net.server;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.uw.cp520.scg.domain.ClientAccount;
import edu.uw.cp520.scg.domain.Consultant;
import edu.uw.cp520.scg.domain.TimeCard;

import edu.uw.cp520.scg.net.client.InvoiceClient;
import edu.uw.cp520.scg.net.server.InvoiceServer;
import edu.uw.ext.util.ListFactory;

/**
 * Run the InvoiceServer and InvoiceClient classes, verify the invoice
 * files are created.
 */
public class EndToEndTest {
    /** Localhost. */
    private static final String LOCALHOST = "127.0.0.1";

    /** The port for the server to listen on. */
    private static final int DEFAULT_PORT = 10888;

    /** Expected number of invoices to be written. */
    private static final int EXPECTED_NUM_INVOICES = 2;

    /** Minimum acceptable size of an invoice file. */
    private static final int MIN_INVOICE_FILE_LEN = 100;

    /** Target directory for invoice files. */
    private static final String INVOICE_PATH = "target/server";
	/** Directory the invoice files will be written to. */
    private File invoiceDir;

    @BeforeEach
    public void setup() throws IOException {    	
		// Delete the invoice target directory
		invoiceDir = new File(INVOICE_PATH);
    	Path invoiceDirPath = invoiceDir.toPath();
    	if (invoiceDir.exists()) {
	    	Files.newDirectoryStream(invoiceDirPath).forEach(p -> {
				try {
					Files.delete(p);
				} catch (IOException e) {
					e.printStackTrace();
				}
			});
	    	Files.delete(invoiceDirPath);
    	}
    }

    @Test
	public void endToEndTest() throws Exception {
		assertFalse(invoiceDir.exists());

		// Initialize an InvoiceServer instance
        final List<ClientAccount> serverAccounts = new ArrayList<ClientAccount>();
        final List<Consultant> serverConsultants = new ArrayList<Consultant>();
        final List<TimeCard> serverTimeCards = new ArrayList<TimeCard>();
        ListFactory.populateLists(serverAccounts, serverConsultants, serverTimeCards);

        final InvoiceServer server = new InvoiceServer(DEFAULT_PORT,
        		                                       serverAccounts, serverConsultants,
                                                       INVOICE_PATH);

        // Run the InvoiceServer on a separate thread
        Thread serverThread = new Thread(new Runnable() {
			@Override
			public void run() {
		        server.run();				
			}
        });
        serverThread.start();

        // Initialize the InvoiceClient
        final List<ClientAccount> accounts = new ArrayList<ClientAccount>();
        final List<Consultant> consultants = new ArrayList<Consultant>();
        final List<TimeCard> timeCards = new ArrayList<TimeCard>();
        ListFactory.populateLists(accounts, consultants, timeCards);

        final InvoiceClient netClient = new InvoiceClient(LOCALHOST,
                                                          DEFAULT_PORT, timeCards);
        // Send the commands
        netClient.run();

        // Sent quit command - on new connection
        InvoiceClient.sendShutdown(LOCALHOST, DEFAULT_PORT);
        serverThread.join();

        // Verify creation of invoice files
        File[] invoiceFiles = invoiceDir.listFiles();
        assertNotNull(invoiceFiles);
        assertEquals(EXPECTED_NUM_INVOICES, invoiceFiles.length);
        for (File invoiceFile : invoiceFiles) {
        	assertTrue(invoiceFile.length() > MIN_INVOICE_FILE_LEN);
        }
	}
}
