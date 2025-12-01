package bricker.gameobjects;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.collisions.GameObjectCollection;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;

/**
 * Represents a heart object that falls from a broken brick.
 * If the main paddle collects it, the player gains an extra life.
 */
public class FallingHeart extends GameObject {
    private static final int MAX_LIVES = 4;
    private static final Vector2 HEART_DIRECTION = new Vector2(0, 100);

    private final GameObjectCollection gameObjects;
    private final Counter lifeCounter;
    private final GameObject mainPaddle; // Reference to the specific main paddle instance
    private final Vector2 windowDimensions;
    /**
     * Constructor for the Heart object.
     *
     * @param topLeftCorner The starting position of the heart (usually the center of the broken brick).
     * @param dimensions    The width and height of the heart.
     * @param renderable    The image/graphic of the heart.
     * @param gameObjects   The collection of all game objects (used to remove the heart later).
     * @param lifeCounter   The global counter for player lives.
     * @param mainPaddle    Reference to the main paddle object to check collisions against.
     */
    public FallingHeart(Vector2 topLeftCorner,
                        Vector2 dimensions,
                        Renderable renderable,
                        GameObjectCollection gameObjects,
                        Counter lifeCounter,
                        GameObject mainPaddle,
                        Vector2 windowDimensions) {
        super(topLeftCorner, dimensions, renderable);
        this.gameObjects = gameObjects;
        this.lifeCounter = lifeCounter;
        this.mainPaddle = mainPaddle;
        this.windowDimensions = windowDimensions;

        // Set constant downward velocity (100 pixels per second)
        // This ensures the heart falls straight down.
        this.setVelocity(HEART_DIRECTION);
    }

    /**
     * Determines which objects the heart should collide with.
     * <p>
     * Logic:
     * The heart should ONLY collide with the original main paddle.
     * We use reference equality (==) instead of 'instanceof' or tags
     * to ensure strict adherence to the assignment requirements.
     *
     * @param other The other game object.
     * @return true if 'other' is the specific mainPaddle instance, false otherwise.
     */
    @Override
    public boolean shouldCollideWith(GameObject other) {
        return other == mainPaddle;
    }

    /**
     * Handles the logic when the heart collides with the paddle.
     *
     * @param other     The object colliding with the heart.
     * @param collision Information about the collision.
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);

        // Double-check that we hit the main paddle using reference equality
        if (other == mainPaddle) {
            // Only increment lives if the player has fewer than 4 lives
            if (lifeCounter.value() < MAX_LIVES) {
                lifeCounter.increment();
            }

            // Remove the heart from the game after it is collected
            gameObjects.removeGameObject(this);
        }
    }

    /**
     * Updates the heart's state every frame.
     * Used to clean up the object if it falls off the screen without being collected.
     *
     * @param deltaTime The time elapsed since the last frame.
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        // If the heart falls below the screen limit (e.g., Y > 800), remove it to save resources.
        // You can adjust '800' to your specific window height.
        if (getTopLeftCorner().y() > windowDimensions.y()) {
            gameObjects.removeGameObject(this);
        }
    }
}

