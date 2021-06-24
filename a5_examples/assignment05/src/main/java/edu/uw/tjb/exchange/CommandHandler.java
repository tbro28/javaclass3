package edu.uw.tjb.exchange;

import java.io.*;
import java.net.Socket;
import java.util.Optional;

import edu.uw.ext.framework.exchange.StockExchange;
import edu.uw.ext.framework.exchange.StockQuote;
import edu.uw.ext.framework.order.MarketBuyOrder;
import edu.uw.ext.framework.order.MarketSellOrder;
import edu.uw.ext.framework.order.Order;

import static edu.uw.tjb.exchange.ProtocolConstants.*;


/**
 * An instance of this class is dedicated to executing commands received from clients.
 */
public class CommandHandler implements Runnable {

    private Socket sock;
    private StockExchange realExchange;


    public CommandHandler(Socket sock, StockExchange realExchange) {
        this.sock = sock;
        this.realExchange = realExchange;
    }

    /**
     * Server accepts connections and reads command from the connection,
     * interprets the string and invokes the corresponding method on the "real" server,
     * encodes the result as a string writes it to the connected socket.
     *
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
     *
     * https://learning.oreilly.com/videos/advanced-java-development/9781491960400/9781491960400-video247585/
     * Get inputstream and outputstream wrap various IO around it.
     * Walks thru echo server, like class example.
     *
     */
    @Override
    public void run() {

        Socket threadSocket = sock;
        try (
            InputStream inputStream = threadSocket.getInputStream();
            //BufferedReader in = new BufferedReader( new InputStreamReader(threadSocket));
            BufferedReader in = new BufferedReader( new InputStreamReader(inputStream));

            OutputStream outputStream = threadSocket.getOutputStream();
            PrintWriter out = new PrintWriter(outputStream);
        ) {

            /*
            reads command from the connection,
            interprets the string and invokes the corresponding method on the "real" server,
            encodes the result as a string writes it to the connected socket.
            */
            String incomingRequest = in.readLine();

            //Parse on delimiter
            String[] requestCommand = incomingRequest.split(ELEMENT_DELIMITER);

            //Figure out the command and send it to the exchange and get the result.
            //Sequence diagram: far right getQuote("SMBL").
            String command = requestCommand[CMD_ELEMENT];

            //Figure out what the command type is.
            //Should be 4 cases: get state, get tickers, get quote, execute trade.
            switch(command) {

                case GET_STATE_CMD:
                    out.println(realExchange.isOpen());
                    break;

                case GET_TICKERS_CMD:
                    String[] tickers = realExchange.getTickers();

                    //Needs to be the "protocol" format before sending result back to the client!
                    //out.println(tickers);
                    String validProtocolFormat = String.join(ELEMENT_DELIMITER, tickers);
                    out.println(validProtocolFormat);
                    break;

                case GET_QUOTE_CMD:
                    //Not sure if this returns the quote value.
                    //What is a "StockQuote"?  Format is probably wrong???
                    Optional<StockQuote> quote = realExchange.getQuote(requestCommand[QUOTE_CMD_TICKER_ELEMENT]);
                    out.println(quote);
                    break;

                default:

                    /*
                    Request:  EXECUTE_TRADE_CMD:BUY_ORDER|SELL_ORDER:account_id:symbol:shares
                    Execute a trade, where BUY_ORDER or SELL_ORDER indicates the type of trade to execute,
                    account_id identifies the account the trade is being performed on the behalf of,
                    symbol specifies the ticker symbol of the stock to be traded, and shares specifies
                    the number of shares to trade.

                    Buy versus sell order.

                    Response: execution_price
                    The price the trade was executed at.

                    final static String BUY_ORDER = "BUY_ORDER";
                    final static int EXECUTE_TRADE_CMD_ACCOUNT_ELEMENT = 2;
                    final static int EXECUTE_TRADE_CMD_SHARES_ELEMENT = 4;
                    final static int EXECUTE_TRADE_CMD_TICKER_ELEMENT = 3;
                    final static int EXECUTE_TRADE_CMD_TYPE_ELEMENT = 1;
                    */

                    String account = requestCommand[EXECUTE_TRADE_CMD_ACCOUNT_ELEMENT];
                    int shares = Integer.parseInt(requestCommand[EXECUTE_TRADE_CMD_SHARES_ELEMENT]);
                    String ticker = requestCommand[EXECUTE_TRADE_CMD_TICKER_ELEMENT];
                    String commandBuyOrSell = requestCommand[EXECUTE_TRADE_CMD_TYPE_ELEMENT];
                    Order order = null;
                    int executionPrice;

                    if(commandBuyOrSell.equals(BUY_ORDER)) {
                        //send buy order to StockExchange.
                        order = new MarketBuyOrder(account, shares, ticker);
                    }
                    else {
                        //send the sell order.
                        order = new MarketSellOrder(account, shares, ticker);
                    }

                    //Executes the "order" using the received values.
                    executionPrice = realExchange.executeTrade(order);
                    out.println(executionPrice);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
