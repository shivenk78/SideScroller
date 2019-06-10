import java.awt.image.*;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.Image;

public class Marine{

    public State STATE;

    private BufferedImage[] runs;
    private int index;
    private int x;
    private int y;

    public Marine(int x, int y){
        this.x = x;
        this.y = y;

        STATE = State.RUN;
        runs = new BufferedImage[4];
        runs = ImageChopper.chop(new File("Sprites/space-marine/PNG/space-marine-run.png"), 48);
    }

    public BufferedImage getImage(){
        BufferedImage img = new BufferedImage(10, 10, 10);
        if(STATE == State.RUN){
            if(index>3)
                index = 0;
            img = runs[index];
            index++;
        }
        return img;
    }

    public int getX(){  return x;  }
    public int getY(){  return y;  }

    public enum State{
        RUN,JUMP;
    }
}