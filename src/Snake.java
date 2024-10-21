import java.awt.Graphics;
import java.awt.Color;
import java.awt.Point;
import java.awt.Image;
import java.util.LinkedList;
import java.awt.event.KeyEvent;

public class Snake {
    private LinkedList<Point> body;
    private LinkedList<Image> images;
    private int direction;
    private boolean moved;
    private static final int UNIT_SIZE = 40;

    public Snake() {
        body = new LinkedList<>();
        images = new LinkedList<>();
        body.add(new Point(400, 300));
        direction = KeyEvent.VK_RIGHT;
        moved = false;
    }

    public void move() {
        Point head = new Point(body.getFirst());
        switch (direction) {
            case KeyEvent.VK_UP:
                head.y -= UNIT_SIZE;
                break;
            case KeyEvent.VK_DOWN:
                head.y += UNIT_SIZE;
                break;
            case KeyEvent.VK_LEFT:
                head.x -= UNIT_SIZE;
                break;
            case KeyEvent.VK_RIGHT:
                head.x += UNIT_SIZE;
                break;
        }
        body.addFirst(head);
        body.removeLast();
        moved = true;
    }

    public void grow(Image image) {
        body.addLast(new Point(body.getLast()));
        images.add(image);
    }

    public Point getHead() {
        return body.getFirst();
    }

    public void changeDirection(int keyCode) {
        if (moved) {
            if (keyCode == KeyEvent.VK_UP && direction != KeyEvent.VK_DOWN) {
                direction = KeyEvent.VK_UP;
            } else if (keyCode == KeyEvent.VK_DOWN && direction != KeyEvent.VK_UP) {
                direction = KeyEvent.VK_DOWN;
            } else if (keyCode == KeyEvent.VK_LEFT && direction != KeyEvent.VK_RIGHT) {
                direction = KeyEvent.VK_LEFT;
            } else if (keyCode == KeyEvent.VK_RIGHT && direction != KeyEvent.VK_LEFT) {
                direction = KeyEvent.VK_RIGHT;
            }
            moved = false;
        }
    }

    public void draw(Graphics g) {
        for (int i = 0; i < body.size(); i++) {
            Point p = body.get(i);
            if (i == 0) {
                g.setColor(Color.GREEN);
                g.fillRect(p.x, p.y, UNIT_SIZE, UNIT_SIZE);
            } else {
                if (i - 1 < images.size() && images.get(i - 1) != null) {
                    g.drawImage(images.get(i - 1), p.x, p.y, UNIT_SIZE, UNIT_SIZE, null);
                } else {
                    g.setColor(Color.GREEN);
                    g.fillRect(p.x, p.y, UNIT_SIZE, UNIT_SIZE);
                }
            }
        }
    }

    public boolean checkCollisionWithWalls(int width, int height) {
        Point head = getHead();
        return head.x < 0 || head.x >= width || head.y < 0 || head.y >= height;
    }

    public boolean checkSelfCollision() {
        Point head = getHead();
        for (int i = 1; i < body.size(); i++) {
            if (head.equals(body.get(i))) {
                return true;
            }
        }
        return false;
    }

    public LinkedList<Point> getBody() {
        return body;
    }
}
