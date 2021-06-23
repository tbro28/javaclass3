package edu.uw.tjb.exchange;

import edu.uw.ext.framework.exchange.ExchangeListener;
import edu.uw.ext.framework.exchange.StockExchange;
import edu.uw.ext.framework.exchange.StockQuote;
import edu.uw.ext.framework.order.Order;

import java.util.Optional;

public class TestExchange implements StockExchange {
    @Override
    public boolean isOpen() {
        return true;
    }

    @Override
    public String[] getTickers() {
        String[] tickers = new String[1];

        tickers[0] = "GME";

        return tickers;
    }

    @Override
    public Optional<StockQuote> getQuote(String s) {

        StockQuote s3 = new StockQuote("SCHX", 300);

        Optional<StockQuote> stockQuote = Optional.of(s3);

        return stockQuote;
    }

    @Override
    public void addExchangeListener(ExchangeListener exchangeListener) {

    }

    @Override
    public void removeExchangeListener(ExchangeListener exchangeListener) {

    }

    @Override
    public int executeTrade(Order order) {
        return 500;
    }
}
