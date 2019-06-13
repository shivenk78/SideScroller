import java.awt.image.*;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.Image;
import java.awt.Rectangle;
import java.util.Arrays;

public class Marine extends Collider{

    public static final int MOVE_SPEED = 10;
    public static final int JUMP_HEIGHT = 14;
    public static final int JUMP_SPEED = 16;

    public State STATE, lastState;
    public boolean right;

    private BufferedImage[] idles, runs, jumps;
    private Rectangle collider;
    private int index, slowCount, jumpCount, yBeforeJump, yVel;

    public Marine(int x, int y){
        super(x, y, 128, 128);
        right = true;
        yVel = slowCount = 0;
        yBeforeJump = getY();

        collider = new Rectangle(x, y, 128, 128);

        STATE = State.RUN;
        lastState = State.IDLE;
        idles = ImageChopper.chop(new File("Sprites/space-marine/PNG/space-marine-idle.png"), 48, 37);
        runs = Arrays.copyOfRange((ImageChopper.chop(new File("Sprites/space-marine/PNG/space-marine-run.png"), 48, 37)), 1, 11);
        jumps = ImageChopper.chop(new File("Sprites/space-marine/PNG/space-marine-jump.png"), 36, 37);
    }

    public void jump(Marine.State prevState){
        if(STATE != State.JUMP){
            STATE = State.JUMP;
            yBeforeJump = getY();
            lastState = prevState;
            setYVel(-JUMP_SPEED);
        }
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
            case JUMP:
                if(index>=idles.length)
                    index = 0;
                img = jumps[index];
                if(slowCount>FRAME_DELAY){
                    index++;
                    slowCount=0;
                    /*jumpCount++;
                    if(jumpCount>=6){
                        STATE = State.IDLE;
                        jumpCount = 0;
                    }*/
                }
                return img;
            default: return img;
        }
    }

    public void stopJump(){
        STATE = lastState;
        setYVel(0);
    }

    public int getYBeforeJump(){
        return yBeforeJump;
    }
    public int getYVel(){
        return yVel;
    }
    public void setYVel(int yVel){
        this.yVel = yVel;
    }

    public enum State{
        IDLE, RUN, JUMP;
    }
}