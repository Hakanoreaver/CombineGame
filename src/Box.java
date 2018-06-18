public class Box extends Sprite{

    int level;
    boolean spawning;
    int speed = 10, count = 0, widthActual, heightActual;
    boolean up;
    int countt = 0;

    public  Box (int x, int y, int level, boolean spawning) {
        super(x,y);

        width = 30 * level / 4;
        widthActual = width;
        height = 30 * level / 4;
        heightActual = height;
        this.level = level;
        this.spawning = spawning;
    }

    public void spawn() {
        if (count <10) {
            x += speed;
        }
        else  {
            x += speed;
            speed--;
        }
        count++;
        if (speed == 0) spawning = false;
    }

    public void animate() {
        if (up && width < heightActual + 4) {
            if (countt%2 == 0) {
                width++;
                height++;
            }
            if (countt == 3) {
                countt = 0;
                x--;
                y--;
            }
            else countt++;

        }
        else up = false;

        if(!up && width > heightActual - 4) {
            if (countt%2 == 0) {
                width--;
                height--;
            }
            if (countt == 3) {
                countt = 0;
                x++;
                y++;
            }
            else countt++;
        }
        else up = true;
    }
}
