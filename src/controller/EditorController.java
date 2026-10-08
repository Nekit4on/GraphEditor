package controller;

import model.*;
import model.PixelFactory;
import model.PixelFactory.PixelType;
import view.EditorView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

public class EditorController {
    private Model model;
    private EditorView view;
    private PixelType currentTool = PixelType.POINT; // Текущий инструмент
    private boolean isDrawing = false;

    public EditorController(Model model) {
        this.model = model;
    }

    public void setView(EditorView view) {
        this.view = view;
    }

    public void setTool(PixelType tool) {
        this.currentTool = tool;
    }

    public PixelType getCurrentTool() {
        return currentTool;
    }


    public void onMousePressed(MouseEvent e) {
        isDrawing = true;


        if (currentTool == PixelType.POINT) {
            Pixel point = PixelFactory.createPixel(PixelType.POINT, e.getX(), e.getY(),
                    view.getSelectedColor());
            model.addPixel(point);
            view.redraw(model);
        }
    }

    public void onMouseDragged(MouseEvent e) {
        if (isDrawing) {
            Pixel point = PixelFactory.createPixel(currentTool, e.getX(), e.getY(),
                    view.getSelectedColor());
            model.addPixel(point);
            view.redraw(model);
        }
    }

    public void onMouseReleased(MouseEvent e) {
        isDrawing = false;
    }

    public void addPixel(PixelType type) {
        double x = 200 + Math.random() * 400;
        double y = 150 + Math.random() * 300;
        Color color = view.getSelectedColor();

        Pixel pixel = PixelFactory.createPixel(type, x, y, color);
        model.addPixel(pixel);
        view.redraw(model);
    }

    public void clearCanvas() {
        model.clear();
        view.redraw(model);
    }
}