package edu.uw.exemplar.protocol.state;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import edu.uw.exemplar.protocol.net.JokeRequestProcessor;

/**
 * JokeRequestProcessor implementation that simply maps the request strings
 * to methods on a JokeStateIF instance, and advances the state after each
 * invocation.
 */
public class StateRequestProcessor implements JokeRequestProcessor {
    /**
     * Reads the request string from the reader and invokes the corresponding 
     * method on a JokeStateIF instance advances to the next state and writes
     * the returned value to the provided writer.
     * @param bufRdr the reader to obtain requests from
     * @param prntWrtr the writer to write responses to
     * @throws IOException if any are raised
     */
    @Override
    public void processRequests(final BufferedReader bufRdr,
                                final PrintWriter prntWrtr) throws IOException {
        String inputLine = null;
        String response = null;
        JokeStateIF joker = new ReadyState();

        while ((inputLine = bufRdr.readLine()) != null) {
            switch (inputLine) {
                case "knock":
                    response = joker.knock();
                    joker = joker.nextState();
                    break;
                case "whosThere":
                    response = joker.whosThere();
                    joker = joker.nextState();
                    break;
                case "who":
                    response = joker.who();
                    joker = joker.nextState();
                    break;
                default:
                    // return null;
            }
            prntWrtr.println(response);
        } 
    }
}
