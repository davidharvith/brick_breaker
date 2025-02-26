package bricker.gameobjects;

import danogl.GameObject;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

import java.awt.event.KeyEvent;

/**
 * Represents the user-controlled paddle in the game.
 */
public class Paddle extends GameObject {
    // Constants
    private static final float MOVEMENT_SPEED = 300; // Movement speed of the paddle
    private static final String USER_PADDLE_TAG = "USERPADDLE"; // Tag for identifying user paddle
    private static final String WALLTAG = "WALL";

    // Saved from initialization
    private final UserInputListener userInputListener;
    private final Vector2 windowDimensions;

    /**
     * Constructs a new UserPaddle instance.
     *
     * @param initialPosition Position of the object, in window coordinates (pixels).
     *                        Note that (0,0) is the top-left corner of the window.
     * @param dimensions      Width and height in window coordinates.
     * @param renderable      The renderable representing the object. Can be null, in which case
     *                        the GameObject will not be rendered.
     * @param inputListener   The input listener to handle user inputs.
     * @param windowDimensions The dimensions of the game window.
     */
    public Paddle(Vector2 initialPosition, Vector2 dimensions, Renderable renderable,
                  UserInputListener inputListener, Vector2 windowDimensions) {
        super(initialPosition, dimensions, renderable);
        this.userInputListener = inputListener;
        this.windowDimensions = windowDimensions;
        setTag(USER_PADDLE_TAG);
    }

    /**
     * Updates movement based on user input and ensures the paddle does not go through the borders.
     *
     * @param deltaTime The time elapsed, in seconds, since the last frame.
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        Vector2 movementDirection = Vector2.ZERO;
        if (userInputListener.isKeyPressed(KeyEvent.VK_LEFT)) {
            movementDirection = movementDirection.add(Vector2.LEFT);
        }
        if (userInputListener.isKeyPressed(KeyEvent.VK_RIGHT)) {
            movementDirection = movementDirection.add(Vector2.RIGHT);
        }
        setVelocity(movementDirection.mult(MOVEMENT_SPEED));
        Vector2 newPosition = getTopLeftCorner().add(movementDirection.mult(MOVEMENT_SPEED * deltaTime));

        // Ensure the paddle does not go through the borders
        if (newPosition.x() < 0) {
            newPosition = new Vector2(0, newPosition.y());
        } else if (newPosition.x() + getDimensions().x() > windowDimensions.x()) {
            newPosition = new Vector2(windowDimensions.x() - getDimensions().x(), newPosition.y());
        }

        setTopLeftCorner(newPosition);
    }
}
