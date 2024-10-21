import javazoom.jl.player.Player;
import java.io.FileInputStream;

public class SoundEffect {
    public void playDeathSound() {
        new Thread(() -> {
            try {
                FileInputStream fis = new FileInputStream("src/assets/soundfx/deathSound.mp3");
                Player player = new Player(fis);
                player.play();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
}
