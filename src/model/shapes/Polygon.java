package model.shapes;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Polygon implements Shape, Serializable {
    private List<Point2D> vertices;
    private double centerX, centerY;

    public Polygon(List<Point2D> vertices) {
        this.vertices = new ArrayList<>(vertices);
        calculateCenter();
    }

    private void calculateCenter() {
        double sumX = 0, sumY = 0;
        for (Point2D p : vertices) { sumX += p.getX(); sumY += p.getY(); }
        this.centerX = vertices.isEmpty() ? 0 : sumX / vertices.size();
        this.centerY = vertices.isEmpty() ? 0 : sumY / vertices.size();
    }

    @Override
    public void draw(GraphicsContext gc) {
        if (vertices.size() < 3) return;
        gc.beginPath();
        gc.moveTo(vertices.get(0).getX(), vertices.get(0).getY());
        for (int i = 1; i < vertices.size(); i++) gc.lineTo(vertices.get(i).getX(), vertices.get(i).getY());
        gc.closePath();
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(1);
        gc.stroke();
    }

    @Override
    public double getX() { return centerX; }
    
    @Override
    public double getY() { return centerY; }

    @Override
    public boolean contains(double px, double py) {
        if (vertices.isEmpty()) return false;
        double minX = vertices.get(0).getX(), maxX = vertices.get(0).getX();
        double minY = vertices.get(0).getY(), maxY = vertices.get(0).getY();
        for (Point2D p : vertices) {
            if (p.getX() < minX) minX = p.getX();
            if (p.getX() > maxX) maxX = p.getX();
            if (p.getY() < minY) minY = p.getY();
            if (p.getY() > maxY) maxY = p.getY();
        }
        return px >= minX - 10 && px <= maxX + 10 && py >= minY - 10 && py <= maxY + 10;
    }

    public List<Point2D> getVertices() { return vertices; }
    
    @Override
    public void move(double dx, double dy) {
        List<Point2D> newVertices = new ArrayList<>();
        for (Point2D p : vertices) newVertices.add(new Point2D(p.getX() + dx, p.getY() + dy));
        this.vertices = newVertices;
        calculateCenter();
    }
}
