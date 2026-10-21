package model.shapes;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import java.io.Serializable;
import java.util.List;

public class FillDecorator extends ShapeDecorator implements Serializable {
    private transient Color fillColor;
    private double fillR, fillG, fillB, fillOpacity;

    public FillDecorator(Shape decoratedShape, Color fillColor) {
        super(decoratedShape);
        setFillColor(fillColor);
    }

    public void setFillColor(Color color) {
        this.fillColor = color;
        this.fillR = color.getRed();
        this.fillG = color.getGreen();
        this.fillB = color.getBlue();
        this.fillOpacity = color.getOpacity();
    }

    @Override
    public void draw(GraphicsContext gc) {
        Polygon poly = findBasePolygon();
        if (poly != null) {
            javafx.scene.paint.Paint oldFill = gc.getFill();
            
            gc.setFill(fillColor);
            gc.beginPath();
            List<Point2D> vertices = poly.getVertices();
            for (int i = 0; i < vertices.size(); i++) {
                Point2D p = vertices.get(i);
                if (i == 0) gc.moveTo(p.getX(), p.getY());
                else gc.lineTo(p.getX(), p.getY());
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
        out.writeDouble(fillR); out.writeDouble(fillG); out.writeDouble(fillB); out.writeDouble(fillOpacity);
    }

    private void readObject(java.io.ObjectInputStream in) throws java.io.IOException, ClassNotFoundException {
        in.defaultReadObject();
        this.fillR = in.readDouble(); this.fillG = in.readDouble();
        this.fillB = in.readDouble(); this.fillOpacity = in.readDouble();
        this.fillColor = new Color(fillR, fillG, fillB, fillOpacity);
    }
}
