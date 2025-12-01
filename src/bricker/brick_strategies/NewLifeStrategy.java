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
    private static final Vector2 HEART_SIZE = new Vector2(20, 20);
    private static final float RELATIVE_POSITION_TO_BRICK = 0.5f;

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

        if (gameObjectCollection.removeGameObject(firstObject, Layer.STATIC_OBJECTS)) {
            brickCounter.decrement();

            Brick myBrick = (Brick) firstObject;
            int row = myBrick.getRow();
            int col = myBrick.getCol();
            brickGrid[row][col] = null;

            if (lifeCounter.value() < MAX_LIVES) {
                Vector2 brickCenter = myBrick.getCenter();
                Vector2 heartTopLeft = brickCenter.subtract(HEART_SIZE.mult(RELATIVE_POSITION_TO_BRICK));

                Renderable heartImage = imageReader.readImage(HEART_IMAGE_PATH, true);
                FallingHeart heart = new FallingHeart(
                        heartTopLeft,
                        HEART_SIZE,
                        heartImage,
                        gameObjectCollection,
                        lifeCounter,
                        mainPaddle,
                        windowDimensions
                );

                gameObjectCollection.addGameObject(heart);
            }
        }
    }
}