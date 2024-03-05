package game.morpion.board;

import game.morpion.board.properties.Transposable;
import game.morpion.enums.Marks;
import game.morpion.enums.Triplets;



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
            case FIRST_ROW -> {
                yield this.board[0];
            }
            case SECOND_ROW -> {
                yield this.board[1];
            }
            case THIRD_ROW -> {
                yield this.board[2];
            }
            case FIRST_COLUMN -> {
                yield transposedBoard[0];
            }
            case SECOND_COLUMN -> {
                yield transposedBoard[1];
            }
            case THIRD_COLUMN -> {
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
