package bricker.gameobjects;

import danogl.GameObject;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

import java.awt.event.KeyEvent;

/**
 * Represents the user-controlled paddle in the game.
 * The paddle moves horizontally based on keyboard input (Left/Right arrows)
 * and is constrained within specific horizontal boundaries (minX and maxX).
 * @author Nehorai Amrusi, Harel Pogoda
 */
public class Paddle extends GameObject {

    static final int PADDLE_WIDTH = 100;
    static final int PADDLE_HEIGHT = 15;
    private static final float MOVEMENT_SPEED = 400f;

    private final UserInputListener inputListener;
    private final float leftBoundary;
    private final float rightBoundary;

    /**
     * Construct a new paddle instance.
     * @param topLeftCorner         Position of the paddle, in window coordinates (pixels).
     *                              Note that (0,0) is the top-left corner of the window.
     * @param renderable            The renderable representing the paddle.
     * @param inputListener         Needed to connect the paddle to the arrows.
     * @param minX                  The left boundary for paddle movement
     * @param maxX                  The right boundary for paddle movement
     */
    public Paddle(Vector2 topLeftCorner,
                  Renderable renderable,
                  UserInputListener inputListener,
                  float minX, float maxX) {
        super(topLeftCorner, new Vector2(PADDLE_WIDTH, PADDLE_HEIGHT), renderable);
        this.leftBoundary = minX;
        this.rightBoundary = maxX;
        this.inputListener = inputListener;
    }

    /**
     * Updates the paddle's state.
     * Checks for left/right keyboard input to set velocity and ensures the paddle
     * does not move beyond the defined left and right boundaries.
     *
     * @param deltaTime The time elapsed, in seconds, since the last frame.
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        Vector2 movementDir = Vector2.ZERO;
        if(inputListener.isKeyPressed(KeyEvent.VK_LEFT)) {
            movementDir = movementDir.add(Vector2.LEFT);
        }
        if(inputListener.isKeyPressed(KeyEvent.VK_RIGHT)) {
            movementDir = movementDir.add(Vector2.RIGHT);
        }
        setVelocity(movementDir.mult(MOVEMENT_SPEED));

        // Checking the paddle doesn't leave the screen
        float topLeftX = getTopLeftCorner().x();

        if (topLeftX < leftBoundary) {
            setTopLeftCorner(new Vector2(leftBoundary, getTopLeftCorner().y()));
        }
        else if (topLeftX + getDimensions().x() > rightBoundary) {
            setTopLeftCorner(new Vector2(rightBoundary - getDimensions().x(), getTopLeftCorner().y()));
        }
    }
}
