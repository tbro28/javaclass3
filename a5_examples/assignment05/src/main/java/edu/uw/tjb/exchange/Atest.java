package edu.uw.tjb.exchange;

import edu.uw.ext.framework.exchange.StockExchange;
import org.springframework.util.StopWatch;

public class Atest implements StockExchange {


    public static void main(String args[]) {

        StockExchange stockExchange = (StockExchange) new StopWatch();

        stockExchange.getQuote("GME");

    }


}
