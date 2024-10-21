import javazoom.jl.player.Player;
import java.io.FileInputStream;

public class MusicPlayer {
    private Player player;
    private Thread thread;

    public void playSong(String path) {
        stop();
        thread = new Thread(() -> {
            try {
                FileInputStream fis = new FileInputStream(path);
                player = new Player(fis);
                player.play();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        thread.start();
    }

    public void stop() {
        if (player != null) {
            player.close();
        }
        if (thread != null) {
            thread.interrupt();
        }
    }
}
