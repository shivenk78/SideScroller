import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Marine{

    private BufferedImage run;
    private BufferedImage[] runs;
    private State state;

    public Marine(int x, int y){
        state = State.RUN;
        try {
			run = ImageIO.read(new File("Sprites/space-marine/PNG/space-marine-run.png"));
            for(int i=0;i<4;i++){
                int imgWidth = (int)(run.getWidth()/((double)i));
                runs[i]=run.getSubimage(imgWidth*i,0,imgWidth,imgWidth);
            }
		}
		catch (IOException e) {
		}
    }

    public enum State{
        RUN,JUMP;
    }
}