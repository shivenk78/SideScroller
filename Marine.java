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
    private static final int JUMP_SPEED = 16;

    public State STATE;
    public boolean right;

    private State lastState;
    private BufferedImage[] idles, runs, jumps;
    private Rectangle collider;
    private int index, x, y, slowCount, jumpCount, jumpTimer;

    public Marine(int x, int y){
        super(x, y, 128, 128);
        right = true;
        this.x = x;
        this.y = y;
        slowCount = 0;

        collider = new Rectangle(x, y, 128, 128);

        STATE = State.RUN;
        idles = new BufferedImage[4];
        idles = ImageChopper.chop(new File("Sprites/space-marine/PNG/space-marine-idle.png"), 48);
        runs = new BufferedImage[10];
        runs = Arrays.copyOfRange((ImageChopper.chop(new File("Sprites/space-marine/PNG/space-marine-run.png"), 48)), 1, 11);
        jumps = ImageChopper.chop(new File("Sprites/space-marine/PNG/space-marine-jump.png"), 36, 48);
    }

    public void jump(Marine.State prevState){
        if(STATE != State.JUMP){
            STATE = State.JUMP;
            jumpTimer = 0;
            lastState = prevState;
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
    @Override
    public int getY(){
        if(STATE == State.JUMP){
            if(jumpTimer > 2*JUMP_HEIGHT){
                jumpTimer = 0;
                STATE = lastState;
            }
            if(jumpTimer < JUMP_HEIGHT){
                jumpTimer++;
                y -= JUMP_SPEED;
            }
            if(jumpTimer >= JUMP_HEIGHT ){
                jumpTimer++;
                y += JUMP_SPEED;
            }
        }  
        return y;  
    }

    public enum State{
        IDLE, RUN, JUMP;
    }
}