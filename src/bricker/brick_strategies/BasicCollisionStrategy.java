package bricker.brick_strategies;

import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.util.Counter;

import danogl.GameObject;

/**
 * the basic behavior of a brick: erases the brick and decrements the brick counter
 * @author Nehirai Amrusi, Harel Pogoda
 */
public class BasicCollisionStrategy implements CollisionStrategy{

    private final GameObjectCollection gameObjectCollection;
    private final Counter brickCounter;

    /**
     * Constructor for the strategy object.
     * @param gameObjectCollection The list which from the brick is to be erased
     * @param brickCounter         The counter to decrement
     */
    public BasicCollisionStrategy(GameObjectCollection gameObjectCollection, Counter brickCounter) {
        this.gameObjectCollection = gameObjectCollection;
        this.brickCounter = brickCounter;
    }

    /**
     * erases the brick and decrements the brick counter
     * @param firstObject The brick
     * @param otherObject The object colliding with the brick
     */
    @Override
    public void onCollision(GameObject firstObject, danogl.GameObject otherObject) {
        // note: this was changed, as if 2 balls hit the brick at the same time only
        // one will remove the brick and decrement the count; the other will return false
        if (gameObjectCollection.removeGameObject(firstObject, Layer.STATIC_OBJECTS)) {
            brickCounter.decrement();
        }
    }
}
