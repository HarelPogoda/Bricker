package bricker.gameobjects;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.gui.Sound;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

/**
 * Represents the ball object in the game.
 * This class manages the ball's velocity, collision behavior (bouncing off objects),
 * and sound effects upon impact. It also tracks the total number of collisions it has participated in.
 * The sound when the ball hits something comes from:
 * https://soundbible.com/2067-Blop.html, as the instructions to the assignment
 * say we should mind copyrights.
 * @author Nehorai Amrusi, Harel Pogoda
 */
public class Ball extends GameObject {
    private Sound collisionSound;

    /**
     * Construct a new GameObject instance.
     *
     * @param topLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param dimensions    Width and height in window coordinates.
     * @param renderable    The renderable representing the object. Can be null, in which case
     */
    public Ball(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable,
                    Sound collisionSound) {
        super(topLeftCorner, dimensions, renderable);
        this.collisionSound = collisionSound;
    }

    /**
     * Called when a collision occurs with another GameObject.
     * This method reverses the ball's velocity based on the collision normal (causing it to bounce),
     * increments the collision counter, and plays the collision sound.
     *
     * @param other     The other GameObject involved in the collision.
     * @param collision The collision information, including the point of impact and the normal vector.
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        Vector2 newVel = getVelocity().flipped(collision.getNormal());
        setVelocity(newVel);
        collisionSound.play();
    }

}
