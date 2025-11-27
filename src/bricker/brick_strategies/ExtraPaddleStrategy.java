package bricker.brick_strategies;

import danogl.GameObject;
import bricker.gameobjects.ExtraPaddle;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.gui.ImageReader;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;

/**
 * This strategy adds another paddle at the middle of the screen. It's connected
 * to the arrow, like the main paddle, but shows right in the middle of the screen;
 * so it isn't placed right on top of the main one necessarily.
 * @author Harel Pogoda, Nehorai Amrusi
 */
public class ExtraPaddleStrategy implements CollisionStrategy {
    static final String PADDLE_IMAGE_PATH = "assets/paddle.png";

    private final GameObjectCollection gameObjectCollection;
    private final Counter brickCounter;
    private final ImageReader imageReader;
    private final Vector2 windowDimensions;
    private final UserInputListener inputListener;
    private final Counter extraPaddleCounter;

    /**
     * Constructor for the extra paddle strategy.
     * @param gameObjectCollection list of objects in the game
     * @param brickCounter         number of bricks in the game
     * @param imageReader          image reader (used for the paddle)
     * @param inputListener        To move the paddle
     * @param windowDimensions     size of the screen
     */
    public ExtraPaddleStrategy(GameObjectCollection gameObjectCollection,
                               Counter brickCounter,
                               ImageReader imageReader,
                               Vector2 windowDimensions,
                               UserInputListener inputListener,
                               Counter extraPaddleCounter) {
        this.gameObjectCollection = gameObjectCollection;
        this.brickCounter = brickCounter;
        this.imageReader = imageReader;
        this.windowDimensions = windowDimensions;
        this.inputListener = inputListener;
        this.extraPaddleCounter = extraPaddleCounter;
    }

    /**
     * Behavior of the brick upon collision: creates another paddle on top of regularly
     * erasing the brick.
     * @param firstObject The brick holding the behavior.
     * @param otherObject The object colliding with the brick.
     */
    public void onCollision(GameObject firstObject, danogl.GameObject otherObject) {
        if (gameObjectCollection.removeGameObject(firstObject, Layer.STATIC_OBJECTS)) {
            brickCounter.decrement();
        }
        if (extraPaddleCounter.value() == 0) {
            Renderable paddleImage = imageReader.readImage(PADDLE_IMAGE_PATH, true);
            danogl.GameObject paddle = new ExtraPaddle(
                    Vector2.ZERO,
                    paddleImage,
                    inputListener,
                    0, windowDimensions.x(),
                    gameObjectCollection,
                    extraPaddleCounter);

            paddle.setCenter(
                    new Vector2(windowDimensions.x() / 2, windowDimensions.y() / 2));

            gameObjectCollection.addGameObject(paddle);
        }
    }

}
