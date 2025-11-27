package bricker.gameobjects;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;

/**
 * An extra paddle to be displayed on top of the main one.
 * This one has a collision counter, and when the counter reaches the ceiling the
 * extra paddle is erased.
 */
public class ExtraPaddle extends Paddle{
    static final int PADDLE_LIVES = 4;

    private int collisionCounter;
    private final GameObjectCollection gameObjectCollection;
    private final Counter extraPaddleCounter;

    /**
     * A constructor for the extra paddle
     * @param topLeftCorner         Position of the paddle, in window coordinates (pixels).
     *                              Note that (0,0) is the top-left corner of the window.
     * @param renderable            The renderable representing the paddle.
     * @param inputListener         Needed to connect the paddle to the arrows.
     * @param minX                  The left boundary for paddle movement
     * @param maxX                  The right boundary for paddle movement
     * @param gameObjectCollection  The list of game objects, including paddles
     * @param extraPaddleCounter    The extra paddle uses this counter to notify when it
     *                              starts and terminates.
     */
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

    /**
     * Upon collision, this is invoked and counts another collision with the
     * ExtraPaddle.
     * @param other      what the ExtraPaddle collided with.
     * @param collision  A collision event.
     */
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
