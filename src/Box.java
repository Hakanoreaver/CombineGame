public class Box extends Sprite{

    int level;

    public  Box (int x, int y, int level) {
        super(x,y);

        width = 30 * level / 4;
        height = 30 * level / 4;
        this.level = level;
    }
}
