import java.util.ArrayList;

public class Snake extends GameObject {
    private ArrayList<int[]> body = new ArrayList<>();
    private String direction;

    public Snake(int x, int y) {
        super(x, y);
        this.body.add(new int[]{x, y});
        this.direction = "RIGHT";
    }

    public ArrayList<int[]> getBody() {
        return this.body;
    }

    public void setDirection(String newDirection) {
        if (!this.direction.equals(newDirection)) {
            if (!((this.direction.equals("UP") && newDirection.equals("DOWN")) ||
                  (this.direction .equals("DOWN") && newDirection.equals("UP")) ||
                  (this.direction.equals("LEFT") && newDirection.equals("RIGHT")) ||
                  (this.direction.equals("RIGHT") && newDirection.equals("LEFT")))) {
                this.direction = newDirection;
            }
        }
    }

    public void move() {
        int headX = this.body.get(0)[0];
        int headY = this.body.get(0)[1];
        switch (this.direction) {
            case "UP":
                headY--;
                break;
            case "DOWN":
                headY++;
                break;
            case "LEFT":
                headX--;
                break;
            case "RIGHT":
                headX++;
                break;
        }
        this.body.add(0, new int[]{headX, headY});
        this.body.remove(this.body.size() - 1);
    }

    public void grow() {
        int[] tail = this.body.get(this.body.size() - 1);
        this.body.add(new int[]{tail[0], tail[1]});
    }

    public boolean checkCollision(int width, int height) {
        int headX = this.body.get(0)[0];
        int headY = this.body.get(0)[1];
        if (headX < 0 || headY < 0 || headX >= width || headY >= height) {
            return true;
        }
        for (int i = 1; i < this.body.size(); i++) {
            if (headX == this.body.get(i)[0] && headY == this.body.get(i)[1]) {
                return true;
            }
        }
        return false;
    }

    public boolean eatFood(Food food) {
        return this.body.get(0)[0] == food.getX() && this.body.get(0)[1] == food.getY();
    }
}