package edu.uw.exemplar.protocol.state;

/**
 * Interface reflecting the joke operations, plus an additional operation to
 * advance the state.
 * Three states are envisioned, with only a single operation allowed in each
 * state, invocation of an operation in the wrong state will result in an
 * IllegalStateException being raised.  The state/operation mapping is:
 * <pre>
 * State           Allowed Operation
 * ----------      -----------------
 * READY           knock
 * INITIATED       whosThere
 * INPROGRESS      who
 * </pre>
 */
public interface JokeStateIF {
    /**
     * Initiates the joke, responds with "Knock!, Knock!".
     * @return Always "Knock!, Knock!"
     * @throws IllegalStateException if invoked while in an illegal state for
     *                               this operation
     */
    String knock();

    /**
     * Delivers the joke set up line.
     * @return The joke set up line
     * @throws IllegalStateException if invoked while in an illegal state for
     *                               this operation
     */
    String whosThere();

    /**
     * Delivers the punch line.
     * @return The punch line
     * @throws IllegalStateException if invoked while in an illegal state for
     *                               this operation
     */
    String who();

    /**
     * Advances to the next state, and returns that new state.
     * @return the new state
     */
    JokeStateIF nextState();
}
