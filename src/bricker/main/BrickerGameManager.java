package bricker.main;
import danogl.GameManager;
import danogl.GameObject;
import danogl.gui.*;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import gameobjects.Ball;
import gameobjects.Paddle;

import java.util.Random;

public class BrickerGameManager extends GameManager{
    static final String GAME_TITLE = "Bricker";
    static final String BALL_IMAGE_PATH = "assets/ball.png";
    static final String BLOP_PATH = "assets/blop.wav";
    static final String PADDLE_IMAGE_PATH = "assets/paddle.png";

    static final int SCREEN_WIDTH = 700;
    static final int SCREEN_HEIGHT = 500;
    static final int BALL_RADIUS = 10; // instructions are that the ball will be 20*20
    static final int BALL_SPEED = 100;
    static final int PADDLE_WIDTH = 100;
    static final int PADDLE_HEIGHT = 15;
    static final float BALL_START_COORDINATES = 0.5f;

    private Vector2 windowDimensions;

    public BrickerGameManager(String windowTitle, Vector2 screenSize) {
        super(windowTitle, screenSize);
    }

    public static void main(String[] args) {
        BrickerGameManager gameManager = new BrickerGameManager(
                GAME_TITLE, new Vector2(SCREEN_WIDTH, SCREEN_HEIGHT));
        gameManager.run();
    }

    @Override
    public void initializeGame(ImageReader imageReader,
                               SoundReader soundReader,
                               UserInputListener inputListener,
                               WindowController windowController) {

        super.initializeGame(imageReader, soundReader, inputListener, windowController);
        this.windowDimensions = windowController.getWindowDimensions();

        createBall(imageReader, soundReader, windowController);

        Renderable paddleImage = imageReader.readImage(PADDLE_IMAGE_PATH, true);
        createPaddle(paddleImage, inputListener, windowDimensions);
    }

    private void createBall(ImageReader imageReader, SoundReader soundReader, WindowController windowController) {
        Renderable ballImage =
                imageReader.readImage(BALL_IMAGE_PATH, true);
        Sound collisionSound = soundReader.readSound(BLOP_PATH);
        GameObject ball = new Ball(
                Vector2.ZERO, new Vector2(BALL_RADIUS, BALL_RADIUS), ballImage, collisionSound);

        ball.setCenter(windowDimensions.mult(BALL_START_COORDINATES));
        gameObjects().addGameObject(ball);

        float ballVelX = BALL_SPEED;
        float ballVelY = BALL_SPEED;
        Random rand = new Random();
        if(rand.nextBoolean())
            ballVelX *= -1;
        if(rand.nextBoolean())
            ballVelY *= -1;
        ball.setVelocity(new Vector2(ballVelX, ballVelY));
    }

    private void createPaddle(Renderable paddleImage, UserInputListener inputListener, Vector2 windowDimensions) {
        GameObject userPaddle = new Paddle(
                Vector2.ZERO,
                new Vector2(PADDLE_WIDTH, PADDLE_HEIGHT),
                paddleImage,
                inputListener);

        userPaddle.setCenter(
                new Vector2(windowDimensions.x()/2, (int)windowDimensions.y()-30));
        gameObjects().addGameObject(userPaddle);
    }

}
