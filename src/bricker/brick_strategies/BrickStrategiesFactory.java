package bricker.brick_strategies;

import java.util.Random;
import danogl.collisions.GameObjectCollection;
import danogl.gui.ImageReader;
import danogl.gui.SoundReader;
import danogl.gui.UserInputListener;
import danogl.util.Counter;
import danogl.util.Vector2;

/**
 * A factory to return all the behaviors (strategies) that a brick can have.
 * it initializes a strategy and returns it using the getStrategy function.
 * @author Nehorai Amrusi, Harel Pogoda
 */
public class BrickStrategiesFactory {
    static final int OPTIONS_NUMBER = 10;
    static final int OPTIONS_NUMBER_NO_DOUBLE = OPTIONS_NUMBER - 1;
    static final int PROBABILITY_FOR_REGULAR = 5;
    static final int PROBABILITY_FOR_PUCKS = 5;
    static final int PROBABILITY_FOR_NEW_PADDLE = 6;
    static final int PROBABILITY_FOR_EXPLOSION = 7;
    static final int PROBABILITY_FOR_NEW_LIFE = 8;

    private final GameObjectCollection gameObjects;
    private final Counter brickCounter;
    private final ImageReader imageReader;
    private final SoundReader soundReader;
    private final UserInputListener inputListener;
    private final Vector2 windowDimensions;
    private static final Random rand = new Random();
    private final Counter extraPaddleCounter;
    private int doublesCounter;
    private int doublesAllowed;

    public BrickStrategiesFactory(GameObjectCollection gameObjectCollection,
                                  Counter brickCounter,
                                  ImageReader imageReader,
                                  SoundReader soundReader,
                                  UserInputListener inputListener,
                                  Vector2 windowDimensions) {
        this.gameObjects = gameObjectCollection;
        this.brickCounter = brickCounter;
        this.imageReader = imageReader;
        this.soundReader = soundReader;
        this.inputListener = inputListener;
        this.windowDimensions = windowDimensions;
        this.extraPaddleCounter = new Counter(0);
        doublesCounter = 0;
    }

    /**
     * A method to return a strategy for the brick behavior. The distribution is as
     * described in the exercise.
     * @param  behaviorsAllowed Number of behaviors a brick can have
     * @return A strategy to be used by a brick upon collision
     */
    public CollisionStrategy getStrategy(int behaviorsAllowed) {
        // Note that a tree with N leafs has N-1 internal nodes
        this.doublesAllowed = behaviorsAllowed - 1;
        CollisionStrategy strategy = getStrategyOrDouble();
        doublesCounter = 0;
        return strategy;
    }

    /**
     * The distribution of strategies is defined in the exercise: 10 percent for each
     * special strategy, and 50 for the basic one (just erase the brick from the game).
     */
    private CollisionStrategy getStrategyOrDouble() {
        int result;
        if (doublesCounter < doublesAllowed) {
            result = rand.nextInt(OPTIONS_NUMBER);
        }
        else {
            result = rand.nextInt(OPTIONS_NUMBER_NO_DOUBLE);
        }

        if (result < PROBABILITY_FOR_REGULAR) {
            return new BasicCollisionStrategy(gameObjects, brickCounter);
        } else if (result == PROBABILITY_FOR_PUCKS) {
            return new PucksStrategy(gameObjects, brickCounter, imageReader, soundReader);
        } else if (result == PROBABILITY_FOR_NEW_PADDLE) {
            return new ExtraPaddleStrategy(gameObjects, brickCounter, imageReader,
                    windowDimensions, inputListener, extraPaddleCounter);
        } //else if (result == PROBABILITY_FOR_EXPLOSION) {
//            return new ExplosionStrategy(gameObjects, brickCounter);
//        } else if (result == PROBABILITY_FOR_NEW_LIFE) {
//            return new NewLifeStrategy(gameObjects, brickCounter);
//        }
        // else, result = 9, and we get a double behavior.
        doublesCounter++;
        return new DoubleStrategy(getStrategyOrDouble(), getStrategyOrDouble());
    }
}