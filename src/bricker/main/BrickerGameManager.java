package bricker.main;
import danogl.GameManager;
import danogl.GameObject;
import danogl.gui.ImageReader;
import danogl.gui.SoundReader;
import danogl.gui.UserInputListener;
import danogl.gui.WindowController;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
public class BrickerGameManager extends GameManager{
    static final String GAME_TITLE = "Bricker";
    static final String BALL_IMAGE_PATH = "assets/ball.png";
    static final String BLOP_PATH = "assets/blop.wav";

    static final int SCREEN_WIDTH = 700;
    static final int SCREEN_HEIGHT = 500;
    static final int BALL_RADIUS = 40;
    static final int BALL_SPEED = 100;
    static final float BALL_COORDINATES = 0.5f;

    private ImageReader;
    private WindowController windowController;
    private Vector2 windowDimensions;

    public BrickerGameManager(String windowTitle, Vector2 screenSize){
        super(windowTitle, screenSize);
    }

    public static void main(String[] args) {
        BrickerGameManager gameManager = new BrickerGameManager(
                GAME_TITLE, new Vector2(SCREEN_WIDTH, SCREEN_HEIGHT));
        gameManager.run();
        this.initializeGame(new ImageReader(),
                new SoundReader(),
                new UserInputListener(),
                new WindowController())
    }

    @Override
    public void initializeGame(ImageReader imageReader,
                               SoundReader soundReader,
                               UserInputListener inputListener,
                               WindowController windowController) {
        this.windowController = windowController;

        super.initializeGame(imageReader, soundReader, inputListener, windowController);

        Renderable ballImage = imageReader.readImage(BALL_IMAGE_PATH, true);
        GameObject ball = new GameObject(Vector2.ZERO, new Vector2(BALL_RADIUS, BALL_RADIUS), ballImage);
        ball.setVelocity(Vector2.DOWN.mult(BALL_SPEED));
        windowDimensions = windowController.getWindowDimensions();
        ball.setCenter(new Vector2(BALL_COORDINATES, BALL_COORDINATES));
        gameObjects().addGameObject(ball);
    }

}
