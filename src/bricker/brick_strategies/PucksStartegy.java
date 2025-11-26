package bricker.brick_strategies;

import java.util.Random;
import bricker.gameobjects.Ball;
import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.gui.Sound;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.gui.ImageReader;
import danogl.gui.SoundReader;
import danogl.util.Vector2;

public class PucksStartegy implements CollisionStrategy {

    private final GameObjectCollection gameObjectCollection;
    private final Counter brickCounter;
    private final ImageReader imageReader;
    private final SoundReader soundReader;

    static final String PUCK_IMAGE_PATH = "assets/mockBall.png";
    static final String BLOP_PATH = "assets/blop.wav";
    static final float PUCK_SPEED = 150f; // faster than the regular ball
    static final float PUCK_RADIUS = 7.5f; // instructions are that puck is 3/4 of the regular ball,
                                            // don't change

    public PucksStartegy(GameObjectCollection gameObjectCollection, Counter brickCounter,
                         ImageReader imageReader, SoundReader soundReader) {
        this.gameObjectCollection = gameObjectCollection;
        this.brickCounter = brickCounter;
        this.imageReader = imageReader;
        this.soundReader = soundReader;
    }

    @Override
    public void onCollision(GameObject gameobject1, GameObject gameobject2) {
        gameObjectCollection.removeGameObject(gameobject1, Layer.STATIC_OBJECTS);
        brickCounter.decrement();
        createPuck(gameobject2.getCenter());
        createPuck(gameobject2.getCenter());
    }

    /**
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

    private Vector2 set_puck_direction() {
        Random random = new Random();
        double angle = random.nextDouble() * Math.PI;
        float velocityX = (float) Math.cos(angle) * PUCK_SPEED;
        float velocityY = (float) Math.sin(angle) * PUCK_SPEED;
        return new Vector2(velocityX, velocityY);
    }
}
