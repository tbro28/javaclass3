package edu.uw.exemplar.protocol.proxy;

/**
 * A proxy implementation requires and interface which is implemented the the
 * "real" class as well as the proxy.
 */
public interface KnockKnockProxyIF {
    /**
     * Initiates the joke, responds with "Knock!, Knock!".
     * @return Always "Knock!, Knock!"
     */
    String knock();

    /**
     * Delivers the joke set up line.
     * @return The joke set up line
     */
    String whosThere();

    /**
     * Delivers the punch line.
     * @return The punch line
     */
    String who();
}
