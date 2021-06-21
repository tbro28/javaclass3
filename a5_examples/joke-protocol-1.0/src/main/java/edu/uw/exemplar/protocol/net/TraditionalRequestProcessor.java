package edu.uw.exemplar.protocol.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import edu.uw.exemplar.joke.KnockKnockJoke;

/**
 * JokeRequestProcessor implementation that simply maps the request strings
 * to methods on a KnockKnockJock instance.
 */
public class TraditionalRequestProcessor implements JokeRequestProcessor {

    /**
     * Reads the request string from the reader and invokes the corresponding 
     * method on a KnockKnockJoke instance and writes the returned value to the
     * provided writer.
     * @param bufRdr the reader to obtain requests from
     * @param prntWrtr the writer to write responses to
     * @throws IOException if any are raised
     */
    @Override
    public void processRequests(final BufferedReader bufRdr,
                                final PrintWriter prntWrtr) throws IOException {
        String inputLine = null;
        String response = null;
        KnockKnockJoke joke = new KnockKnockJoke();
        while ((inputLine = bufRdr.readLine()) != null) {
            switch (inputLine) {
                case "knock":
                    response = joke.knock();
                    break;
                case "whosThere":
                    response = joke.whosThere();
                    break;
                case "who":
                    response = joke.who();
                    break;
                default:
                    // return null;
            }
            prntWrtr.println(response);
        } 
    }

}
