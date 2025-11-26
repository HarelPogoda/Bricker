package bricker.brick_strategies;

import danogl.GameObject;

public interface CollisionStrategy {
    void onCollision(GameObject firstObject, danogl.GameObject otherObject);

}
