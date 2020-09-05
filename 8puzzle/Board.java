import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Stack;

import java.util.Arrays;


public class Board {

    private final int[][] board;
    private final int N;
    private final int size;
    //THe position of 0 tile
    private final int blankI;
    private final int blankJ;


    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        N = tiles.length;
        size = N * N;
        board = new int[N][N];

        int tempI = 0;
        int tempJ = 0;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                board[i][j] = tiles[i][j];
                if (tiles[i][j] == 0) {
                    tempI = i;
                    tempJ = j;
                }
            }
        }
        blankI = tempI;
        blankJ = tempJ;

    }

    // string representation of this board
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(N).append("\n");
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                s.append(String.format("%2d ", board[i][j]));
            }
            s.append("\n");
        }
        return s.toString();
    }

    // board dimension n
    public int dimension() {
        return N;
    }

    // Given i, and j returns the value that should be standing at i, and j
    private int goalTableConvert(int i, int j) {
        return (i * N) + j + 1;
    }

    // Given a number, returns the i where the number has to be
    private int IGoal(int numb) {

        if (numb != 0) {
            int temp = numb / N;
            if (numb % N > 0) {
                return temp;
            } else return temp - 1;
        } else return -1;


    }

    // Given a number, returns the j where the number has to be
    private int JGoal(int numb) {

        if (numb != 0) {
            if (numb % N > 0) {
                return (numb % N) - 1;
            } else return N - 1;
        } else return -1;


    }

    // number of tiles out of place
    public int hamming() {
        int counter = 0;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                // System.out.println("board i,j " + i + " " + j + " " + board[i][j]);
                // System.out.println("goal i,j " + goalTableConvert(i, j));
                if (board[i][j] != 0) {
                    if (board[i][j] != goalTableConvert(i, j)) {
                        counter++;
                    }
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
                if (board[i][j] != 0) {
                    sum += Math.abs((i - IGoal(board[i][j]))) + Math.abs(j - JGoal(board[i][j]));
                }
            }
        }
        return sum;
    }

    // is this board the goal board?
    public boolean isGoal() {
        return hamming() == 0;
    }

    // does this board equal y?
    public boolean equals(Object y) {
        if (y == this) return true;
        if (y == null) return false;
        if (y.getClass() != this.getClass()) return false;
        Board that = (Board) y;
        return (this.N == that.N) && (Arrays.deepEquals(this.board, that.board))
                && (this.size == that.size);
    }

    // Given i, and j validate if those are within the arrays bounds
    private boolean validateNeighbor(int i, int j) {
        return i >= 0 && j >= 0 && i <= N - 1 && j <= N - 1;
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        Stack<Board> stackOfBoards = new Stack<>();
        //new instance of a board (crucial for neighbour of neighbour)
        Board b = new Board(this.board);

        //top
        //If it has a top neighbour then add neighbour to stack
        if (validateNeighbor(b.blankI - 1, b.blankJ)) {
            Board board1 = new Board(this.board);
            //System.out.println("topVal: " + board1);
            int tempHolder = board1.board[b.blankI - 1][b.blankJ];
            board1.board[b.blankI - 1][b.blankJ] = 0;
            board1.board[b.blankI][b.blankJ] = tempHolder;
            stackOfBoards.push(board1);
        }
        //right
        //System.out.println("rightBeforeVal: " + (b.blankI) + "," + (b.blankJ + 1));
        if (validateNeighbor(b.blankI, b.blankJ + 1)) {
            Board board2 = new Board(this.board);
            // System.out.println("rightVal: " + board2);
            int tempHolder = board2.board[b.blankI][b.blankJ + 1];
            board2.board[b.blankI][b.blankJ + 1] = 0;
            board2.board[b.blankI][b.blankJ] = tempHolder;
            stackOfBoards.push(board2);
        }
        //bottom
        //System.out.println("botBeforeVal: " + (b.blankI + 1) + "," + b.blankJ);
        if (validateNeighbor(b.blankI + 1, b.blankJ)) {
            Board board3 = new Board(this.board);
            //System.out.println("botVal: " + board3);
            int tempHolder = board3.board[b.blankI + 1][b.blankJ];
            board3.board[b.blankI + 1][b.blankJ] = 0;
            board3.board[b.blankI][b.blankJ] = tempHolder;
            stackOfBoards.push(board3);
        }
        //left
        //System.out.println("leftBeforeVal: " + (b.blankI) + "," + (b.blankJ - 1));
        if (validateNeighbor(b.blankI, b.blankJ - 1)) {
            Board board4 = new Board(this.board);
            //System.out.println("leftVal: " + board4);
            int tempHolder = board4.board[b.blankI][b.blankJ - 1];
            board4.board[b.blankI][b.blankJ - 1] = 0;
            board4.board[b.blankI][b.blankJ] = tempHolder;
            stackOfBoards.push(board4);
        }

        return stackOfBoards;
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        Board b = new Board(board);
        int iThatsNot0 = 0;
        int jThatsNot0 = 0;
        int secondI = 0;
        int secondJ = 0;

        for (int i = 0; i < N; i++) {
            if (i != blankI) iThatsNot0 = i;
            if (i != blankJ) jThatsNot0 = i;
        }
        for (int i = 0; i < N; i++) {
            if (i != blankI && i != iThatsNot0) secondI = i;
            if (i != blankJ && i != jThatsNot0) secondJ = i;
        }
        int tempVal = b.board[iThatsNot0][jThatsNot0];
        b.board[iThatsNot0][jThatsNot0] = b.board[secondI][secondJ];
        b.board[secondI][secondJ] = tempVal;

        return b;
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
        System.out.println();
        //System.out.println("MANHATTNA " + testBoard.manhattan());

    }

}
