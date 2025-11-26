package bricker.gameobjects;

import danogl.GameObject;
import danogl.gui.rendering.TextRenderable;
import danogl.util.Vector2;
import java.awt.Color;

/**
 * This class manages the numeric representation of the life counter.
 * It displays the number of lives and changes the text color based on the value.
 */
public class NumericLifeCounter extends GameObject {
    private final TextRenderable textRenderable;
    private int lives;

    /**
     * Constructor for NumericLifeCounter.
     *
     * @param topLeftCorner The top-left position of the numeric counter.
     * @param dimensions    The dimensions of the counter object.
     * @param lives         The initial number of lives.
     */
    public NumericLifeCounter(Vector2 topLeftCorner, Vector2 dimensions, int lives) {
        super(topLeftCorner, dimensions, null);
        this.lives = lives;

        this.textRenderable = new TextRenderable(String.valueOf(lives));
        this.renderer().setRenderable(textRenderable);

        updateColor();
    }

    /**
     * Updates the numeric display and the text color.
     *
     * @param newLives The new number of lives to display.
     */
    public void setLives(int newLives) {
        this.lives = newLives;
        textRenderable.setString(String.valueOf(lives));
        updateColor();
    }

    /**
     * Updates the color of the text based on the remaining lives.
     * Green for 3 or more, Yellow for 2, Red for 1.
     */
    private void updateColor() {
        if (lives >= 3) {
            textRenderable.setColor(Color.green);
        } else if (lives == 2) {
            textRenderable.setColor(Color.yellow);
        } else {
            textRenderable.setColor(Color.red);
        }
    }
}