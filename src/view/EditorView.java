package view;

import controller.EditorController;
import model.*;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.FileChooser;
import java.util.List;

public class EditorView {
    private Canvas canvas;
    private ColorPicker colorPicker;
    private ComboBox<String> shapeSelector;
    private CheckBox cbShadow;
    private CheckBox cbEraser;
    private EditorController controller;

    public EditorView(Stage stage, EditorController controller) {
        this.controller = controller;

        BorderPane root = new BorderPane();

        canvas = new Canvas(800, 600);
        canvas.setOnMousePressed(controller::onMousePressed);
        canvas.setOnMouseDragged(controller::onMouseDragged);
        canvas.setOnMouseReleased(controller::onMouseReleased);
        root.setCenter(canvas);

        HBox topPanel = new HBox(10);
        topPanel.setPadding(new Insets(10));

        Button btnGroup = new Button("Shift+ЛКМ и Сгруппировать");
        Button btnUngroup = new Button("Разгруппировать");
        Button btnDelete = new Button("Удалить");
        Button btnFront = new Button("На передний план");
        Button btnBack = new Button("На задний план");
        Button btnCopyProps = new Button("Копировать св-ва");
        Button btnApplyProps = new Button("Применить св-ва");
        Button btnToggleVisible = new Button("Скрыть/Показать");

        btnGroup.setOnAction(e -> controller.groupSelected());
        btnUngroup.setOnAction(e -> controller.ungroupSelected());
        btnDelete.setOnAction(e -> controller.deleteSelected());
        btnFront.setOnAction(e -> controller.bringSelectedToFront());
        btnBack.setOnAction(e -> controller.sendSelectedToBack());
        btnCopyProps.setOnAction(e -> controller.copyProperties());
        btnApplyProps.setOnAction(e -> controller.applyProperties());
        btnToggleVisible.setOnAction(e -> controller.toggleVisibility());

        topPanel.getChildren().addAll(btnGroup, btnUngroup, btnDelete, new Separator(),
                btnFront, btnBack, new Separator(),
                btnCopyProps, btnApplyProps, new Separator(),
                btnToggleVisible);
        root.setTop(topPanel);

        VBox leftPanel = new VBox(10);
        leftPanel.setPadding(new Insets(10));
        leftPanel.setPrefWidth(180);

        Label lblShape = new Label("Фигура:");
        shapeSelector = new ComboBox<>();
        shapeSelector.getItems().addAll("Круг", "Прямоугольник", "Квадрат", "Эллипс");
        shapeSelector.setValue("Круг");

        Button btnAdd = new Button("Добавить фигуру");
        btnAdd.setOnAction(e -> {
            String val = shapeSelector.getValue();
            if (val == null) return;
            switch (val) {
                case "Круг": controller.addShape("circle"); break;
                case "Прямоугольник": controller.addShape("rectangle"); break;
                case "Квадрат": controller.addShape("square"); break;
                case "Эллипс": controller.addShape("ellipse"); break;
            }
        });

        colorPicker = new ColorPicker(Color.RED);
        cbShadow = new CheckBox("Тень");
        cbEraser = new CheckBox("Ластик");
        cbEraser.setOnAction(e -> controller.setEraserMode(cbEraser.isSelected()));

        TextField textField = new TextField();
        textField.setPromptText("Текст для фигуры");
        textField.setPrefWidth(150);

        Button btnSetText = new Button("Установить текст");
        btnSetText.setOnAction(e -> controller.setTextForSelected(textField.getText()));

        Label lblSize = new Label("Размер:");
        Slider sizeSlider = new Slider(0.5, 2.0, 1.0);
        sizeSlider.setShowTickLabels(true);
        sizeSlider.setShowTickMarks(true);
        sizeSlider.setMajorTickUnit(0.5);
        sizeSlider.setMinorTickCount(5);
        sizeSlider.setPrefWidth(150);

        Button btnResize = new Button("Применить размер");
        btnResize.setOnAction(e -> controller.resizeSelected(sizeSlider.getValue()));

        Label lblPattern = new Label("Заполнение:");
        ComboBox<String> patternSelector = new ComboBox<>();
        patternSelector.getItems().addAll("Сплошной", "Штриховка", "Точки", "Сетка");
        patternSelector.setValue("Сплошной");
        patternSelector.setPrefWidth(150);

        Button btnSetPattern = new Button("Применить заполнение");
        btnSetPattern.setOnAction(e -> {
            String val = patternSelector.getValue();
            if (val == null) return;
            switch (val) {
                case "Сплошной": controller.setFillPattern("solid"); break;
                case "Штриховка": controller.setFillPattern("hatch"); break;
                case "Точки": controller.setFillPattern("dots"); break;
                case "Сетка": controller.setFillPattern("grid"); break;
            }
        });

        Button btnSave = new Button("Сохранить");
        Button btnLoad = new Button("Загрузить");
        btnSave.setOnAction(e -> saveFile(stage));
        btnLoad.setOnAction(e -> loadFile(stage));

        leftPanel.getChildren().addAll(
                lblShape, shapeSelector, btnAdd,
                new Separator(),
                new Label("Цвет:"), colorPicker,
                cbShadow, cbEraser,
                new Separator(),
                textField, btnSetText,
                new Separator(),
                lblSize, sizeSlider, btnResize,
                new Separator(),
                lblPattern, patternSelector, btnSetPattern,
                new Separator(),
                btnSave, btnLoad
        );
        root.setLeft(leftPanel);

        Scene scene = new Scene(root, 1200, 750);
        stage.setTitle("Графический редактор");
        stage.setScene(scene);
        stage.show();
    }

    public void redraw(Model model, List<Shape> selected) {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        for (Dot d : model.getDots()) d.draw(gc);
        for (Shape s : model.getShapes()) {
            s.draw(gc);
            if (selected != null && selected.contains(s)) {
                gc.setStroke(Color.BLUE);
                gc.setLineWidth(2);
                gc.setLineDashes(5);
                gc.strokeRect(s.getX() - 35, s.getY() - 35, 70, 70);
                gc.setLineDashes(0);
            }
        }
    }

    private void saveFile(Stage stage) {
        FileChooser fc = new FileChooser();
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Graph", "*.graph"));
        java.io.File f = fc.showSaveDialog(stage);
        if (f != null) controller.save(f.getAbsolutePath());
    }

    private void loadFile(Stage stage) {
        FileChooser fc = new FileChooser();
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Graph", "*.graph"));
        java.io.File f = fc.showOpenDialog(stage);
        if (f != null) controller.load(f.getAbsolutePath());
    }

    public Color getSelectedColor() {
        return colorPicker.getValue();
    }
}