package demo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Formatter;

import edu.uw.exemplar.multicast.TimeServer;

/**
 * A simple multicast client server.  Joins a multicast group and sends the
 * date/time string every second.
 *
 * @author Russ Moul
 */
public final class TimeServerDemo {
    private static final String MULTICAST_OPTION = "-m";
    private static final String PORT_OPTION = "-p";
    /**
     * Prevent instantiation.
     */
    private TimeServerDemo() {
    }

    public static void usage(final String msg) {
        Formatter fmt = new Formatter();
        fmt.format("%s%n", msg)
        .format("usage: TimeServerDemo [-a adapter/address] [-m multicast_address] [-p port]%n")
        .format("    -m multicast_address - destination multicast address%n")
        .format("    -p port              - destination port");
        System.out.println(fmt.toString());
        System.exit(1);
    }

    /**
     * Create the server and start it.
     *
     * @param args command line arguments,
     *               -m multicast_address - destination multicast address, defaults to 224.0.0.1
     *               -p port              - destination port, defaults to 5500
     * @throws IOException 
     */
    public static void main(final String[] args) throws IOException {
        String multicastAddr = "224.0.1.0";
        int port = 5500;
        // -a adapter/address, -m multicast address -p port
        for (int i = 0; i < args.length; i += 2) {
            if (i + 1 < args.length) {
                if (MULTICAST_OPTION.equals(args[i])) {
                    multicastAddr = args[i+1];
                } else if (PORT_OPTION.equals(args[i])) {
                    port = Integer.parseInt(args[i+1]);
                } else {
                    usage("invalid option " + args[i]);

                }
            } else {
                usage("option " + args[i] + " requires a value");
            }
        }
        

        TimeServer server = new TimeServer(multicastAddr, port);
        server.start();
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        br.readLine();
    }
}
