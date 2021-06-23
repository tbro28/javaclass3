package edu.uw.tjb.exchange;


/**
 * Constants for the command strings composing the exchange protocol.
 * The protocol supports events and commands.
 * Events are one way messages sent from the exchange to the broker(s).
 *
 * The protocol supports the following events:
 *  Event: [OPEN_EVNT]
 *  -
 *  Event: [CLOSED_EVNT]
 *  -
 *  Event: [PRICE_CHANGE_EVNT][ELEMENT_DELIMITER]symbol[ELEMENT_DELIMITER]price
 */
public final class ProtocolConstants {

    /**
     * Identifies an order as a buy order.
     */
    final static String BUY_ORDER = "BUY_ORDER";

    /**
     * Event indicating the exchange has closed.
     */
    final static String CLOSED_EVNT = "CLOSED_EVNT";

    /**
     * Indicates the exchange is closed.
     */
    final static String CLOSED_STATE = "CLOSED_STATE";

    /**
     * The index of the command element.
     */
    final static int	CMD_ELEMENT = 0;

    /**
     * The character used to separate elements in the protocol.
     */
    final static String ELEMENT_DELIMITER = ":";

    /**
     * Character encoding to use.
     * Used with the sockets/streams?
     */
    final static String ENCODING = "";

    /**
     * A request to execute a trade.
     */
    final static String EXECUTE_TRADE_CMD = "EXECUTE_TRADE_CMD";

    /**
     * The index of the ticker element in the price quote command.
     */
    final static int QUOTE_CMD_TICKER_ELEMENT = 1;

    /**
     * The index of the account id element in the execute trade command.
     */
    final static int	EXECUTE_TRADE_CMD_ACCOUNT_ELEMENT = 2;

    /**
     * The index of the shares element in the execute trade command.
     */
    final static int EXECUTE_TRADE_CMD_SHARES_ELEMENT = 4;

    /**
     * The index of the ticker element in the execute trade command.
     */
    final static int EXECUTE_TRADE_CMD_TICKER_ELEMENT = 3;

    /**
     * The index of the order type element in the execute trade command.
     */
    final static int EXECUTE_TRADE_CMD_TYPE_ELEMENT = 1;

    /**
     * A request for a stock price quote.
     */
    final static String GET_QUOTE_CMD = "GET_QUOTE_CMD";

    /**
     * A request for the exchange's state.
     */
    final static String GET_STATE_CMD = "GET_STATE_CMD";

    /**
     * A request for the ticker symbols for the traded stocks.
     */
    final static String GET_TICKERS_CMD = "GET_TICKERS_CMD";

    /**
     * The invalid stock price - indicates stock is not on the exchange.
     */
    final static int INVALID_STOCK = -1;


    /**
     * Event indicating the exchange has opened.
     */
    final static String OPEN_EVNT = "OPEN_EVNT";

    /**
     * Indicates the exchange is open.
     */
    final static String OPEN_STATE = "OPEN_STATE";

    /**
     * Event indicating a stock price has changed.
     */
    final static String PRICE_CHANGE_EVNT = "PRICE_CHANGE_EVNT";

    /**
     * The index of the event type element.
     */
    final static int EVENT_ELEMENT = 0;

    /**
     * The index of the ticker element.
     */
    final static int PRICE_CHANGE_EVNT_TICKER_ELEMENT = 1;

    /**
     * The index of the price element.
     */
    final static int PRICE_CHANGE_EVNT_PRICE_ELEMENT = 2;

    /**
     * Identifies an order as a sell order.
     */
    final static String SELL_ORDER = "SELL_ORDER";



}
