package bricker.brick_strategies;

import danogl.GameObject;

/**
 * An interface for the strategies. Every strategy should have an onCollision method,
 * so that the brick can just call it (encapsulation between the brick and the behavior).
 * @author Nehorai Amrusi, Harel Pogoda
 */
public interface CollisionStrategy {
    void onCollision(GameObject firstObject, danogl.GameObject otherObject);

}
