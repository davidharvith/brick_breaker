package bricker.Factorys;

import bricker.brick_strategies.*;
import danogl.GameManager;
import danogl.collisions.GameObjectCollection;
import danogl.gui.Sound;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;
import bricker.gameobjects.Ball;
import bricker.gameobjects.LivesTracker;

import java.util.Random;

/**
 * Factory class for creating collision strategies.
 */
public class StrategyFactory {
    private final GameManager gameManager;
    private final GameObjectCollection gameObjectCollection;
    private final Vector2 windowDimensions;
    private final Ball mainBall;
    private final Renderable puckImage;
    private final Sound ballSound;
    private final float xCenter;
    private final float yCenter;
    private final UserInputListener inputListener;
    private final Vector2 paddleDimensions;
    private final Renderable paddleImage;
    private final Counter paddleCounter;
    private final Renderable heartImage;
    private final Vector2 heartDimensions;
    private final LivesTracker livesTracker;
    private final Strategies[] options;
    private final Counter brickCounter;
    private final Random random;

    /**
     *  it receives all the different variables that different
     *  strategies could find usefull
     * @param gameManager
     * @param gameObjectCollection
     * @param windowDimensions
     * @param mainBall
     * @param puckImage
     * @param ballSound
     * @param xCenter
     * @param yCenter
     * @param inputListener
     * @param paddleDimensions
     * @param paddleImage
     * @param paddleCounter
     * @param heartImage
     * @param heartDimensions
     * @param livesTracker
     * @param options
     * @param brickCounter
     */
    public StrategyFactory(GameManager gameManager,
                           GameObjectCollection gameObjectCollection,
                           Vector2 windowDimensions,
                           Ball mainBall,
                           Renderable puckImage,
                           Sound ballSound,
                           float xCenter,
                           float yCenter,
                           UserInputListener inputListener,
                           Vector2 paddleDimensions,
                           Renderable paddleImage,
                           Counter paddleCounter,
                           Renderable heartImage,
                           Vector2 heartDimensions,
                           LivesTracker livesTracker,
                           Strategies[] options,
                           Counter brickCounter) {
        this.gameManager = gameManager;
        this.gameObjectCollection = gameObjectCollection;
        this.windowDimensions = windowDimensions;
        this.mainBall = mainBall;
        this.puckImage = puckImage;
        this.ballSound = ballSound;
        this.xCenter = xCenter;
        this.yCenter = yCenter;
        this.inputListener = inputListener;
        this.paddleDimensions = paddleDimensions;
        this.paddleImage = paddleImage;
        this.paddleCounter = paddleCounter;
        this.heartImage = heartImage;
        this.heartDimensions = heartDimensions;
        this.livesTracker = livesTracker;
        this.options = options;
        this.brickCounter = brickCounter;
        this.random = new Random();
    }

    /**
     * Builds a collision strategy based on the provided strategy type and current count.
     *
     * @param strategy           The type of strategy to build.
     * @param currentStrategyCount The current count of strategies applied.
     * @return CollisionStrategy instance based on the specified strategy type.
     */
    public CollisionStrategy buildStrategy(Strategies strategy, int currentStrategyCount) {
        if (currentStrategyCount >= 3) {
            // If the current count is 3 or more, return a basic strategy to prevent exceeding the limit
            return new BasicCollisionStrategy(gameObjectCollection, brickCounter);
        }

        switch (strategy) {
            case Double -> {
                return new DoubleCollisionStrategy(
                        buildStrategy(options[random.nextInt(options.length)],
                                currentStrategyCount + 1),
                        buildStrategy(options[random.nextInt(options.length)],
                                currentStrategyCount + 1));
            }
            case Puck -> {
                return new PuckCollisionStrategy(gameObjectCollection, puckImage, ballSound,
                        mainBall.getVelocity().y(), mainBall.getDimensions(),
                        brickCounter);
            }
            case Paddle -> {
                return new PaddleCollisionStrategy(paddleImage, paddleCounter, gameObjectCollection,
                        xCenter, yCenter, inputListener, paddleDimensions,
                        brickCounter);
            }
            case Camera -> {
                return new CameraCollisionStrategy(gameObjectCollection, gameManager,
                        windowDimensions, mainBall, brickCounter);
            }
            case Heart -> {
                return new HeartCollisionStrategy(heartImage,
                        gameObjectCollection, heartDimensions,
                         livesTracker, brickCounter);
            }
            default -> {
                return new BasicCollisionStrategy(gameObjectCollection, brickCounter);
            }
        }
    }

    /**
     * Builds a basic collision strategy.
     *
     * @return BasicCollisionStrategy instance.
     */
    public CollisionStrategy buildBasic() {
        return new BasicCollisionStrategy(gameObjectCollection, brickCounter);
    }
}
