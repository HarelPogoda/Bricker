package bricker.main;

/**
 * A class that holds ONLY CONSTANTS SHARED BETWEEN CLASSES THAT ARE NOT
 * INHERITING FROM ONE ANOTHER
 */
public class Constants {
    /**
     * the puck's radius is 75 percent of the main ball, so this is shared between the
     * pucks strategy and the game manager
     */
    public static final float BALL_RADIUS = 10f;

    /**
     * This tag is used to make objects neutral to some effects of colliding,
     * so that collision-handling functions can check for this tag.
     */
    public static final String NO_COLLIDE_TAG = "transparent";
}
