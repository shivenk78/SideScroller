import java.awt.Rectangle;

public class Collider{

    private Rectangle collider;
    private int x, y, width, height;

    public Collider(int x, int y, int width, int height){
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.collider = new Rectangle(x, y, width, height);
    }

    public Rectangle getCollider(){
        collider = new Rectangle(x, y, width, height);
        return collider;
    }
    public Rectangle getFutureCollider(int deltaX, int deltaY){
        return new Rectangle(this.x+deltaX, this.y+deltaY, width, height);
    }
    public void changeX(int deltaX){
        this.x += deltaX;
    }
    public void changeY(int deltaY){
        this.y += deltaY;
    }
    
    public int getX(){  return x;  }
    public int getY(){  return y;  }
    public void setX(int x){  this.x = x;   }
    public void setY(int y){  this.y = y;   }
}