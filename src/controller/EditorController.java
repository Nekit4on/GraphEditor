package controller;

import model.*;
import model.PixelFactory.PixelType;
import view.EditorView;
import javafx.geometry.Point2D;
import javafx.scene.input.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class EditorController {
    private Model model;
    private EditorView view;
    private String currentTool = "POINT";

    private boolean isDrawingPolygon = false;
    private List<Point2D> currentPolygonVertices = new ArrayList<>();
    private boolean isDragging = false;
    private double lastX, lastY;

    public EditorController(Model model) {
        this.model = model;
    }

    public void setView(EditorView view) {
        this.view = view;
    }

    public void setTool(String tool) {
        this.currentTool = tool;
        this.isDrawingPolygon = "POLYGON".equals(tool);

        if (!this.isDrawingPolygon) {
            this.currentPolygonVertices.clear();
            model.setSelectedElement(null);
            view.updatePolygonButton(false);
        } else {
            view.updatePolygonButton(true);
        }
        view.redraw(model, currentPolygonVertices);
    }

    public void startPolygonDrawing() {
        setTool("POLYGON");
    }

    public void finishPolygon() {
        if (currentPolygonVertices.size() >= 3) {
            Polygon poly = new Polygon(new ArrayList<>(currentPolygonVertices));
            model.addElement(poly);
            model.setSelectedElement(poly);
        }
        currentPolygonVertices.clear();
        view.updatePolygonButton(true);
        view.redraw(model, currentPolygonVertices);
    }

    public List<Point2D> getTempPolygonVertices() {
        return currentPolygonVertices;
    }

    public void onMousePressed(MouseEvent e) {
        lastX = e.getX();
        lastY = e.getY();

        if (isDrawingPolygon) {
            currentPolygonVertices.add(new Point2D(e.getX(), e.getY()));
            view.redraw(model, currentPolygonVertices);
            return;
        }

        if ("SELECT".equals(currentTool)) {
            Shape clicked = model.getElementAt(e.getX(), e.getY());
            if (clicked != null) {
                model.setSelectedElement(clicked);
                isDragging = true;
            } else {
                model.setSelectedElement(null);
            }
            view.redraw(model, currentPolygonVertices);
            return;
        }

        if ("POINT".equals(currentTool)) {
            Pixel point = PixelFactory.createPixel(PixelType.POINT, e.getX(), e.getY(), view.getSelectedColor());
            model.addElement(point);
            view.redraw(model, currentPolygonVertices);
            return;
        }

        if ("CIRCLE".equals(currentTool) || "SQUARE".equals(currentTool)) {
            PixelType type = "CIRCLE".equals(currentTool) ? PixelType.CIRCLE : PixelType.SQUARE;
            Pixel pixel = PixelFactory.createPixel(type, e.getX(), e.getY(), view.getSelectedColor());
            model.addElement(pixel);
            view.redraw(model, currentPolygonVertices);
            return;
        }
    }

    public void onMouseDragged(MouseEvent e) {
        if (isDragging && model.getSelectedElement() != null) {
            double dx = e.getX() - lastX;
            double dy = e.getY() - lastY;
            
            model.getSelectedElement().move(dx, dy);
            
            lastX = e.getX();
            lastY = e.getY();
            view.redraw(model, currentPolygonVertices);
        } else if ("POINT".equals(currentTool)) {
            Pixel point = PixelFactory.createPixel(PixelType.POINT, e.getX(), e.getY(), view.getSelectedColor());
            model.addElement(point);
            view.redraw(model, currentPolygonVertices);
        } else if ("CIRCLE_DRAW".equals(currentTool)) {
            Pixel circle = PixelFactory.createPixel(PixelType.CIRCLE, e.getX(), e.getY(), view.getSelectedColor());
            model.addElement(circle);
            view.redraw(model, currentPolygonVertices);
        } else if ("SQUARE_DRAW".equals(currentTool)) {
            Pixel square = PixelFactory.createPixel(PixelType.SQUARE, e.getX(), e.getY(), view.getSelectedColor());
            model.addElement(square);
            view.redraw(model, currentPolygonVertices);
        }
    }

    public void onMouseReleased(MouseEvent e) {
        isDragging = false;
    }

    public void clearCanvas() {
        model.clear();
        currentPolygonVertices.clear();
        view.updatePolygonButton(false);
        view.redraw(model, currentPolygonVertices);
    }

    public void applyFill() {
        Shape selected = model.getSelectedElement();
        if (selected != null) {
            replaceSelectedWith(new FillDecorator(selected, view.getSelectedColor()));
        }
    }

    public void applyBorder() {
        Shape selected = model.getSelectedElement();
        if (selected != null) {
            replaceSelectedWith(new BorderDecorator(selected, view.getSelectedColor(), 3.0));
        }
    }

    public void applyShadow() {
        Shape selected = model.getSelectedElement();
        if (selected != null) {
            replaceSelectedWith(new ShadowDecorator(selected, 8.0, 8.0, javafx.scene.paint.Color.GRAY));
        }
    }

    private void replaceSelectedWith(Shape newShape) {
        model.removeElement(model.getSelectedElement());
        model.addElement(newShape);
        model.setSelectedElement(newShape);
        view.redraw(model, currentPolygonVertices);
    }
}