import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class Board extends JPanel implements ActionListener, MouseListener, MouseMotionListener {
    private Timer timer;
    private boolean ingame;
    private final int B_WIDTH = 1920;
    private final int B_HEIGHT = 1080;
    private final int DELAY = 16;
    ArrayList<Box> boxes;
    int offsetX, offsetY;
    Box dragging;
    boolean drag = false;
    int count = 0, spawnPeriod = 120;

    public Board() {
        initBoard();
    }


    private void initBoard() {
        setFocusable(true);
        setBackground(Color.BLACK);
        ingame = true;
        setPreferredSize(new Dimension(B_WIDTH, B_HEIGHT));

        timer = new Timer(DELAY, this);
        timer.start();
        addMouseListener(this);
        addMouseMotionListener(this);

        initBoxes();
        initMenus();
    }

    private void initMenus() {

    }

    public void initBoxes() {
        boxes = new ArrayList<>();
        boxes.add(new Box(500,500, 3));
        boxes.add(new Box(400,400, 4));

    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (ingame) {
           drawObjects(g);

        } else drawGameOver(g);
        Toolkit.getDefaultToolkit().sync();
    }

    private void drawGameOver(Graphics g) {
        g.setColor(Color.WHITE);
        g.drawString("Game Over" , 160, 140);
    }
    private void drawObjects(Graphics g) {
        for (Box b : boxes) {
            switch (b.level%5) {
                case 1 : g.setColor(Color.CYAN);
                    break;
                case 2 : g.setColor(Color.MAGENTA);
                    break;
                case 3 : g.setColor(Color.RED);
                    break;
                case 4 : g.setColor(Color.YELLOW);
                    break;
                case 0 : g.setColor(Color.GREEN);
                    break;
            }
            g.fillRect(b.getX(), b.getY(), b.height, b.width);

            g.setColor(Color.WHITE);
            g.setFont(new Font("TimesRoman", Font.PLAIN, 16));
            g.drawString("Cubes : " + boxes.size(), 50,50);
        }
    }

    private void collisions() {
        try {
            for (int i = 0; i < boxes.size(); i++) {
                Rectangle r1 = boxes.get(i).getBounds();
                for (int x = i + 1; x < boxes.size(); x++) {
                    Rectangle r2 = boxes.get(x).getBounds();
                    if (r1.intersects(r2)) {
                        if (boxes.get(i).level == boxes.get(x).level) {
                            boxes.get(i).setVisible(false);
                            boxes.get(x).setVisible(false);
                            boxes.add(new Box((boxes.get(i).getX()+ boxes.get(x).getX())/2,(boxes.get(i).getY()+ boxes.get(x).getY())/2, boxes.get(i).level + 1));
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void update() {
        for (int i = 0; i < boxes.size(); i++) {

            Box m = boxes.get(i);

            if (m.x > B_WIDTH - m.width) {
                m.x = B_WIDTH - m.width;
            }
            else if (m.x < 0) {
                m.x = 0;
            }
            else if (m.y < 0) {
                m.y = 0;
            }
            else if (m.y > B_HEIGHT - m.height) {
                m.y = B_HEIGHT - m.height;
            }
            if (m.isVisible()) {

            } else {
                boxes.remove(i);
            }
        }
    }

     private void spawnBox() {
        count = 0;
        boolean add = true;
        for (Box b : boxes) {
            if (b.getX() == 100 && b.getY() == 100) {
                add = false;
            }
        }
        if (add) {
            boxes.add(new Box( 100, 100, 1));
        }
    }

    private void calcCoins() {

    }
    @Override
    public void actionPerformed(ActionEvent e) {
        collisions();
        update();
        if (count == 60) {
            calcCoins();
        }
        if(count == 120) {
            spawnBox();
        }
        count++;
        repaint();
    }

    public void mousePressed(MouseEvent e) {
        for (Box b : boxes) {
            Rectangle x = b.getBounds();
            if (x.contains(new Point(e.getX(), e.getY()))) {
                offsetX = e.getX() - b.getX();
                offsetY = e.getY() - b.getY();
                dragging = b;
                drag = true;
            }
        }
    }
    public void mouseReleased(MouseEvent e) {
        drag = false;
        dragging = null;
    }
    public void mouseEntered(MouseEvent e) {

    }
    public void mouseExited(MouseEvent e) {

    }
    public void mouseMoved(MouseEvent e) {

    }
    public void mouseClicked(MouseEvent e) {

    }
    public void mouseDragged(MouseEvent e) {
        try {
            if (dragging != null) {
                dragging.x = e.getX() - offsetX;
                dragging.y = e.getY() - offsetY;
            }
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }
}
