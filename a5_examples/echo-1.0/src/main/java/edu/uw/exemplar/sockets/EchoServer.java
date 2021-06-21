package edu.uw.exemplar.sockets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Reader;
import java.net.ServerSocket;
import java.net.Socket;


/**
 * A simple Echo server.  Replies to the client by echoing the string.
 * Telnet can be used to "talk" to the server.
 */
public final class EchoServer implements Runnable {
    /** The listening port. */
    private int mPort;

    /**
     * Constructor.
     *
     * @param port listening port
     */
    public EchoServer(final int port) {
        mPort = port;

        Thread t = new Thread(this);
        t.start();
    }

    /**
     * Creates a listining socket and waits for a connection.  When a connection
     * is received a work thread is created to process the request and this
     * thread resumes listening.
     */
    public void run() {
        ServerSocket servSock = null;

        try {
            servSock = new ServerSocket(mPort);
            System.out.println("Server ready on port " + mPort + "...");

            while (true) {
                Socket sock = servSock.accept(); // blocks
                EchoProcessor proc = new EchoProcessor(sock);
                Thread t = new Thread(proc);
                t.start();
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
}


/**
 * Processes individual responses.
 */
final class EchoProcessor implements Runnable {
    /** The communications socket. */
    private Socket mSocket;

    /**
     * Constructor.
     *
     * @param sock the socket connection to process
     */
    public EchoProcessor(final Socket sock) {
        System.out.println("New processor");
        mSocket = sock;
    }

    /**
     * Read input off the socket and write it back until end of stream is
     * reached.
     */
    public void run() {
        try {
            InputStream inStrm = mSocket.getInputStream();
            Reader rdr = new InputStreamReader(inStrm);
            BufferedReader br = new BufferedReader(rdr);
            OutputStream outStrm = mSocket.getOutputStream();

            PrintWriter wrtr = new PrintWriter(outStrm, true);

            while (true) {
                int x = br.read();

                if (x == -1) {
                    break;
                }

                char ch = (char) x;
                System.out.print(ch);
                wrtr.print(ch);
            }

            System.out.flush();
            wrtr.flush();
            wrtr.close();
            br.close();
            mSocket.close();
        } catch (IOException ex) {
            System.out.println("Server error: " + ex);
        }
    }
}
