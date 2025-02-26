package bricker.brick_strategies;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;
import bricker.gameobjects.tempPaddle;
import bricker.gameobjects.Paddle;

/**
 * Collision strategy for handling paddle collisions.
 */
public class PaddleCollisionStrategy implements CollisionStrategy {
    private static final int MAX_PADDLES = 2; // Maximum number of extra paddles allowed
    private final Renderable paddleImage;
    private final Counter paddleCounter;
    private final GameObjectCollection gameObjectCollection;
    private final float xCenter;
    private final float yCenter;
    private final UserInputListener inputListener;
    private final Vector2 paddleDimensions;
    private final Counter brickCounter;
    private Paddle paddle;

    /**
     *
     * @param paddleImage what to render
     * @param paddleCounter how to keep count of how many we have
     * @param gameObjectCollection to add the obj
     * @param xCenter where to render
     * @param yCenter where to render
     * @param inputListener for creating the paddle
     * @param paddleDimensions for creating the paddle
     */
    public PaddleCollisionStrategy(Renderable paddleImage, Counter paddleCounter,
                                   GameObjectCollection gameObjectCollection,
                                   float xCenter, float yCenter,
                                   UserInputListener inputListener,
                                   Vector2 paddleDimensions,
                                   Counter brickCounter) {
        this.paddleImage = paddleImage;
        this.paddleCounter = paddleCounter;
        this.gameObjectCollection = gameObjectCollection;
        this.xCenter = xCenter;
        this.yCenter = yCenter;
        this.inputListener = inputListener;
        this.paddleDimensions = paddleDimensions;
        this.brickCounter = brickCounter;
    }

    /**
     * adds temp paddle if relevant
     * @param thisObj to remove
     */
    @Override
    public void onCollision(GameObject thisObj, GameObject otherObj) {
        // Remove collided brick and decrement counter
        if (gameObjectCollection.removeGameObject(thisObj, Layer.STATIC_OBJECTS)) {
            brickCounter.decrement();
        }

        // Check if maximum number of paddles reached
        if (paddleCounter.value() >= MAX_PADDLES) {
            return;
        }

        // Increment paddle counter and add an extra paddle
        paddleCounter.increment();
        Paddle extraPaddle = new tempPaddle(new Vector2(xCenter, yCenter),
                paddleDimensions, paddleImage, inputListener, new Vector2(xCenter * 2, yCenter * 2),
                paddleCounter, gameObjectCollection);
        gameObjectCollection.addGameObject(extraPaddle);
        this.paddle = extraPaddle;
    }



}
