package geom;

/**
 * Created by lvanp on 17/04/2016.
 *
 * 2 Dimensional Point
 */
public class Point {

    public double x;
    public double y;

    public Point(double x, double y){
        this.x = x;
        this.y = y;
    }

    //Distance to the origin
    public double distanceOrigin(){
        return Math.sqrt(x*x+y*y);
    }

    //Distance to the origin
    public double distance(Point p){
        return Math.sqrt(Math.pow(p.x-x,2)+Math.pow(p.y-y,2));
    }

    //Orientation wrt the origin
    public double orientation(){
        return Math.atan2(y,x);
    }

    //distance with everything normalized to 0-1
    public Point normalized(){
        return new Point((int)(x/distanceOrigin()),(int)(y/distanceOrigin()));
    }

    //addition
    public Point add(Point p){
        return new Point(x+p.x,y+p.y);
    }

    //subtraction
    public Point subtract(Point p){
        return new Point(x-p.x,y-p.y);
    }

    //multiplication with a constant
    public Point multiply(double d){
        return new Point((int)(x*d),(int)(y*d));
    }

    //division with a constant
    public Point divide(double d){
        return new Point((int)(x/d),(int)(y/d));
    }
}
