package bricker.gameobjects;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.gui.Sound;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

/**
 * Class representing a Ball in the game.
 */
public class Ball extends GameObject {
    private static final String BALLTAG = "BALL";
    private static final int INITIAL_COLLISION_COUNT = 0;
    private static final boolean INITIAL_CAMERA_STATE = false;

    private final Sound collisionSound;
    private int collisionCounter = INITIAL_COLLISION_COUNT;
    private boolean isCameraOn = INITIAL_CAMERA_STATE;
    private int lastCollisions = INITIAL_COLLISION_COUNT;

    /**
     * Constructor for the Ball class.
     *
     * @param topLeftCorner Coordinates to render the ball.
     * @param dimensions Dimensions of the ball.
     * @param renderable Image of the ball.
     * @param collisionSound Sound that the ball should make on collision.
     */
    public Ball(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable, Sound collisionSound) {
        super(topLeftCorner, dimensions, renderable);
        this.collisionSound = collisionSound;
        setTag(BALLTAG);
    }

    /**
     * Handles the behavior when a collision occurs.
     *
     * @param other The GameObject with which a collision occurred.
     * @param collision Information regarding this collision.
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);

        // Change direction upon collision
        Vector2 newVelocity = getVelocity().flipped(collision.getNormal());
        setVelocity(newVelocity);

        // Increase collision counter and play sound
        collisionCounter++;
        collisionSound.play();
    }

    /**
     * Checks if the camera is currently on.
     *
     * @return True if the camera is on, otherwise false.
     */
    public boolean isCameraOn() {
        return isCameraOn;
    }

    /**
     * Sets the state of the camera.
     *
     * @param cameraOn True to turn the camera on, false to turn it off.
     */
    public void setCameraOn(boolean cameraOn) {
        isCameraOn = cameraOn;
    }

    /**
     * Gets the count of collisions since the last camera state change.
     *
     * @return The number of collisions since the last camera state change.
     */
    public int getLastCollisions() {
        return lastCollisions;
    }

    /**
     * Updates the collision counter to the current count.
     */
    public void updateLastCollisions() {
        lastCollisions = collisionCounter;
    }

    /**
     * Gets the total collision counter.
     *
     * @return The total number of collisions.
     */
    public int getCollisionCounter() {
        return collisionCounter;
    }
}
