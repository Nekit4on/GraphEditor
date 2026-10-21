package model.shapes;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import java.io.Serializable;
import java.util.List;

public class ShadowDecorator extends ShapeDecorator implements Serializable {
    private double offsetX;
    private double offsetY;
    private transient Color shadowColor;
    private double shadowR, shadowG, shadowB, shadowOpacity;

    public ShadowDecorator(Shape decoratedShape, double offsetX, double offsetY, Color shadowColor) {
        super(decoratedShape);
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        setShadowColor(shadowColor);
    }

    public void setShadowColor(Color color) {
        this.shadowColor = color;
        this.shadowR = color.getRed();
        this.shadowG = color.getGreen();
        this.shadowB = color.getBlue();
        this.shadowOpacity = color.getOpacity();
    }

    @Override
    public void draw(GraphicsContext gc) {
        Polygon poly = findBasePolygon();
        if (poly != null) {
            javafx.scene.paint.Paint oldFill = gc.getFill();
            
            gc.setFill(shadowColor);
            gc.beginPath();
            List<Point2D> vertices = poly.getVertices();
            for (int i = 0; i < vertices.size(); i++) {
                Point2D p = vertices.get(i);
                if (i == 0) gc.moveTo(p.getX() + offsetX, p.getY() + offsetY);
                else gc.lineTo(p.getX() + offsetX, p.getY() + offsetY);
            }
            gc.closePath();
            gc.fill();
            gc.setFill(oldFill);
        }
        decoratedShape.draw(gc);
    }

    private Polygon findBasePolygon() {
        Shape s = decoratedShape;
        while (s instanceof ShapeDecorator) {
            s = ((ShapeDecorator) s).decoratedShape;
        }
        return (s instanceof Polygon) ? (Polygon) s : null;
    }

    @Override
    public boolean contains(double px, double py) {
        return decoratedShape.contains(px, py);
    }

    private void writeObject(java.io.ObjectOutputStream out) throws java.io.IOException {
        out.defaultWriteObject();
        out.writeDouble(shadowR); out.writeDouble(shadowG); out.writeDouble(shadowB); out.writeDouble(shadowOpacity);
    }

    private void readObject(java.io.ObjectInputStream in) throws java.io.IOException, ClassNotFoundException {
        in.defaultReadObject();
        this.shadowR = in.readDouble(); this.shadowG = in.readDouble();
        this.shadowB = in.readDouble(); this.shadowOpacity = in.readDouble();
        this.shadowColor = new Color(shadowR, shadowG, shadowB, shadowOpacity);
    }
}
