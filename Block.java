import java.awt.image.BufferedImage;
import java.io.*;
import javax.imageio.ImageIO;

public class Block{

    private BufferedImage image;
    private int x, y;
    private boolean isVisible;

    public Block(int x, int y, boolean isVisible){
        this.x = x;
        this.y = y;
        this.isVisible = isVisible;

        try {
            BufferedImage blockSheet = ImageIO.read(new File("Environments/another-world/PNG/layered/another-world-tileset.png"));
            image=blockSheet.getSubimage(32,16,32,32);
		}
		catch (IOException e) {
        }
    }

    public BufferedImage getImage() {   return this.image;  }
    public int getX() { return this.x;  }
    public int getY() { return this.y;  }
    public boolean isVisible(){ return this.isVisible;   }

    public void changeX(int deltaX){
        this.x += deltaX;
    }

    public static void changeAllX(Block[][] blocks, int deltaX){
        for(int r=0; r<blocks.length; r++)
			for(int c=0; c<blocks[r].length; c++)
				blocks[r][c].changeX(deltaX);
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
}