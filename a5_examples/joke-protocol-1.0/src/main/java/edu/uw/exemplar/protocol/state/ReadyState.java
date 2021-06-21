package edu.uw.exemplar.protocol.state;

import edu.uw.exemplar.joke.KnockKnockData;
/**
 * Implementation of the READY state, initializes the joke.
 */
public class ReadyState implements JokeStateIF {
	/** The joke to be used. */
    private KnockKnockData joke = KnockKnockData.getJoke();

    /**
     * Initiates the joke, and return "Knock!, Knock!"
     * @return always "Knock!, Knock!"
     */
    @Override
    public String knock() {
        return KnockKnockData.KNOCK_KNOCK;
    }

    /**
     * Not allowed in the state.
     * @return Always raises the exception
     * @throws IllegalStateException always raises this exception
     */
    @Override
    public String whosThere() {
        throw new IllegalStateException("Operation 'whosThere' not allowed in 'Ready' state.");
    }

    /**
     * Not allowed in the state.
     * @return Always raises the exception
     * @throws IllegalStateException always raises this exception
     */
    @Override
    public String who() {
        throw new IllegalStateException("Operation 'who' not allowed in 'Ready' state.");
    }

    /**
     * Advances to the INITIATED state.
     * @return an instance of InitiatedState.
     */
    @Override
    public JokeStateIF nextState() {
        return new InitiatedState(joke);
    }

}
