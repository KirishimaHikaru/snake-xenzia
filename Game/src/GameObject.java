public class GameObject {
    protected int x;
    protected int y;


    // Constructor Object (Ular dan Makanan)
    public GameObject(int x, int y) {
        this.x = x;
        this.y = y;
    }

    // Getter and Setter
    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }
}
