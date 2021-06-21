package edu.uw.exemplar.protocol.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Client for knock-knock jokes, interacts with a joke server using the
 * "knock-knock" protocol.
 */
public class Client {

    /**
     * Connects the the joke server and retrieves jokes using the "knock-knock" protocol
     * 
     * @param args not uses
     * @throws IOException if any are raised
     */
    public static void main(final String[] args) throws IOException {
        String hostname = "localhost";
        try (Socket server = new Socket(hostname, Server.DEFAULT_PORT);
             PrintWriter prntWrtr = new PrintWriter(server.getOutputStream(), true);
             BufferedReader bufRdr = new BufferedReader(new InputStreamReader(server.getInputStream()))) {
            for (int i = 0; i < 4; i++) {
                String response;
                prntWrtr.println("knock");
                response = bufRdr.readLine();
                if (response == null) {
                    throw new IllegalStateException("Received illegal response from 'knock' request.");
                }
                System.out.printf("> %s%n", response);
                
                prntWrtr.println("whosThere");
                response = bufRdr.readLine();
                if (response == null) {
                    throw new IllegalStateException("Received illegal response from 'whosThere' request.");
                }
                System.out.printf("< Who's there?%n> %s%n", response);
    
                String who = response;
                prntWrtr.println("who");
                response = bufRdr.readLine();
                if (response == null) {
                    throw new IllegalStateException("Received illegal response from 'who' request.");
                }
                System.out.printf("< %s who?%n> %s%n%n", who, response);
            }

        } catch (UnknownHostException e) {
            System.err.printf("Unable to resolve host: %s%n", hostname);
        } catch (IOException e) {
            System.err.printf("Unable to connect to %s:%d%n", hostname, Server.DEFAULT_PORT);
        }
    }

}
