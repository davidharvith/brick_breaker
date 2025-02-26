package bricker.gameobjects;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.collisions.GameObjectCollection;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;

/**
 * Class representing a temporary paddle in the game.
 */
public class tempPaddle extends Paddle {
    private static final String TEMPPADDLETAG = "TEMPPADDLE";
    private static final String WALLTAG = "WALL";
    private static final int INITIAL_COLLISIONS = 0;
    private static final int MAX_COLLISIONS = 4;

    private final Counter paddleCounter;
    private final GameObjectCollection gameObjectCollection;
    private int collisions = INITIAL_COLLISIONS;

    /**
     * Constructs a new tempPaddle instance.
     *
     * @param topLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param dimensions Width and height in window coordinates.
     * @param renderable The renderable representing the object. Can be null, in which case
     *                   the GameObject will not be rendered.
     * @param inputListener The input listener to handle user inputs.
     * @param windowDimensions The dimensions of the game window.
     * @param paddleCounter Counter for the paddle.
     * @param gameObjectCollection Collection of game objects.
     */
    public tempPaddle(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable,
                      UserInputListener inputListener, Vector2 windowDimensions,
                      Counter paddleCounter, GameObjectCollection gameObjectCollection) {
        super(topLeftCorner, dimensions, renderable, inputListener, windowDimensions);
        this.paddleCounter = paddleCounter;
        this.gameObjectCollection = gameObjectCollection;
        setTag(TEMPPADDLETAG);
    }

    /**
     * Handles the behavior when a collision occurs.
     * increases collision count if it is relevant
     *
     * @param other The GameObject with which a collision occurred.
     * @param collision Information regarding this collision.
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        if (!other.getTag().equals(WALLTAG)) {
            collisions++;
        }
    }

    /**
     * Updates the temporary paddle state in each frame.
     * cehcks if it should remove itself
     *
     * @param deltaTime The time, in seconds, that passed since the last invocation
     *                  of this method (i.e., since the last frame). This is useful
     *                  for either accumulating the total time that passed since some
     *                  event, or for physics integration (i.e., multiply this by
     *                  the acceleration to get an estimate of the added velocity or
     *                  by the velocity to get an estimate of the difference in position).
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        if (collisions >= MAX_COLLISIONS) {
            gameObjectCollection.removeGameObject(this);
            paddleCounter.decrement();
        }
    }
}
