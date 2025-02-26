package bricker.brick_strategies;

import danogl.GameObject;
import danogl.util.Counter;

/**
 * implements a double ability brick
 */
public class DoubleCollisionStrategy implements CollisionStrategy {

    private final CollisionStrategy firstStrategy;
    private final CollisionStrategy secondStrategy;

    /**
     *
     * @param firstStrategy a strat to use
     * @param secondStrategy a strat to use
     */
    public DoubleCollisionStrategy(CollisionStrategy firstStrategy,
                                   CollisionStrategy secondStrategy){
        this.firstStrategy = firstStrategy;
        this.secondStrategy = secondStrategy;
    }

    /**
     * uses its strats to do cool stuff
     * @param thisObj to remove
     * @param otherObj
     */
    @Override
    public void onCollision(GameObject thisObj, GameObject otherObj) {
        firstStrategy.onCollision(thisObj, otherObj);
        secondStrategy.onCollision(thisObj, otherObj);
    }
}
