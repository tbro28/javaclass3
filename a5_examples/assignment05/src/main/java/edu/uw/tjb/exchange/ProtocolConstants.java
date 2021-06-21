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
    static String BUY_ORDER = "BUY_ORDER";

    /**
     * Event indicating the exchange has closed.
     */
    static String CLOSED_EVNT = "CLOSED_EVNT";

    /**
     * Indicates the exchange is closed.
     */
    static String CLOSED_STATE = "CLOSED_STATE";

    /**
     * The index of the command element.
     */
    static int	CMD_ELEMENT = ;

    /**
     * The character used to separate elements in the protocol.
     */
    static String ELEMENT_DELIMITER = "ELEMENT_DELIMITER";

    /**
     * Character encoding to use.
     */
    static String ENCODING = "ENCODING";

    /**
     * The index of the event type element.
     */
    static int EVENT_ELEMENT = ;

    /**
     * A request to execute a trade.
     */
    static String EXECUTE_TRADE_CMD = "EXECUTE_TRADE_CMD";

    /**
     * The index of the account id element in the execute trade command.
     */
    static int	EXECUTE_TRADE_CMD_ACCOUNT_ELEMENT = ;

    /**
     * The index of the shares element in the execute trade command.
     */
    static int EXECUTE_TRADE_CMD_SHARES_ELEMENT = ;

    /**
     * The index of the ticker element in the execute trade command.
     */
    static int EXECUTE_TRADE_CMD_TICKER_ELEMENT = ;

    /**
     * The index of the order type element in the execute trade command.
     */
    static int EXECUTE_TRADE_CMD_TYPE_ELEMENT = ;

    /**
     * A request for a stock price quote.
     */
    static String GET_QUOTE_CMD = "GET_QUOTE_CMD";

    /**
     * A request for the exchange's state.
     */
    static String GET_STATE_CMD = "GET_STATE_CMD";

    /**
     * A request for the ticker symbols for the traded stocks.
     */
    static String GET_TICKERS_CMD = "GET_TICKERS_CMD";

    /**
     * The invalid stock price - indicates stock is not on the exchange.
     */
    static int INVALID_STOCK = ;


    /**
     * Event indicating the exchange has opened.
     */
    static String OPEN_EVNT = "";

    /**
     * Indicates the exchange is open.
     */
    static String OPEN_STATE = "OPEN_STATE";

    /**
     * Event indicating a stock price has changed.
     */
    static String PRICE_CHANGE_EVNT = "PRICE_CHANGE_EVNT";

    /**
     * The index of the price element.
     */
    static int PRICE_CHANGE_EVNT_PRICE_ELEMENT = ;

    /**
     * The index of the ticker element.
     */
    static int PRICE_CHANGE_EVNT_TICKER_ELEMENT = ;

    /**
     * The index of the ticker element in the price quote command.
     */
    static int QUOTE_CMD_TICKER_ELEMENT = ;

    /**
     * Identifies an order as a sell order.
     */
    static String SELL_ORDER = "SELL_ORDER";



}
