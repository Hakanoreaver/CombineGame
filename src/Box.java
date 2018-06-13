public class Box extends Sprite{

    int level;
    boolean spawning;
    int speed = 10, count = 0;

    public  Box (int x, int y, int level, boolean spawning) {
        super(x,y);

        width = 30 * level / 4;
        height = 30 * level / 4;
        this.level = level;
        this.spawning = spawning;
    }

    public void spawn() {
        if (count <10) {
            x += speed;
            System.out.println("full speed " + count);
        }
        else  {
            x += speed;
            speed--;
            System.out.println("Speed slowing");
        }
        count++;
        if (speed == 0) spawning = false;
    }
}
