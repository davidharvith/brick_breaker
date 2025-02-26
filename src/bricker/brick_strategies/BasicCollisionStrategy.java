package bricker.brick_strategies;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.util.Counter;

/**
 * the basic behvoiur when breaking a brick
 */
public class BasicCollisionStrategy implements CollisionStrategy {

    private final GameObjectCollection gameObjectCollection;
    private final Counter brickCounter;

    /**
     * should be able to make changes to GameObjects
     * so it receives the game obj collection
     * @param gameObjectCollection a object to use to remove other
     *                             objs
     */
    public BasicCollisionStrategy(GameObjectCollection gameObjectCollection, Counter brickCounter) {
        this.gameObjectCollection = gameObjectCollection;
        this.brickCounter = brickCounter;
    }


    /**
     * print the output and remove the brick
     * @param thisObj Gameobj
     * @param otherObj Gameobj
     */
    @Override
    public void onCollision(GameObject thisObj, GameObject otherObj) {
        if(gameObjectCollection.removeGameObject(thisObj,Layer.STATIC_OBJECTS)) {
            brickCounter.decrement();
        }
    }

}
