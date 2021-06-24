package edu.uw.tjb.exchange;

import edu.uw.ext.framework.exchange.ExchangeEvent;
import edu.uw.ext.framework.exchange.ExchangeListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.event.EventListenerList;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

import static edu.uw.tjb.exchange.ProtocolConstants.*;

/**
 * Listens for (by joining the multicast group) and processes events received from the exchange.
 * Processing the events consists of propagating them to registered listeners.
 */
public class NetEventProcessor implements Runnable {

    private static final Logger log = LoggerFactory.getLogger(NetEventProcessor.class);

    private String eventIpAddress;
    private int eventPort;

    ExchangeListener exchangeListener;

    //From notes and reviews.
    private EventListenerList eventListenerList = new EventListenerList();


    public NetEventProcessor (String eventIpAddress, int eventPort) {
        this.eventIpAddress = eventIpAddress;
        this.eventPort = eventPort;
    }


    /**
     * Continuously accepts and processes market and price change events.
     *
     * When an object implementing interface {@code Runnable} is used
     * to create a thread, starting the thread causes the object's
     * {@code run} method to be called in that separately executing
     * thread.
     * <p>
     * The general contract of the method {@code run} is that it may
     * take any action whatsoever.
     *
     * @see Thread#run()
     */
    @Override
    public void run() {

        byte[] buffer=new byte[1024];

        MulticastSocket socket = null;
        try {
            socket = new MulticastSocket(eventPort);
            InetAddress group = InetAddress.getByName(eventIpAddress);
            socket.joinGroup(group);
        } catch (IOException e) {
            e.printStackTrace();
        }

        while(!socket.isClosed()){

            log.info("Waiting for message...");

            DatagramPacket packet=new DatagramPacket(buffer,
                    buffer.length);
            try {
                socket.receive(packet);
            } catch (IOException e) {
                e.printStackTrace();
            }
            String msg = new String(packet.getData(),
                    packet.getOffset(),packet.getLength());

            log.info("Message received - " + msg);

            String[] receivedMessage = msg.split(ELEMENT_DELIMITER);
            String event = receivedMessage[EVENT_ELEMENT];

            //Needs to "fireListeners" (see Typical Even Sequence diagram) for events
            //ExchangeEvent to the broker
            switch (event) {

                case PRICE_CHANGE_EVNT:
                    //event priceChanged("BA",500)
                    //String ticker = receivedMessage[GET_TICKERS_CMD];
                    String ticker = receivedMessage[PRICE_CHANGE_EVNT_TICKER_ELEMENT];
                    int price = Integer.parseInt(receivedMessage[PRICE_CHANGE_EVNT_PRICE_ELEMENT]);

                    //This won't work, because there is more than one event?
                    //Solution had a list of listeners that were cycled through
                    // (I think); this seems okay to me though??? Need to review.
                    exchangeListener.priceChanged(ExchangeEvent.newPriceChangedEvent(this, ticker, price));
                    break;
                case OPEN_EVNT:
                    exchangeListener.exchangeOpened(ExchangeEvent.newOpenedEvent(this));
                    break;
                case CLOSED_EVNT:
                    exchangeListener.exchangeClosed(ExchangeEvent.newClosedEvent(this));
                    break;
                default:
                    log.info("No event matched.");
            }
        }
    }


    /**
     * Adds a market listener?
     *
     * @param l
     */
    public void addExchangeListener(ExchangeListener l) {
        eventListenerList.add(ExchangeListener.class, l);
    }


    /**
     * Removes a market listener?
     *
     * @param l
     */
    public void removeExchangeListener(ExchangeListener l) {
        eventListenerList.remove(ExchangeListener.class, l);
    }

}
