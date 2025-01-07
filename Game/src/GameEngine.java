import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;

public class GameEngine {
    private Snake snake;
    private Food food;
    private boolean isRunning;
    private final int WIDTH = 50;
    private final int HEIGHT = 30;
    private final int CELL_SIZE = 20;
    private final int BORDER_THICKNESS = 5;
    private GamePanel gamePanel;
    private int score = 0;

    public GameEngine() {
        snake = new Snake(WIDTH / 2, HEIGHT / 2);
        food = new Food(0, 0);
        spawnFoodSafely();
        isRunning = true;
        gamePanel = new GamePanel();
    }

    private void spawnFoodSafely() {
        do {
            food.spawn(WIDTH - 2, HEIGHT - 2);  // Adjust for border
            food.setPosition(food.getX() + 1, food.getY() + 1);  // Offset from border
        } while (isCollisionWithSnake(food.getX(), food.getY()));
    }

    private boolean isCollisionWithSnake(int x, int y) {
        for (int[] segment : snake.getBody()) {
            if (segment[0] == x && segment[1] == y) {
                return true;
            }
        }
        return false;
    }

    public void start() {
        JFrame frame = new JFrame("Snake Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(WIDTH * CELL_SIZE + BORDER_THICKNESS * 2, HEIGHT * CELL_SIZE + BORDER_THICKNESS * 2 + 30);
        frame.setResizable(false);
        frame.add(gamePanel);
        frame.setVisible(true);
        frame.setFocusable(true);
        frame.requestFocus();

        Timer timer = new Timer(200, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (isRunning) {
                    snake.move();
                    checkCollisions(frame);
                    gamePanel.repaint();
                }
            }
        });
        timer.start();

        frame.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent evt) {
                switch (evt.getKeyCode()) {
                    // VK mewakili ASCII dari Keyboard
                    case KeyEvent.VK_UP:
                    case KeyEvent.VK_W:
                        snake.setDirection("UP");
                        break;
                    case KeyEvent.VK_DOWN:
                    case KeyEvent.VK_S:
                        snake.setDirection("DOWN");
                        break;
                    case KeyEvent.VK_LEFT:
                    case KeyEvent.VK_A:
                        snake.setDirection("LEFT");
                        break;
                    case KeyEvent.VK_RIGHT:
                    case KeyEvent.VK_D:
                        snake.setDirection("RIGHT");
                        break;
                    case KeyEvent.VK_SPACE:
                        if (!isRunning) {
                            resetGame();
                        }
                        break;
                }
            }
        });
    }

    private void checkCollisions(JFrame frame) {
        int headX = snake.getBody().get(0)[0];
        int headY = snake.getBody().get(0)[1];

        // Nabrak Pager
        if (headX < 1 || headY < 1 || headX >= WIDTH - 1 || headY >= HEIGHT - 1) {
            gameOver(frame);
            return;
        }

        // Bunuh diri
        for (int i = 1; i < snake.getBody().size(); i++) {
            if (headX == snake.getBody().get(i)[0] && headY == snake.getBody().get(i)[1]) {
                gameOver(frame);
                return;
            }
        }

        // Nabrak Mkanan (Kemakan)
        if (snake.eatFood(food)) {
            snake.grow();
            score += 10;
            spawnFoodSafely();
        }
    }

    private void gameOver(JFrame frame) {
        isRunning = false;
        int choice = JOptionPane.showOptionDialog(frame,
                "Game Over! Score: " + score + "\nPress SPACE to restart or OK to exit",
                "Game Over",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                new String[]{"Exit", "Restart"},
                "Restart");
        
        if (choice == 1) {
            resetGame();
        } else {
            System.exit(0);
        }
    }

    private void resetGame() {
        snake = new Snake(WIDTH / 2, HEIGHT / 2);
        spawnFoodSafely();
        score = 0;
        isRunning = true;
    }

    // Class Internal khusus implementasi Panel Game
    private class GamePanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            drawGame(g);
        }

        private void drawGame(Graphics g) {
            // Siapkan background
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, getWidth(), getHeight());

            /* Gagal Digunakan -> Kode Percobaan
            // Gambar Border Aplikasi (Ga terlalu keliatan/Ga kepake)
            g.setColor(Color.BLUE);
            for (int x = 0; x < WIDTH; x++) {
                g.fillRect(x * CELL_SIZE + BORDER_THICKNESS, 0, CELL_SIZE, BORDER_THICKNESS); // Top
                g.fillRect(x * CELL_SIZE + BORDER_THICKNESS, (HEIGHT-1) * CELL_SIZE + BORDER_THICKNESS, CELL_SIZE, BORDER_THICKNESS); // Bottom
            }
            for (int y = 0; y < HEIGHT; y++) {
                g.fillRect(0, y * CELL_SIZE + BORDER_THICKNESS, BORDER_THICKNESS, CELL_SIZE); // Left
                g.fillRect((WIDTH-1) * CELL_SIZE + BORDER_THICKNESS, y * CELL_SIZE + BORDER_THICKNESS, BORDER_THICKNESS, CELL_SIZE); // Right
            }
            // Gambar Grid Kotak-kotak
            g.setColor(new Color(20, 20, 20));
            for (int x = 0; x < WIDTH; x++) {
                for (int y = 0; y < HEIGHT; y++) {
                    g.drawRect(x * CELL_SIZE + BORDER_THICKNESS, 
                    y * CELL_SIZE + BORDER_THICKNESS, 
                    CELL_SIZE, CELL_SIZE);
                }
            }
            */

            // Aturan Makanna
            g.setColor(Color.RED);
            g.fillRect(food.getX() * CELL_SIZE + BORDER_THICKNESS,
                      food.getY() * CELL_SIZE + BORDER_THICKNESS,
                      CELL_SIZE, CELL_SIZE);

            // Aturan Ularnya
            g.setColor(Color.GREEN);
            for (int[] segment : snake.getBody()) {
                g.fillRect(segment[0] * CELL_SIZE + BORDER_THICKNESS,
                          segment[1] * CELL_SIZE + BORDER_THICKNESS,
                          CELL_SIZE, CELL_SIZE);
            }

            // Aturan penulisan score
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 16));
            g.drawString("Score: " + score, 10, getHeight() - 10);
        }
    }
}