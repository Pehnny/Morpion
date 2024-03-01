/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package game.morpion;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

/**
 *
 * @author theo_
 */

enum Marks {
    EMPTY,
    CIRCLE,
    CROSS
}

enum Players {
    PLAYER, 
    COMPUTER
}

enum Triplets {
    ROW1,
    ROW2,
    ROW3,
    COLUMN1,
    COLUMN2,
    COLUMN3,
    DIAGONAL,
    ANTIDIAGONAL
}

interface Transposable<T> {
    public T[][] transpose();
}

class Board implements Transposable<Marks> {
    private final int size;
    private final Marks[][] board;

    public Board() {
        this(3);
    }

    public Board(int size) {
        this.size = size;
        this.board = new Marks[size][size];
        for (int row = 0; row < size; row++) {
            for (int column = 0; column < size; column++) {
                this.board[row][column] = Marks.EMPTY;
            }
        }
    }

    @Override
    public Marks[][] transpose() {
        Marks[][] transposed = new Marks[this.size][this.size];
        for (int i = 0; i < this.size; i++) {
            for (int j = 0; j < this.size; j++) {
                transposed[i][j] = this.board[j][i];
            }
        }
        return transposed;
    }

    public int getSize() {
        return this.size;
    }

    public boolean isEmptyCell(int row, int column) {
        if (0 <= row && row < this.size) {
            if (0 <= column && column < this.size) {
                return this.board[row][column] == Marks.EMPTY;
            }
        }
        return false;
    }

    public void draw(int row, int column, Marks mark) {
        if (0 <= row && row < this.size) {
            if (0 <= column && column < this.size) {
                this.board[row][column] = mark;
            }
        }
    }

    public Marks[] getTriplet(Triplets triplet) {
        Marks[][] transposedBoard = this.transpose();
        Marks[] diagonal = new Marks[this.size];
        Marks[] antidiagonal = new Marks[this.size];
        for (int i = 0; i < this.size; i++) {
            diagonal[i] = this.board[i][i];
            antidiagonal[i] = this.board[i][this.size-(i+1)];
        }
        return switch (triplet) {
            case ROW1 -> {
                yield this.board[0];
            }
            case ROW2 -> {
                yield this.board[1];
            }
            case ROW3 -> {
                yield this.board[2];
            }
            case COLUMN1 -> {
                yield transposedBoard[0];
            }
            case COLUMN2 -> {
                yield transposedBoard[1];
            }
            case COLUMN3 -> {
                yield transposedBoard[2];
            }
            case DIAGONAL -> {
                yield diagonal;
            }
            case ANTIDIAGONAL -> {
                yield antidiagonal;
            }
        };
    }

    public void displayBoard() {
        String symbol;
        for (int row = 0; row < this.size; row++) {
            for (int column = 0; column < this.size; column++) {
                symbol = switch (this.board[row][column]) {
                    case Marks.EMPTY -> "_";
                    case Marks.CIRCLE -> "O";
                    case Marks.CROSS -> "X";
                };
                System.out.print("| " + symbol + " |");
            }
            System.out.print("\n");
        }
    }
}

class PlayableBoard {
    private final Board board = new Board();
    private final Random seed = new Random();
    private Players playerTurn = Players.PLAYER;
    
    public void playTurn() {
        int row;
        int column;
        if (this.playerTurn == Players.PLAYER) {
            this.board.displayBoard();
            Scanner input = new Scanner(System.in);
            System.out.println("Choose a row (0, 1 or 2)");
            row = input.nextInt();
            System.out.println("Choose a column (0, 1 or 2)");
            column = input.nextInt();
        }
        else {
            row = this.seed.nextInt(this.board.getSize());
            column = this.seed.nextInt(this.board.getSize());
        }
        this.drawMark(row, column);
    }

    public void drawMark(int row, int column) {
        if (this.board.isEmptyCell(row, column)) {
            switch (this.playerTurn) {
                case PLAYER -> this.board.draw(row, column, Marks.CIRCLE);
                case COMPUTER -> this.board.draw(row, column, Marks.CROSS);
            }
            this.endTurn();
        }
    }

    public void endTurn() {
        if (this.isNotVictory()) {
            this.playerTurn = switch (this.playerTurn) {
                case PLAYER -> Players.COMPUTER;
                case COMPUTER -> Players.PLAYER;
            };
        }
        else {
            this.board.displayBoard();
            System.out.println(this.playerTurn + " won !");
        }
    }

    public boolean isNotVictory() {
        for (Triplets triplet : Triplets.values()) {
            List<Marks> solution = Arrays.asList(this.board.getTriplet(triplet));
            boolean noneEmpty = solution.stream().noneMatch(mark -> mark == Marks.EMPTY);
            boolean allSame = solution.stream().distinct().count() == 1;
            if (noneEmpty && allSame) {
                return false;
            }
        }
        return true;
    }

    public boolean continuePlaying(String input) {
        Scanner userInput = new Scanner(System.in);
        return switch (input) {
            case "yes" -> {
                yield true;
            }
            case "no" -> {
                yield false;
            }
            default -> {
                System.out.println("Wrong input ! Do you want to keep playing ? yes or no");
                yield this.continuePlaying(userInput.next().toLowerCase());
            }
        };
    }
}

public class Morpion {
    public static void main(String[] args) {
        boolean continuePlaying = true;
        while (continuePlaying) {
            Scanner input = new Scanner(System.in);
            PlayableBoard game = new PlayableBoard();
            while (game.isNotVictory()) {
                game.playTurn();
            }
            System.out.println("Do you want to play again ? yer or no");
            continuePlaying = game.continuePlaying(input.next().toLowerCase());
        }       
    }
}
