import controller.EditorController;
import model.Model;
import view.EditorView;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage stage) {
        Model model = new Model();
        EditorController controller = new EditorController(model);
        EditorView view = new EditorView(stage, controller);
        controller.setView(view);
        view.redraw(model, null);
    }

    public static void main(String[] args) {
        launch(args);
    }
}