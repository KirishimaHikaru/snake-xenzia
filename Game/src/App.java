import javax.swing.SwingUtilities;

public class App {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GameEngine game = new GameEngine();
            game.start();
        });
    }
}