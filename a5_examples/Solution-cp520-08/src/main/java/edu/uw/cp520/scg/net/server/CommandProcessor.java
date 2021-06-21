package edu.uw.cp520.scg.net.server;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.uw.cp520.scg.domain.ClientAccount;
import edu.uw.cp520.scg.domain.Consultant;
import edu.uw.cp520.scg.domain.Invoice;
import edu.uw.cp520.scg.domain.TimeCard;

import edu.uw.cp520.scg.net.cmd.AddClientCommand;
import edu.uw.cp520.scg.net.cmd.AddConsultantCommand;
import edu.uw.cp520.scg.net.cmd.AddTimeCardCommand;
import edu.uw.cp520.scg.net.cmd.CreateInvoicesCommand;
import edu.uw.cp520.scg.net.cmd.DisconnectCommand;
import edu.uw.cp520.scg.net.cmd.ShutdownCommand;

/**
 * The command processor for the invoice server. Implements the receiver role in
 * the Command design pattern, provides the execute method for all of the
 * supported commands.  Is provided with the client and consultant lists from the
 * Invoice server, maintains its own time card list.
 *
 * @author Russ Moul
 */

public class CommandProcessor {
    /** The class' logger. */
    private static final Logger logger =
                         LoggerFactory.getLogger(CommandProcessor.class);

	/** Character encoding to use. */
	private static final String ENCODING = "ISO-8859-1";

	/** The socket connection. */
    private final Socket clientSocket;

    /** The client list to be maintained by this CommandProcessor. */
    private final List<ClientAccount> clientList;

    /** The consultant list to be maintained by this CommandProcessor. */
    private final List<Consultant> consultantList;

    /** The time card list to be maintained by this CommandProcessor. */
    private final List<TimeCard> timeCardList = new ArrayList<TimeCard>();

    /** The name of the directory to be used for files output by commands. */
    private String outputDirectoryName = ".";

    /** The server this command processor is spawned from. */
    private final InvoiceServer server;

    /**
     * Construct a CommandProcessor to run in a networked environment.
     *
     * @param connection the Socket connecting the server to the client.
     * @param clientList the ClientList to add Clients to.
     * @param consultantList the ConsultantList to add Consultants to.
     * @param server the server that created this command processor
     */
    public CommandProcessor(final Socket connection,
                            final List<ClientAccount> clientList,
                            final List<Consultant> consultantList,
                            final InvoiceServer server) {
        this.clientSocket = connection;
        this.clientList = clientList;
        this.consultantList = consultantList;
        this.server = server;
    }

    /**
     * Set the output directory name.
     *
     * @param outPutDirectoryName the output directory name.
     */
    public void setOutPutDirectoryName(final String outPutDirectoryName) {
        this.outputDirectoryName = outPutDirectoryName;
    }

    /**
     * Execute and AddTimeCardCommand.
     *
     * @param command the command to execute.
     */
    public void execute(final AddTimeCardCommand command) {
        logger.info(String.format("Processor executing add time card command: %s",
                                  command.getTarget().getConsultant().getName()));
        timeCardList.add(command.getTarget());
    }

    /**
     * Execute an AddClientCommand.
     *
     * @param command the command to execute.
     */
    public void execute(final AddClientCommand command) {
        logger.info(String.format("Processor executing add client command: %s",
                                  command.getTarget().getName()));
        clientList.add(command.getTarget());
    }

    /**
     * Execute and AddConsultantCommand.
     *
     * @param command the command to execute.
     */
    public void execute(final AddConsultantCommand command) {
        logger.info(String.format("Processor executing add consultant command: %s",
                                  command.getTarget().getName()));
        consultantList.add(command.getTarget());
    }

    /**
     * Execute a CreateInvoicesCommand.
     *
     * @param command the command to execute.
     */
    public void execute(final CreateInvoicesCommand command) {
        logger.info(String.format("Processor executing invoices command: %s", command));
        Invoice invoice = null;
        LocalDate date = command.getTarget();
        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMMyyyy");
        final String monthString = formatter.format(date);
        for (final ClientAccount client : clientList) {
            invoice = new Invoice(client, date.getMonth(), date.getYear());
            for (final TimeCard currentTimeCard : timeCardList) {
                invoice.extractLineItems(currentTimeCard);
            }
            if (invoice.getTotalHours() > 0) {
                final File serverDir = new File(outputDirectoryName);
                if (!serverDir.exists()) {
                    if (!serverDir.mkdirs()) {
                        logger.error("Unable to create directory, " + serverDir.getAbsolutePath());
                        return;
                    }
                }
                final String outFileName = String.format("%s%sInvoice.txt",
                                           client.getName().replaceAll(" ", ""),
                                           monthString);
                final File outFile = new File(outputDirectoryName, outFileName);
                try (PrintStream printOut = new PrintStream(new FileOutputStream(outFile), true, ENCODING);) {
                    printOut.println(invoice.toReportString());
                } catch (final FileNotFoundException e) {
                    logger.error("Can't open file " + outFileName, e);
                } catch (UnsupportedEncodingException e) {
                    logger.error("Can't write to file, bad encoding.", e);
				}
            }
        }
    }

    /**
     * Execute a DisconnectCommand.
     *
     * @param command the input DisconnectCommand.
     */
    public void execute(final DisconnectCommand command) {
        logger.info(String.format("Processor executing disconnect command: %s", command));
        try {
            clientSocket.close();
        } catch (final IOException e) {
            logger.warn("Disconnect unable to close client connection.", e);
        }
    }

    /**
     * Execute a ShutdownCommand.
     *
     * @param command the input ShutdownCommand.
     */
    public void execute(final ShutdownCommand command) {
        logger.info(String.format("Processor executing shutdown command: %s", command));
        try {
            clientSocket.close();
        } catch (final IOException e) {
            logger.warn("Shutdown unable to close client connection.", e);
        } finally {
            server.shutdown();
        }
    }

}
