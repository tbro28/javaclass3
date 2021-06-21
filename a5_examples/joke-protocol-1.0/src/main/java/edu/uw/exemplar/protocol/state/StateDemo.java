package edu.uw.exemplar.protocol.state;

import edu.uw.exemplar.joke.KnockKnockData;

/**
 * Simple demonstration using the State pattern implementation.
 */
public class StateDemo {

    /**
     * Demonstrates the use of the State pattern implementation, walks through
     * the states.
     * @param args not used
     */
    public static void main(final String[] args) {
        JokeStateIF joker = new ReadyState();

        for(int i = 0; i < 4; i++) {
            System.out.printf("> %s%n", joker.knock());
            joker = joker.nextState();
            
            String who = joker.whosThere();
            joker = joker.nextState();
            System.out.printf("< %s%n> %s%n", KnockKnockData.WHOS_THERE, who);
    
            System.out.printf("< %s %s%n> %s%n%n", who, KnockKnockData.WHO, joker.who());
            joker = joker.nextState();
        }
    }

}
