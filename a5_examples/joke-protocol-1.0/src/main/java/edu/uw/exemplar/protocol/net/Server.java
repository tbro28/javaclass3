package edu.uw.exemplar.protocol.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import edu.uw.exemplar.protocol.state.StateRequestProcessor;

/**
 * Joke server, serves jokes using the knock-knock protocol.  Allows selection
 * of different implementations of the knock-knock protocol.
 */
public class Server implements Runnable {
    /** The listening port. */
    public static final int DEFAULT_PORT = 5500;

    /** Thread pool to listener and request servicing threads. */
    private ExecutorService executor = Executors.newCachedThreadPool();
    /** The listening server socket. */
    private ServerSocket serverSocket;
    /** The protocol implementation type. */
    private Class<? extends JokeRequestProcessor> requestProcessorType;

    /**
     * Helper class to perform the interaction wit a specific client/connection.
     */
    class JokeHandler implements Runnable {
        /** The client socket. */
        private Socket clientSocket;
        /** The request processor, the class that implements the protocol. */
        private JokeRequestProcessor requestProcessor;
        /**
         * Constructor.
         * @param client the client socket
         */
        private JokeHandler(final Socket client, final JokeRequestProcessor processor) {
            clientSocket = client;
            requestProcessor = processor;
        }
        
        /**
         * Obtains a reader and a writer for the client and delegates to the
         * active RequestProcessor.
         */
        public void run() {
            try (Socket client = clientSocket) {
                PrintWriter prntWrtr = new PrintWriter(client.getOutputStream(), true);
                BufferedReader bufRdr = new BufferedReader(new InputStreamReader(client.getInputStream()));
                requestProcessor.processRequests(bufRdr, prntWrtr);
            } catch (IOException e) {
                System.err.println("An error occured communicating with client.");
            }
        }
    }

    /**
     * Constructor.
     * @param processorType the type of JokeRequestProcessor to use
     */
    private Server(final Class<? extends JokeRequestProcessor> processorType) {
    	requestProcessorType = processorType;
    }

    /**
     * Accepts client connections and hands them off to a JokeHandler instance.
     */
    public void run() {
        try (ServerSocket ss = new ServerSocket(DEFAULT_PORT)) {
            serverSocket = ss;
            Socket client = null;
            while (true) {
                try {
                    client = serverSocket.accept();
					try {
						JokeRequestProcessor processor = requestProcessorType.newInstance();
	                    executor.execute(new JokeHandler(client, processor));

					} catch (InstantiationException | IllegalAccessException e) {
		                System.err.println("Unable to instantiate request processor.");
						e.printStackTrace();
					}
                } catch (SocketException e) {
                    if (!serverSocket.isClosed()) {
                        System.err.printf("Error accepting connection on port: %d%n", DEFAULT_PORT);
                    }
                    break;
                } catch (IOException e) {
                    System.err.println("Attempt to accept a connection failed.");
                }
            }
        } catch (IOException e) {
            System.err.printf("Unable to bind to port: %d%n", DEFAULT_PORT);
        }
    }

    /**
     * Terminates the accepting of client connections.
     */
    public void terminate() {
        if (!serverSocket.isClosed()) {
            try {
                serverSocket.close();
            } catch (IOException e) {
                System.err.println("Error closing listening socket.");
                e.printStackTrace();
            }
        }
        executor.shutdown();
        try {
            executor.awaitTermination(10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            // ignore this
        }
    }

    /**
     * Initiates the execution of the listening thread.
     */
    public void start() {
        executor.execute(this);
    }
    
    /**
     * Starts the server and waits for the user to initiate termination.
     * @param args not used
     * @throws IOException if any are raised
     */
    public static void main(final String[] args) throws IOException {
        BufferedReader consoleRdr = new BufferedReader(new InputStreamReader(System.in));
        String input;
        Class<? extends JokeRequestProcessor> requestProcessorType = null;

        do {
           System.out.print("Select implementation [t]raditional, or [s]tate pattern: ");
           input = consoleRdr.readLine();
           if ("t".equals(input)) {
        	   requestProcessorType = TraditionalRequestProcessor.class;

           } else if("s".equals(input)) {
        	   requestProcessorType = StateRequestProcessor.class;
           }

        } while (requestProcessorType == null);
    	
        Server server = new Server(requestProcessorType);
        server.start();
        
        do {
            System.out.print("Enter 'q' to quit: ");
            input = consoleRdr.readLine();
        } while (! "q".equals(input));
        server.terminate();
    }
}
