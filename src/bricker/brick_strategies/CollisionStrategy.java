package bricker.brick_strategies;

import danogl.GameObject;

public interface CollisionStrategy {
    void onCollision(GameObject gameobject1,GameObject gameobject2 );

}
