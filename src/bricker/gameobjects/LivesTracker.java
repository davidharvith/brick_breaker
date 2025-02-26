package bricker.gameobjects;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.gui.rendering.Renderable;
import danogl.gui.rendering.TextRenderable;
import danogl.util.Vector2;

import java.awt.*;

/**
 * Represents a tracker for player lives in the game.
 */
public class LivesTracker extends GameObject {
    private static final int MAX_LIVES = 4;
    private static final int UNIT = 1;
    private static final int FULL_LIFE = 3;
    private static final int MID_LIVES = 2;

    // Fields to save from initialization
    private final Vector2 bottomLeftCorner;
    private final Vector2 dimensions;
    private final TextRenderable textRenderable;
    private int livesCounter;
    private final Renderable heartImage;
    private final int buffer;
    private final GameObjectCollection gameObjectCollection;

    // List of hearts to render
    private final GameObject[] heartObjects;

    /**
     * Constructs a LivesTracker instance.
     *
     * @param bottomLeftCorner     Bottom left corner position of the tracker.
     * @param dimensions           Dimensions of each heart.
     * @param heartImage           Renderable image for representing a heart.
     * @param initialLives         Initial number of lives.
     * @param buffer               Buffer space between hearts.
     * @param gameObjectCollection Collection of game objects.
     * @param textRenderable       Renderable for numeric representation of lives.
     */
    public LivesTracker(Vector2 bottomLeftCorner, Vector2 dimensions,
                        Renderable heartImage, int initialLives, int buffer,
                        GameObjectCollection gameObjectCollection,
                        TextRenderable textRenderable) {
        super(bottomLeftCorner, dimensions, heartImage);
        // Save initialization parameters
        this.bottomLeftCorner = bottomLeftCorner;
        this.heartImage = heartImage;
        this.dimensions = dimensions;
        this.livesCounter = initialLives;
        this.buffer = buffer;
        this.gameObjectCollection = gameObjectCollection;
        this.textRenderable = textRenderable;

        // Create list to hold heart representations
        heartObjects = new GameObject[MAX_LIVES];

        // Initialize heart images
        initializeHearts(initialLives);

        // Initialize the numeric representation
        Vector2 textPosition = new Vector2(bottomLeftCorner); // Adjust position as needed
        GameObject numericLivesTracker = new GameObject(textPosition, new Vector2(dimensions.x(),
                dimensions.y()), textRenderable);
        gameObjectCollection.addGameObject(numericLivesTracker, Layer.BACKGROUND);
    }

    private void initializeHearts(int initialLives) {
        for (int i = 0; i < initialLives; i++) {
            Vector2 heartPosition = bottomLeftCorner.add(new Vector2(dimensions.x()
                    + (i * (dimensions.x()) + buffer),
                    0)); // Adjust as needed
            heartObjects[i] = new GameObject(heartPosition, dimensions, heartImage);
            gameObjectCollection.addGameObject(heartObjects[i], Layer.BACKGROUND);
        }
    }

    /**
     * Increases lives and ensures the correct representation is rendered.
     */
    public void increaseLives() {
        if (livesCounter == MAX_LIVES) {
            return;
        }
        livesCounter++;
        // If no more space, copy the heart to a larger one
        Vector2 heartPosition = bottomLeftCorner.add(new Vector2(dimensions.x() +
                (livesCounter * buffer), 0));
        heartObjects[livesCounter - UNIT] = new GameObject(heartPosition, dimensions, heartImage);
        gameObjectCollection.addGameObject(heartObjects[livesCounter - UNIT], Layer.BACKGROUND);
        // Update numeric
        updateTextColor();
        textRenderable.setString(String.valueOf(livesCounter));
    }

    /**
     * Decreases lives and updates the representation.
     */
    public void decreaseLives() {
        gameObjectCollection.removeGameObject(heartObjects[livesCounter - UNIT], Layer.BACKGROUND);
        heartObjects[livesCounter - UNIT] = null;
        livesCounter--;
        updateTextColor();
        textRenderable.setString(String.valueOf(livesCounter));
    }

    /**
     * Logic for changing numeric representation color based on lives.
     */
    private void updateTextColor() {
        if (livesCounter >= FULL_LIFE) {
            textRenderable.setColor(Color.green);
        } else if (livesCounter == MID_LIVES) {
            textRenderable.setColor(Color.yellow);
        } else {
            textRenderable.setColor(Color.red);
        }
    }

    /**
     * Gets the remaining lives.
     *
     * @return Number of lives left.
     */
    public int getLivesCounter() {
        return livesCounter;
    }
}
