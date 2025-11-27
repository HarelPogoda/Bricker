package bricker.brick_strategies;

import java.util.Random;

import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.gui.Sound;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.gui.ImageReader;
import danogl.gui.SoundReader;
import danogl.util.Vector2;

import bricker.gameobjects.Ball;
import danogl.GameObject;

/**
 * A collision strategy for a brick. This one creates 2 new small balls in the game.
 * These balls don't waste life when leaving the screen.
 * @author Harel Pogoda, Nehorai Amrusi
 */
public class PucksStrategy implements CollisionStrategy {

    private final GameObjectCollection gameObjectCollection;
    private final Counter brickCounter;
    private final ImageReader imageReader;
    private final SoundReader soundReader;

    private static final String PUCK_IMAGE_PATH = "assets/mockBall.png";
    private static final String BLOP_PATH = "assets/blop.wav";
    private static final float PUCK_SPEED = 150f; // faster than the regular ball
    private static final float PUCK_RADIUS = 7.5f; // instructions are that puck is 3/4 of the regular ball,
                                            // don't change

    /**
     * Constructor for the pucks strategy.
     * @param gameObjectCollection A list of the objects in the game.
     * @param brickCounter         number of bricks in the game.
     * @param imageReader          An image reader to read the puck image.
     * @param soundReader          A sound reader to read the puck collision sound.
     */
    public PucksStrategy(GameObjectCollection gameObjectCollection, Counter brickCounter,
                         ImageReader imageReader, SoundReader soundReader) {
        this.gameObjectCollection = gameObjectCollection;
        this.brickCounter = brickCounter;
        this.imageReader = imageReader;
        this.soundReader = soundReader;
    }

    /**
     * This method erases a brick and creates two pucks
     * @param firstObject The brick holding the puck strategy.
     * @param otherObject The object colliding with the brick.
     */
    @Override
    public void onCollision(GameObject firstObject, danogl.GameObject otherObject) {
        if (gameObjectCollection.removeGameObject(firstObject, Layer.STATIC_OBJECTS)) {
            brickCounter.decrement();
        }
        createPuck(otherObject.getCenter());
        createPuck(otherObject.getCenter());
    }

    /*
     * Creates the ball object for the game.
     * Sets its visual representation, collision sound, initial position (centered),
     * and randomized initial velocity direction.
     */
    private void createPuck(Vector2 location) {
        Renderable ballImage = imageReader.readImage(PUCK_IMAGE_PATH, true);
        Sound collisionSound = soundReader.readSound(BLOP_PATH);
        Ball puck = new Ball(
                Vector2.ZERO, new Vector2(PUCK_RADIUS, PUCK_RADIUS), ballImage, collisionSound);

        puck.setCenter(location);
        gameObjectCollection.addGameObject(puck);
        puck.setVelocity(set_puck_direction());
    }

    /*
     * A method to set the puck in a random direction on the top half of the unite circle.
     */
    private Vector2 set_puck_direction() {
        Random random = new Random();
        double angle = random.nextDouble() * Math.PI;
        float velocityX = (float) Math.cos(angle) * PUCK_SPEED;
        float velocityY = (float) Math.sin(angle) * PUCK_SPEED;
        return new Vector2(velocityX, velocityY);
    }
}
