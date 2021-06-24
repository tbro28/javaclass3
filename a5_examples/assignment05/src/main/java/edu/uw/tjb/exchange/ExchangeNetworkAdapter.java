package edu.uw.tjb.exchange;

import edu.uw.ext.framework.exchange.ExchangeAdapter;
import edu.uw.ext.framework.exchange.ExchangeEvent;
import edu.uw.ext.framework.exchange.StockExchange;

import java.io.Closeable;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static edu.uw.tjb.exchange.ProtocolConstants.*;

/**
 * Provides a network interface to an exchange.
 * Handles the outgoing multicast to NetEventProcessor.
 * Look at the Typical Event Sequence.
 */
public class ExchangeNetworkAdapter implements ExchangeAdapter {


    private ExecutorService commandListenerExecutor;
    private CommandListener commandListener;
    private String exchange;
    private String multicastIP;
    private int multicastPort;
    private int commandPort;



    MulticastSocket socket;
    InetAddress group;
    byte[] buffer=new byte[1024];
    DatagramPacket packet;



    public ExchangeNetworkAdapter(StockExchange exchange,
                                  String multicastIP,
                                  int multicastPort,
                                  int commandPort) {

        commandListener = new CommandListener(commandPort, exchange);
        commandListenerExecutor = Executors.newSingleThreadExecutor();
        commandListenerExecutor.execute(commandListener);

        // Added after review in class.
        // It registers to listen for events (an ExchangeEvent in the diagram)
        // from the exchange (matching the events below).
        exchange.addExchangeListener(this);

        //https://www.developer.com/design/how-to-multicast-using-java-sockets/

        try {
            socket = new MulticastSocket(multicastPort);
            group = InetAddress.getByName(multicastIP);
            socket.joinGroup(group);
        } catch (IOException e) {
            e.printStackTrace();
        }




    }


    /**
     * The exchange has opened and prices are adjusting - add listener to receive
     * price change events from the exchange and multicast them to brokers.
     *
     * The Typical Event Sequence diagram details this flow.
     *
     * @param exchangeEvent
     */
    @Override
    public void exchangeOpened(ExchangeEvent exchangeEvent) {
        byte[] msg = OPEN_EVNT.getBytes();
        packet = new DatagramPacket(msg, msg.length);
        try {
            socket.send(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    /**
     * The exchange has closed - notify clients and remove price change listener.
     *
     * @param exchangeEvent
     */
    @Override
    public void exchangeClosed(ExchangeEvent exchangeEvent) {
        byte[] msg = CLOSED_EVNT.getBytes();
        packet = new DatagramPacket(msg, msg.length);
        try {
            socket.send(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    /**
     * Processes price change events.
     *
     * This is detailed specifically in the event diagram.
     * Build the message and send it out via multicast:
     *
     * in: priceChanged(BA:500)
     * out: multicastSend(PRICE_CHANGE:BA:500)
     *
     * multicast, beginning of class
     *
     * https://www.baeldung.com/java-broadcast-multicast
     *
     * @param exchangeEvent
     */
    @Override
    public void priceChanged(ExchangeEvent exchangeEvent) {
        String pChange = String.join(ELEMENT_DELIMITER,
                        PRICE_CHANGE_EVNT,
                        exchangeEvent.getTicker(),
                        Integer.toString(exchangeEvent.getPrice()));

        //socket = new DatagramSocket();
        //InetAddress group = InetAddress.getByName(ipAddress);
        byte[] msg = pChange.getBytes();
        packet = new DatagramPacket(msg, msg.length);
        try {
            socket.send(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    /**
     * Closes this resource, relinquishing any underlying resources.
     * This method is invoked automatically on objects managed by the
     * {@code try}-with-resources statement.
     *
     * <p>While this interface method is declared to throw {@code
     * Exception}, implementers are <em>strongly</em> encouraged to
     * declare concrete implementations of the {@code close} method to
     * throw more specific exceptions, or to throw no exception at all
     * if the close operation cannot fail.
     *
     * <p> Cases where the close operation may fail require careful
     * attention by implementers. It is strongly advised to relinquish
     * the underlying resources and to internally <em>mark</em> the
     * resource as closed, prior to throwing the exception. The {@code
     * close} method is unlikely to be invoked more than once and so
     * this ensures that the resources are released in a timely manner.
     * Furthermore it reduces problems that could arise when the resource
     * wraps, or is wrapped, by another resource.
     *
     * <p><em>Implementers of this interface are also strongly advised
     * to not have the {@code close} method throw {@link
     * InterruptedException}.</em>
     * <p>
     * This exception interacts with a thread's interrupted status,
     * and runtime misbehavior is likely to occur if an {@code
     * InterruptedException} is {@linkplain Throwable#addSuppressed
     * suppressed}.
     * <p>
     * More generally, if it would cause problems for an
     * exception to be suppressed, the {@code AutoCloseable.close}
     * method should not throw it.
     *
     * <p>Note that unlike the {@link Closeable#close close}
     * method of {@link Closeable}, this {@code close} method
     * is <em>not</em> required to be idempotent.  In other words,
     * calling this {@code close} method more than once may have some
     * visible side effect, unlike {@code Closeable.close} which is
     * required to have no effect if called more than once.
     * <p>
     * However, implementers of this interface are strongly encouraged
     * to make their {@code close} methods idempotent.
     *
     * @throws Exception if this resource cannot be closed
     */
    @Override
    public void close() throws Exception {
        socket.close();
    }
}
