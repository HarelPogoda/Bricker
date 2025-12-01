package bricker.brick_strategies;

import danogl.GameObject;

/**
 * An interface for the strategies. Every strategy should have an onCollision method,
 * so that the brick can just call it (encapsulation between the brick and the behavior).
 * @author Nehorai Amrusi, Harel Pogoda
 */
public interface CollisionStrategy {
    /**
     * The method to be called upon a collision.
     * @param firstObject In our four implemented strategies, this is the brick.
     * @param otherObject the other object.
     */
    void onCollision(GameObject firstObject, danogl.GameObject otherObject);

}
