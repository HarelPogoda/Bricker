package bricker.brick_strategies;

import danogl.GameObject;

public class DoubleStrategy implements CollisionStrategy {

    CollisionStrategy collisionStrategy1;
    CollisionStrategy collisionStrategy2;

    DoubleStrategy(CollisionStrategy collisionStrategy1, CollisionStrategy collisionStrategy2) {
        this.collisionStrategy1 = collisionStrategy1;
        this.collisionStrategy2 = collisionStrategy2;
    }

    @Override
    public void onCollision(GameObject firstObject, danogl.GameObject otherObject) {
        collisionStrategy1.onCollision(firstObject, otherObject);
        collisionStrategy2.onCollision(otherObject, firstObject);
    }
}
