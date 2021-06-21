package demo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.ServerSocket;
import java.net.Socket;


/**
 * A simple server simply reads messages and prints them.
 */
public final class DemoServer {
    /** The listening port. */
    private int port;
    /** The server socket. */
    private ServerSocket servSock;

    /**
     * Constructor.
     *
     * @param port listening port
     */
    public DemoServer(final int port) {
        this.port = port;
    }

    /**
     * Creates a listening socket and waits for a connection.  When a connection
     * is received lines of text are read and printed.  The "quit" message
     * causes the server to disconnect, the "exit" message causes the server
     * to terminate.
     */
    public void run() {

        try {
            servSock = new ServerSocket(port);
            System.out.println("Server ready on port " + port + "...");

            while (servSock != null && !servSock.isClosed()) {
                Socket sock = servSock.accept(); // blocks
                process(sock);
            }
        } catch (IOException ex) {
            System.err.println("Server error: " + ex);
        } finally {
            if (servSock != null) {
                try {
                    servSock.close();
                } catch (IOException ioex) {
                    System.err.println("Error closing server socket. " + ioex);
                }
            }
        }
    }

    /**
     * Read the messages and processes them.
     */
    public void process(final Socket socket) {
        try {
            InputStream inStrm = socket.getInputStream();
            Reader rdr = new InputStreamReader(inStrm);
            BufferedReader br = new BufferedReader(rdr);
            while (true) {
                String s = br.readLine();

                if (s == null || s.equals("quit")) {
                    System.out.println("quiting");
                    break;
                } else if (s.equals("exit")) {
                    System.out.println("exiting");
                    servSock.close();
                    servSock = null;
                    break;
                }

                System.out.println(s);
            }

            br.close();
            socket.close();
        } catch (IOException ex) {
            System.out.println("Server error: " + ex);
        }
    }

    /**
     * Run the server.
     * 
     * @param args - not used
     */
    public static void main(final String[] args) {
        DemoServer server = new DemoServer(5555);
        server.run();
    }
}
