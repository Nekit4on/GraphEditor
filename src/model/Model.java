package model;

import model.shapes.*;
import java.util.ArrayList;
import java.util.List;

public class Model {
    private List<Shape> elements = new ArrayList<>();
    private Shape selectedElement = null;

    public void addElement(Shape element) {
        elements.add(element);
    }

    public void removeElement(Shape element) {
        elements.remove(element);
        if (selectedElement == element) selectedElement = null;
    }

    public void clear() {
        elements.clear();
        selectedElement = null;
    }

    public List<Shape> getElements() {
        return elements;
    }

    public Shape getSelectedElement() {
        return selectedElement;
    }

    public void setSelectedElement(Shape element) {
        this.selectedElement = element;
    }

    public Shape getElementAt(double x, double y) {
        for (int i = elements.size() - 1; i >= 0; i--) {
            Shape s = elements.get(i);
            if (s.contains(x, y)) {
                return s;
            }
        }
        return null;
    }
}