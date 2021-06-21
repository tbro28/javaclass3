package edu.uw.exemplar;


public class Commander {

    /**
     * Demonstrate use of commands.
     *
     * @param args - unused
     */
    public static void main(String[] args) {
        // client creates the commands...
        Command on = new OnCommand();
        Command off = new OffCommand();
        
        // The server does the rest...
        Receiver r = new Receiver();
        
        Command commands[] = {on, off};
        // Accept the command and process it
        for (Command cmd : commands) {
            cmd.setReceiver(r);
            cmd.execute();
        }
    }

///////////////////////////////////////////////////////////////////////////////
// The classes/interface below this point would normally be separate files.  //
///////////////////////////////////////////////////////////////////////////////

    /**
     * The command interface
     */
    interface Command {
        void execute();
        void setReceiver(Receiver r);
    }
    
    /** The receiver. */
    public static class Receiver {
        public void action(OnCommand cmd) {
            System.out.println("Do the ON action. Command type: " + cmd.getClass().getSimpleName());
        }
        public void action(OffCommand cmd) {
            System.out.println("Do the OFF action. Command type: " + cmd.getClass().getSimpleName());
        }
    }

    /**
     * A concrete command.
     */
    public static class OnCommand implements Command {
        Receiver recvr;
        public void setReceiver(Receiver recvr) {
            this.recvr = recvr;
        }
        public void execute() {
            recvr.action(this);
        }
    }

    /**
     * Another concrete command.
     */
    public static class OffCommand implements Command {
        Receiver recvr;
        public void setReceiver(Receiver recvr) {
            this.recvr = recvr;
        }
        public void execute() {
            recvr.action(this);
        }
    }

}
