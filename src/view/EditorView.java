package view;

import controller.EditorController;
import model.*;
import model.PixelFactory.PixelType;
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
    private ComboBox<String> toolSelector;
    private EditorController controller;

    public EditorView(Stage stage, EditorController controller) {
        this.controller = controller;

        BorderPane root = new BorderPane();

        // === ХОЛСТ  ===
        canvas = new Canvas(800, 600);
        canvas.setOnMousePressed(controller::onMousePressed);
        canvas.setOnMouseDragged(controller::onMouseDragged);
        canvas.setOnMouseReleased(controller::onMouseReleased);
        root.setCenter(canvas);


        HBox topPanel = new HBox(10);
        topPanel.setPadding(new Insets(10));

        Button btnClear = new Button("Очистить холст");
        btnClear.setOnAction(e -> controller.clearCanvas());
        topPanel.getChildren().addAll(btnClear);

        /*

        Button btnGroup = new Button("Сгруппировать");
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

        topPanel.getChildren().addAll(btnClear, new Separator(),
                                      btnGroup, btnUngroup, btnDelete, new Separator(),
                                      btnFront, btnBack, new Separator(),
                                      btnCopyProps, btnApplyProps, new Separator(),
                                      btnToggleVisible);
        */

        root.setTop(topPanel);

        // === ЛЕВАЯ ПАНЕЛЬ  ===
        VBox leftPanel = new VBox(10);
        leftPanel.setPadding(new Insets(10));
        leftPanel.setPrefWidth(180);

        Label lblTool = new Label("Инструмент:");
        toolSelector = new ComboBox<>();
        toolSelector.getItems().addAll("Точка", "Круг", "Квадрат");
        toolSelector.setValue("Точка");

        toolSelector.setOnAction(e -> {
            String val = toolSelector.getValue();
            if ("Точка".equals(val)) {
                controller.setTool(PixelType.POINT);
            } else if ("Круг".equals(val)) {
                controller.setTool(PixelType.CIRCLE);
            } else if ("Квадрат".equals(val)) {
                controller.setTool(PixelType.SQUARE);
            }
        });

        Button btnAdd = new Button("Добавить фигуру");
        btnAdd.setOnAction(e -> {
            String val = toolSelector.getValue();
            if ("Круг".equals(val)) {
                controller.addPixel(PixelType.CIRCLE);
            } else if ("Квадрат".equals(val)) {
                controller.addPixel(PixelType.SQUARE);
            }
        });

        colorPicker = new ColorPicker(Color.RED);

        leftPanel.getChildren().addAll(
                lblTool, toolSelector, btnAdd,
                new Separator(),
                new Label("Цвет:"), colorPicker
        );

        /*
        CheckBox cbShadow = new CheckBox("Тень");
        CheckBox cbEraser = new CheckBox("Ластик");
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
            new Separator(),
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
        */

        root.setLeft(leftPanel);

        Scene scene = new Scene(root, 1200, 650);
        stage.setTitle("Графический редактор (Инкремент 1)");
        stage.setScene(scene);
        stage.show();
    }

    // === МЕТОД ОТРИСОВКИ ===
    public void redraw(Model model) {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        for (Pixel p : model.getPixels()) {
            p.draw(gc);
        }

        /*
        // Отрисовка точек (если они хранятся отдельно)
        // for (Dot d : model.getDots()) d.draw(gc);

        // Отрисовка рамки выделения
        // for (Shape s : model.getShapes()) {
        //     s.draw(gc);
        //     if (selected.contains(s)) {
        //         gc.setStroke(Color.BLUE);
        //         gc.setLineWidth(2);
        //         gc.setLineDashes(5);
        //         gc.strokeRect(s.getX() - 35, s.getY() - 35, 70, 70);
        //         gc.setLineDashes(0);
        //     }
        // }
        */
    }

    public Color getSelectedColor() {
        return colorPicker.getValue();
    }

    /*
    // ============================================================
    // СОХРАНЕНО ДЛЯ ИНКРЕМЕНТА 3 (Раскомментировать позже)
    // ============================================================
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
    */
}