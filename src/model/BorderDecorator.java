package model;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import java.util.List;

public class BorderDecorator extends ShapeDecorator {
    private Color borderColor;
    private double borderWidth;

    public BorderDecorator(Shape decoratedShape, Color borderColor, double borderWidth) {
        super(decoratedShape);
        this.borderColor = borderColor;
        this.borderWidth = borderWidth;
    }

    @Override
    public void draw(GraphicsContext gc) {
        Polygon poly = findBasePolygon();
        if (poly != null) {
            Paint oldStroke = gc.getStroke();
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
}
