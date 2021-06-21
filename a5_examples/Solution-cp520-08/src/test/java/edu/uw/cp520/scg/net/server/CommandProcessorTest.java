package edu.uw.cp520.scg.net.server;

import static edu.uw.ext.util.ListFactory.TEST_INVOICE_MONTH;
import static edu.uw.ext.util.ListFactory.TEST_INVOICE_YEAR;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.io.File;
import java.io.IOException;
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
import org.mockito.junit.jupiter.MockitoExtension;

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
import edu.uw.cp520.scg.net.server.CommandProcessor;
import edu.uw.cp520.scg.net.server.InvoiceServer;
import edu.uw.ext.util.ListFactory;

@ExtendWith(MockitoExtension.class)
public class CommandProcessorTest {
    /** Target directory for invoice files. */
    private static final String INVOICE_PATH = "target/invoices";

    /** The month to order the invoice for. */
    private static final LocalDate INVOICE_MONTH  = LocalDate.of(TEST_INVOICE_YEAR, TEST_INVOICE_MONTH, 1);

    /** Expected number of invoices to be written. */
    private static final int EXPECTED_NUM_INVOICES = 2;

    /** Minimum acceptable size of an invoice file. */
    private static final int MIN_INVOICE_FILE_LEN = 100;

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
		final List<ClientAccount> testAccounts = new ArrayList<ClientAccount>();
		final List<Consultant> testConsultants = new ArrayList<Consultant>();
		final List<TimeCard> testTimeCards = new ArrayList<TimeCard>();
		ListFactory.populateLists(testAccounts, testConsultants, testTimeCards);

		final List<ClientAccount> accounts = new ArrayList<ClientAccount>();
		final List<Consultant> consultants = new ArrayList<Consultant>();

		//@Mock Socket socket; // = mock(Socket.class);
		//final InvoiceServer server = mock(InvoiceServer.class);

		CommandProcessor proc = new CommandProcessor(socket, accounts, consultants, server);
		proc.setOutPutDirectoryName(INVOICE_PATH);
		
		ClientAccount newClient = new ClientAccount("Nanosoft",
                new PersonalName("Bridges", "Betty", "S."),
                new Address("1 Fiber Lane", "Brightville", StateCode.WA, "98234"));

        testAccounts.add(newClient);
		for (ClientAccount client : testAccounts) {
		    AddClientCommand command = new AddClientCommand(client);
		    proc.execute(command);
		}
		assertEquals(testAccounts.size(), accounts.size());

		for (Consultant consultant : testConsultants) {
			AddConsultantCommand command = new AddConsultantCommand(consultant);
		    proc.execute(command);
		}
		assertEquals(testConsultants.size(), consultants.size());

		for (TimeCard timeCard : testTimeCards) {
			AddTimeCardCommand command = new AddTimeCardCommand(timeCard);
		    proc.execute(command);
		}
		
		CreateInvoicesCommand invoiceCommand = new CreateInvoicesCommand(INVOICE_MONTH);
        proc.execute(invoiceCommand);

        // Verify creation of invoice files
        File[] invoiceFiles = invoiceDir.listFiles();
        assertNotNull(invoiceFiles);
        assertEquals(EXPECTED_NUM_INVOICES, invoiceFiles.length);
        for (File invoiceFile : invoiceFiles) {
        	assertTrue(invoiceFile.length() > MIN_INVOICE_FILE_LEN);
        }

	    proc.execute(new DisconnectCommand());
	    // Verify the Socket.close() was called as a result of the DisconnectCommand
        verify(socket, times(1)).close();
       
		ShutdownCommand shutdownCmd = new ShutdownCommand();
	    proc.execute(shutdownCmd);
	    // Verify the Socket.close() was called (again) as a result of the ShutdownCommand
        verify(socket, times(2)).close();
	    // Verify the InvoiceServer.shutdown() was called as a result of the ShutdownCommand
        verify(server, times(1)).shutdown();
	}
}
