package bricker.gameobjects;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;

/**
 * This class manages the graphical representation of the life counter (hearts).
 * <p>
 * It observes a global {@link Counter} object representing the player's lives
 * and automatically updates the visual display (adding or removing heart icons)
 * whenever the counter value changes.
 */
public class GraphicLifeCounter extends GameObject {

    // Array to store references to all potential heart objects (active and inactive)
    private final GameObject[] hearts;

    // Reference to the game's object collection allows adding/removing hearts dynamically
    private final GameObjectCollection gameObjectCollection;

    // Reference to the global counter that holds the actual number of lives
    private final Counter lifeCounter;

    // Internal tracker to detect changes in the life counter between frames
    private int numOfLives;

    /**
     * Constructor for GraphicLifeCounter.
     * Creates all the heart objects based on the maximum possible lives,
     * but only adds the initial amount of lives to the game layer.
     *
     * @param widgetTopLeftCorner   The screen position (top-left) where the hearts start.
     * @param widgetDimensions      The total size (width/height) allocated for all hearts.
     * @param lifeCounter           The global counter object to track.
     * @param widgetRenderable      The image/graphic to be used for a single heart.
     * @param gameObjectCollection  The global collection of game objects.
     * @param maxLives              The maximum number of lives the widget can display.
     */
    public GraphicLifeCounter(Vector2 widgetTopLeftCorner,
                              Vector2 widgetDimensions,
                              Counter lifeCounter,
                              Renderable widgetRenderable,
                              GameObjectCollection gameObjectCollection,
                              int maxLives) {
        // Initialize this object as a "dummy" container (it has no visual of its own)
        super(widgetTopLeftCorner, Vector2.ZERO, null);

        this.gameObjectCollection = gameObjectCollection;
        this.lifeCounter = lifeCounter;
        this.hearts = new GameObject[maxLives];

        // Sync internal state with the current value of the global counter
        this.numOfLives = lifeCounter.value();

        // Calculate the width of a single heart based on total width and max lives
        float heartSize = widgetDimensions.x() / maxLives;

        // Loop to create all heart objects upfront
        for (int i = 0; i < maxLives; i++) {
            // Calculate position for the current heart (offset by index)
            Vector2 heartPos = widgetTopLeftCorner.add(new Vector2(i * heartSize, 0));

            // Create the heart object
            hearts[i] = new GameObject(
                    heartPos,
                    new Vector2(heartSize, widgetDimensions.y()),
                    widgetRenderable);

            // Only add the heart to the game if the player currently has this life
            if (i < numOfLives) {
                gameObjectCollection.addGameObject(hearts[i], Layer.UI);
            }
        }
    }

    /**
     * Updates the state of the hearts.
     * This method runs every frame and checks if the life counter has changed.
     *
     * @param deltaTime The time elapsed since the last frame (in seconds).
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        // Get the current value from the global counter
        int currentLives = lifeCounter.value();

        // --- Safety Check ---
        // Ensure we don't exceed the array bounds (e.g., if maxLives is 4 but counter is 5)
        if (currentLives > hearts.length) {
            currentLives = hearts.length;
        }

        // Check if the number of lives has changed since the last frame
        if (currentLives != numOfLives) {

            if (currentLives < numOfLives) {
                // Case 1: Lives decreased. Remove the extra hearts from the UI layer.
                for (int i = currentLives; i < numOfLives; i++) {
                    gameObjectCollection.removeGameObject(hearts[i], Layer.UI);
                }
            } else {
                // Case 2: Lives increased. Add the missing hearts to the UI layer.
                for (int i = numOfLives; i < currentLives; i++) {
                    gameObjectCollection.addGameObject(hearts[i], Layer.UI);
                }
            }

            // Update the internal tracker for the next frame
            this.numOfLives = currentLives;
        }
    }
}