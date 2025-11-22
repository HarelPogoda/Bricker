package gameobjects;
import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

/**
 * This class manages the graphical representation of the life counter (hearts).
 * It handles the creation, removal, and addition of heart objects based on the current life count.
 */
public class GraphicLifeCounter extends GameObject {
    private final GameObject[] hearts;
    private final GameObjectCollection gameObjectCollection;
    private int numOfLives;

    /**
     * Constructor for GraphicLifeCounter.
     *
     * @param widgetTopLeftCorner   The top-left position of the life counter widget.
     * @param widgetDimensions      The total dimensions of the widget (containing all hearts).
     * @param lives                 The initial number of lives.
     * @param widgetRenderable      The image renderable to be used for a single heart.
     * @param gameObjectCollection  The global collection of game objects to add/remove hearts from.
     * @param maxLives              The maximum number of lives supported by this counter.
     */
    public GraphicLifeCounter(Vector2 widgetTopLeftCorner,
                              Vector2 widgetDimensions,
                              int lives,
                              Renderable widgetRenderable,
                              GameObjectCollection gameObjectCollection,
                              int maxLives) {
        super(widgetTopLeftCorner, Vector2.ZERO, null);
        this.gameObjectCollection = gameObjectCollection;
        this.numOfLives = lives;
        this.hearts = new GameObject[maxLives];

        float heartSize = widgetDimensions.x() / maxLives;
        for (int i = 0; i < maxLives; i++) {
            Vector2 heartPos = widgetTopLeftCorner.add(new Vector2(i * heartSize, 0));
            hearts[i] = new GameObject(
                    heartPos,
                    new Vector2(heartSize, widgetDimensions.y()),
                    widgetRenderable);

            if (i < lives) {
                gameObjectCollection.addGameObject(hearts[i], Layer.UI);
            }
        }
    }

    /**
     * Updates the number of lives displayed.
     * Adds or removes heart objects from the game collection as necessary.
     *
     * @param newLives The new number of lives to display.
     */
    public void setLives(int newLives) {
        if (newLives < numOfLives) {
            // Remove hearts if lives decreased
            for (int i = newLives; i < numOfLives; i++) {
                gameObjectCollection.removeGameObject(hearts[i], Layer.UI);
            }
        } else if (newLives > numOfLives) {
            // Add hearts if lives increased
            for (int i = numOfLives; i < newLives; i++) {
                gameObjectCollection.addGameObject(hearts[i], Layer.UI);
            }
        }
        this.numOfLives = newLives;
    }
}