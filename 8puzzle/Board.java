import edu.princeton.cs.algs4.In;

import java.util.Arrays;
import java.util.Iterator;

public class Board {

    private final int[][] board;
    private final int N;
    private final int size;

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        N = tiles.length;
        size = N * N;
        board = new int[N][N];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                board[i][j] = tiles[i][j];
            }
        }
    }

    // string representation of this board
    public String toString() {
        String tempString = N + "\n";
        for (int i = 0; i < N; i++) {
            tempString += String.join(tempString, Arrays.toString(board[i]) + "\n");
        }

        return tempString;
    }

    // board dimension n
    public int dimension() {
        return N;
    }

    private int goalTableConvert(int i, int j) {
        if ((i + 1) * (j + 1) == (size)) {
            return 0;
        } else return (i * N) + j + 1;
    }

    private int IGoal(int numb) {
        if (numb == 0) {
            return N - 1;
        } else {
            int temp = numb / N;
            if (numb % N > 0) {
                return temp;
            } else return temp - 1;
        }

    }

    private int JGoal(int numb) {
        if (numb == 0) {
            return N - 1;
        } else {
            if (numb % N > 0) {
                return (numb % N) - 1;
            } else return N - 1;
        }

    }

    // number of tiles out of place
    public int hamming() {
        int counter = 0;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                // System.out.println("board i,j " + i + " " + j + " " + board[i][j]);
                // System.out.println("goal i,j " + goalTableConvert(i, j));
                if (board[i][j] != goalTableConvert(i, j)) {
                    counter++;
                }
            }
        }
        return counter;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        int sum = 0;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {

                sum += Math.abs((i - IGoal(board[i][j])) + (j - JGoal(board[i][j])));
            }
        }
        return sum;
    }

    // is this board the goal board?
    public boolean isGoal() {
        return false;
    }

    // does this board equal y?
    public boolean equals(Object y) {
        return false;
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        return new Iterable<Board>() {
            public Iterator<Board> iterator() {
                return null;
            }
        };
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        int[][] tiles = new int[0][0];
        return new Board(tiles);
    }

    // unit testing (not graded)
    public static void main(String[] args) {
        In In = new In(args[0]);
        int N = In.readInt();
        int[][] tiles = new int[N][N];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                tiles[i][j] = In.readInt();

            }
        }
        Board testBoard = new Board(tiles);
        System.out.println(testBoard.toString());
        // System.out.println(testBoard.hamming());
        System.out.println("MANHATTAN " + testBoard.manhattan());
        // System.out.println("IGOAL " + testBoard.IGoal(0));
        // System.out.println("JGOAL " + testBoard.JGoal(0));


    }

}
