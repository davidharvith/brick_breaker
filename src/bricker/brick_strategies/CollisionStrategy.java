package bricker.brick_strategies;

import danogl.GameObject;
import danogl.util.Counter;

/**
 * defines the API for collisions strategyes
 */
public interface CollisionStrategy {
    /**
     * the basic interface for a collision with a brick
     * @param thisObj
     * @param otherObj
     */
    public void onCollision(GameObject thisObj, GameObject otherObj);
}
