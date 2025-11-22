package bricker.main;
import bricker.brick_strategies.BasicCollisionStrategy;
import bricker.brick_strategies.CollisionStrategy;
import danogl.GameManager;
import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.components.CoordinateSpace;
import danogl.gui.*;
import danogl.gui.rendering.RectangleRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;
import gameobjects.Ball;
import gameobjects.Brick;
import gameobjects.Paddle;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Random;
import gameobjects.GraphicLifeCounter;
import gameobjects.NumericLifeCounter;

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
    static final String BRICK_PATH = "assets/brick.png";
    static final String HEART_PATH = "assets/heart.png";
    static final String LOSE_MASSAGE = "You lose! Play again?";

    static final Color WALLS_COLOR = Color.MAGENTA;

    static final int SCREEN_WIDTH = 1400;
    static final int SCREEN_HEIGHT = 500;
    static final int BALL_RADIUS = 10; // instructions are that the ball will be 20*20
    static final int BALL_SPEED = 200;
    static final int PADDLE_WIDTH = 100;
    static final int PADDLE_HEIGHT = 15;
    static final float BALL_START_COORDINATES = 0.5f;
    static final int SCREEN_FIRST_ROW = 0;
    static final float SCREEN_FIRST_COLUMN = 0f;
    static final int WALL_WIDTH = 5;
    static final int RIGHT_WALL_LEFT_COLUMN = SCREEN_WIDTH - WALL_WIDTH + 1;
    static final int REVERSE = -1;
    static final int BRICK_HEIGHT = 15;
    static final float SPACE_BETWEEN_OBJECTS = 1;
    static final int DEFAULT_BRICKS_PER_ROW= 2;
    static final int DEFAULT_BRICK_ROWS =1;
    private static final int MAX_LIVES = 3;
    private static final float HEARTS_X_POS = 30f;
    private static final float HEARTS_Y_MARGIN = 50f;
    private static final float HEARTS_WIDTH = 100f;
    private static final float HEARTS_HEIGHT = 25f;
    private static final float LIVES_SPACING = 20f;
    private static final float NUMERIC_COUNTER_SIZE = 25f;


    private Vector2 windowDimensions;
    private ImageReader imageReader;
    private UserInputListener inputListener;
    private SoundReader soundReader;
    private int rowsOfBricks;
    private int bricksPerRow;
    private WindowController windowController;
    private Ball ball;
    private Counter brickCounter;
    private int lifeCount;
    private GraphicLifeCounter graphicLifeCounter;
    private NumericLifeCounter numericLifeCounter;

    /**
     * Constructs a new BrickerGameManager instance.
     *
     * @param windowTitle  The title to be displayed on the game window.
     * @param screenSize   The dimensions (width and height) of the game window.
     * @param rowsOfBricks
     * @param bricksPerRow
     */
    public BrickerGameManager(String windowTitle, Vector2 screenSize, int rowsOfBricks, int bricksPerRow) {
        super(windowTitle, screenSize);
        this.rowsOfBricks = rowsOfBricks;
        this.bricksPerRow = bricksPerRow;
        this.brickCounter = new Counter(0);

    }

    /**
     * The entry point of the application.
     * Creates an instance of the game manager and starts the game execution.
     *
     * @param args Command line arguments (not used).
     */
    public static void main(String[] args) {
        int bricksPerRow = DEFAULT_BRICKS_PER_ROW;
        int rowsOfBricks = DEFAULT_BRICK_ROWS;

        if (args.length >= 2) {
            bricksPerRow = Integer.parseInt(args[0]);
            rowsOfBricks = Integer.parseInt(args[1]);
        }
        BrickerGameManager gameManager = new BrickerGameManager(
                GAME_TITLE, new Vector2(SCREEN_WIDTH, SCREEN_HEIGHT),rowsOfBricks,bricksPerRow);
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
        this.windowController = windowController;

        super.initializeGame(imageReader, soundReader, inputListener, windowController);
        this.windowDimensions = windowController.getWindowDimensions();
        this.imageReader = imageReader;
        this.soundReader = soundReader;
        this.inputListener = inputListener;
        this.lifeCount = MAX_LIVES;


        createBackground();
        createBall();
        createPaddle();
        createWalls();
        createBricks(this.rowsOfBricks,this.bricksPerRow);
        createHearts();
    }


    /**
     * Initializes the visual representations of the player's remaining lives.
     * This method sets up both the graphical counter (hearts) and the numeric counter.
     * It calculates their positions relative to the window dimensions using pre-defined constants,
     * loads the required assets, creates the counter objects, and adds them to the game's object
     * collection on the appropriate layers.
     */
    private void createHearts() {
        Vector2 windowDimensions = windowController.getWindowDimensions();

        Vector2 heartPos = new Vector2(HEARTS_X_POS, windowDimensions.y() - HEARTS_Y_MARGIN);
        Vector2 heartDims = new Vector2(HEARTS_WIDTH, HEARTS_HEIGHT);

        Renderable heartImage = imageReader.readImage(HEART_PATH, true);

        GraphicLifeCounter graphicLifeCounter = new GraphicLifeCounter(
                heartPos,
                heartDims,
                lifeCount,
                heartImage,
                gameObjects(),
                MAX_LIVES
        );

        gameObjects().addGameObject(graphicLifeCounter, Layer.BACKGROUND);

        Vector2 numericPos = new Vector2(heartPos.x() + heartDims.x() + LIVES_SPACING, heartPos.y());
        Vector2 numericDims = new Vector2(NUMERIC_COUNTER_SIZE, NUMERIC_COUNTER_SIZE);

        NumericLifeCounter numericLifeCounter = new NumericLifeCounter(
                numericPos,
                numericDims,
                lifeCount
        );

        gameObjects().addGameObject(numericLifeCounter, Layer.UI);

        this.graphicLifeCounter = graphicLifeCounter;
        this.numericLifeCounter = numericLifeCounter;
    }


    /**
     * Overrides the update method to handle game logic.
     * Checks for loss conditions (ball falling out of bounds) and updates life counters.
     *
     * @param deltaTime The time elapsed, in seconds, since the last frame.
     */

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        if (brickCounter.value() <= 0 || inputListener.isKeyPressed(KeyEvent.VK_W)) {
            handleGameEnd("You win! Play again?");
            return;
        }

        if (ball.getCenter().y() > windowController.getWindowDimensions().y()) {
            lifeCount--;
            graphicLifeCounter.setLives(lifeCount);
            numericLifeCounter.setLives(lifeCount);

            if (lifeCount > 0) {
                ball.setCenter(windowController.getWindowDimensions().mult(0.5f));
                ball.setVelocity(set_ball_direction());
            } else {
                handleGameEnd("You Lose! Play again?");
            }
        }
    }

        private void handleGameEnd(String prompt) {
            if (windowController.openYesNoDialog(prompt)) {
                windowController.resetGame();
            } else {
                windowController.closeWindow();
            }
        }



    /**
     * Initializes and constructs the grid of bricks for the game level.
     * This method calculates the width of each brick based on the screen width and the requested
     * number of columns to ensure they fit perfectly between the walls.
     * It creates the brick objects, assigns them a collision strategy, and adds them to the
     * game's static object layer.
     *
     * @param rows    The number of rows of bricks to create.
     * @param cols    The number of columns of bricks to create in each row.
     * @param counter
     */
    private void createBricks(int rows, int cols) {
    Renderable renderable = imageReader.readImage(BRICK_PATH, false);
    this.brickCounter = new Counter(0);
    CollisionStrategy collisionStrategy = new BasicCollisionStrategy(this.gameObjects(),this.brickCounter);


    float totalAvailableWidth = SCREEN_WIDTH - (2 * WALL_WIDTH) - ((cols - 1) * SPACE_BETWEEN_OBJECTS);
    float brickWidth = totalAvailableWidth / cols;

    for (int i = 0; i < rows; i++) {
        for (int j = 0; j < cols; j++) {
            float x = WALL_WIDTH + (j * (brickWidth + SPACE_BETWEEN_OBJECTS));
            float y = WALL_WIDTH + (i * (BRICK_HEIGHT + SPACE_BETWEEN_OBJECTS));


            Brick brick = new Brick(
                    new Vector2(x, y),
                    new Vector2(brickWidth, BRICK_HEIGHT),
                    renderable,i,j,
                    collisionStrategy

            );

            gameObjects().addGameObject(brick, Layer.STATIC_OBJECTS);
            brickCounter.increment();
        }
    }
}

    /**
     * Calculates a random initial velocity vector for the ball.
     * The method uses a fixed speed magnitude but randomizes the direction
     * (positive or negative) for both the X and Y axes, resulting in a diagonal movement
     * in one of four possible directions.
     *
     * @return A Vector2 representing the new velocity of the ball.
     */

    private Vector2 set_ball_direction(){
        float ballVelX = BALL_SPEED;
        float ballVelY = BALL_SPEED;
        Random rand = new Random();
        if(rand.nextBoolean()) {
            ballVelX *= REVERSE;
        }
        if(rand.nextBoolean()) {
            ballVelY *= REVERSE;
        }
        return new Vector2(ballVelX, ballVelY);
    }




    /**
     * Creates the ball object for the game.
     * Sets its visual representation, collision sound, initial position (centered),
     * and randomized initial velocity direction.
     */
    private void createBall() {
        Renderable ballImage = imageReader.readImage(BALL_IMAGE_PATH, true);
        Sound collisionSound = soundReader.readSound(BLOP_PATH);
        this.ball = new Ball(
                Vector2.ZERO, new Vector2(BALL_RADIUS, BALL_RADIUS), ballImage, collisionSound);

        ball.setCenter(windowDimensions.mult(BALL_START_COORDINATES));
        gameObjects().addGameObject(ball);
        ball.setVelocity(set_ball_direction());
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
