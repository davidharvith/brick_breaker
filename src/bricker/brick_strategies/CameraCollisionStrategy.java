package bricker.brick_strategies;

import danogl.GameManager;
import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.gui.rendering.Camera;
import danogl.util.Counter;
import danogl.util.Vector2;
import bricker.gameobjects.Ball;

/**
 * implements the behviour when hitting a camera brick
 */
public class CameraCollisionStrategy implements CollisionStrategy{
    private static final float WINDOWFACTOR = 1.2f;
    private static final String BALLTAG = "BALL";
    private final GameObjectCollection gameObjectCollection;
    private final GameManager brickerGameManager;
    private final Vector2 windowDimensions;
    private final Ball mainBall;
    private final Counter brickCounter;

    /**
     *
     * @param gameObjectCollection to remove stuff
     * @param brickerGameManager to change the camera
     * @param windowDimensions to widen the camera
     * @param mainBall to update about camera
     */
    public CameraCollisionStrategy(GameObjectCollection gameObjectCollection,
                                   GameManager brickerGameManager,
                                   Vector2 windowDimensions,
                                   Ball mainBall,
                                   Counter brickCounter){

        this.gameObjectCollection = gameObjectCollection;
        this.brickerGameManager = brickerGameManager;
        this.windowDimensions = windowDimensions;
        this.mainBall = mainBall;
        this.brickCounter = brickCounter;
    }

    /**
     * checks if camera should change and does it if relavent
     * @param thisObj to remove
     * @param otherObj
     */
    @Override
    public void onCollision(GameObject thisObj, GameObject otherObj) {
        if(gameObjectCollection.removeGameObject(thisObj, Layer.STATIC_OBJECTS)) {
            brickCounter.decrement();
        }
        if(brickerGameManager.camera()==null&&
        otherObj.getTag().equals(BALLTAG)) {
            brickerGameManager.setCamera(new Camera(otherObj,
                    Vector2.ZERO, windowDimensions.mult(WINDOWFACTOR),
                    windowDimensions));
            mainBall.updateLastCollisions();
            mainBall.setCameraOn(true);
        }
    }

}
