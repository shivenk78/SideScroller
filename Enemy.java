import java.awt.image.BufferedImage;
import java.io.File;

public class Enemy extends Collider {

    private int slowCount, index;
    private BufferedImage[] ships;

    public Enemy(int x, int y){
        super(x, y, 80, 80);
        index = slowCount = 0;

        ships = new BufferedImage[8];
        ships = ImageChopper.chop(new File("Sprites/spaceship-unit/PNG/ship-unit-with-thrusts.png"), 106, 77);
    }

    public BufferedImage getImage(){
        int FRAME_DELAY = 5;
        slowCount++;
        BufferedImage img = new BufferedImage(10, 10, 10);
        if(index>=ships.length)
            index = 0;
        img = ships[index];
        if(slowCount>FRAME_DELAY){
            index++;
            slowCount=0;
        }
        return img;
    }
}