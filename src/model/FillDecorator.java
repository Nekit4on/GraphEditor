package model;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import java.util.List;

public class FillDecorator extends ShapeDecorator {
    private Color fillColor;

    public FillDecorator(Shape decoratedShape, Color fillColor) {
        super(decoratedShape);
        this.fillColor = fillColor;
    }

    @Override
    public void draw(GraphicsContext gc) {
        Polygon poly = findBasePolygon();
        if (poly != null) {
            Paint oldFill = gc.getFill();

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
}
