package bricker.brick_strategies;
import java.util.Random;
import danogl.collisions.GameObjectCollection;
import danogl.gui.ImageReader;
import danogl.gui.SoundReader;
import danogl.gui.UserInputListener;
import danogl.util.Counter;
import danogl.util.Vector2;

public class BrickStrategyFactory {
    static final int OPTIONS_NUMBER = 10;
    static final int OPTIONS_NUMBER_NO_DOUBLE = 9;
    static final int PROBABILITY_FOR_REGULAR = 5;
    static final int PROBABILITY_FOR_PUCKS = 5;
    static final int PROBABILITY_FOR_NEW_PADDLE = 6;
    static final int PROBABILITY_FOR_EXPLOSION = 7;
    static final int PROBABILITY_FOR_NEW_LIFE = 8;
    static final int CAN_GET_DOUBLE = 1;
    static final int RECURSION_STEP = 1;

    private final GameObjectCollection gameObjects;
    private final Counter counter;
    private final ImageReader imageReader;
    private final SoundReader soundReader;
    private final UserInputListener inputListener;
    private final Vector2 windowDimensions;
    private static final Random rand = new Random();

    public BrickStrategyFactory(GameObjectCollection gameObjectCollection,
                                Counter brickCounter,
                                ImageReader imageReader,
                                SoundReader soundReader,
                                UserInputListener inputListener,
                                Vector2 windowDimensions) {
        this.gameObjects = gameObjectCollection;
        this.counter = brickCounter;
        this.imageReader = imageReader;
        this.soundReader = soundReader;
        this.inputListener = inputListener;
        this.windowDimensions = windowDimensions;
    }

    public CollisionStrategy getStrategy(){
        return getStrategyOrDouble(0);
    }

    private CollisionStrategy getStrategyOrDouble(int depth) {
        int result;
        if (depth <= CAN_GET_DOUBLE) {
            result = rand.nextInt(OPTIONS_NUMBER);
        }
        else {
            result = rand.nextInt(OPTIONS_NUMBER_NO_DOUBLE);
        }

        if (result < PROBABILITY_FOR_REGULAR) { // 50% (0,1,2,3,4)
            return new BasicCollisionStrategy(gameObjects, counter);
        } else if (result == PROBABILITY_FOR_PUCKS) {
            return new PucksStrategy(gameObjects, counter, imageReader, soundReader);
        } else if (result == PROBABILITY_FOR_NEW_PADDLE) {
            return new NewPaddleStrategy(gameObjects, counter);
        } else if (result == PROBABILITY_FOR_EXPLOSION) {
            return new ExplosionStrategy(gameObjects, counter);
        } else if (result == PROBABILITY_FOR_NEW_LIFE) {
            return new NewLifeStrategy(gameObjects, counter);
        }
        // else, result = 9, and we get a double behavior:
        return new DoubleStrategy(getStrategyOrDouble(depth + RECURSION_STEP),
                getStrategyOrDouble(depth + RECURSION_STEP));
    }
}