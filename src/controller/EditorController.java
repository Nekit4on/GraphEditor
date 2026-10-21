package controller;

import model.*;
import model.shapes.*;
import model.commands.*;
import model.PixelFactory.PixelType;
import view.EditorView;
import javafx.geometry.Point2D;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class EditorController {
    private Model model;
    private EditorView view;
    private String currentTool = "POINT";

    private boolean isDrawingPolygon = false;
    private List<Point2D> currentPolygonVertices = new ArrayList<>();
    private boolean isDragging = false;
    private double lastX, lastY;

    private Stack<Command> undoStack = new Stack<>();
    private Stack<Command> redoStack = new Stack<>();

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
            Command cmd = new AddShapeCommand(model, poly);
            cmd.execute();
            undoStack.push(cmd);
            redoStack.clear();
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
            Command cmd = new AddShapeCommand(model, point);
            cmd.execute();
            undoStack.push(cmd);
            redoStack.clear();
            view.redraw(model, currentPolygonVertices);
            return;
        }

        if ("CIRCLE".equals(currentTool) || "SQUARE".equals(currentTool)) {
            PixelType type = "CIRCLE".equals(currentTool) ? PixelType.CIRCLE : PixelType.SQUARE;
            Pixel pixel = PixelFactory.createPixel(type, e.getX(), e.getY(), view.getSelectedColor());
            Command cmd = new AddShapeCommand(model, pixel);
            cmd.execute();
            undoStack.push(cmd);
            redoStack.clear();
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
            Command cmd = new AddShapeCommand(model, point);
            cmd.execute();
            undoStack.push(cmd);
            redoStack.clear();
            view.redraw(model, currentPolygonVertices);
        } else if ("CIRCLE_DRAW".equals(currentTool)) {
            Pixel circle = PixelFactory.createPixel(PixelType.CIRCLE, e.getX(), e.getY(), view.getSelectedColor());
            Command cmd = new AddShapeCommand(model, circle);
            cmd.execute();
            undoStack.push(cmd);
            redoStack.clear();
            view.redraw(model, currentPolygonVertices);
        } else if ("SQUARE_DRAW".equals(currentTool)) {
            Pixel square = PixelFactory.createPixel(PixelType.SQUARE, e.getX(), e.getY(), view.getSelectedColor());
            Command cmd = new AddShapeCommand(model, square);
            cmd.execute();
            undoStack.push(cmd);
            redoStack.clear();
            view.redraw(model, currentPolygonVertices);
        }
    }

    public void onMouseReleased(MouseEvent e) {
        isDragging = false;
    }

    public void clearCanvas() {
        Command cmd = new Command() {
            private List<Shape> removedShapes = new ArrayList<>(model.getElements());
            
            @Override
            public void execute() {
                model.clear();
            }
            
            @Override
            public void undo() {
                for (Shape s : removedShapes) {
                    model.addElement(s);
                }
            }
        };
        cmd.execute();
        undoStack.push(cmd);
        redoStack.clear();
        currentPolygonVertices.clear();
        view.updatePolygonButton(false);
        view.redraw(model, currentPolygonVertices);
    }

    public void undo() {
        if (!undoStack.isEmpty()) {
            Command cmd = undoStack.pop();
            cmd.undo();
            redoStack.push(cmd);
            view.redraw(model, currentPolygonVertices);
        }
    }

    public void redo() {
        if (!redoStack.isEmpty()) {
            Command cmd = redoStack.pop();
            cmd.execute();
            undoStack.push(cmd);
            view.redraw(model, currentPolygonVertices);
        }
    }

    public void applyFill() {
        Shape selected = model.getSelectedElement();
        if (selected != null) {
            Shape decorated = new FillDecorator(selected, view.getSelectedColor());
            Command cmd = new Command() {
                @Override
                public void execute() {
                    model.removeElement(selected);
                    model.addElement(decorated);
                    model.setSelectedElement(decorated);
                }
                
                @Override
                public void undo() {
                    model.removeElement(decorated);
                    model.addElement(selected);
                    model.setSelectedElement(selected);
                }
            };
            cmd.execute();
            undoStack.push(cmd);
            redoStack.clear();
            view.redraw(model, currentPolygonVertices);
        }
    }

    public void applyBorder() {
        Shape selected = model.getSelectedElement();
        if (selected != null) {
            Shape decorated = new BorderDecorator(selected, view.getSelectedColor(), 3.0);
            Command cmd = new Command() {
                @Override
                public void execute() {
                    model.removeElement(selected);
                    model.addElement(decorated);
                    model.setSelectedElement(decorated);
                }
                
                @Override
                public void undo() {
                    model.removeElement(decorated);
                    model.addElement(selected);
                    model.setSelectedElement(selected);
                }
            };
            cmd.execute();
            undoStack.push(cmd);
            redoStack.clear();
            view.redraw(model, currentPolygonVertices);
        }
    }

    public void applyShadow() {
        Shape selected = model.getSelectedElement();
        if (selected != null) {
            Shape decorated = new ShadowDecorator(selected, 8.0, 8.0, javafx.scene.paint.Color.GRAY);
            Command cmd = new Command() {
                @Override
                public void execute() {
                    model.removeElement(selected);
                    model.addElement(decorated);
                    model.setSelectedElement(decorated);
                }
                
                @Override
                public void undo() {
                    model.removeElement(decorated);
                    model.addElement(selected);
                    model.setSelectedElement(selected);
                }
            };
            cmd.execute();
            undoStack.push(cmd);
            redoStack.clear();
            view.redraw(model, currentPolygonVertices);
        }
    }

    public void save(String filePath) {
        try {
            model.save(filePath);
            System.out.println("Файл успешно сохранён: " + filePath);
        } catch (Exception ex) {
            System.err.println("Ошибка сохранения: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    public void load(String filePath) {
        try {
            model.load(filePath);
            undoStack.clear();
            redoStack.clear();
            view.redraw(model, currentPolygonVertices);
            System.out.println("Файл успешно загружен: " + filePath);
        } catch (Exception ex) {
            view.showError("Ошибка загрузки файла: " + ex.getMessage());
        }
    }

    public void newDrawing() {
        model.setDrawing(new Drawing("Новый рисунок"));
        undoStack.clear();
        redoStack.clear();
        view.redraw(model, currentPolygonVertices);
    }
}
