package ch.epfl.cs107.play.math;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;

public class Rectangle extends Shape{

    private float height;
    private float width;

    public Rectangle(float h, float w){
        height = h;
        width = w;

    }

    @Override
    public float getArea() {
        return height*width;
    }

    @Override
    public float getPerimeter() {
        return 2*(height+width);
    }

    @Override
    public Vector sample() {
        return null;
    }

    @Override
    public Path2D toPath() {
        // TODO is it possible to cache this? need to check if SwingWindow modifies it...
        Rectangle2D rectangle2D = new Rectangle2D() {
            @Override
            public void setRect(double x, double y, double w, double h) {
                width = (float) w;
                height = (float) h;

            }

            @Override
            public int outcode(double x, double y) {
                return 0;
            }

            @Override
            public Rectangle2D createIntersection(Rectangle2D r) {
                return null;
            }

            @Override
            public Rectangle2D createUnion(Rectangle2D r) {
                return null;
            }

            @Override
            public double getX() {
                return 0;
            }

            @Override
            public double getY() {
                return 0;
            }

            @Override
            public double getWidth() {
                return 0;
            }

            @Override
            public double getHeight() {
                return 0;
            }

            @Override
            public boolean isEmpty() {
                return false;
            }
        };
        return new Path2D.Float(rectangle2D);
    }
}
