package geom;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by lvanp on 17/04/2016.
 *
 * A rectangle that serves as the geometric representation of an intersection
 */
public class Rectangle {

    public Point p;
    public double width,height;
    public boolean[] occupied;

    public Rectangle(double x, double y, double width, double height){
        this.p = new Point(x,y);
        this.width = width;
        this.height = height;
        this.occupied = new boolean[]{false,false,false,false};
    }

    //Returns the area of the rectangle
    public double area(){
        return width*height;
    }

    //the x coordinate of the left side of the rectangle, insert null for getter, number for setter
    public double left(Integer left){
        if(left != null) {
            p.x = left;
        }
        return p.x;
    }

    //the x coordinate of the right side of the rectangle, insert null for getter, number for setter
    public double right(Integer right){
        if(right != null) {
            p.x = right - width;
        }
        return p.x + width;
    }

    //the y coordinate of the top side of the rectangle, insert null for getter, number for setter
    public double top(Integer top){
        if(top != null) {
            p.y = top;
        }
        return p.y;
    }

    //the y coordinate of the bottom side of the rectangle, insert null for getter, number for setter
    public double bottom(Integer bottom){
        if(bottom != null) {
            p.y = bottom - height;
        }
        return p.y + height;
    }

    //the width of the rectangle, insert null for getter, number for setter
    public double width(Integer width){
        if(width != null) {
            this.width = width;
        }
        return this.width;
    }

    //the heigght of the rectangle, insert null for getter, number for setter
    public double height(Integer height){
        if(height != null) {
            this.height = height;
        }
        return this.height;
    }

    //the Point in the center of the rectangle, insert null for getter, number for setter
    public Point center(Point center){
        if(center != null) {
            p.x = (int)(center.x - width / 2);
            p.y = (int)(center.y - height / 2);
        }
        return new Point(p.x + width / 2,p.y + height / 2);
    }

    //Checks if the point p is in the Rectangle
    public boolean containsPoint(Point p){
        double refX = p.x;
        double refY = p.y;
        return ((this.left(null) <= refX) && (refX <= this.right(null)) && ((this.top(null) <= refY) && (refY <= this.bottom(null))));
    }

    //Checks if the rectangle r is in the Rectangle
    public boolean containsRect(Rectangle r){
        return this.left(null) <= r.left(null) && r.right(null) <= this.right(null) && this.top(null) < r.top(null) && r.bottom(null) <= this.bottom(null);
    }

    //gets vertices of the rectangle
    public ArrayList<Point> getVertices(){
        return new ArrayList<Point>(Arrays.asList(new Point(this.left(null), this.top(null)), new Point(this.right(null), this.top(null)), new Point(this.right(null), this.bottom(null)), new Point(this.left(null), this.bottom(null))));
    }

    //Returns a side of the rectangle depending on the nummber i
    // i = 0 - TOP
    // i = 1 - RIGHT
    // i = 2 - BOTTOM
    // i = 3 - LEFT
    public int getSide(Point p){
        Point topMid = new Point(left(null)+width/2,top(null));
        Point botMid = new Point(left(null)+width/2,bottom(null));
        Point leftMid = new Point(left(null),top(null)-height/2);
        Point rightMid = new Point(right(null),top(null)-height/2);

        ArrayList<Point> ap = new ArrayList<Point>();
        ap.add(topMid);
        ap.add(rightMid);
        ap.add(botMid);
        ap.add(leftMid);

        double minDist = Double.MAX_VALUE;
        Point theChosenOne = null;

        for(Point point : ap){
            double dist = p.distance(point);
            if(dist < minDist){
                minDist = dist;
                theChosenOne = point;
            }
        }

        return ap.indexOf(theChosenOne);
    }

    public Point getMidPoint(int i){
        Point topMid = new Point(left(null)+width/2,top(null));
        Point botMid = new Point(left(null)+width/2,bottom(null));
        Point leftMid = new Point(left(null),top(null)-height/2);
        Point rightMid = new Point(right(null),top(null)-height/2);

        ArrayList<Point> ap = new ArrayList<Point>();
        ap.add(topMid);
        ap.add(rightMid);
        ap.add(botMid);
        ap.add(leftMid);

        return ap.get(i);
    }

    public boolean isOccupied(int i){
        return occupied[i];
    }

    public void setOccupied(int i, boolean b){
        occupied[i] = b;
    }

    //Find the sector closest to the point p
    public int getSectorID(Point p){
        Point offset = p.subtract(this.center(null));
        if((offset.y <= 0) && (Math.abs(offset.x) <= Math.abs(offset.y)))
            return 0;
        else if((offset.x >= 0) && (Math.abs(offset.x) >= Math.abs(offset.y)))
            return 1;
        else if((offset.y >= 0) && (Math.abs(offset.x) <= Math.abs(offset.y)))
            return 2;
        else if((offset.x <= 0) && (Math.abs(offset.x) >= Math.abs(offset.y)))
            return 3;
        else {
            System.err.println("ALGORITHM ERROR");
            return -1;
        }
    }

}
