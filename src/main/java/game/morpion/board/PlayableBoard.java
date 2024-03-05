package game.morpion.board;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import game.morpion.enums.Marks;
import game.morpion.enums.Players;
import game.morpion.enums.Triplets;
import game.morpion.utils.HelperInput;



public class PlayableBoard {
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
            row = HelperInput.getInt(input);
            System.out.println("Choose a column (0, 1 or 2)");
            column = HelperInput.getInt(input);
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

    public boolean continuePlaying(Scanner input) {
        System.out.println("Do you want to play again ? yer or no");
        String userInput = input.next().toLowerCase();
        return switch (userInput) {
            case "yes" -> {
                yield true;
            }
            case "no" -> {
                yield false;
            }
            default -> {
                System.out.println("Wrong input ! Do you want to keep playing ? yes or no");
                yield this.continuePlaying(input);
            }
        };
    }
}
