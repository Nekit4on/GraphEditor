package model.shapes;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import java.io.Serializable;
import java.util.List;

public class BorderDecorator extends ShapeDecorator implements Serializable {
    private transient Color borderColor;
    private double borderR, borderG, borderB, borderOpacity;
    private double borderWidth;

    public BorderDecorator(Shape decoratedShape, Color borderColor, double borderWidth) {
        super(decoratedShape);
        setBorderColor(borderColor);
        this.borderWidth = borderWidth;
    }

    public void setBorderColor(Color color) {
        this.borderColor = color;
        this.borderR = color.getRed();
        this.borderG = color.getGreen();
        this.borderB = color.getBlue();
        this.borderOpacity = color.getOpacity();
    }

    @Override
    public void draw(GraphicsContext gc) {
        Polygon poly = findBasePolygon();
        if (poly != null) {
            javafx.scene.paint.Paint oldStroke = gc.getStroke();
            double oldWidth = gc.getLineWidth();
            
            gc.setStroke(borderColor);
            gc.setLineWidth(borderWidth);
            
            gc.beginPath();
            List<Point2D> vertices = poly.getVertices();
            for (int i = 0; i < vertices.size(); i++) {
                Point2D p = vertices.get(i);
                if (i == 0) gc.moveTo(p.getX(), p.getY());
                else gc.lineTo(p.getX(), p.getY());
            }
            gc.closePath();
            gc.stroke();
            
            gc.setStroke(oldStroke);
            gc.setLineWidth(oldWidth);
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
        out.writeDouble(borderR); out.writeDouble(borderG); out.writeDouble(borderB); out.writeDouble(borderOpacity);
    }

    private void readObject(java.io.ObjectInputStream in) throws java.io.IOException, ClassNotFoundException {
        in.defaultReadObject();
        this.borderR = in.readDouble(); this.borderG = in.readDouble();
        this.borderB = in.readDouble(); this.borderOpacity = in.readDouble();
        this.borderColor = new Color(borderR, borderG, borderB, borderOpacity);
    }
}
