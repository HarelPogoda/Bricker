package bricker.brick_strategies;
import java.util.Random;
import danogl.collisions.GameObjectCollection;
import danogl.util.Counter;

import javax.print.DocFlavor;

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
    private static final Random rand = new Random();

    public BrickStrategyFactory(GameObjectCollection gameObjectCollection,  Counter brickCounter) {
        gameObjects = gameObjectCollection;
        counter = brickCounter;
    }

    public CollisionStrategy getStrategy(){
        return getStrategyOrDouble(0);
    }

    public CollisionStrategy getStrategyOrDouble(int depth) {
        int result;
        if (depth <= CAN_GET_DOUBLE) {
            result = rand.nextInt(OPTIONS_NUMBER); // 0-9
        }
        else {
            result =  rand.nextInt(OPTIONS_NUMBER_NO_DOUBLE); // 0-8, meaning up 'till new life
        }
        if (result < PROBABILITY_FOR_REGULAR) { // 50% (0,1,2,3,4)
            return new BasicCollisionStrategy(gameObjects, counter);
        } else if (result == PROBABILITY_FOR_PUCKS) {
            return new PucksStrategy(gameObjects, counter);
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