import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

public class Solver {
    private final Stack<Board> sq;
    private boolean solvable;

    // SearchNode for the Priority Queue
    private static class SearchNode implements Comparable<SearchNode> {
        private final Board board;
        private final int moves;
        private final SearchNode prev;
        private final int manhattan;


        private SearchNode(Board b, int moves, SearchNode prev) {
            this.board = b;
            this.moves = moves;
            this.prev = prev;
            // precompute manhattan for caching
            this.manhattan = b.manhattan();
        }

        // PRIORITY FUNCTION which takes manhattan and moves to get to the tile
        public int compareTo(SearchNode that) {
            return Integer.compare(this.manhattan + this.moves, that.manhattan + that.moves);
        }
    }

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null) throw new IllegalArgumentException();
        solvable = true;
        int moves = 0;
        sq = new Stack<>();
        Stack<Board> sqTwin = new Stack<>();

        // Initialize Priority Queues
        // 2 Instances of Priority queue, to check the solvability
        MinPQ<SearchNode> PQ = new MinPQ<>();
        MinPQ<SearchNode> PQTwin = new MinPQ<>();

        // Create initial Node
        SearchNode sn = new SearchNode(initial, moves, null);
        SearchNode snTwin = new SearchNode(initial.twin(), moves, null);
        //System.out.println("INITIAL BAORD: " + initial);
        //System.out.println("TWIN BAORD: " + initial.twin());


        PQ.insert(sn);
        PQTwin.insert(snTwin);

        SearchNode tempSn = PQ.delMin();
        SearchNode tempSnTwin = PQTwin.delMin();
        // System.out.println("before while");

        // RUN while either initial or the twin is solved, depending on which is solved we know if solvable or not
        while (!tempSn.board.isGoal() && !tempSnTwin.board.isGoal()) {

            // Neighbours of board
            Iterable<Board> minSn = tempSn.board.neighbors();
            Iterable<Board> minSnTwin = tempSnTwin.board.neighbors();

            for (Board b : minSn) {
                // First element corner case check
                if (tempSn.prev != null) {
                    // Optimiztion for getting rid of repeated boards added
                    if (!b.equals(tempSn.prev.board)) {
                        // moves are the previous nodes moves + 1
                        SearchNode temp = new SearchNode(b, tempSn.moves + 1, tempSn);
                        PQ.insert(temp);

                    }
                } else {
                    SearchNode temp = new SearchNode(b, tempSn.moves + 1, tempSn);
                    PQ.insert(temp);
                }

            }
            tempSn = PQ.delMin();


            for (Board bTwin : minSnTwin) {
                if (tempSnTwin.prev != null) {
                    if (!bTwin.equals(tempSnTwin.prev.board)) {
                        SearchNode tempTwin = new SearchNode(bTwin, tempSnTwin.moves + 1, tempSnTwin);
                        PQTwin.insert(tempTwin);

                    }
                } else {
                    SearchNode tempTwin = new SearchNode(bTwin, tempSnTwin.moves + 1, tempSnTwin);
                    PQTwin.insert(tempTwin);
                }

            }
            tempSnTwin = PQTwin.delMin();

            // Checking if twin or initial was solved
            if (tempSn.board.isGoal()) {
                this.solvable = true;
            }
            if (tempSnTwin.board.isGoal()) {
                this.solvable = false;
            }
        }

        // Traverse through the nodes starting from the completed board until the "prev" is null
        // Push the nodes` board on the stack which then iterated through will be reversed for the solution
        sq.push(tempSn.board);
        while (tempSn.prev != null) {
            sq.push(tempSn.prev.board);
            tempSn = tempSn.prev;
        }

        sqTwin.push(tempSnTwin.board);
        while (tempSnTwin.prev != null) {
            sqTwin.push(tempSnTwin.prev.board);
            tempSnTwin = tempSnTwin.prev;
        }


    }

    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        return solvable;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        if (!isSolvable()) return -1;
        return sq.size() - 1;
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        if (!isSolvable()) return null;
        return sq;
    }


    // test client (see below)
    public static void main(String[] args) {

        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }


    }

}
