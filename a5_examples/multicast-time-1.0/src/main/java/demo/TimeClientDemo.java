package demo;

import java.util.Formatter;

import edu.uw.exemplar.multicast.TimeClient;

/**
 * Runs the time of day client.
 *
 * @author Russ Moul
 */
public final class TimeClientDemo {
    private static final String MULTICAST_OPTION = "-m";
    private static final String PORT_OPTION = "-p";
    private static final String SAMPLES_OPTION = "-s";

    /**
     * Prevent instantiation.
     */
    private TimeClientDemo() {
    }

    public static void usage(final String msg) {
        Formatter fmt = new Formatter();
        fmt.format("%s%n", msg)
        .format("usage: TimeServerDemo [-a adapter/address] [-m multicast_address] [-p port] [-s samples]%n")
        .format("    -m multicast_address - destination multicast address, defaults to 224.0.0.1%n")
        .format("    -p port              - destination port, defaults to 5500%n")
        .format("    -s samples           - the number of samples to receive, 0 for infinite, defaults to 10%n");
        System.out.println(fmt.toString());
        fmt.close();
        System.exit(1);
    }

    /**
     * Create the client and start it.
     *
     * @param args command line arguments,
     *               -m multicast_address - destination multicast address, defaults to 224.0.0.1
     *               -p port              - destination port, defaults to 5500
     *               -s samples           - the number of samples to receive, 0 for infinite, defaults to 10
     */
    public static void main(final String[] args) {        
        String multicastAddr = "224.0.1.0";
        int port = 5500;
        int samples = 10;

        for (int i = 0; i < args.length; i += 2) {
            if (i + 1 < args.length) {
                if (MULTICAST_OPTION.equals(args[i])) {
                    multicastAddr = args[i+1];
                } else if (PORT_OPTION.equals(args[i])) {
                    port = Integer.parseInt(args[i+1]);
                } else if (SAMPLES_OPTION.equals(args[i])) {
                    samples = Integer.parseInt(args[i+1]);
                } else {
                    usage("invalid option " + args[i]);

                }
            } else {
                usage("option " + args[i] + " requires a value");
            }
        }

        TimeClient client = new TimeClient(multicastAddr, port, samples);
        client.start();
    }
}
