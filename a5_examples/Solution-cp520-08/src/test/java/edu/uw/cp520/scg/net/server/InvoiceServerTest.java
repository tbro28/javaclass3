package edu.uw.cp520.scg.net.server;

import static edu.uw.ext.util.ListFactory.TEST_INVOICE_MONTH;
import static edu.uw.ext.util.ListFactory.TEST_INVOICE_YEAR;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;

import edu.uw.cp520.scg.domain.ClientAccount;
import edu.uw.cp520.scg.domain.Consultant;
import edu.uw.cp520.scg.domain.TimeCard;
import edu.uw.cp520.scg.util.Address;
import edu.uw.cp520.scg.util.PersonalName;
import edu.uw.cp520.scg.util.StateCode;

import edu.uw.cp520.scg.net.cmd.AddClientCommand;
import edu.uw.cp520.scg.net.cmd.AddConsultantCommand;
import edu.uw.cp520.scg.net.cmd.AddTimeCardCommand;
import edu.uw.cp520.scg.net.cmd.CreateInvoicesCommand;
import edu.uw.cp520.scg.net.cmd.DisconnectCommand;
import edu.uw.cp520.scg.net.cmd.ShutdownCommand;
import edu.uw.cp520.scg.net.server.InvoiceServer;
import edu.uw.ext.util.ListFactory;
/**
 * Unit test for the InvoiceServer, uses a mock for Socket to allow
 * testing without using network connections.
 */
@ExtendWith(MockitoExtension.class)
public class InvoiceServerTest {
    /** Target directory for invoice files. */
    private static final String INVOICE_PATH = "target/server";
    
    /** The month to orde the invoice for. */
    private static final LocalDate INVOICE_MONTH  = LocalDate.of(TEST_INVOICE_YEAR, TEST_INVOICE_MONTH, 1);

    /** The number of messages to be sent, needed to mock Socket.close(). */
	private static final int NUMBER_OF_MSGS = 10;

	/** Expected number of invoices to be written. */
    private static final int EXPECTED_NUM_INVOICES = 2;

    /** Expected number of accounts. */
    private static final int EXPECTED_NUM_ACCOUNTS = 3;

    /** Expected number of clients. */
    private static final int EXPECTED_NUM_CONSULTANTS = 2;

    /** Minimum acceptable size of an invoice file. */
    private static final int MIN_INVOICE_FILE_LEN = 100;

    /** Size of the pipes buffer. */
    private static final int PIPE_BUFFER_SIZE = 4096;

	/** Number of messages sent. */
	private int msgCnt = 0;

	/** Directory the invoice files will be written to. */
    private File invoiceDir;

    /** Mock socket. */
	@Mock private Socket socket;
	
	/** Mock invoice server. */
	@Mock private InvoiceServer server;

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
	public void testCommands() throws Exception {
		assertFalse(invoiceDir.exists());

		// Initialize an InvoiceServer instance
		final List<ClientAccount> testAccounts = new ArrayList<ClientAccount>();
		final List<Consultant> testConsultants = new ArrayList<Consultant>();
		final List<TimeCard> testTimeCards = new ArrayList<TimeCard>();
		ListFactory.populateLists(testAccounts, testConsultants, testTimeCards);

		final List<ClientAccount> accounts = new ArrayList<ClientAccount>();
		final List<Consultant> consultants = new ArrayList<Consultant>();

		final InvoiceServer server = new InvoiceServer(0, accounts, consultants, INVOICE_PATH);

		// Use pipes in place of the socket streams
		try(PipedOutputStream outPipe = new PipedOutputStream();
		    PipedInputStream inPipe = new PipedInputStream(outPipe, PIPE_BUFFER_SIZE)) {
		    ObjectOutputStream oos = new ObjectOutputStream(outPipe);

			// Use the mock socket
			when(socket.getInputStream()).thenReturn(inPipe);
			when(socket.isClosed()).thenAnswer(new Answer<Boolean>() {
				@Override
				public Boolean answer(InvocationOnMock invocation) throws Throwable {
					return msgCnt++ > NUMBER_OF_MSGS;
				}
			});
	
			// Create and send all of the commands
			ClientAccount newClient = new ClientAccount("Nanosoft",
	                new PersonalName("Bridges", "Betty", "S."),
	                new Address("1 Fiber Lane", "Brightville", StateCode.WA, "98234"));

	        testAccounts.add(newClient);
			for (ClientAccount client : testAccounts) {
			    AddClientCommand command = new AddClientCommand(client);
				oos.writeObject(command);
			}
	
			oos.writeObject("NOT_A_COMMAND");
			oos.flush();
	
			for (Consultant consultant : testConsultants) {
				AddConsultantCommand command = new AddConsultantCommand(consultant);
				oos.writeObject(command);
			}
	
	        for (final TimeCard timeCard : testTimeCards) {
	        	AddTimeCardCommand command = new AddTimeCardCommand(timeCard);
				oos.writeObject(command);
	        }
	        
	        CreateInvoicesCommand invoiceCommand = new CreateInvoicesCommand(INVOICE_MONTH);
    		oos.writeObject(invoiceCommand);
            oos.flush();

			oos.writeObject(new DisconnectCommand());
            oos.flush();

			ShutdownCommand shutdownCmd = new ShutdownCommand();
			oos.writeObject(shutdownCmd);

            // All the commands are now buffered in the pipe, so lets process them
			server.serviceConnection(socket);

			// Verify creation of invoice files
	        File[] invoiceFiles = invoiceDir.listFiles();
	        assertNotNull(invoiceFiles);
	        assertEquals(EXPECTED_NUM_INVOICES, invoiceFiles.length);
	        for (File invoiceFile : invoiceFiles) {
	        	assertTrue(invoiceFile.length() > MIN_INVOICE_FILE_LEN);
	        }

			// Verify account and consultant lists
			assertEquals(EXPECTED_NUM_ACCOUNTS, accounts.size());
			assertEquals(EXPECTED_NUM_CONSULTANTS, consultants.size());
		}
	}
	
	@Test
	public void testShutdownCommand() throws Exception {
		final List<ClientAccount> accounts = new ArrayList<ClientAccount>();
		final List<Consultant> consultants = new ArrayList<Consultant>();

		final InvoiceServer server = new InvoiceServer(0, accounts, consultants, INVOICE_PATH);

		// Use pipes in place of the socket streams
		try(PipedOutputStream outPipe = new PipedOutputStream();
		    PipedInputStream inPipe = new PipedInputStream(outPipe, PIPE_BUFFER_SIZE)) {
		    ObjectOutputStream oos = new ObjectOutputStream(outPipe);

			// Use the mock socket
			when(socket.getInputStream()).thenReturn(inPipe);
			
			when(socket.isClosed()).thenAnswer(new Answer<Boolean>() {
				@Override
				public Boolean answer(InvocationOnMock invocation) throws Throwable {
					return 0 < msgCnt++;
				}
			});
	
            final ShutdownCommand command = new ShutdownCommand();

			oos.writeObject(command);
			oos.flush();

			server.serviceConnection(socket);
		}
	}
}
