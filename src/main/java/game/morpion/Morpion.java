/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package game.morpion;

import java.util.Scanner;

import game.morpion.board.PlayableBoard;

/**
 *
 * @author theo_
 */

public class Morpion {
    public static void main(String[] args) {
        boolean continuePlaying = true;
        Scanner input = new Scanner(System.in);
        while (continuePlaying) {
            PlayableBoard game = new PlayableBoard();
            while (game.isNotVictory()) {
                game.playTurn();
            }
            continuePlaying = game.continuePlaying(input);
        }
    }
}
