package model;

import javafx.scene.paint.Color;
import java.io.*;
import java.util.*;

public class Model implements Serializable {
    private List<Shape> shapes = new ArrayList<>();
    private List<Dot> dots = new ArrayList<>();
    private Shape copiedShape = null;

    public void addShape(Shape shape) {
        shapes.add(shape);
        shapes.sort(Comparator.comparingInt(Shape::getZOrder));
    }

    public void addDot(Dot dot) {
        dots.add(dot);
    }

    public void removeShape(Shape shape) {
        shapes.remove(shape);
    }

    public Shape getShapeAt(double x, double y) {
        for (int i = shapes.size() - 1; i >= 0; i--) {
            Shape s = shapes.get(i);
            if (s.isVisible() && s.contains(x, y)) return s;
        }
        return null;
    }

    public List<Shape> getShapes() { return shapes; }
    public List<Dot> getDots() { return dots; }
    public void clear() { shapes.clear(); dots.clear(); }

    public void bringToFront(Shape shape) {
        if (!shapes.contains(shape)) return;
        int maxZ = shapes.stream().mapToInt(Shape::getZOrder).max().orElse(0);
        shape.setZOrder(maxZ + 1);
        shapes.sort(Comparator.comparingInt(Shape::getZOrder));
    }

    public void sendToBack(Shape shape) {
        if (!shapes.contains(shape)) return;
        int minZ = shapes.stream().mapToInt(Shape::getZOrder).min().orElse(0);
        shape.setZOrder(minZ - 1);
        shapes.sort(Comparator.comparingInt(Shape::getZOrder));
    }

    public ShapeGroup groupShapes(List<Shape> selected) {
        if (selected.isEmpty()) return null;
        double sumX = 0, sumY = 0;
        for (Shape s : selected) { sumX += s.getX(); sumY += s.getY(); }
        ShapeGroup group = new ShapeGroup(sumX / selected.size(), sumY / selected.size());
        for (Shape s : selected) {
            shapes.remove(s);
            group.addChild(s.copy());
        }
        addShape(group);
        return group;
    }

    public void ungroupShape(ShapeGroup group) {
        if (!shapes.contains(group)) return;
        shapes.remove(group);
        for (Shape child : group.getChildren()) {
            addShape(child);
        }
    }

    public void save(String filePath) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
            oos.writeObject(shapes);
            oos.writeObject(dots);
        }
    }

    public void load(String filePath) throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath))) {
            shapes = (List<Shape>) ois.readObject();
            dots = (List<Dot>) ois.readObject();
        }
    }

    public void copyProperties(Shape source) {
        copiedShape = source;
    }

    public void applyProperties(Shape target) {
        if (copiedShape == null || target == null) return;
        target.setColor(copiedShape.getColor());
        target.setShadow(copiedShape.isShadowEnabled());
        target.setText(copiedShape.getText());
    }

    public Shape getCopiedShape() { return copiedShape; }
}