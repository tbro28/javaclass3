package demo;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * A simple client that just sends messgaes to the server.
 * @author russ
 *
 */
public class DemoClient {
    /** The socket. */
    private Socket socket;
    /** The printwriter. */
    private PrintWriter wtr;

    /**
     * Constructor.
     * 
     * @param address server address
     * @param port server port
     * @throws IOException if an exception is thrown
     */
    public DemoClient(final String address, final int port) throws IOException {
        socket = new Socket(address, port);
        OutputStream os = socket.getOutputStream();
        wtr = new PrintWriter(new OutputStreamWriter(os), true);
    }

    /**
     * Sends messages to the server.
     * 
     * @param msgs the messages to send
     */
    public void sendMessages(final String ... msgs) {
        for (String m : msgs) {
            wtr.println(m);
        }
    }

    /**
     * Send the quit message.
     */
    public void sendQuit() {
        wtr.println("quit");
        wtr.close();
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Send the exit message.
     */
    public void sendExit() {
        wtr.println("exit");
        wtr.close();
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Run a few clients.
     * 
     * @param args - not used
     * @throws IOException if any occur
     */
    public static void main(final String[] args) throws IOException {
        DemoClient client = new DemoClient("127.0.0.1", 5555);
        client.sendMessages("hello", "howdy", "how do you do");
        client.sendQuit();
        
        client = new DemoClient("127.0.0.1", 5555);
        client.sendMessages("g' day", "mornin'");
        client.sendQuit();

        client = new DemoClient("127.0.0.1", 5555);
        client.sendMessages("hello", "howdy", "how do you do");
        client.sendExit();
    }

}
