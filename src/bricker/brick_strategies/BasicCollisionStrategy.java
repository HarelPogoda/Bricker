package bricker.brick_strategies;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.util.Counter;

public class BasicCollisionStrategy implements CollisionStrategy{
    static final String COLLISION_MESSAGE = "collision with brick detected";

    private final GameObjectCollection gameObjectCollection;
    private final Counter brickCounter;

    public BasicCollisionStrategy(GameObjectCollection gameObjectCollection, Counter brickCounter) {
        this.gameObjectCollection = gameObjectCollection;
        this.brickCounter = brickCounter;
    }

    @Override
    public void onCollision(GameObject gameobject1, GameObject gameobject2) {
        // note: this was changed, as if 2 balls hit the brick at the same time only
        // one will remove the brick and decrement the count; the other will return false
        if (gameObjectCollection.removeGameObject(gameobject1, Layer.STATIC_OBJECTS)) {
            brickCounter.decrement();
        }
    }
}
