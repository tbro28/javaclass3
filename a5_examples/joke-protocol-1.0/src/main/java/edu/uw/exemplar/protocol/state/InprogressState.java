package edu.uw.exemplar.protocol.state;

import edu.uw.exemplar.joke.KnockKnockData;

public class InprogressState implements JokeStateIF {
    /** The joke. */
	private KnockKnockData joke;
    
    /**
     * Constructor.
     * @param joke the joke being prosecuted
     */
    InprogressState(final KnockKnockData joke) {
        this.joke = joke;
    }

    /**
     * Not allowed in the state.
     * @return Always raises the exception
     * @throws IllegalStateException always raises this exception
     */
    @Override
    public String knock() {
        throw new IllegalStateException("Operation 'knock' not allowed in 'InprogressState' state.");
    }

    /**
     * Not allowed in the state.
     * @return Always raises the exception
     * @throws IllegalStateException always raises this exception
     */
    @Override
    public String whosThere() {
        throw new IllegalStateException("Operation 'whosThere' not allowed in 'InprogressState' state.");
    }

    /**
     * Delivers the punch line.
     * @return The punch line
     */
    @Override
    public String who() {
        return joke.getPunchLine();
    }

    /**
     * Advances/returns to the READY state.
     * @return an instance of ReadyState.
     */
    @Override
    public JokeStateIF nextState() {
        return new ReadyState();
    }

}
