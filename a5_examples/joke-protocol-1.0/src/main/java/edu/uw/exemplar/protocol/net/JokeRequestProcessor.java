package edu.uw.exemplar.protocol.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Request processor interface, allows plugging in of different knock-knock
 * protocol implementations.
 */
public interface JokeRequestProcessor {
    /**
     * Operation for reading a request and writing a response.
     * @param bufRdr the Reader to read the request from
     * @param prntWrtr the Writer to write the response to
     * @throws IOException if any are raised
     */
    void processRequests(BufferedReader bufRdr, PrintWriter prntWrtr) throws IOException;
}
