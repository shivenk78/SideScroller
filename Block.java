import java.awt.image.BufferedImage;
import java.io.*;
import javax.imageio.ImageIO;

public class Block extends Collider{

    private BufferedImage image;
    private boolean isVisible;
    private static int xCounter;

    public Block(int x, int y, boolean isVisible){
        super(x, y, 128, 128);
        this.isVisible = isVisible;
        xCounter = 0;

        try {
            BufferedImage blockSheet = ImageIO.read(new File("Environments/another-world/PNG/layered/another-world-tileset.png"));
            image=blockSheet.getSubimage(32,16,32,32);
		}
		catch (IOException e) {
        }
    }

    public BufferedImage getImage() {   return this.image;  }
    public boolean isVisible(){ return this.isVisible;   }

    public static void changeAllX(Block[][] blocks, int deltaX){
        for(int r=0; r<blocks.length; r++)
			for(int c=0; c<blocks[r].length; c++)
                blocks[r][c].changeX(deltaX);
        xCounter += deltaX;
    }

    public static int[][] readMap(String fileName){
        File name = new File(fileName);
        int[][] arr = new int[6][60];

		try{
			BufferedReader input = new BufferedReader(new FileReader(name));

            int r = 0;
            String text;
			while( (text=input.readLine())!= null){
                for(int c=0; c<text.length(); c++)
                    arr[r][c] = Integer.parseInt(text.substring(c, c+1));
                r++;
			}
		}
		catch (IOException io){
			System.err.println("File does not exist");
        }
        
        return arr;
    }

    public static int getXCounter(){
        return xCounter;
    }
}