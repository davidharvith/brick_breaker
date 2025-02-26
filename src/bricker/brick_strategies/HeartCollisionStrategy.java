package bricker.brick_strategies;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;
import bricker.gameobjects.FallingHeart;
import bricker.gameobjects.LivesTracker;

/**
 * implements a brick that drops a life
 */
public class HeartCollisionStrategy extends BasicCollisionStrategy{
    private static final float FALLSPEAD = 100;
    private final Renderable heartImage;
    private final GameObjectCollection gameObjectCollection;
    private final Vector2 dimensions;
    private final LivesTracker livesTracker;
    private final Counter brickCounter;

    /**
     *
     * @param heartImage what to render
     * @param gameObjectCollection to add and remove objs
     * @param dimensions how big to render
     * @param livesTracker
     */
    public HeartCollisionStrategy(Renderable heartImage,
                                  GameObjectCollection gameObjectCollection,
                                  Vector2 dimensions,
                                  LivesTracker livesTracker,
                                  Counter brickCounter) {
        super(gameObjectCollection, brickCounter);

        this.heartImage = heartImage;
        this.gameObjectCollection = gameObjectCollection;
        this.dimensions = dimensions;
        this.livesTracker = livesTracker;
        this.brickCounter = brickCounter;
    }

    @Override
    public void onCollision(GameObject thisObj, GameObject otherObj) {
        if(gameObjectCollection.removeGameObject(thisObj, Layer.STATIC_OBJECTS)) {
            brickCounter.decrement();
        }
        FallingHeart fallingHeart = new FallingHeart(thisObj.getCenter(), dimensions,
                heartImage, livesTracker, gameObjectCollection);
        fallingHeart.setVelocity(Vector2.DOWN.mult(FALLSPEAD));
        gameObjectCollection.addGameObject(fallingHeart);
    }
}
