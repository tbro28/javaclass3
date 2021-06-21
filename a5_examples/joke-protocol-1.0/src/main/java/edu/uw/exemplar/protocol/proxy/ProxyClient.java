package edu.uw.exemplar.protocol.proxy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import edu.uw.exemplar.joke.KnockKnockData;
import edu.uw.exemplar.joke.KnockKnockJoke;
import edu.uw.exemplar.protocol.net.Server;

/**
 * Implements the client, conforming to the proxy interface.  Each method sends
 * the command name to the server which then invokes the corresponding method
 * on the "real" knock-knock joke instance, which also implements the proxy
 * interface.
 */
public class ProxyClient implements KnockKnockProxyIF {
    private PrintWriter prntWrtr;
    private BufferedReader bufRdr;
    
    public ProxyClient(final Socket server) throws IOException {
        prntWrtr = new PrintWriter(server.getOutputStream(), true);
        bufRdr = new BufferedReader(new InputStreamReader(server.getInputStream()));

    }

    /**
     * Send the "knock" request, reads the response and returns it.
     * @return the knock response
     */
    @Override
    public String knock() {
        prntWrtr.println("knock");
        String response = null;
        try {
            response = bufRdr.readLine();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return response;
    }

    /**
     * Send the "whosThere" request, reads the response and returns it.
     * @return the joke set up line
     */
    @Override
    public String whosThere() {
        prntWrtr.println("whosThere");
        String response = null;
        try {
            response = bufRdr.readLine();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return response;
    }

    /**
     * Send the "who" request, reads the response and returns it.
     * @return the punch line
     */
    @Override
    public String who() {
        prntWrtr.println("who");
        String response = null;
        try {
            response = bufRdr.readLine();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return response;
    }

    /**
     * Demonstrates the direct use of the KnockKnockJoke and the use through
     * a proxy instance.
     * @param args not used
     * @throws UnknownHostException if unable to resolve host
     * @throws IOException if any occur
     */
    public static final void main(final String[] args) throws UnknownHostException, IOException {
    	System.out.println("Using the KnockKnockJoke directly (local)");
    	System.out.println("=========================================");
    	KnockKnockJoke joker = new KnockKnockJoke();
    	for(int i = 0; i < 2; i++) {
    		System.out.printf("> %s%n", joker.knock());
    		String who = joker.whosThere();
    		System.out.printf("< %s%n> %s%n", KnockKnockData.WHOS_THERE, who);
    		System.out.printf("< %s %s%n> %s%n%n", who, KnockKnockData.WHO, joker.who());
    	}

    	System.out.println();
    	System.out.println("Using the KnockKnockJoke through a proxy (distributed)");
    	System.out.println("======================================================");
    	try (Socket server = new Socket("localhost", Server.DEFAULT_PORT)) {
    		ProxyClient proxyJoker = new ProxyClient(server);
    		for(int i = 0; i < 2; i++) {
    			System.out.printf("> %s%n", proxyJoker.knock());
    			String who = proxyJoker.whosThere();
    			System.out.printf("< %s%n> %s%n", KnockKnockData.WHOS_THERE, who);
    			System.out.printf("< %s %s%n> %s%n%n", who, KnockKnockData.WHO, proxyJoker.who());
    		}
    	}
    }
}
