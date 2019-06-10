import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class ImageChopper {

    public static BufferedImage[] chop(File file, int imageWidth){
        BufferedImage initial;
        BufferedImage[] result = new BufferedImage[1];
        try {
            initial = ImageIO.read(file);
            int frameCount = initial.getWidth()/imageWidth;
            result = new BufferedImage[frameCount];
            for(int i=0; i<frameCount; i++){
                result[i]=initial.getSubimage(imageWidth*i,0,imageWidth,imageWidth);
            }
		}
		catch (IOException e) {
        }
        return result;
    }
}