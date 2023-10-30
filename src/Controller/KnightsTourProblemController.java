/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

/**
 *
 * @author ndila
 */
import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class KnightsTourProblemController {
    private int size;
    private int[][] board;
    public int[][] moveSquare;
    private int moveCount;
    private List<Point> solution;
    public String enteredName;

    // Possible moves for the knight
    private final int[] moveX = {2, 1, -1, -2, -2, -1, 1, 2};
    private final int[] moveY = {1, 2, 2, 1, -1, -2, -2, -1};
    private Set<Point> visitedSquares;

    public KnightsTourProblemController(int size) {
        this.size = size;
        this.board = new int[size][size];
        this.moveSquare = new int[size][size];
        this.solution = new ArrayList<>();
        this.visitedSquares = new HashSet<>();
        this.enteredName = enteredName;
    }

    // Reset All for new game
    public void reset() {
        board = new int[size][size];
        moveSquare = new int[size][size];
        moveCount = 0;
        solution.clear();
        visitedSquares.clear();
    }

    public boolean solveKnightTour(int startX, int startY) {
        reset();
        int x = startX;
        int y = startY;

        board[x][y] = 1;
        solution.add(new Point(x, y));
        moveCount = 1;

        if (solveKnightTourUtil(x, y, 2)) {
            return true;
        } else {
            return false;
        }
    }

    private boolean solveKnightTourUtil(int x, int y, int step) {
        if (step > size * size) {
            return true;
        }

        List<Point> nextMoves = getNextMoves(x, y);

        if (nextMoves.isEmpty()) {
            return false;
        }

        nextMoves.sort((a, b) -> {
            int aCount = getNextMovesCount(a.x, a.y);
            int bCount = getNextMovesCount(b.x, b.y);
            return aCount - bCount;
        });

        for (Point nextMove : nextMoves) {
            int nextX = nextMove.x;
            int nextY = nextMove.y;

            board[nextX][nextY] = step;
            solution.add(new Point(nextX, nextY));

            if (solveKnightTourUtil(nextX, nextY, step + 1)) {
                return true;
            }

            board[nextX][nextY] = 0;
            solution.remove(solution.size() - 1);
        }
        return false;
    }

    private List<Point> getNextMoves(int x, int y) {
        List<Point> moves = new ArrayList<>();

        for (int i = 0; i < 8; i++) {
            int nextX = x + moveX[i];
            int nextY = y + moveY[i];

            if (isValidMove(nextX, nextY) && board[nextX][nextY] == 0) {
                moves.add(new Point(nextX, nextY));
            }
        }

        return moves;
    }

    private int getNextMovesCount(int x, int y) {
        int count = 0;
        for (int i = 0; i < 8; i++) {
            int nextX = x + moveX[i];
            int nextY = y + moveY[i];

            if (isValidMove(nextX, nextY) && board[nextX][nextY] == 0) {
                count++;
            }
        }
        return count;
    }

    public boolean isValidMove(int x, int y) {
        return x >= 0 && x < size && y >= 0 && y < size;
    }

    public int[][] getBoard() {
        return board;
    }

    public List<Point> getSolution() {
        return solution;
    }

    public int getSize() {
        return size;
    }

    public int getMoveCount() {
        return moveCount;
    }
    
    public boolean makeMove(int x, int y) {
        if (isValidMove(x, y) && isKnightMove(x, y) && !visitedSquares.contains(new Point(x, y))) {
        // Clear the previous square if not the initial move
        if (moveCount > 0) {
            Point previousMove = solution.get(moveCount - 1);
            int prevX = (int) previousMove.getX();
            int prevY = (int) previousMove.getY();
            board[prevX][prevY] = 0;
        }
        board[x][y] = 1 + moveCount;
        moveSquare[x][y] = moveCount + 1; // Store the move count in the matrix
        moveCount++;
        solution.add(new Point(x, y));
        System.out.println("SolutionMy: "+solution);
        
        // Mark the current square as visited
        visitedSquares.add(new Point(x, y));
        hasNoEmptyMoveSquares(x, y);
        
        // print moves
        getMetrixAnswer();

        return true; // move successful
    }
    return false; // invalid or already visited square
    }
    
    private boolean isKnightMove(int x, int y) {
        if (moveCount == 0) {
            return true; // Initial move is always valid
        }

        Point previousMove = solution.get(moveCount - 1);
        int prevX = (int) previousMove.getX();
        int prevY = (int) previousMove.getY();

        int dx = Math.abs(x - prevX);
        int dy = Math.abs(y - prevY);

        return (dx == 2 && dy == 1) || (dx == 1 && dy == 2);
    }
    
    public boolean hasNoEmptyMoveSquares(int x, int y) {
        for (int i = 0; i < 8; i++) {
            int nextX = x + moveX[i];
            int nextY = y + moveY[i];

            if (isValidMove(nextX, nextY) && board[nextX][nextY] == 0 && !visitedSquares.contains(new Point(nextX, nextY))) {
                System.out.println("Have");
                return false; // at least one empty square to move
            }
        }
        System.out.println("Finsihed");
        return true; // No empty squares to move to
    }

    public List<Point> calculatePossibleMoves(int x, int y) {
        List<Point> possibleMoves = new ArrayList<>();
        // Define the possible relative positions for a knight's move
        for (int i = 0; i < 8; i++) {
            int nextX = x + moveX[i];
            int nextY = y + moveY[i];

            // Check if the move is within the chessboard bounds and not visited
            if (isValidMove(nextX, nextY) && board[nextX][nextY] == 0 && !visitedSquares.contains(new Point(nextX, nextY))) {
                possibleMoves.add(new Point(nextX, nextY));
            }
        }

        return possibleMoves;
    }
    
    public String getMetrixAnswer(){
        StringBuilder answerBuilder = new StringBuilder();
        
        for(int i = 0; i < size; i++){
            for(int j = 0; j < size; j++){
                answerBuilder.append(moveSquare[i][j] + "\t");
            }
            
            if(i < size - 1){
                answerBuilder.append("\n");
            }
        }
        System.out.println(answerBuilder.toString());
        return answerBuilder.toString();
    }
}




