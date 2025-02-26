package bricker.brick_strategies;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.gui.Sound;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;
import bricker.gameobjects.Ball;

import java.util.Random;

/**
 * Collision strategy for handling puck collisions.
 */
public class PuckCollisionStrategy implements CollisionStrategy {
    private static final float PUCK_SIZE_FACTOR = 0.75f; // Factor to reduce puck size
    private static final String PUCKTAG = "PUCK";
    private final GameObjectCollection gameObjectCollection;
    private final Renderable puckImage;
    private final Sound ballSound;
    private final float ballSpeed;
    private final Vector2 ballDimensions;
    private final Counter brickCounter;

    /**
     *
     * @param gameObjectCollection to remvoe the brick
     * @param puckImage what to render
     * @param ballSound what sound to make
     * @param ballSpeed in what speed to create
     * @param ballDimensions how large to create
     */
    public PuckCollisionStrategy(GameObjectCollection gameObjectCollection,
                                 Renderable puckImage,
                                 Sound ballSound,
                                 float ballSpeed,
                                 Vector2 ballDimensions,
                                 Counter brickCounter) {
        this.gameObjectCollection = gameObjectCollection;
        this.puckImage = puckImage;
        this.ballSound = ballSound;
        this.ballSpeed = ballSpeed;
        this.ballDimensions = ballDimensions;

        this.brickCounter = brickCounter;
    }

    /**
     * removes brick and creates 2 new pucks
     *
     * @param thisObj
     * @param otherObj
     */
    @Override
    public void onCollision(GameObject thisObj, GameObject otherObj) {
        // Remove collided brick and decrement counter
        if (gameObjectCollection.removeGameObject(thisObj, Layer.STATIC_OBJECTS)) {
            brickCounter.decrement();
        }
        // Create two new pucks
        createPuck(otherObj.getCenter());
        createPuck(otherObj.getCenter());
    }

    /**
     * Create a new puck object.
     *
     * @param position Position to spawn the puck.
     */
    private void createPuck(Vector2 position) {
        Ball puck = new Ball(position,
                ballDimensions.mult(PUCK_SIZE_FACTOR),
                puckImage, ballSound);
        Random random = new Random();
        double angle = random.nextDouble() * Math.PI; // Random angle in radians
        puck.setVelocity(new Vector2((float) Math.cos(angle) * ballSpeed,
                (float) Math.sin(angle) * ballSpeed));
        puck.setTag(PUCKTAG);
        gameObjectCollection.addGameObject(puck);
    }
}
