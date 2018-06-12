public class Box extends Sprite{

    int level;

    public  Box (int x, int y, int level) {
        super(x,y);
        width = 20 * level /2;
        height = 20 * level /2;
        this.level = level;
    }
}
