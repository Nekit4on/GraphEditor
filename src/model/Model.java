package model;

import model.shapes.Shape;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Model implements Serializable {
    private Drawing drawing;
    private Settings settings;
    private Shape selectedElement = null;

    public Model() {
        this.drawing = new Drawing("Новый рисунок");
        this.settings = new Settings();
    }

    public void addElement(Shape element) {
        drawing.addShape(element);
    }

    public void removeElement(Shape element) {
        drawing.removeShape(element);
        if (selectedElement == element) selectedElement = null;
    }

    public void clear() {
        drawing.clear();
        selectedElement = null;
    }

    public List<Shape> getElements() {
        return drawing.getShapes();
    }

    public Shape getSelectedElement() {
        return selectedElement;
    }

    public void setSelectedElement(Shape element) {
        this.selectedElement = element;
    }

    public Shape getElementAt(double x, double y) {
        for (int i = drawing.getShapes().size() - 1; i >= 0; i--) {
            Shape s = drawing.getShapes().get(i);
            if (s.contains(x, y)) {
                return s;
            }
        }
        return null;
    }

    public Drawing getDrawing() {
        return drawing;
    }

    public void setDrawing(Drawing drawing) {
        this.drawing = drawing;
    }

    public Settings getSettings() {
        return settings;
    }

    public void setSettings(Settings settings) {
        this.settings = settings;
    }

    public void save(String filePath) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
            oos.writeObject(drawing);
            oos.writeObject(settings);
        }
    }

    public void load(String filePath) throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath))) {
            drawing = (Drawing) ois.readObject();
            settings = (Settings) ois.readObject();
            selectedElement = null;
        }
    }
}
