import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.math.BigInteger;
import java.util.ArrayList;

public class Board extends JPanel implements ActionListener, MouseListener, MouseMotionListener {
    private Timer timer;
    private boolean ingame;
    private final int B_WIDTH = 1920;
    private final int B_HEIGHT = 860;
    private final int DELAY = 16;
    ArrayList<Box> boxes;
    ArrayList<CubeCreator> creators;
    int offsetX, offsetY;
    Box dragging;
    boolean drag = false;
    int count = 0, spawnPeriod = 60;
    BigInteger points;

    public Board() {
        initBoard();
    }


    private void initBoard() {
        setFocusable(true);
        setBackground(Color.BLACK);
        this.setLayout(null);
        ingame = true;
        setPreferredSize(new Dimension(B_WIDTH, B_HEIGHT));
        initBoxes();
        initMenus();
        points = new BigInteger("0");
        timer = new Timer(DELAY, this);
        timer.start();
        addMouseListener(this);
        addMouseMotionListener(this);


    }

    private void initMenus() {
        JPanel temp = new JPanel();
        GridLayout grid = new GridLayout(3,16);
        temp.setLayout(grid);
        for (int i = 0; i < 48; i++) {
            JButton label = new JButton(Integer.toString(i));
            label.setBackground(Color.RED);
            temp.add(label);
        }
        temp.setBackground(Color.DARK_GRAY);
        /**JButton button = new JButton("Button");
        temp.add(button);**/
        temp.setBounds(0, 860, 1920, 200);
        this.add(temp);
    }

    public void initBoxes() {
        boxes = new ArrayList<>();

        creators = new ArrayList<>();
        creators.add(new CubeCreator(500,500));

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
            g.setColor(Color.BLACK);
            g.drawRect(b.getX(), b.getY(), b.height, b.width);
        }

        g.setColor(Color.WHITE);
        for (CubeCreator c : creators) {
            g.fillRect(c.getX(), c.getY(), c.height, c.width);
        }

        g.setColor(Color.WHITE);
        g.setFont(new Font("TimesRoman", Font.PLAIN, 16));
        g.drawString("Cubes : " + boxes.size(), 50,50);

        int length = points.toString().length();

        g.drawString("Points : " + points, 50, 100);
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
                            boxes.add(new Box((boxes.get(i).getX()+ boxes.get(x).getX())/2,(boxes.get(i).getY()+ boxes.get(x).getY())/2, boxes.get(i).level + 1, false));
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
            if (m.spawning) m.spawn();
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

            if(!m.spawning) {
                m.animate();
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
            if (b.getX() == B_WIDTH/2 && b.getY() == B_HEIGHT/2) {
                add = false;
            }
        }
        if (add) {
            boxes.add(new Box( B_WIDTH/2, B_HEIGHT/2, 1, true));
        }
    }

    private void calcCoins() {
        for (Box b : boxes) {
            String add = Integer.toString(b.level * 2 - 1);
            BigInteger addd = new BigInteger(add);
            points = points.add(addd);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        collisions();
        update();
        if (count%60 == 0) {
            calcCoins();
        }
        if(count == spawnPeriod) {
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
        System.out.println("pressed");
    }
    public void mouseEntered(MouseEvent e) {

    }
    public void mouseExited(MouseEvent e) {

    }
    public void mouseMoved(MouseEvent e) {

    }
    public void mouseClicked(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
            System.out.println("Left Click");
        }
        if (e.getButton() == MouseEvent.BUTTON2) {
            System.out.println("Middle Click");
        }
        if (e.getButton() == MouseEvent.BUTTON3) {
            System.out.println("Right Click");
        }
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
