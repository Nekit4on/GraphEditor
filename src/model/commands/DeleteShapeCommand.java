package model.commands;

import model.Model;
import model.shapes.Shape;

public class DeleteShapeCommand implements Command {
    private Model model;
    private Shape shape;

    public DeleteShapeCommand(Model model, Shape shape) {
        this.model = model;
        this.shape = shape;
    }

    @Override
    public void execute() {
        model.removeElement(shape);
    }

    @Override
    public void undo() {
        model.addElement(shape);
    }
}
