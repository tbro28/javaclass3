package edu.uw.exemplar.joke;

/**
 * Represents a knock-knock joke through three methods, knock, whosThere and who.
 * These methods must always be executed in the prescribe sequence;
 * knock->whosThere->who, if a method is executed out of this sequence an
 * IlleganStateException is thrown.  Invocation of the who method also resets
 * the sequence, so that it may be repeated.
 */
public class KnockKnockJoke {
    /** The current joke. */
    private KnockKnockData joke;

    /** The states in the knock-knock joke protocol. */
    private enum JokeState {
        READY,
        INITIATED,
        INPROGRESS
    }

    /** The current state in the knock-knock joke protocol. */
    private JokeState state = JokeState.READY;

    /**
     * Initiates the joke, responds with "Knock!, Knock!".
     * @return Always "Knock!, Knock!"
     * @throws IllegalStateException if invoked out of sequence
     */
    public synchronized String knock() {
        if (state != JokeState.READY) {
            throw new IllegalStateException("Command 'knock' only valid in READY state, current state is " + state);
        }
        joke = KnockKnockData.getJoke();
        state = JokeState.INITIATED;
        return KnockKnockData.KNOCK_KNOCK;
    }

    /**
     * Delivers the joke set up line.
     * @return The joke set up line
     * @throws IllegalStateException if invoked out of sequence
     */
    public synchronized String whosThere() {
        if (state != JokeState.INITIATED) {
            throw new IllegalStateException("Command 'whosThere' only valid in INITIATED state, current state is " + state);
        }
        state = JokeState.INPROGRESS;
        return joke.getLeadIn();
    }

    /**
     * Delivers the punch line.
     * @return The punch line
     * @throws IllegalStateException if invoked out of sequence
     */
    public synchronized String who() {
        if (state != JokeState.INPROGRESS) {
            throw new IllegalStateException("Command 'who' only valid in INPROGRESS state, current state is " + state);
        }
        state = JokeState.READY;
        return joke.getPunchLine();
    }

    /**
     * Demonstrates this class, processes a number of jokes.
     * @param args not used
     */
    public static void main(String[] args) {
        KnockKnockJoke joker = new KnockKnockJoke();

        for(int i = 0; i < 4; i++) {
            System.out.printf("> %s%n", joker.knock());
            String who = joker.whosThere();
            System.out.printf("< %s%n> %s%n", KnockKnockData.WHOS_THERE, who);
            System.out.printf("< %s %s%n> %s%n%n", who, KnockKnockData.WHO, joker.who());
        }
    }

}
