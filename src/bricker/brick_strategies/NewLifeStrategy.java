package bricker.brick_strategies;

import bricker.gameobjects.Brick;
import bricker.gameobjects.FallingHeart;
import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.gui.ImageReader;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;

/**
 * This class implements a collision strategy that adds an extra life to the game.
 * <p>
 * When a brick with this strategy is broken:
 * 1. The brick is removed from the game.
 * 2. If the player has fewer than the maximum number of lives, a heart object falls from the brick's position.
 * 3. If the paddle collects the heart, the life counter increases.
 */
public class NewLifeStrategy implements CollisionStrategy {

    // Constant for the maximum allowed lives
    private static final int MAX_LIVES = 4;

    // Path to the heart image asset
    private static final String HEART_IMAGE_PATH = "assets/heart.png";

    private final ImageReader imageReader;
    private final Brick[][] brickGrid;
    private final GameObjectCollection gameObjectCollection;
    private final Counter brickCounter;
    private final Counter lifeCounter;
    private final GameObject mainPaddle;
    private final Vector2 windowDimensions;

    /**
     * Constructor for NewLifeStrategy.
     *
     * @param gameObjects      The global collection of game objects.
     * @param brickCounter     Counter tracking remaining bricks.
     * @param brickGrid        The 2D array representing the brick layout.
     * @param imageReader      Reader to load the heart image.
     * @param lifeCounter      Global counter for player lives.
     * @param mainPaddle       Reference to the main paddle (required for the heart's collision logic).
     * @param windowDimensions The dimensions of the game window (required for the heart's boundary logic).
     */
    public NewLifeStrategy(GameObjectCollection gameObjects,
                           Counter brickCounter,
                           Brick[][] brickGrid,
                           ImageReader imageReader,
                           Counter lifeCounter,
                           GameObject mainPaddle,
                           Vector2 windowDimensions) {
        this.imageReader = imageReader;
        this.brickGrid = brickGrid;
        this.gameObjectCollection = gameObjects;
        this.brickCounter = brickCounter;
        this.lifeCounter = lifeCounter;
        this.mainPaddle = mainPaddle;
        this.windowDimensions = windowDimensions;
    }

    /**
     * Handles the collision event.
     * Removes the brick and spawns a falling heart if the life limit hasn't been reached.
     *
     * @param firstObject  The brick object involved in the collision.
     * @param otherObject  The other object involved (usually the ball).
     */
    @Override
    public void onCollision(GameObject firstObject, GameObject otherObject) {

        // Try to remove the brick from the static layer.
        // The boolean check ensures we process this logic only once per brick.
        if (gameObjectCollection.removeGameObject(firstObject, Layer.STATIC_OBJECTS)) {

            // 1. Handle brick removal and tracking
            brickCounter.decrement();

            Brick myBrick = (Brick) firstObject;
            int row = myBrick.getRow();
            int col = myBrick.getCol();
            brickGrid[row][col] = null;

            // 2. Heart Spawning Logic

            // Only spawn a heart if the player is not at full health
            if (lifeCounter.value() < MAX_LIVES) {

                // Calculate spawn position (center of the broken brick)
                Vector2 heartSize = new Vector2(20, 20);
                Vector2 brickCenter = myBrick.getCenter();
                // Adjust to top-left corner so the heart appears centered
                Vector2 heartTopLeft = brickCenter.subtract(heartSize.mult(0.5f));

                Renderable heartImage = imageReader.readImage(HEART_IMAGE_PATH, true);

                // Create the falling heart object
                FallingHeart heart = new FallingHeart(
                        heartTopLeft,
                        heartSize,
                        heartImage,
                        gameObjectCollection,
                        lifeCounter,
                        mainPaddle,
                        windowDimensions
                );

                // Add the heart to the game
                gameObjectCollection.addGameObject(heart);
            }
        }
    }
}