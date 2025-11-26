package bricker.gameobjects;

import bricker.brick_strategies.CollisionStrategy;
import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

public class Brick extends GameObject {

    private final int row;
    private final int col;
    private final CollisionStrategy collisionstrategy;

    public Brick(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable,
                 int row, int col, CollisionStrategy collisionstrategy) {
        super(topLeftCorner, dimensions, renderable);
        this.row = row;
        this.col = col;
        this.collisionstrategy = collisionstrategy;
    }

    @Override
    public void onCollisionEnter(GameObject otherObject, Collision collision) {
        super.onCollisionEnter(otherObject, collision);
        this.collisionstrategy.onCollision(this, otherObject);
    }


}
