import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class SnakeGameCLI {
    private static final int WIDTH = 20;
    private static final int HEIGHT = 10;
    private static final char WALL = '#';
    private static final char SNAKE = 'o';
    private static final char FOOD = '*';
    private static final char EMPTY = ' ';

    private ArrayList<int[]> snake;
    private int[] food;
    private String direction;
    private boolean isRunning;
    private int score;

    public SnakeGameCLI() {
        snake = new ArrayList<>();
        snake.add(new int[]{HEIGHT / 2, WIDTH / 2}); // Snake starts at center
        direction = "RIGHT";
        food = new int[2];
        spawnFood();
        isRunning = true;
        score = 0;
    }

    public void start() {
        Scanner scanner = new Scanner(System.in);
        while (isRunning) {
            printBoard();
            System.out.print("Move (W/A/S/D): ");
            String input = scanner.nextLine().toUpperCase();
            updateDirection(input);
            moveSnake();
            checkCollisions();
        }
        System.out.println("Game Over! Your score: " + score);
        scanner.close();
    }

    private void printBoard() {
        for (int i = 0; i < HEIGHT + 2; i++) {
            for (int j = 0; j < WIDTH + 2; j++) {
                if (i == 0 || i == HEIGHT + 1 || j == 0 || j == WIDTH + 1) {
                    System.out.print(WALL);
                } else if (isSnake(i - 1, j - 1)) {
                    System.out.print(SNAKE);
                } else if (food[0] == i - 1 && food[1] == j - 1) {
                    System.out.print(FOOD);
                } else {
                    System.out.print(EMPTY);
                }
            }
            System.out.println();
        }
    }

    private boolean isSnake(int x, int y) {
        for (int[] segment : snake) {
            if (segment[0] == x && segment[1] == y) {
                return true;
            }
        }
        return false;
    }

    private void updateDirection(String input) {
        switch (input) {
            case "W":
                if (!direction.equals("DOWN")) direction = "UP";
                break;
            case "A":
                if (!direction.equals("RIGHT")) direction = "LEFT";
                break;
            case "S":
                if (!direction.equals("UP")) direction = "DOWN";
                break;
            case "D":
                if (!direction.equals("LEFT")) direction = "RIGHT";
                break;
        }
    }

    private void moveSnake() {
        int[] head = snake.get(0);
        int newX = head[0], newY = head[1];

        switch (direction) {
            case "UP":
                newX--;
                break;
            case "DOWN":
                newX++;
                break;
            case "LEFT":
                newY--;
                break;
            case "RIGHT":
                newY++;
                break;
        }

        snake.add(0, new int[]{newX, newY});

        if (newX == food[0] && newY == food[1]) {
            score += 10;
            spawnFood();
        } else {
            snake.remove(snake.size() - 1);
        }
    }

    private void checkCollisions() {
        int[] head = snake.get(0);

        // Check wall collision
        if (head[0] < 0 || head[0] >= HEIGHT || head[1] < 0 || head[1] >= WIDTH) {
            isRunning = false;
        }

        // Check self collision
        for (int i = 1; i < snake.size(); i++) {
            if (head[0] == snake.get(i)[0] && head[1] == snake.get(i)[1]) {
                isRunning = false;
            }
        }
    }

    private void spawnFood() {
        Random rand = new Random();
        do {
            food[0] = rand.nextInt(HEIGHT);
            food[1] = rand.nextInt(WIDTH);
        } while (isSnake(food[0], food[1]));
    }

    public static void main(String[] args) {
        SnakeGameCLI game = new SnakeGameCLI();
        game.start();
    }
}
