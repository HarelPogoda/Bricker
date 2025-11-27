package bricker.brick_strategies;

import danogl.GameObject;

/**
 * A decorator class that holds two other collision strategies.
 * @author Harel Pogoda, Nehorai Amrusi
 */
public class DoubleStrategy implements CollisionStrategy {

    private final CollisionStrategy collisionStrategy1;
    private final CollisionStrategy collisionStrategy2;

    /**
     * constructor for the double behavior.
     * @param collisionStrategy1 The first behavior to be invoked.
     * @param collisionStrategy2 The second behavior to be invoked.
     */
    public DoubleStrategy(CollisionStrategy collisionStrategy1, CollisionStrategy collisionStrategy2) {
        this.collisionStrategy1 = collisionStrategy1;
        this.collisionStrategy2 = collisionStrategy2;
    }

    /**
     * Upon collision, DoubleStrategy invokes it's two contained behaviors.
     * @param firstObject the brick holding the double behavior.
     * @param otherObject the object colliding with the brick.
     */
    @Override
    public void onCollision(GameObject firstObject, danogl.GameObject otherObject) {
        collisionStrategy1.onCollision(firstObject, otherObject);
        collisionStrategy2.onCollision(otherObject, firstObject);
    }
}
