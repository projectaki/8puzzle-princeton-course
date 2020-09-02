import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

public class Solver {
    private int moves;
    private final Stack<Board> sq;
    private int tempCrasher = 0;

    private static class SearchNode implements Comparable<SearchNode> {
        private final Board board;
        private final int moves;
        private final SearchNode prev;
        private final int manhattan;


        private SearchNode(Board b, int moves, SearchNode prev) {
            this.board = b;
            this.moves = moves;
            this.prev = prev;
            this.manhattan = b.manhattan();
        }


        public int compareTo(SearchNode that) {
            return Integer.compare(this.manhattan + this.moves, that.manhattan + that.moves);
        }
    }

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null) throw new IllegalArgumentException();
        sq = new Stack<>();
        MinPQ<SearchNode> PQ = new MinPQ<>();

        SearchNode sn = new SearchNode(initial, sq.size(), null);
        PQ.insert(sn);

        SearchNode tempSn = PQ.delMin();
        // System.out.println("before while");

        while (!tempSn.board.isGoal()) {
            // System.out.println("ENtered while");

            Iterable<Board> minSn = tempSn.board.neighbors();

            for (Board b : minSn) {
                // System.out.println("Entered for");
                if (!b.equals(tempSn.board)) {
                    SearchNode temp = new SearchNode(b, sq.size(), tempSn);


                    PQ.insert(temp);
                    // if (b.isGoal()) break;
                }
            }
            tempSn = PQ.delMin();
            tempCrasher++;
            System.out.println(tempCrasher);
            if (tempCrasher > 100) break;
        }

        sq.push(tempSn.board);
        while (tempSn.prev != null) {
            sq.push(tempSn.prev.board);
            tempSn = tempSn.prev;
        }


    }

    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        return true;
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
