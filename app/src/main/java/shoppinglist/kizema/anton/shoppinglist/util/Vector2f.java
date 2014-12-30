package shoppinglist.kizema.anton.shoppinglist.util;

public class Vector2f {
    private float x;
    private float y;

    public Vector2f(float x, float y){
        this.x = x;
        this.y = y;
    }

    public Vector2f(Vector2f v){
        this.x = v.x;
        this.y = v.y;
    }

    public float getX(){
        return x;
    }

    public float getY(){
        return x;
    }
}
