package model;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import java.util.List;

public class ShadowDecorator extends ShapeDecorator {
    private double offsetX;
    private double offsetY;
    private Color shadowColor;

    public ShadowDecorator(Shape decoratedShape, double offsetX, double offsetY, Color shadowColor) {
        super(decoratedShape);
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.shadowColor = shadowColor;
    }

    @Override
    public void draw(GraphicsContext gc) {
        if (decoratedShape instanceof Polygon) {
            Polygon poly = (Polygon) decoratedShape;
            Paint oldFill = gc.getFill();
            
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

    @Override
    public boolean contains(double px, double py) {
        return decoratedShape.contains(px, py);
    }
}
