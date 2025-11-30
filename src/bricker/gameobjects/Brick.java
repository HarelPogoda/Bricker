package bricker.gameobjects;

import bricker.brick_strategies.CollisionStrategy;
import danogl.collisions.Collision;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import danogl.GameObject;

/**
 * A class representing a brick. A brick can hold different behaviors, to be invoked
 * when an object hits the brick.
 */
public class Brick extends GameObject {

    private final int row;
    private final int col;
    private final CollisionStrategy collisionstrategy;

    /**
     * Constructor for the brick.
     * @param topLeftCorner     position of the brick's top left corner.
     * @param dimensions        size of the brick.
     * @param renderable        A renderable (image) for the brick.
     * @param row               row of the brick on the screen, relative to other bricks.
     * @param col               column of the brick on the screen, relative to other bricks.
     * @param collisionstrategy What happens when something hits the brick.
     */
    public Brick(Vector2 topLeftCorner,
                 Vector2 dimensions,
                 Renderable renderable,
                 int row, int col,
                 CollisionStrategy collisionstrategy) {
        super(topLeftCorner, dimensions, renderable);
        this.row = row;
        this.col = col;
        this.collisionstrategy = collisionstrategy;
    }

    /**
     * This is invoked whenever a brick is hit. Invokes different behaviors - the
     * class in charge of choosing the behaviors is BrickStrategiesFactory.
     * @param otherObject The object hitting the brick.
     * @param collision   a collision event.
     */
    @Override
    public void onCollisionEnter(GameObject otherObject, Collision collision) {
        super.onCollisionEnter(otherObject, collision);
        this.collisionstrategy.onCollision(this, otherObject);
    }


    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public CollisionStrategy getCollisionStrategy() {
        return this.collisionstrategy;
    }
}
