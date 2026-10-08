package controller;

import model.*;
import view.EditorView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import java.util.ArrayList;
import java.util.List;

public class EditorController {
    private Model model;
    private EditorView view;
    private List<Shape> selectedShapes = new ArrayList<>();
    private boolean isDrawingDots = false;
    private boolean eraserMode = false;
    private double lastX, lastY;

    public EditorController(Model model) {
        this.model = model;
    }

    public void setView(EditorView view) {
        this.view = view;
    }

    public void setEraserMode(boolean enabled) {
        this.eraserMode = enabled;
    }

    public void onMousePressed(MouseEvent e) {
        lastX = e.getX();
        lastY = e.getY();

        if (eraserMode) {
            Shape shape = model.getShapeAt(e.getX(), e.getY());
            if (shape != null) {
                model.removeShape(shape);
                selectedShapes.remove(shape);
            }
            model.getDots().removeIf(d -> d.contains(e.getX(), e.getY()));
            view.redraw(model, selectedShapes);
            return;
        }

        Shape shape = model.getShapeAt(e.getX(), e.getY());
        if (shape != null) {
            if (e.isShiftDown()) {
                if (selectedShapes.contains(shape)) {
                    selectedShapes.remove(shape);
                } else {
                    selectedShapes.add(shape);
                }
            } else {
                selectedShapes.clear();
                selectedShapes.add(shape);
            }
            isDrawingDots = false;
        } else {
            if (!e.isShiftDown()) {
                selectedShapes.clear();
            }
            isDrawingDots = true;
        }
        view.redraw(model, selectedShapes);
    }

    public void onMouseDragged(MouseEvent e) {
        if (eraserMode) {
            model.getDots().removeIf(d -> d.contains(e.getX(), e.getY()));
            view.redraw(model, selectedShapes);
            return;
        }

        if (isDrawingDots) {
            model.addDot(new Dot(view.getSelectedColor(), e.getX(), e.getY(), 4));
        } else if (!selectedShapes.isEmpty()) {
            double dx = e.getX() - lastX;
            double dy = e.getY() - lastY;
            for (Shape s : selectedShapes) {
                s.move(dx, dy);
            }
            lastX = e.getX();
            lastY = e.getY();
        }
        view.redraw(model, selectedShapes);
    }

    public void onMouseReleased(MouseEvent e) {
        isDrawingDots = false;
    }

    public void addShape(String type) {
        Color color = view.getSelectedColor();
        double x = 200 + Math.random() * 400;
        double y = 150 + Math.random() * 300;
        switch (type) {
            case "circle": model.addShape(new Circle(color, x, y, 40)); break;
            case "rectangle": model.addShape(new Rectangle(color, x, y, 80, 60)); break;
            case "square": model.addShape(new Square(color, x, y, 60)); break;
            case "ellipse": model.addShape(new Ellipse(color, x, y, 60, 35)); break;
        }
        view.redraw(model, selectedShapes);
    }

    public void deleteSelected() {
        for (Shape s : new ArrayList<>(selectedShapes)) {
            model.removeShape(s);
        }
        selectedShapes.clear();
        view.redraw(model, selectedShapes);
    }

    public void bringSelectedToFront() {
        for (Shape s : selectedShapes) {
            model.bringToFront(s);
        }
        view.redraw(model, selectedShapes);
    }

    public void sendSelectedToBack() {
        for (Shape s : selectedShapes) {
            model.sendToBack(s);
        }
        view.redraw(model, selectedShapes);
    }

    public void groupSelected() {
        if (selectedShapes.size() >= 2) {
            ShapeGroup group = model.groupShapes(new ArrayList<>(selectedShapes));
            selectedShapes.clear();
            if (group != null) selectedShapes.add(group);
            view.redraw(model, selectedShapes);
        }
    }

    public void ungroupSelected() {
        List<Shape> toRemove = new ArrayList<>();
        for (Shape s : selectedShapes) {
            if (s instanceof ShapeGroup) {
                model.ungroupShape((ShapeGroup) s);
                toRemove.add(s);
            }
        }
        selectedShapes.removeAll(toRemove);
        view.redraw(model, selectedShapes);
    }

    public void toggleVisibility() {
        for (Shape s : selectedShapes) {
            s.setVisible(!s.isVisible());
        }
        view.redraw(model, selectedShapes);
    }

    public void copyProperties() {
        if (!selectedShapes.isEmpty()) {
            model.copyProperties(selectedShapes.get(0));
        }
    }

    public void applyProperties() {
        for (Shape s : selectedShapes) {
            model.applyProperties(s);
        }
        view.redraw(model, selectedShapes);
    }

    public void setTextForSelected(String text) {
        for (Shape s : selectedShapes) {
            s.setText(text);
        }
        view.redraw(model, selectedShapes);
    }

    public void resizeSelected(double factor) {
        for (Shape s : selectedShapes) {
            s.resize(factor);
        }
        view.redraw(model, selectedShapes);
    }

    public void save(String path) {
        try { model.save(path); } catch (Exception ex) { ex.printStackTrace(); }
    }

    public void load(String path) {
        try {
            model.load(path);
            selectedShapes.clear();
            view.redraw(model, selectedShapes);
        } catch (Exception ex) { ex.printStackTrace(); }
    }

    public void setFillPattern(String pattern) {
        for (Shape s : selectedShapes) {
            s.setFillPattern(pattern);
        }
        view.redraw(model, selectedShapes);
    }
}