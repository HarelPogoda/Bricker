package bricker.gameobjects;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;

public class ExtraPaddle extends Paddle{
    static final int PADDLE_LIVES = 4;

    private int collisionCounter;
    private final GameObjectCollection gameObjectCollection;
    private final Counter extraPaddleCounter;

    public ExtraPaddle(Vector2 topLeftCorner,
                  Renderable renderable, UserInputListener inputListener,
                  float minX, float maxX, GameObjectCollection gameObjectCollection,
                       Counter extraPaddleCounter) {
        super(topLeftCorner, renderable, inputListener, minX, maxX);
        this.gameObjectCollection = gameObjectCollection;
        collisionCounter = 0;
        this.extraPaddleCounter = extraPaddleCounter;
        this.extraPaddleCounter.increment();
    }

    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        collisionCounter++;
        if (collisionCounter == PADDLE_LIVES) {
            gameObjectCollection.removeGameObject(this, Layer.DEFAULT);
            extraPaddleCounter.decrement();
        }
    }
}
