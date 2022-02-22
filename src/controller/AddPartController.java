package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;
import main.DialogHandler;
import main.Main;
import model.InHouse;
import model.Inventory;
import model.Outsourced;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * The controller for the AddPart view.
 *
 * @author Alex Bright
 */
public class AddPartController implements Initializable {

    @FXML private RadioButton inhouseRadio;
    @FXML private RadioButton outsourcedRadio;
    @FXML private TextField idField;
    @FXML private TextField nameField;
    @FXML private TextField invField;
    @FXML private TextField priceField;
    @FXML private TextField maxField;
    @FXML private TextField sourceField;
    @FXML private TextField minField;
    @FXML private Label sourceLabel;
    @FXML private ToggleGroup type;
    @FXML private Label errorText;

    /**
     * Initializes the controller.
     *
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        inhouseRadio.setSelected(true);
        sourceLabel.setText("Machine ID");
        errorText.setText("");
    }

    /**
     * Updates the source type label when the in-house radio button is selected.
     *
     * @param actionEvent
     */
    public void inhouseChecked(ActionEvent actionEvent) {
        sourceLabel.setText("Machine ID");
    }

    /**
     * Updates the source type label when the outsourced radio button is selected.
     *
     * @param actionEvent
     */
    public void outsourcedChecked(ActionEvent actionEvent) {
        sourceLabel.setText("Company Name");
    }

    /**
     * Adds the new part to the inventory when the save button is clicked.
     * Validates input and provides errors if necessary, otherwise create the part instance and add to the inventory.
     * If part is successfully added to the inventory, return to the MainScreen view.
     *
     * @param actionEvent
     * @throws IOException
     */
    public void savePart(ActionEvent actionEvent) throws IOException {
        if (DialogHandler.confirm("Add Part", "Do you want to add the new part?")) {
            String error = "";
            errorText.setText("");
            if (nameField.getText().trim().isEmpty()) error += "No data in name field\n";
            if (!Main.isInt(invField.getText().trim())) error += "Inventory is not an integer\n";
            if (!Main.isInt(priceField.getText().trim()) && !Main.isDouble(priceField.getText().trim()))
                error += "Price must be numeric\n";
            if (!Main.isInt(maxField.getText().trim())) error += "Max is not an integer\n";
            if (!Main.isInt(minField.getText().trim())) error += "Min is not an integer\n";
            if ((Main.isInt(maxField.getText().trim()) && Main.isInt(minField.getText().trim()))) {
                int max = Integer.parseInt(maxField.getText().trim());
                int min = Integer.parseInt(minField.getText().trim());
                if (min >= max)
                    error += "Min must be less than Max\n";
                if (Main.isInt(invField.getText().trim())) {
                    int inv = Integer.parseInt(invField.getText().trim());
                    if (!(inv <= max && inv >= min)) error += "Inventory must be between Min and Max\n";
                }
            }
            if (type.getSelectedToggle() == inhouseRadio) {
                if (!Main.isInt(sourceField.getText().trim())) error += "Machine ID is not an integer\n";
            } else if (sourceField.getText().trim().isEmpty()) error += "No data in company name field\n";

            if (!error.isEmpty()) {
                errorText.setText("Error:\n" + error);
                return;
            }

            String name = nameField.getText().trim();
            double price = 0.00;
            if (Main.isInt(priceField.getText().trim())) price += Integer.parseInt(priceField.getText().trim());
            else price = Double.parseDouble(priceField.getText().trim());
            int stock = Integer.parseInt(invField.getText().trim());
            int min = Integer.parseInt(minField.getText().trim());
            int max = Integer.parseInt(maxField.getText().trim());
            String source = sourceField.getText().trim();

            if (type.getSelectedToggle() == inhouseRadio) {
                Inventory.addPart(new InHouse(Inventory.getNextPartId(), name, price, stock, min, max, Integer.parseInt(source)));
            } else {
                Inventory.addPart(new Outsourced(Inventory.getNextPartId(), name, price, stock, min, max, source));
            }

            Parent parent = FXMLLoader.load(getClass().getResource("/view/MainScreen.fxml"));
            Scene scene = new Scene(parent);
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        }
    }

    /**
     * Cancels and sends the user back to the main menu.
     * If they confirm, the user is sent back to the MainScreen view.
     *
     * @param actionEvent
     * @throws IOException
     */
    public void cancel(ActionEvent actionEvent) throws IOException {
        if (DialogHandler.confirm("Add Part", "Do you want to cancel adding a part?")) {
            Parent parent = FXMLLoader.load(getClass().getResource("/view/MainScreen.fxml"));
            Scene scene = new Scene(parent);
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        }
    }

}
