package edu.uw.tjb.exchange;


/**
 * Accepts connections and passes them to a CommandHandler,
 * for the reading and processing of commands.
 */
public class CommandListener implements Runnable {




    /**
     * Accept connections, and creates a CommandExecutor for servicing the connection.
     *
     * When an object implementing interface {@code Runnable} is used
     * to create a thread, starting the thread causes the object's
     * {@code run} method to be called in that separately executing
     * thread.
     * <p>
     * The general contract of the method {@code run} is that it may
     * take any action whatsoever.
     *
     * @see Thread#run()
     */
    @Override
    public void run() {

    }


    /**
     * Terminates this thread gracefully.
     */
    public void terminate(){

    }

}
