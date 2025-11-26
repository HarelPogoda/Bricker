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

public class ExtraPaddleStrategy implements CollisionStrategy {
    static final String PADDLE_IMAGE_PATH = "assets/paddle.png";

    static boolean extraPaddleExists = false;
    private final GameObjectCollection gameObjectCollection;
    private final Counter brickCounter;
    private final ImageReader imageReader;
    private final Vector2 windowDimensions;
    private final UserInputListener inputListener;
    private Counter extraPaddleCounter;

    public ExtraPaddleStrategy(GameObjectCollection gameObjectCollection, Counter brickCounter,
                               ImageReader imageReader, Vector2 windowDimensions, UserInputListener inputListener,
                               Counter extraPaddleCounter) {
        this.gameObjectCollection = gameObjectCollection;
        this.brickCounter = brickCounter;
        this.imageReader = imageReader;
        this.windowDimensions = windowDimensions;
        this.inputListener = inputListener;
        this.extraPaddleCounter = extraPaddleCounter;
    }

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
