import java.awt.image.*;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.Image;
import java.util.Arrays;

public class Marine{

    public State STATE;
    public boolean right;

    private BufferedImage[] idles, runs;
    private int index;
    private int x;
    private int y;
    private int slowCount;

    public Marine(int x, int y){
        this.x = x;
        this.y = y;
        slowCount = 0;

        STATE = State.RUN;
        idles = new BufferedImage[4];
        idles = ImageChopper.chop(new File("Sprites/space-marine/PNG/space-marine-idle.png"), 48);
        runs = new BufferedImage[10];
        runs = Arrays.copyOfRange((ImageChopper.chop(new File("Sprites/space-marine/PNG/space-marine-run.png"), 48)), 1, 11);
    }

    public BufferedImage getImage(){
        int FRAME_DELAY = 5;
        slowCount++;
        BufferedImage img = new BufferedImage(10, 10, 10);
        switch(STATE){
            case RUN:
                if(index>=runs.length)
                    index = 0;
                img = runs[index];
                if(slowCount>FRAME_DELAY){
                    index++;
                    slowCount=0;
                }
                return img;
            case IDLE:
                if(index>=idles.length)
                    index = 0;
                img = idles[index];
                if(slowCount>FRAME_DELAY){
                    index++;
                    slowCount=0;
                }
                return img;
            default: return img;
        }
    }

    public int getX(){  return x;  }
    public int getY(){  return y;  }

    public enum State{
        IDLE, RUN, JUMP;
    }
}