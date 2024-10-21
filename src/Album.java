import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Color;
import java.io.File;
import java.util.Random;
import java.util.ArrayList;
import java.util.List;
import java.awt.Toolkit;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.images.Artwork;

public class Album {
    private Point position;
    private String songPath;
    private Image image;
    private List<String> songPaths;
    private List<String> usedSongs;
    private Random random;
    private static final int UNIT_SIZE = 40;

    public Album() {
        songPaths = new ArrayList<>();
        usedSongs = new ArrayList<>();
        File folder = new File("src/assets/songs");
        for (File file : folder.listFiles()) {
            if (file.isFile() && file.getName().endsWith(".mp3")) {
                songPaths.add(file.getPath());
            }
        }
        random = new Random();
        // Do not call generateNewAlbum here
    }

    public void generateNewAlbum(int width, int height, Snake snake) {
        if (songPaths.isEmpty()) {
            // Reset the song list when all songs have been used
            songPaths.addAll(usedSongs);
            usedSongs.clear();
        }
        int index = random.nextInt(songPaths.size());
        songPath = songPaths.remove(index);
        usedSongs.add(songPath);
        image = extractAlbumCover(songPath);
        generateNewPosition(width, height, snake);
    }

    private void generateNewPosition(int width, int height, Snake snake) {
        do {
            int x = random.nextInt(width / UNIT_SIZE) * UNIT_SIZE;
            int y = random.nextInt(height / UNIT_SIZE) * UNIT_SIZE;
            position = new Point(x, y);
        } while (snake.getBody().contains(position));
    }

    public Image extractAlbumCover(String path) {
        try {
            File file = new File(path);
            AudioFile audioFile = AudioFileIO.read(file);
            Tag tag = audioFile.getTag();
            Artwork artwork = tag.getFirstArtwork();
            if (artwork != null) {
                byte[] imageData = artwork.getBinaryData();
                return Toolkit.getDefaultToolkit().createImage(imageData);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void draw(Graphics g) {
        if (image != null && position != null) {
            g.drawImage(image, position.x, position.y, UNIT_SIZE, UNIT_SIZE, null);
        } else if (position != null) {
            g.setColor(Color.RED);
            g.fillRect(position.x, position.y, UNIT_SIZE, UNIT_SIZE);
        }
    }

    public Point getPosition() {
        return position;
    }

    public Image getImage() {
        return image;
    }

    public String getSongPath() {
        return songPath;
    }
}
