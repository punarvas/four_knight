/*
 * Solving Four Knight Puzzle using Breadth First Search
 * @author: Akash Mahajan (UMID: 64930841)
 */

// I shall use ArrayList, Queue, and LinkedList data structures
import java.util.*;

public class BFSChessboard {

    public class State {
        /*
         * This data structure stores the information about the position of knights on
         * the
         * chessboard
         */
        public int[][] knightLocations;
        // Total cost it took to search the goal state
        public int cost;

        public State(int[][] knightLocations, int cost) {
            this.knightLocations = knightLocations;
            this.cost = cost;
        }
    }

    public int[][] possibleMoves(int[] position) {
        /*
         * This function returns possible moves one knight can take from its original
         * `position`
         */
        int x = position[0];
        int y = position[1];

        int[][] possibleMoves = { { x + 2, y - 1 }, { x + 2, y + 1 }, { x - 2, y - 1 }, { x - 2, y + 1 },
                { x + 1, y + 2 }, { x - 1, y + 2 }, { x + 1, y - 2 }, { x - 1, y - 2 } };

        return possibleMoves;
    }

    public boolean isValidMove(int[] prevLocation, int[] newLocation) {
        /*
         * This function validates the movement. If a knight moves from `prevLocation`
         * to `newLocation`
         * then it should conform to the chess standards and the limitations of the
         * board coordinates.
         */
        int nx = newLocation[0];
        int ny = newLocation[1];

        int px = prevLocation[0];
        int py = prevLocation[1];

        if (nx < 3 && ny < 3 && nx >= 0 && ny >= 0) {
            // Calculate the displacement of the knight on X and Y coordinate
            int disX = Math.abs(nx - px);
            int disY = Math.abs(ny - py); // displacement calculated

            if ((disX == 2 && disY == 1) || (disX == 1 && disY == 2)) {
                return true;
            }
        }
        return false;
    }

    public ArrayList<int[][]> expand(State s) {
        /*
         * During runtime this function dynamically creates the neighbours of a node or
         * state
         * of the board. I ensured that the knight positions in the state are valid.
         */
        ArrayList<int[][]> knightLocations = new ArrayList<>();
        for (int i = 0; i < s.knightLocations.length; i++) {
            // Generate all possible moves from original location
            int[][] possibleMoves = possibleMoves(s.knightLocations[i]);
            for (int[] possibleMove : possibleMoves) {
                // Array is copied to avoid similar memory references
                int[][] newLocation = Arrays.copyOf(s.knightLocations, s.knightLocations.length);
                int[] prevLocation = newLocation[i];
                if (isValidMove(prevLocation, possibleMove)) {
                    newLocation[i] = possibleMove;
                    knightLocations.add(newLocation);
                }
            }
        }
        return knightLocations;
    }

    public boolean isVisited(Set<int[][]> visited, int[][] knightLocations) {
        // This function verifies if the current knight position, which is generated by
        // expand function,
        // is been already visited. This avoid infinite loops and memory run overs
        for (int[][] location : visited) {
            if (Arrays.equals(location, knightLocations)) {
                return true;
            }
        }
        return false;
    }

    public boolean isContains(Queue<State> queue, int[][] positions) {
        /*
         * This function ensure if a new state being added to the queue is already in
         * the queue.
         * This function and the `isVisited` function avoid infinite loop in two-fold
         * process.
         */
        for (State state : queue) {
            if (Arrays.deepEquals(state.knightLocations, positions)) {
                return true;
            }
        }
        return false;
    }

    public int[][] searchAndSolve(int[][] initialPositions, int[][] goalState) {
        /*
         * The base function to run BFS on initialPositions i.e., the initial state of
         * chessboard
         */
        Queue<State> queue = new LinkedList<>();
        // The initial state is added and the queue is initiated here
        queue.add(new State(initialPositions, 0));
        Set<int[][]> visited = new HashSet<>();

        while (!queue.isEmpty()) {
            State state = queue.poll();
            System.out.println(Arrays.deepToString(state.knightLocations));

            // If the current state being examined is already a goal state, exit.
            if (Arrays.deepEquals(state.knightLocations, goalState)) {
                System.out.println("Total cost: " + state.cost);
                return state.knightLocations;
            }

            ArrayList<int[][]> newKnightLocations = expand(state);
            /*
             * For every new state generated by expand function, create a new state for each
             * knight and add it to queue with precaution.
             */
            for (int[][] location : newKnightLocations) {
                State newState = new State(location, state.cost + 1);

                // Precaution is taken to avoid duplicate entries and traversals for search
                if (!isVisited(visited, newState.knightLocations)) {
                    if (!isContains(queue, newState.knightLocations)) {
                        queue.add(newState);
                    }
                }
            }
            visited.add(state.knightLocations);
            System.out.flush(); // This code simply flush out the print stack of java to avoid memory run outs
        }
        return null;
    }

    public static void main(String[] args) {

        // read the goal state from goalstate.in file and develop search results
        Scanner stdin = new Scanner(System.in);

        int[][] goalState = new int[4][2];
        int[][] initialState = { { 0, 0 }, { 0, 2 }, { 2, 0 }, { 2, 2 } };

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 2; j++) {
                goalState[i][j] = stdin.nextInt();
            }
        }
        stdin.close();

        // Initiate a empty chessboard and perform search
        BFSChessboard board = new BFSChessboard();
        long startTime = System.currentTimeMillis();
        int[][] solution = board.searchAndSolve(initialState, goalState);
        long executionTime = System.currentTimeMillis() - startTime;
        System.out.println("BFS Result:" + Arrays.deepToString(solution));
        System.out.println("Time taken for run: " + executionTime + " ms");

        Runtime runtime = Runtime.getRuntime();
        long totalMemory = runtime.totalMemory();
        long freeMemory = runtime.freeMemory();
        long usedMemory = totalMemory - freeMemory;
        System.out.println("Total Memory: " + totalMemory / 1024 + " bytes");
        System.out.println("Used Memory: " + usedMemory / 1024 + " bytes");

    }
}