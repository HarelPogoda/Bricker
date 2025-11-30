
package bricker.gameobjects;

import danogl.GameObject;
import danogl.gui.rendering.TextRenderable;
import danogl.util.Counter;
import danogl.util.Vector2;
import java.awt.Color;

/**
 * This class manages the numeric representation of the life counter.
 * It tracks the global life counter and updates the text/color automatically.
 */
public class NumericLifeCounter extends GameObject {
    private final TextRenderable textRenderable;
    private final Counter lifeCounter; // Reference to the global counter
    private int lives; // Internal tracker to detect changes

    /**
     * Constructor for NumericLifeCounter.
     *
     * @param topLeftCorner The top-left position of the numeric counter.
     * @param dimensions    The dimensions of the counter object.
     * @param lifeCounter   The global life counter object to track.
     */
    public NumericLifeCounter(Vector2 topLeftCorner, Vector2 dimensions, Counter lifeCounter) {
        super(topLeftCorner, dimensions, null);
        this.lifeCounter = lifeCounter;
        this.lives = lifeCounter.value();

        // Initialize text renderable with the current value
        this.textRenderable = new TextRenderable(String.valueOf(lives));
        this.renderer().setRenderable(textRenderable);

        updateColor();
    }

    /**
     * Updates the object every frame.
     * Checks if the life counter value has changed and updates the display accordingly.
     *
     * @param deltaTime Time elapsed since last frame.
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        int currentLives = lifeCounter.value();

        // Only update text and color if the value has changed
        if (currentLives != lives) {
            this.lives = currentLives;
            textRenderable.setString(String.valueOf(lives));
            updateColor();
        }
    }

    /*
     * Updates the color of the text based on the remaining lives.
     * Green for 3 or more, Yellow for 2, Red for 1.
     */
    private void updateColor() {
        if (lives >= 3) {
            textRenderable.setColor(Color.green);
        } else if (lives == 2) {
            textRenderable.setColor(Color.yellow);
        } else {
            textRenderable.setColor(Color.red); // Includes 1 and 0
        }
    }
}