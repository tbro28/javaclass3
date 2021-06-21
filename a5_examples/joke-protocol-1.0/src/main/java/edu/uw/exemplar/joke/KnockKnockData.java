package edu.uw.exemplar.joke;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Represents the set up and punch-line of a knock-knock joke.
 */
public class KnockKnockData {
    /** String used to indicate the initiation of the joke. */
    public static final String KNOCK_KNOCK = "Knock! Knock!";
    /** String response to the knocking. */
    public static final String WHOS_THERE = "Who's there?";
    /** String uses as a suffix on the confirmation query. */
    public static final String WHO = "Who?";

    /** The known jokes. */
    private static final KnockKnockData[] allJokes = { 
        new KnockKnockData("Turnip",          "Turnip the heat, it's cold in here!"),
        new KnockKnockData("Little Old Lady", "I didn't know you could yodel!"),
        new KnockKnockData("Atch",            "Bless you!"),
        new KnockKnockData("Who",             "Is there an owl in here?"),
        new KnockKnockData("Who",             "Is there an echo in here?"),
        new KnockKnockData("Hatch",           "Gesundheit!"),
        new KnockKnockData("Boo",             "Don't cry, it's only a joke."),
        new KnockKnockData("Cows go",         "No, cows go 'moo', owls go 'who'!"),
        new KnockKnockData("Doris",           "Doris locked, that's why I had to knock!"),
        new KnockKnockData("Dwayne",          "Dwayne the bathtub, I'm dwowning!"),
        new KnockKnockData("Max",             "Max no difference -- open ze door."),
        new KnockKnockData("Police",          "Police open the door -- it's cold out here!"),
        new KnockKnockData("Wendy",           "Wendy wind blows, de cradle will rock!")
    };
    
    /** Shuffled list of all known jokes. */
    private static List<KnockKnockData> jokes;

    /** Index to the current joke. */
    private static int jokeNdx;

    /** Initialize the shuffled list of jokes. */
    static {
        jokes = Arrays.asList(allJokes);
        Collections.shuffle(jokes);
    }

    /** The joke lead in.  */
    private String leadIn;
    /** The punch line. */
    private String punchLine;

    /**
     * Constructor.
     * @param leadIn the lead-in or set up line for a joke
     * @param punchLine the jokes punch-line
     */
    private KnockKnockData(final String leadIn, final String punchLine) {
        this.leadIn = leadIn;
        this.punchLine = punchLine;
    }

    /**
     * Advances the joke index to the next joke, when the end is reached
     * repositions to the first joke.
     */
    private static synchronized void advanceJoke() {
        jokeNdx = (jokeNdx == (jokes.size() - 1) ? 0 : jokeNdx + 1);
    }

    /**
     * Gets advances the joke index and returns the current joke.
     * @return the next joke
     */
    public static final KnockKnockData getJoke() {
        advanceJoke();
        return jokes.get(jokeNdx);
    }

    /**
     * Gets the jokes lead-in or set up line.
     * @return the jokes lead-in or set up line
     */
    public String getLeadIn() {
        return leadIn;
    }

    /**
     * Gets the jokes punch-line.
     * @return the jokes punch-line
     */
    public String getPunchLine() {
        return punchLine;
    }

}
