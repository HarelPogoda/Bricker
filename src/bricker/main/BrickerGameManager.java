package bricker.main;
import danogl.GameManager;
import danogl.util.Vector2;
public class BrickerGameManager extends GameManager{
    static final String GAME_TITLE = "Bricker";

    static final int SCREEN_WIDTH = 700;
    static final int SCREEN_HEIGHT = 500;

    public BrickerGameManager(String windowTitle, Vector2 screenSize){
        super(windowTitle, screenSize);
    }

    public static void main(String[] args) {
        BrickerGameManager gameManager = new BrickerGameManager(
                GAME_TITLE, new Vector2(SCREEN_WIDTH, SCREEN_HEIGHT));
        gameManager.run();
    }


}
