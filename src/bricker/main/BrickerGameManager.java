package bricker.main;

import bricker.Factorys.StrategyFactory;
import bricker.brick_strategies.CollisionStrategy;
import bricker.brick_strategies.Strategies;
import danogl.GameManager;
import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.components.CoordinateSpace;
import danogl.gui.*;
import danogl.gui.rendering.Renderable;
import danogl.gui.rendering.TextRenderable;
import danogl.util.Counter;
import danogl.util.Vector2;
import bricker.gameobjects.*;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Random;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
// game manager for a paddle game

/**
 * maneges a game of bricker
 */
public class BrickerGameManager extends GameManager {
    // Paths
    private static final String ASSETSPATH = "assets";
    private static final String BALLPATH = "/ball.png";
    private static final String COLLISIONSOUND = "/blop_cut_silenced.wav";
    private static final String PADDLEPATH = "/paddle.png";
    private static final String BACKROUNDPATH = "/DARK_BG2_small.jpeg";
    private static final String BRICKPATH = "/brick.png";
    private static final String HEARTPATH = "/heart.png";
    private static final String PUCKPATH = "/mockBall.png";

    // Ball constants
    private static final float BALLVELOCITY = 150;
    private static final float CENTERFACTOR = 0.5f;
    private static final String WINSTRING = "You win! Play again?";
    private static final String LOSEMSG = "You lose, play again?";
    private static final float BALLDIMENSIONS = 50;
    private static final float HEARTDIMENSIONS = 50;
    private static final int MAXCOLLISIONSWITHCAMERA = 5;
    private static final String WALLTAG = "WALL";
    private static final int DOUBLE = 2;
    private static final int ZERO = 0;
    private static final float HALF = 0.5f;
    private static final int UNIT = 1;
    private static final float WINDOWWIDTH = 700;
    private static final float WINDOWHEIGHT = 500;
    private static final String TITLE = "Bricker";
    private static final int FIRSTARG = 0;
    private static final int SECONDARG = 1;
    private static final int GOTARGS = 2;

    // Paddle constants
    private static final float PADDLEHEIGHT = 10;
    private static final float PADDLEWIDTH = 200;

    // Brick constants
    private static final int DEFAULTROW = 8;
    private static final int DEFAULTCOL = 7;
    private static final int BRICKHEIGHT = 15;

    // Lives constants
    private static final int INITIALLIVES = 3;

    // Border constants
    private static final int BORDERWIDTH = 5;

    // Small buffer constant
    private static final int EPSILON = 10;
    private static final String EMPTYSTRING = "";
    private final int numRow;
    private final int numColsl;

    private Random random;
    private Ball ball;
    private final Strategies[] strategies;
    private LivesTracker livesTracker;
    private Counter brickCounter;
    private Counter paddleCounter;
    private final Vector2 windowDimensions;
    private WindowController windowController;
    private ImageReader imageReader;
    private SoundReader soundReader;
    private UserInputListener inputListener;

    /**
     * Constructor that creates the game manager
     * mainly using super but also saving
     * specific variables it receives.
     * @param windowTitle The title of the game window.
     * @param windowDimensions The dimensions of the game window.
     * @param numRow Number of rows of bricks.
     * @param numCols Number of columns of bricks.
     */
    public BrickerGameManager(String windowTitle, Vector2 windowDimensions, int numRow, int numCols) {
        super(windowTitle, windowDimensions);
        this.windowDimensions = windowDimensions;
        this.numRow = numRow;
        this.numColsl = numCols;
        this.strategies = Strategies.values();
    }

    /**
     * Creates the borders of the game in 3 directions.
     * @param xLen The width of the screen.
     * @param yLen The height of the screen.
     */
    private void initializeBorders(int xLen, int yLen) {
        GameObject borderLeft = new GameObject(Vector2.ZERO, new Vector2(BORDERWIDTH, yLen), null);
        GameObject borderRight = new GameObject(new Vector2(xLen - BORDERWIDTH, ZERO),
                new Vector2(BORDERWIDTH, yLen), null);
        GameObject borderUp = new GameObject(Vector2.ZERO, new Vector2(xLen, BORDERWIDTH), null);
        gameObjects().addGameObject(borderRight);
        gameObjects().addGameObject(borderLeft);
        gameObjects().addGameObject(borderUp);
        borderUp.setTag(WALLTAG);
        borderLeft.setTag(WALLTAG);
        borderRight.setTag(WALLTAG);
    }

    /**
     * Renders and place bricks to explode.
     */
    private void initializeBrick() {
        // Initiate a factory to build bricks
        StrategyFactory strategyFactory = initializeFactory();
        // Initiate params for bricks
        Renderable brickImage = imageReader.readImage(ASSETSPATH + BRICKPATH, false);
        int width = ((int) windowDimensions.x() - EPSILON * DOUBLE) / numRow;

        for (int row = ZERO; row < numColsl; row++) {
            for (int col = ZERO; col < numRow; col++) {
                createBrick(strategyFactory, row, col, width, brickImage);
            }
        }
    }

    /**
     * flips a coin to see if the brick should have a
     * special ability. uses factory to create brick
     * @param strategyFactory obj to build the strategeis
     * @param row where to put the brick
     * @param col where to put the brick
     * @param width how to space the brickds
     * @param brickImage what to render
     */
    private void createBrick(StrategyFactory strategyFactory,
                             int row, int col,
                             int width,
                             Renderable brickImage) {
        CollisionStrategy collisionStrategy;
        if (random.nextBoolean()) { // Coin flip to see what to build
            collisionStrategy = strategyFactory.buildBasic();
        } else {
            collisionStrategy = strategyFactory.buildStrategy(
                    strategies[random.nextInt(strategies.length)], ZERO); // Start with a strategy count of 0
        }

        Brick curBrick = new Brick(new Vector2((EPSILON) + (width * col), (EPSILON) + (BRICKHEIGHT * row)),
                new Vector2(width, BRICKHEIGHT),
                brickImage, collisionStrategy, brickCounter);
        gameObjects().addGameObject(curBrick, Layer.STATIC_OBJECTS);
    }

    /**
     * Initiates the factory to be able to build all factories.
     * @return A StrategyFactory.
     */
    private StrategyFactory initializeFactory() {
        Renderable puckImage = imageReader.readImage(ASSETSPATH + PUCKPATH, true);
        Sound collisionSound = soundReader.readSound(ASSETSPATH + COLLISIONSOUND);
        Renderable paddleImage = imageReader.readImage(ASSETSPATH + PADDLEPATH, false);
        Renderable heartImage = imageReader.readImage(ASSETSPATH + HEARTPATH, false);
        return new StrategyFactory(this, gameObjects(),
                windowDimensions, ball, puckImage, collisionSound, windowDimensions.x() * HALF,
                windowDimensions.y() / DOUBLE, inputListener, new Vector2(PADDLEWIDTH, PADDLEHEIGHT),
                paddleImage, paddleCounter, heartImage, new Vector2(HEARTDIMENSIONS, HEARTDIMENSIONS),
                livesTracker, strategies, brickCounter);
    }

    /**
     * Initializes the background.
     */
    private void initializeBackground() {
        Renderable backgroundImage = imageReader.readImage(ASSETSPATH + BACKROUNDPATH,
                true);
        GameObject background = new GameObject(Vector2.ZERO,
                new Vector2(windowController.getWindowDimensions().x(),
                windowController.getWindowDimensions().y()), backgroundImage);
        background.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        gameObjects().addGameObject(background, Layer.BACKGROUND);
    }

    /**
     * Initializes the ball while starting randomly in different directions.
     */
    private void initializeBall() {
        // Extract rendering assets of the ball
        Renderable ballImage = imageReader.readImage(ASSETSPATH + BALLPATH, true);
        Sound collisionSound = soundReader.readSound(ASSETSPATH + COLLISIONSOUND);
        // Create and assign the ball to its field ball
        this.ball = new Ball(Vector2.ZERO,
                new Vector2(BALLDIMENSIONS, BALLDIMENSIONS), ballImage, collisionSound);
        setBallDir();
    }

    /**
     * randomly sets the balls directions when rendered
     */
    private void setBallDir() {
        int ballDir = UNIT;
        if (random.nextBoolean()) {
            ballDir *= -UNIT;
        } // Choose direction randomly
        ball.setVelocity(new Vector2(ballDir * BALLVELOCITY, BALLVELOCITY));
        ball.setCenter(windowController.getWindowDimensions().mult(CENTERFACTOR));
        gameObjects().addGameObject(ball);
    }

    /**
     * Initiate paddle.
     */
    public void initializePaddle() {
        // Paddle assets
        Renderable paddleImage = imageReader.readImage(ASSETSPATH + PADDLEPATH, false);
        // Creation
        Paddle paddle = new Paddle(Vector2.ZERO,
                new Vector2(PADDLEWIDTH, PADDLEHEIGHT),
                paddleImage,
                inputListener,
                windowDimensions);
        paddle.setCenter(new Vector2(windowController.getWindowDimensions().x() * HALF,
                windowController.getWindowDimensions().y() - PADDLEHEIGHT));
        gameObjects().addGameObject(paddle);
        paddleCounter = new Counter(UNIT);
    }

    /**
     * Create a LivesTracker made to track and render the lives left.
     */
    private void initializeLives() {
        // Create params for tracker
        Renderable heartImage = imageReader.readImage(ASSETSPATH + HEARTPATH,
                false);
        TextRenderable textRenderable = new TextRenderable(String.valueOf(INITIALLIVES));
        textRenderable.setColor(Color.GREEN);
        // Initiate the tracker
        this.livesTracker = new LivesTracker(new Vector2(HEARTDIMENSIONS,
                windowDimensions.y() - HEARTDIMENSIONS),
                new Vector2(HEARTDIMENSIONS, HEARTDIMENSIONS),
                heartImage, INITIALLIVES, (int) HEARTDIMENSIONS,
                gameObjects(),
                textRenderable);
    }

    /**
     * Initializes the game with provided readers and input listeners.
     * @param imageReader Contains a single method: readImage, which reads an image from disk.
     *                    See its documentation for help.
     * @param soundReader Contains a single method: readSound, which reads a wav file from
     *                    disk. See its documentation for help.
     * @param inputListener Contains a single method: isKeyPressed, which returns whether
     *                      a given key is currently pressed by the user or not. See its
     *                      documentation.
     * @param windowController Contains an array of helpful, self-explanatory methods
     *                         concerning the window.
     */
    public void initializeGame(ImageReader imageReader,
                               SoundReader soundReader,
                               UserInputListener inputListener,
                               WindowController windowController) {
        // Save variables to private fields for further use
        this.imageReader = imageReader;
        this.soundReader = soundReader;
        this.inputListener = inputListener;
        this.windowController = windowController;
        this.random = new Random();
        this.brickCounter = new Counter(numColsl * numRow);

        // Use super initialization
        super.initializeGame(imageReader, soundReader, inputListener, windowController);

        initializeBall();
        initializePaddle();
        initializeBorders((int) windowController.getWindowDimensions().x(),
                (int) windowController.getWindowDimensions().y());
        initializeBackground();
        initializeLives();
        initializeBrick();
    }

    /**
     * Updates the game state.
     * @param deltaTime The time, in seconds, that passed since the last invocation
     *                  of this method (i.e., since the last frame). This is useful
     *                  for either accumulating the total time that passed since some
     *                  event, or for physics integration (i.e., multiply this by
     *                  the acceleration to get an estimate of the added velocity or
     *                  by the velocity to get an estimate of the difference in position).
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        checkCamera();
        removeOutOfBound();
        checkIfOver(); // Check if game is over
    }

    /**
     * resets the camera focus if relevant
     */
    private void checkCamera() {
        if (ball.isCameraOn() &&
                ((ball.getCollisionCounter() - ball.getLastCollisions()) >= MAXCOLLISIONSWITHCAMERA)) {
            setCamera(null);
            ball.setCameraOn(false);
        }
    }

    /**
     * removes objects that are out of the screen
     */
    private void removeOutOfBound() {
        checkBallInBounds();
        for (GameObject obj : gameObjects()) {
            if ((obj.getCenter().y() - obj.getDimensions().y()) / DOUBLE > windowDimensions.y()) {
                gameObjects().removeGameObject(obj);
            }
        }
    }

    /**
     * Check if ball left the parameters of the board.
     * If so, create a new one and remove a life.
     */
    private void checkBallInBounds() {
        if (ball.getCenter().y() - ball.getDimensions().y() * HALF > windowDimensions.y()) {
            livesTracker.decreaseLives();
            gameObjects().removeGameObject(ball);
            initializeBall();
            setCamera(null);
        }
    }

    /**
     * Checks if there are any lives left or any bricks left
     */
    private void checkIfOver() {
        String msg = EMPTYSTRING;
        msg += checkIfWon();
        msg += checkIfLost();

        if (!msg.isBlank()) {
            if (windowController.openYesNoDialog(msg)) {
                windowController.resetGame();
            } else {
                windowController.closeWindow();
            }
        }
    }

    /**
     * checks if there are any lives left
     * @return string to print or empty string
     * to signify that it is not over yet
     */
    private String checkIfLost() {
        if (livesTracker.getLivesCounter() == ZERO) {
            return LOSEMSG;
        }
        return EMPTYSTRING;
    }

    /**
     * checks if there are any bricks left
     * @return string to print or empty string
     * to signify that it is not over yet
     */
    private String checkIfWon() {
        if (brickCounter.value() == ZERO || inputListener.isKeyPressed(KeyEvent.VK_W)) {
            return WINSTRING;
        }
        return EMPTYSTRING;
    }


    /**
     * Reads input and initiates the game by the input.
     * @param args Input arguments.
     * @return GameManager
     */
    private static BrickerGameManager initiateGameByInput(String[] args) {
        if (args.length == GOTARGS) {
            return new BrickerGameManager(TITLE,
                    new Vector2(WINDOWWIDTH, WINDOWHEIGHT), Integer.parseInt(args[FIRSTARG]),
                    Integer.parseInt(args[SECONDARG]));
        } else {
            return new BrickerGameManager(TITLE,
                    new Vector2(WINDOWWIDTH, WINDOWHEIGHT), DEFAULTROW, DEFAULTCOL);
        }
    }

    /**
     * Main method to start the game.
     * @param args Optional for how many bricks to put in the window, default 7*8
     */
    public static void main(String[] args) {
        BrickerGameManager game = initiateGameByInput(args);
        game.run();
    }
}
