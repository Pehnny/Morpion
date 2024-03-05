package game.morpion.utils;

import java.util.Scanner;

public class HelperInput {
    public static int getInt(Scanner scanner) {
        while (!scanner.hasNextInt()) {
            System.out.println("Wrong input. Please chose another number.");
            scanner.next();
        }
        return scanner.nextInt();
    }
}
