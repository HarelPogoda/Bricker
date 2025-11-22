package bricker.brick_strategies;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.util.Counter;

public class BasicCollisionStrategy implements CollisionStrategy{

    private final GameObjectCollection gameObjectCollection;
    private final Counter brickCounter;

    public BasicCollisionStrategy(GameObjectCollection gameObjectCollection, Counter brickCounter) {
        this.gameObjectCollection = gameObjectCollection;
        this.brickCounter = brickCounter;
    }

    @Override
    public void onCollision(GameObject gameobject1, GameObject gameobject2) {
        System.out.println("collision with brick detected");
        gameObjectCollection.removeGameObject(gameobject1, Layer.STATIC_OBJECTS);
        brickCounter.decrement();
    }
}
