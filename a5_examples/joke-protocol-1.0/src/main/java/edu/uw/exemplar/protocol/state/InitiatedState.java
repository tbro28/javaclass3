package edu.uw.exemplar.protocol.state;

import edu.uw.exemplar.joke.KnockKnockData;

public class InitiatedState implements JokeStateIF {
    /** The joke. */
    private KnockKnockData joke;

    /**
     * Constructor.
     * @param joke the joke being prosecuted
     */
    InitiatedState(final KnockKnockData joke) {
        this.joke = joke;
    }

    /**
     * Not allowed in the state.
     * @return Always raises the exception
     * @throws IllegalStateException always raises this exception
     */
    @Override
    public String knock() {
        throw new IllegalStateException("Operation 'knock' not allowed in 'Initiated' state.");
    }

    /**
     * Delivers the joke set up line.
     * @return The joke set up line
     */
    @Override
    public String whosThere() {
        return joke.getLeadIn();
    }

    /**
     * Not allowed in the state.
     * @return Always raises the exception
     * @throws IllegalStateException always raises this exception
     */
    @Override
    public String who() {
        throw new IllegalStateException("Operation 'who' not allowed in 'Initiated' state.");
    }
    
    /**
     * Advances to the INPROGRESS state.
     * @return an instance of InprogressState.
     */
    @Override
    public JokeStateIF nextState() {
        return new InprogressState(joke);
    }

}
