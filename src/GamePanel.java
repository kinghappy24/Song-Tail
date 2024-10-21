import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Font;
import java.awt.Rectangle; 
import javax.swing.JPanel;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;





public class GamePanel extends JPanel implements Runnable, KeyListener {
    public static final int WIDTH = 800;
    public static final int HEIGHT = 600;
    public static final int UNIT_SIZE = 40;

    private Thread thread;
    private boolean running;
    private Snake snake;
    private Album album;
    private MusicPlayer musicPlayer;
    private SoundEffect soundEffect;
    private int score;
    private boolean gameOver;

    public GamePanel() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setFocusable(true);
        requestFocus();
        addKeyListener(this);

        snake = new Snake();
        album = new Album();
        musicPlayer = new MusicPlayer();
        soundEffect = new SoundEffect();
        score = 0;
        gameOver = false;

        album.generateNewAlbum(WIDTH, HEIGHT, snake);
        musicPlayer.playSong(album.getSongPath());
    }

    public void startGame() {
        running = true;
        thread = new Thread(this);
        thread.start();
    }

    @Override
    public void run() {
        while (running) {
            if (!gameOver) {
                snake.move();
                checkCollisions();
            }
            repaint();
            try {
                Thread.sleep(200); 
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void checkCollisions() {
        
        if (snake.checkCollisionWithWalls(WIDTH, HEIGHT) || snake.checkSelfCollision()) {
            gameOver = true;
            musicPlayer.stop();
            soundEffect.playDeathSound();
        }

        Rectangle snakeHeadRect = new Rectangle(snake.getHead().x, snake.getHead().y, UNIT_SIZE, UNIT_SIZE);
        Rectangle albumRect = new Rectangle(album.getPosition().x, album.getPosition().y, UNIT_SIZE, UNIT_SIZE);

        if (snakeHeadRect.intersects(albumRect)) {
            snake.grow(album.getImage());
            score++;
            album.generateNewAlbum(WIDTH, HEIGHT, snake);
            musicPlayer.playSong(album.getSongPath());
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (!gameOver) {
            snake.draw(g);
            album.draw(g);
            g.setColor(Color.BLACK);
            g.setFont(new Font("Arial", Font.PLAIN, 20));
            g.drawString("Score: " + score, WIDTH - 120, 30);
        } else {
            g.setColor(Color.BLACK);
            g.setFont(new Font("Arial", Font.BOLD, 50));
            g.drawString("Game Over", WIDTH / 2 - 150, HEIGHT / 2);
            g.drawString("Score: " + score, WIDTH / 2 - 100, HEIGHT / 2 + 60);
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        snake.changeDirection(e.getKeyCode());
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }
}
