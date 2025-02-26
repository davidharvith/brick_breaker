package bricker.gameobjects;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.collisions.GameObjectCollection;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

/**
 * Class representing a falling heart in the game.
 * Increases the player's lives upon collision with the paddle.
 */
public class FallingHeart extends GameObject {
    private static final String USERPADDLETAG = "USERPADDLE";

    private final LivesTracker livesTracker;
    private final GameObjectCollection gameObjectCollection;

    /**
     * Construct a new FallingHeart instance.
     *
     * @param topLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param dimensions    Width and height in window coordinates.
     * @param renderable    The renderable representing the object. Can be null, in which case
     *                      the GameObject will not be rendered.
     * @param livesTracker  Tracker to manage the player's lives.
     */
    public FallingHeart(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable,
                        LivesTracker livesTracker,
                        GameObjectCollection gameObjectCollection) {
        super(topLeftCorner, dimensions, renderable);
        this.livesTracker = livesTracker;
        this.gameObjectCollection = gameObjectCollection;
    }

    /**
     * Determines whether this object should collide with another object.
     *
     * @param other The other GameObject.
     * @return True if the other object is a user paddle, false otherwise.
     */
    @Override
    public boolean shouldCollideWith(GameObject other) {
        return other.getTag().equals(USERPADDLETAG);
    }

    /**
     * Handles the behavior when a collision occurs.
     * if a collision happens it removes itslef and gives
     * the life to the player
     *
     * @param other The GameObject with which a collision occurred.
     * @param collision Information regarding this collision.
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        livesTracker.increaseLives();
        gameObjectCollection.removeGameObject(this);
    }
}
