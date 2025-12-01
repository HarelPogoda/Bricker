    package bricker.brick_strategies;

    import bricker.gameobjects.Brick;
    import danogl.GameObject;
    import danogl.collisions.GameObjectCollection;
    import danogl.collisions.Layer;
    import danogl.gui.SoundReader;
    import danogl.gui.Sound;
    import danogl.util.Counter;

    /**
     * ExplosionStrategy handles bricks that explode on collision.
     * <p>
     * Behavior:
     * 1. When a brick with this strategy is hit, it explodes.
     * 2. All direct neighbors (up, down, left, right) are affected:
     *    - If the neighbor is a regular brick, it only breaks (onCollisionEnter is called).
     *    - If the neighbor has ExplosionStrategy, it triggers a recursive explosion.
     * 3. Prevents double explosions using the brick grid.
     * 4. Plays an explosion sound for each brick broken.
     */
    public class ExplosionStrategy implements CollisionStrategy {

        static final String EXPLOSION_SOUND_PATH = "assets/explosion.wav";

        private final GameObjectCollection gameObjects;
        private final Counter brickCounter;
        private final Brick[][] brickGrid;
        private final Sound explosionSound;

        /**
         * Constructor for ExplosionStrategy.
         *
         * @param gameObjects   Collection of all game objects.
         * @param brickCounter  Counter tracking remaining bricks.
         * @param soundReader   Sound reader to load the explosion sound.
         * @param brickGrid     2D array of bricks representing the board.
         */
        public ExplosionStrategy(GameObjectCollection gameObjects,
                                 Counter brickCounter,
                                 SoundReader soundReader,
                                 Brick[][] brickGrid) {
            this.gameObjects = gameObjects;
            this.brickCounter = brickCounter;
            this.brickGrid = brickGrid;
            this.explosionSound = soundReader.readSound(EXPLOSION_SOUND_PATH);
        }

        /**
         * Called when a brick collides with another object.
         * Starts the explosion process.
         *
         * @param firstObject  The brick itself.
         * @param otherObject  The object that collided with the brick (can be null for explosions).
         */
        @Override
        public void onCollision(GameObject firstObject, GameObject otherObject) {
            if (!(firstObject instanceof Brick)) {
                return;
            }
            Brick myBrick = (Brick) firstObject;
            int row = myBrick.getRow();
            int col = myBrick.getCol();


            if (gameObjects.removeGameObject(firstObject, Layer.STATIC_OBJECTS)) {
                brickCounter.decrement();
                brickGrid[row][col] = null;
                explosionSound.play();
            }
            // Process neighbors
            explodeNeighbor(row - 1, col, myBrick);
            explodeNeighbor(row + 1, col, myBrick);
            explodeNeighbor(row, col - 1, myBrick);
            explodeNeighbor(row, col + 1, myBrick);
        }



        /*
         * Handles a single neighboring brick:
         * - Calls onCollisionEnter to break it.
         * - If it has ExplosionStrategy, triggers recursive explosion.
         *
         * @param row    Row index of the neighbor.
         * @param col    Column index of the neighbor.
         * @param source The original brick that triggered the explosion.
         */
        private void explodeNeighbor(int row, int col, Brick source) {
            // Bounds check
            if (row < 0 || col < 0 || row >= brickGrid.length || col >= brickGrid[0].length)
                return;

            Brick neighbor = brickGrid[row][col];
            if (neighbor == null) return;

            // Call onCollisionEnter to simulate collision/break
            neighbor.onCollisionEnter(source, null);
        }
    }
