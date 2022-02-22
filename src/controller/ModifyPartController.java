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
import model.*;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * The controller for the ModifyPart view.
 *
 * @author Alex Bright
 */
public class ModifyPartController implements Initializable {

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

    private Part selectedPart;

    /**
     * Initializes the controller.
     * The fields are populated with existing part data.
     *
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        idField.setText(Integer.toString(selectedPart.getId()));
        nameField.setText(selectedPart.getName());
        invField.setText(Integer.toString(selectedPart.getStock()));
        priceField.setText(Double.toString(selectedPart.getPrice()));
        maxField.setText(Integer.toString(selectedPart.getMax()));
        minField.setText(Integer.toString(selectedPart.getMin()));
        if (selectedPart instanceof InHouse) {
            sourceLabel.setText("Machine ID");
            sourceField.setText(Integer.toString(((InHouse) selectedPart).getMachineId()));
            inhouseRadio.setSelected(true);
        } else {
            sourceLabel.setText("Company Name");
            sourceField.setText(((Outsourced) selectedPart).getCompanyName());
            outsourcedRadio.setSelected(true);
        }
    }

    /**
     * Initializes the selected part criteria.
     *
     * @param selectedPart  selected part criteria
     */
    public ModifyPartController(Part selectedPart) {
        this.selectedPart = selectedPart;
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
     * Updates the selected part in the inventory when the save button is clicked.
     * Validates input and provides errors if necessary, otherwise replace the selected part with the new part.
     * If part is successfully updated, return to the MainScreen view.
     *
     * @param actionEvent
     * @throws IOException
     */
    public void savePart(ActionEvent actionEvent) throws IOException {
        if (DialogHandler.confirm("Modify Part", "Do you want to save the changes to the part?")) {
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

            int index = Inventory.getAllParts().indexOf(selectedPart);
            Part newPart;
            if (type.getSelectedToggle() == inhouseRadio) {
                newPart = new InHouse(selectedPart.getId(), name, price, stock, min, max, Integer.parseInt(source));
                Inventory.updatePart(index, newPart);
            } else {
                newPart = new Outsourced(selectedPart.getId(), name, price, stock, min, max, source);
                Inventory.updatePart(index, newPart);
            }

            for (Product p : Inventory.getAllProducts()) {
                if (p.getAllAssociatedParts().contains(selectedPart)) {
                    p.deleteAssociatedPart(selectedPart);
                    p.addAssociatedPart(newPart);
                }
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
        if (DialogHandler.confirm("Modify Part", "Do you want to cancel modifying this part?")) {
            Parent parent = FXMLLoader.load(getClass().getResource("/view/MainScreen.fxml"));
            Scene scene = new Scene(parent);
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        }
    }

}
