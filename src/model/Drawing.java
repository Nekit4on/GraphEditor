package model;

import model.shapes.Shape;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Drawing implements Serializable {
    private List<Shape> shapes = new ArrayList<>();
    private String title = "Без названия";

    public Drawing() {}

    public Drawing(String title) {
        this.title = title;
    }

    public void addShape(Shape shape) {
        shapes.add(shape);
    }

    public void removeShape(Shape shape) {
        shapes.remove(shape);
    }

    public void clear() {
        shapes.clear();
    }

    public List<Shape> getShapes() {
        return shapes;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getShapeCount() {
        return shapes.size();
    }
}
