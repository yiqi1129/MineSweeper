import java.util.Scanner;
import java.util.Random;

public class Main {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);

        while (true) {
            Grid grid = null;
            int choice = 0;

            while (true) {
                System.out.println("Choose mode:");
                System.out.println("1. Beginner");
                System.out.println("2. Intermediate");
                System.out.println("3. Expert");
                System.out.println("4. Custom");
                System.out.println("5. Random");
                System.out.println("6. Quit");
                String input = in.nextLine().trim();
                try {
                    choice = Integer.parseInt(input);
                    if (choice >= 1 && choice <= 6) break;
                    System.out.println("Enter 1-6.\n");
                } catch (NumberFormatException e) {
                    System.out.println("human?\n");
                }
            }

            if (choice == 6) {
                break;
            }

            if (choice == 1) {
                grid = new Beginner();
            }
            else if (choice == 2) {
                grid = new Intermediate();
            }
            else if (choice == 3) {
                grid = new Expert();
            }
            else if (choice == 4) {
                int w, h, m;
                while (true) {
                    System.out.print("width: ");
                    try {
                        w = Integer.parseInt(in.nextLine());
                        if (w >= 1) break;
                    } catch (NumberFormatException e) {
                        System.out.println("Trump is that u?");
                    }
                }
                while (true) {
                    System.out.print("height: ");
                    try {
                        h = Integer.parseInt(in.nextLine());
                        if (h >= 1) break;
                    } catch (NumberFormatException e) {
                        System.out.println("Trump is that u?");
                    }
                }

                while (true) {
                    System.out.print("mines: ");
                    try {
                        m = Integer.parseInt(in.nextLine());
                        if (m < 0) continue;
                        break;
                    } catch (NumberFormatException e) {
                        System.out.println("Trump is that u?");
                    }
                }
                m = Math.min(m, w * h - 1);
                grid = new Grid(w, h, m);
            }
            else if (choice == 5) {
                Random rand = new Random();
                int w = rand.nextInt(6) + 5;
                int h = rand.nextInt(6) + 5;
                int m = rand.nextInt((w * h) / 4) + 1;
                System.out.println("Random board: " + w + "x" + h + ", Mines: " + m);
                grid = new Grid(w, h, m);
            }

            while (true) {
                System.out.println("Bombs left: " + grid.bombsLeft);
                System.out.println(grid);
                System.out.print("Enter x col y row col F: ");
                String line = in.nextLine().trim();
                String[] parts = line.split(" ");
                if (parts.length < 2) {
                    System.out.println("Enter: x y or x y F");
                    continue;
                }
                int c;
                int r;
                try {
                    c = Integer.parseInt(parts[0]) - 1;
                    r = Integer.parseInt(parts[1]) - 1;
                } catch (NumberFormatException e) {
                    System.out.println("?");
                    continue;
                }
                if (parts.length == 3 && parts[2].equalsIgnoreCase("F")) {
                    grid.toggleFlag(r, c);
                } else {
                    boolean alive = grid.click(r, c);
                    if (!alive) {
                        System.out.println("nerd");
                        grid.revealAll();
                        System.out.println(grid);
                        break;
                    }
                }
                if (grid.checkWin()) {
                    System.out.println("Guess whose NOT invited to the party?  YOU!!!");
                    System.out.println(grid);
                    break;
                }
            }
        }
    }
}