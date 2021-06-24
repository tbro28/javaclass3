package edu.uw.tjb.exchange;

import edu.uw.ext.framework.exchange.ExchangeListener;
import edu.uw.ext.framework.exchange.StockExchange;
import edu.uw.ext.framework.exchange.StockQuote;
import edu.uw.ext.framework.order.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Optional;

import static edu.uw.tjb.exchange.ProtocolConstants.*;


/**
 * Client for interacting with a network accessible exchange.
 * This SocketExchange methods encode the method request as a string,
 * per ProtocolConstants, and send the command to the ExchangeNetworkAdapter,
 * receive the response decode it and return the result.
 */
public class ExchangeNetworkProxy implements StockExchange {


    private static final Logger log = LoggerFactory.getLogger(ExchangeNetworkProxy.class);

    private String cmdIpAddress;
    private int cmdPort;
    private NetEventProcessor netEventProcessor;



    /**
     * eventIpAddress - the multicast IP address to connect to
     * eventPort - the multicast port to connect to
     * cmdIpAddress - the address the exchange accepts request on
     * cmdPort - the address the exchange accepts request on
     *
     * @param eventIpAddress
     * @param eventPort
     * @param cmdIpAddress
     * @param cmdPort
     */
    public ExchangeNetworkProxy(String eventIpAddress,
                                int eventPort,
                                String cmdIpAddress,
                                int cmdPort) {

        this.cmdIpAddress = cmdIpAddress;
        this.cmdPort = cmdPort;
        netEventProcessor = new NetEventProcessor(eventIpAddress, eventPort);
    }


    /**
     * The state of the exchange.
     * https://learning.oreilly.com/videos/advanced-java-development/9781491960400/9781491960400-video247585/
     * Walks through echo example, like from class.
     *
     *
     * @return
     */
    @Override
    public boolean isOpen() {

        String response = "";

        try (
                Socket client = new Socket(cmdIpAddress, cmdPort);
                InputStream inputStream = client.getInputStream();
                BufferedReader in = new BufferedReader( new InputStreamReader(inputStream));
                OutputStream outputStream = client.getOutputStream();
                PrintWriter out = new PrintWriter(outputStream);
        ) {
                out.println(GET_STATE_CMD);
                response = in.readLine();

                /*
                out.writeUTF("Hello from " + client.getLocalSocketAddress());
                InputStream inFromServer = client.getInputStream();
                DataInputStream in = new DataInputStream(inFromServer);
                System.out.println("Server says " + in.readUTF())*/
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return OPEN_STATE.equals(response);
    }


    /**
     * Gets the ticker symbols for all of the stocks in the traded on the exchange.
     *
     * @return
     */
    @Override
    public String[] getTickers() {

        String response = "";
        String[] tickerList = null;

        try (
                Socket client = new Socket(cmdIpAddress, cmdPort);
                InputStream inputStream = client.getInputStream();
                BufferedReader in = new BufferedReader( new InputStreamReader(inputStream));
                OutputStream outputStream = client.getOutputStream();
                PrintWriter out = new PrintWriter(outputStream);
        ) {
            out.println(GET_TICKERS_CMD);
            response = in.readLine();

            tickerList = response.split(ELEMENT_DELIMITER);

        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return tickerList;
    }


    /**
     * Gets a stocks current price.
     *
     * @param ticker
     * @return
     */
    @Override
    public Optional<StockQuote> getQuote(String ticker) {

        String response = "";
        Optional<StockQuote> stockQuote = null;

        try (
                Socket client = new Socket(cmdIpAddress, cmdPort);
                InputStream inputStream = client.getInputStream();
                BufferedReader in = new BufferedReader( new InputStreamReader(inputStream));
                OutputStream outputStream = client.getOutputStream();
                PrintWriter out = new PrintWriter(outputStream);
        ) {
            String quoteCommand = String.join(ELEMENT_DELIMITER, GET_QUOTE_CMD, ticker);
            out.println(quoteCommand);
            response = in.readLine();
            stockQuote = Optional.of(new StockQuote(ticker, Integer.parseInt(response)));
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return stockQuote;
    }


    /**
     * Adds a market listener.
     * For the multicast, which is handled by the NetEventProcessor.
     *
     * @param exchangeListener
     */
    @Override
    public void addExchangeListener(ExchangeListener exchangeListener) {
        netEventProcessor.addExchangeListener(exchangeListener);
    }


    /**
     * Removes a market listener.
     * For the multicast, which is handled by the NetEventProcessor.
     *
     * @param exchangeListener
     */
    @Override
    public void removeExchangeListener(ExchangeListener exchangeListener) {
        netEventProcessor.removeExchangeListener(exchangeListener);
    }


    /**
     * Creates a command to execute a trade and sends it to the exchange.
     *
     * Request:  EXECUTE_TRADE_CMD:BUY_ORDER|SELL_ORDER:account_id:symbol:shares
     *
     * Execute a trade, where BUY_ORDER or SELL_ORDER indicates the type of trade to execute,
     * account_id identifies the account the trade is being performed on the behalf of,
     * symbol specifies the ticker symbol of the stock to be traded, and shares specifies
     * the number of shares to trade.
     *
     * Response: execution_price
     *
     * The price the trade was executed at.
     *
     * @param order
     * @return
     */
    @Override
    public int executeTrade(Order order) {

        //Put the trade together with delimiter.
        //Buy or sell, send it out, get the response.
        String response = "";
        String orderCommand = "";
        String orderToExecute = "";

        if (order.isBuyOrder())
            orderToExecute = BUY_ORDER;
        else
            orderToExecute = SELL_ORDER;

        orderCommand = String.join(ELEMENT_DELIMITER,
                EXECUTE_TRADE_CMD,
                orderToExecute,
                order.getAccountId(),
                order.getStockTicker(),
                Integer.toString(order.getNumberOfShares()));

        try (
                Socket client = new Socket(cmdIpAddress, cmdPort);
                InputStream inputStream = client.getInputStream();
                BufferedReader in = new BufferedReader( new InputStreamReader(inputStream));
                OutputStream outputStream = client.getOutputStream();
                PrintWriter out = new PrintWriter(outputStream);
        ) {
            out.println(orderCommand);
            response = in.readLine();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return Integer.parseInt(response);
    }

}
