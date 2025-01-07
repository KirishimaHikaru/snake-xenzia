import java.util.Random;

public class Food extends GameObject {
    public Food(int x, int y) {
        super(x, y);
    }

    public void spawn(int width, int height) {
        Random rand = new Random();
        x = rand.nextInt(width);
        y = rand.nextInt(height);
    }
}