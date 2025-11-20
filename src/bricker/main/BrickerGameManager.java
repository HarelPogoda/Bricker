package bricker.main;
import danogl.GameManager;
import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.components.CoordinateSpace;
import danogl.gui.*;
import danogl.gui.rendering.RectangleRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import gameobjects.Ball;
import gameobjects.Paddle;

import java.awt.*;
import java.util.Random;

/**
 * The main manager for the Bricker game.
 * This class is responsible for initializing the game window, handling game resources
 * (images, sounds), and creating the game objects (Ball, Paddle, Walls, Background).
 * It extends the basic GameManager logic from the DanoGameLab library.
 * @author Nehorai Amrusi, Harel Pogoda
 */
public class BrickerGameManager extends GameManager{
    static final String GAME_TITLE = "Bricker";
    static final String BALL_IMAGE_PATH = "assets/ball.png";
    static final String BLOP_PATH = "assets/blop.wav";
    static final String PADDLE_IMAGE_PATH = "assets/paddle.png";
    static final String BACKGROUND_IMAGE_PATH = "assets/DARK_BG2_small.jpeg";

    static final Color WALLS_COLOR = Color.MAGENTA;

    static final int SCREEN_WIDTH = 700;
    static final int SCREEN_HEIGHT = 500;
    static final int BALL_RADIUS = 10; // instructions are that the ball will be 20*20
    static final int BALL_SPEED = 100;
    static final int PADDLE_WIDTH = 100;
    static final int PADDLE_HEIGHT = 15;
    static final float BALL_START_COORDINATES = 0.5f;
    static final int SCREEN_FIRST_ROW = 0;
    static final float SCREEN_FIRST_COLUMN = 0f;
    static final int WALL_WIDTH = 5;
    static final int RIGHT_WALL_LEFT_COLUMN = SCREEN_WIDTH - WALL_WIDTH + 1;
    static final int REVERSE = -1;

    private Vector2 windowDimensions;
    private ImageReader imageReader;
    private UserInputListener inputListener;
    private SoundReader soundReader;

    /**
     * Constructs a new BrickerGameManager instance.
     *
     * @param windowTitle The title to be displayed on the game window.
     * @param screenSize  The dimensions (width and height) of the game window.
     */
    public BrickerGameManager(String windowTitle, Vector2 screenSize) {
        super(windowTitle, screenSize);
    }

    /**
     * The entry point of the application.
     * Creates an instance of the game manager and starts the game execution.
     *
     * @param args Command line arguments (not used).
     */
    public static void main(String[] args) {
        BrickerGameManager gameManager = new BrickerGameManager(
                GAME_TITLE, new Vector2(SCREEN_WIDTH, SCREEN_HEIGHT));
        gameManager.run();
    }

    /**
     * Initializes the game state, resources, and objects.
     * This method is called once when the game starts. It sets up the window dimensions,
     * initializes the image and sound readers, and creates the background, ball, paddle, and walls.
     *
     * @param imageReader      Object used to read images from disk.
     * @param soundReader      Object used to read sound files from disk.
     * @param inputListener    Object used to read user input (keyboard).
     * @param windowController Object used to control the game window (e.g., getting dimensions).
     */
    @Override
    public void initializeGame(ImageReader imageReader,
                               SoundReader soundReader,
                               UserInputListener inputListener,
                               WindowController windowController) {

        super.initializeGame(imageReader, soundReader, inputListener, windowController);
        this.windowDimensions = windowController.getWindowDimensions();
        this.imageReader = imageReader;
        this.soundReader = soundReader;
        this.inputListener = inputListener;

        createBackground();
        createBall();
        createPaddle();
        createWalls();
    }

    /**
     * Creates the ball object for the game.
     * Sets its visual representation, collision sound, initial position (centered),
     * and randomized initial velocity direction.
     */
    private void createBall() {
        Renderable ballImage = imageReader.readImage(BALL_IMAGE_PATH, true);
        Sound collisionSound = soundReader.readSound(BLOP_PATH);
        GameObject ball = new Ball(
                Vector2.ZERO, new Vector2(BALL_RADIUS, BALL_RADIUS), ballImage, collisionSound);

        ball.setCenter(windowDimensions.mult(BALL_START_COORDINATES));
        gameObjects().addGameObject(ball);

        float ballVelX = BALL_SPEED;
        float ballVelY = BALL_SPEED;
        Random rand = new Random();
        if(rand.nextBoolean())
            ballVelX *= REVERSE;
        if(rand.nextBoolean())
            ballVelY *= REVERSE;
        ball.setVelocity(new Vector2(ballVelX, ballVelY));
    }

    /**
     * Creates the user-controlled paddle.
     * Sets its visual representation, input listener for movement, and
     * defines the movement boundaries (min/max X coordinates) to prevent it from leaving the screen.
     */
    private void createPaddle() {
        Renderable paddleImage = imageReader.readImage(PADDLE_IMAGE_PATH, true);
        GameObject paddle = new Paddle(
                Vector2.ZERO,
                new Vector2(PADDLE_WIDTH, PADDLE_HEIGHT),
                paddleImage,
                inputListener,
                SCREEN_FIRST_COLUMN,  // minX
                windowDimensions.x()); // maxX

        paddle.setCenter(
                new Vector2(windowDimensions.x()/2, (int)windowDimensions.y()-30));
        gameObjects().addGameObject(paddle);
    }

    /**
     * Creates the static walls around the game area.
     * Generates three walls: Left, Right, and Top, using the defined wall width and color.
     */
    private void createWalls() {
        GameObject leftWall = new GameObject(
                Vector2.ZERO,
                new Vector2(WALL_WIDTH, SCREEN_HEIGHT),
                new RectangleRenderable(WALLS_COLOR));

        GameObject rightWall = new GameObject(
                new Vector2(RIGHT_WALL_LEFT_COLUMN, SCREEN_FIRST_ROW),
                new Vector2(WALL_WIDTH, SCREEN_HEIGHT),
                new RectangleRenderable(WALLS_COLOR));

        GameObject topWall = new GameObject(
                new Vector2(SCREEN_FIRST_COLUMN, SCREEN_FIRST_ROW),
                new Vector2(SCREEN_WIDTH, WALL_WIDTH),
                new RectangleRenderable(WALLS_COLOR));

        gameObjects().addGameObject(leftWall);
        gameObjects().addGameObject(rightWall);
        gameObjects().addGameObject(topWall);
    }

    /**
     * Creates the background for the game scene.
     * Sets the background image and assigns it to the CAMERA_COORDINATES coordinate space
     * to ensure it remains static relative to the view. It is added to the BACKGROUND layer.
     */
    private void createBackground() {
        Renderable backgroundImage = imageReader.readImage(BACKGROUND_IMAGE_PATH, false);
        GameObject backGround = new GameObject(
                Vector2.ZERO,
                new Vector2(SCREEN_WIDTH, SCREEN_HEIGHT),
                backgroundImage);

        backGround.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);

        gameObjects().addGameObject(backGround, Layer.BACKGROUND);
    }

}
