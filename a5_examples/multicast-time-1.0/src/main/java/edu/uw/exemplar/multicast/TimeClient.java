package edu.uw.exemplar.multicast;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;


/**
 * A simple time of day client. Joins a multicast group and reads 10 messages
 * and then exits.
 *
 * @author Russ Moul
 */
public final class TimeClient {
    /** Message buffer size. */
    private static final int BUF_SIZE = 128;

    /** The server multicast address. */
    private String multicastAddress;

    /** The server multicast port. */
    private int multicastPort;

    /** The number of time messages to receive. */
    private int samplings;

    /**
     * Constructor.
     *
     * @param ipAddress the multicast IP address to connect to
     * @param port the port to connect to
     * @param samplings the number of time messages to receive before
     *        terminating, 0 for infinite
     */
    public TimeClient(final String ipAddress, final int port,final int samplings) {
        multicastAddress = ipAddress;
        multicastPort = port;
        this.samplings = samplings;
    }

    /**
     * Joins a multicast group and reads 10 messages.
     */
    public void start() {
        MulticastSocket multiSock = null;

        try {
            InetAddress group = InetAddress.getByName(multicastAddress);
            // Can bind to either INADDR_ANY or the multicast address - not to an adapter
            //multiSock = new MulticastSocket(multicastPort);
            multiSock = new MulticastSocket(new InetSocketAddress(group, multicastPort));
            //NetworkInterface netIf = NetworkInterface.getByInetAddress(addressOfAdapter);
            // multiSock.setNetworkInterface(netIf);
            multiSock.joinGroup(group);
            String localAddr = multiSock.getLocalAddress().getHostAddress();

            byte[] buf = new byte[BUF_SIZE];
            DatagramPacket packet = new DatagramPacket(buf, buf.length);

            System.out.printf("Get %d [%s:%d] time messages...%n", samplings,
                               multicastAddress, multicastPort);

            for (int i = 0; samplings == 0 || i < samplings; i++) {
                multiSock.receive(packet);
                System.out.printf("TimeOfDay: %s [%s:%d]->[%s:%d]%n",
                                  (new String(packet.getData(), 0, packet.getLength())),
                                  group.getHostAddress(), multicastPort,
                                  localAddr, multicastPort);
            }

            multiSock.leaveGroup(group);
        } catch (IOException ex) {
            System.out.println("Server error: " + ex);
        } finally {
            if (multiSock != null) {
                multiSock.close();
            }
        }
    }
}
