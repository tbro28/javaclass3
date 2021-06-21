package edu.uw.exemplar.multicast;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Date;


/**
 * A simple multicast server.  Sends a datagram containing a date/time string
 * to a multicast address every second.
 *
 * @author Russ Moul
 */
public final class TimeServer {
    /** Milliseconds per second. */
    private static final int ONE_SECOND = 1000;

    /** The server multicast address. */
    private String multicastAddress;

    /** The server multicast port. */
    private int port;
    
    /**
     * Constructor.
     *
     * @param ipAddress the multicast IP address to connect to
     * @param port the port to connect to
     */
    public TimeServer(final String multicastAddress, final int port) {
        this.multicastAddress = multicastAddress;
        this.port = port;
    }

    /**
     * Joins a multicast group and sends the date/time string every second.
     */
    public void start() {
        DatagramSocket datagramSocket = null;

        try {
            datagramSocket = new DatagramSocket();
            String localAddr = datagramSocket.getLocalAddress().getHostAddress();
            int localPort = datagramSocket.getLocalPort();
            InetAddress group = InetAddress.getByName(multicastAddress);
	        byte[] buf = new byte[256];
            DatagramPacket packet = new DatagramPacket(buf, buf.length,
			                                          group, port);
            System.out.println("Server ready...");

            while (true) {
                String ds = (new Date()).toString().trim();

                System.out.println("Sending: " + ds + ", [" + localAddr + ":" + localPort + " -> " + multicastAddress + ":" + port +"]");
                byte[] bytes = ds.getBytes();
                packet.setData(bytes);
                packet.setLength(bytes.length);
                datagramSocket.send(packet);
                Thread.sleep(ONE_SECOND);
            }
        } catch (IOException ex) {
            System.out.println("Server error: " + ex);
        } catch (InterruptedException ex) {
            System.out.println("Server error: " + ex);
        } finally {
            if (datagramSocket != null) {
                datagramSocket.close();
            }
        }
    }
}
