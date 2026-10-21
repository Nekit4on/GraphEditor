package view;

import controller.EditorController;
import model.*;
import model.shapes.*;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.FileChooser;
import javafx.scene.control.Alert;
import java.util.List;

public class EditorView {
    private Canvas canvas;
    private ColorPicker colorPicker;
    private ComboBox<String> toolSelector;
    private Button btnFinishPoly;
    private EditorController controller;
    private Stage stage;

    public EditorView(Stage stage, EditorController controller) {
        this.stage = stage;
        this.controller = controller;

        BorderPane root = new BorderPane();

        canvas = new Canvas(800, 600);
        canvas.setOnMousePressed(controller::onMousePressed);
        canvas.setOnMouseDragged(controller::onMouseDragged);
        canvas.setOnMouseReleased(controller::onMouseReleased);

        // Меню бар
        MenuBar menuBar = new MenuBar();
        
        Menu fileMenu = new Menu("Файл");
        MenuItem newFile = new MenuItem("Новый");
        newFile.setOnAction(e -> controller.newDrawing());
        MenuItem saveFile = new MenuItem("Сохранить");
        saveFile.setOnAction(e -> saveFile());
        MenuItem loadFile = new MenuItem("Открыть");
        loadFile.setOnAction(e -> loadFile());
        fileMenu.getItems().addAll(newFile, saveFile, loadFile);

        Menu editMenu = new Menu("Правка");
        MenuItem undo = new MenuItem("Отменить (Ctrl+Z)");
        undo.setOnAction(e -> controller.undo());
        MenuItem redo = new MenuItem("Повторить (Ctrl+Y)");
        redo.setOnAction(e -> controller.redo());
        editMenu.getItems().addAll(undo, redo);

        menuBar.getMenus().addAll(fileMenu, editMenu);

        HBox topPanel = new HBox(10);
        topPanel.setPadding(new Insets(10));

        Button btnClear = new Button("Очистить холст");
        btnClear.setOnAction(e -> controller.clearCanvas());

        btnFinishPoly = new Button("Завершить полигон");
        btnFinishPoly.setOnAction(e -> controller.finishPolygon());
        btnFinishPoly.setVisible(false);
        btnFinishPoly.setStyle("-fx-base: #ffcc00; -fx-font-weight: bold;");

        topPanel.getChildren().addAll(btnClear, btnFinishPoly);

        VBox centerBox = new VBox(menuBar, topPanel, canvas);
        root.setCenter(centerBox);

        VBox leftPanel = new VBox(10);
        leftPanel.setPadding(new Insets(10));
        leftPanel.setPrefWidth(220);

        Label lblTool = new Label("Инструмент:");
        toolSelector = new ComboBox<>();
        toolSelector.getItems().addAll(
            "Точка (рисовать мышью)",
            "Круг (клик для создания)",
            "Круг (рисовать мышью)",
            "Квадрат (клик для создания)",
            "Квадрат (рисовать мышью)",
            "Полигон (серия кликов)",
            "Выделение / Перемещение"
        );
        toolSelector.setValue("Точка (рисовать мышью)");

        toolSelector.setOnAction(e -> {
            String val = toolSelector.getValue();
            if ("Точка (рисовать мышью)".equals(val)) controller.setTool("POINT");
            else if ("Круг (клик для создания)".equals(val)) controller.setTool("CIRCLE");
            else if ("Круг (рисовать мышью)".equals(val)) controller.setTool("CIRCLE_DRAW");
            else if ("Квадрат (клик для создания)".equals(val)) controller.setTool("SQUARE");
            else if ("Квадрат (рисовать мышью)".equals(val)) controller.setTool("SQUARE_DRAW");
            else if ("Полигон (серия кликов)".equals(val)) controller.startPolygonDrawing();
            else if ("Выделение / Перемещение".equals(val)) controller.setTool("SELECT");
        });

        colorPicker = new ColorPicker(Color.RED);

        Separator sep1 = new Separator();
        Label lblDecorators = new Label("Декораторы (для полигона):");
        Button btnFill = new Button("Заливка");
        Button btnBorder = new Button("Контур");
        Button btnShadow = new Button("Тень");

        btnFill.setOnAction(e -> controller.applyFill());
        btnBorder.setOnAction(e -> controller.applyBorder());
        btnShadow.setOnAction(e -> controller.applyShadow());

        leftPanel.getChildren().addAll(
            lblTool, toolSelector,
            new Separator(),
            new Label("Цвет фигуры / декора:"), colorPicker,
            sep1,
            lblDecorators, btnFill, btnBorder, btnShadow
        );
        root.setLeft(leftPanel);

        Scene scene = new Scene(root, 1200, 650);
        stage.setTitle("Графический редактор (Инкремент 3)");
        stage.setScene(scene);
        stage.show();
    }

    public void updatePolygonButton(boolean visible) {
        btnFinishPoly.setVisible(visible);
    }

    public void redraw(Model model, List<Point2D> tempVertices) {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        for (Shape s : model.getElements()) {
            s.draw(gc);
        }

        if (tempVertices != null && !tempVertices.isEmpty()) {
            gc.setStroke(Color.BLUE);
            gc.setLineWidth(2);
            gc.beginPath();
            gc.moveTo(tempVertices.get(0).getX(), tempVertices.get(0).getY());
            for (int i = 1; i < tempVertices.size(); i++) {
                gc.lineTo(tempVertices.get(i).getX(), tempVertices.get(i).getY());
            }
            gc.stroke();

            gc.setFill(Color.RED);
            for (Point2D p : tempVertices) {
                gc.fillOval(p.getX() - 3, p.getY() - 3, 6, 6);
            }
        }

        Shape selected = model.getSelectedElement();
        if (selected != null) {
            gc.setStroke(Color.GREEN);
            gc.setLineWidth(2);
            gc.setLineDashes(5);
            gc.strokeRect(selected.getX() - 40, selected.getY() - 40, 80, 80);
            gc.setLineDashes(0);
        }
    }

    public void redraw(Model model) {
        redraw(model, null);
    }

    public Color getSelectedColor() {
        return colorPicker.getValue();
    }

    private void saveFile() {
        FileChooser fc = new FileChooser();
        fc.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("Graph Editor", "*.graph"),
            new FileChooser.ExtensionFilter("Все файлы", "*.*")
        );
        java.io.File f = fc.showSaveDialog(stage);
        if (f != null) {
            String path = f.getAbsolutePath();
            if (!path.endsWith(".graph")) {
                path += ".graph";
            }
            controller.save(path);
        }
    }

    private void loadFile() {
        FileChooser fc = new FileChooser();
        fc.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("Graph Editor", "*.graph"),
            new FileChooser.ExtensionFilter("Все файлы", "*.*")
        );
        java.io.File f = fc.showOpenDialog(stage);
        if (f != null) {
            controller.load(f.getAbsolutePath());
        }
    }

    public void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Ошибка");
        alert.setHeaderText("Не удалось выполнить операцию");
        alert.setContentText(message);
        alert.showAndWait();
    }
}
