package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import main.DialogHandler;
import main.Main;
import model.Inventory;
import model.Part;
import model.Product;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * The controller for the ModifyProduct view.
 *
 * @author Alex Bright
 */
public class ModifyProductController implements Initializable {

    @FXML private TextField idField;
    @FXML private TextField nameField;
    @FXML private TextField invField;
    @FXML private TextField priceField;
    @FXML private TextField maxField;
    @FXML private TextField minField;

    @FXML private TextField searchField;

    @FXML private TableView<Part> partTable;
    @FXML private TableColumn<Part, Integer> partIdCol;
    @FXML private TableColumn<Part, String> partNameCol;
    @FXML private TableColumn<Part, Integer> partInvCol;
    @FXML private TableColumn<Part, Double> partPriceCol;

    @FXML private TableView<Part> associatedTable;
    @FXML private TableColumn<Part, Integer> assIdCol;
    @FXML private TableColumn<Part, String> assNameCol;
    @FXML private TableColumn<Part, Integer> assInvCol;
    @FXML private TableColumn<Part, Double> assPriceCol;

    @FXML private Label errorText;

    private Product selectedProduct;

    /**
     * Initializes the controller.
     * The top table is populated with all parts.
     * The bottom table is populated with associated parts.
     *
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        partTable.setItems(Inventory.getAllParts());
        partIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        partNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        partInvCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        partTable.getSortOrder().add(partIdCol);

        for (Part p : selectedProduct.getAllAssociatedParts())
            associatedTable.getItems().add(p);
        assIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        assNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        assInvCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        assPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        associatedTable.getSortOrder().add(assIdCol);

        idField.setText(Integer.toString(selectedProduct.getId()));
        nameField.setText(selectedProduct.getName());
        invField.setText(Integer.toString(selectedProduct.getStock()));
        priceField.setText(Double.toString(selectedProduct.getPrice()));
        maxField.setText(Integer.toString(selectedProduct.getMax()));
        minField.setText(Integer.toString(selectedProduct.getMin()));
    }

    /**
     * Initializes the selected product criteria.
     *
     * @param selectedProduct
     */
    public ModifyProductController(Product selectedProduct) {
        this.selectedProduct = selectedProduct;
    }

    /**
     * Searches for a part when the user presses "enter" in the search box.
     * Allows the use of ID, partial name, or full name.
     * If input is an integer, searches both ID and name simultaneously.
     *
     * @param keyEvent
     */
    public void searchKeyPressed(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.ENTER) {
            String searchStr = searchField.getText().trim();
            if (searchStr.isEmpty()) {
                partTable.setItems(Inventory.getAllParts());
                partTable.getSortOrder().add(partIdCol);
            } else {
                ObservableList<Part> searchedParts = FXCollections.observableArrayList();
                if (Main.isInt(searchStr)) {
                    Part p = Inventory.lookupPart(Integer.parseInt(searchStr));
                    if (p != null) searchedParts.add(p);
                }
                searchedParts.addAll(Inventory.lookupPart(searchStr));
                if (searchedParts.isEmpty()) {
                    DialogHandler.inform("Search Part", "No parts found using that search criteria.");
                    partTable.setItems(Inventory.getAllParts());
                    partTable.getSortOrder().add(partIdCol);
                    return;
                }
                partTable.setItems(searchedParts);
            }
        }
    }

    /**
     * Adds selected part to the associated parts list.
     * Called when user clicks the add button.
     *
     * @param actionEvent
     */
    public void addAssociated(ActionEvent actionEvent) {
        if (partTable.getItems().size() == 0) {
            DialogHandler.inform("Add Association", "There are no parts to associate.");
            return;
        } else if (partTable.getSelectionModel().isEmpty()) {
            DialogHandler.inform("Add Association", "Please select a part to associate.");
            return;
        }

        Part selectedPart = partTable.getSelectionModel().getSelectedItem();

        associatedTable.getItems().add(selectedPart);
        partTable.getSelectionModel().clearSelection();
        associatedTable.refresh();
        associatedTable.sort();
    }

    /**
     * Removes selected part from the associated parts list.
     * Called when user clicks the remove associated part button.
     *
     * @param actionEvent
     */
    public void removeAssociated(ActionEvent actionEvent) {
        if (associatedTable.getItems().size() == 0) {
            DialogHandler.inform("Remove Association", "There are no parts to disassociate.");
            return;
        } else if (associatedTable.getSelectionModel().isEmpty()) {
            DialogHandler.inform("Remove Association", "Please select a part to disassociate.");
            return;
        }

        Part selectedPart = associatedTable.getSelectionModel().getSelectedItem();
        if (DialogHandler.confirm("Remove Association", "Do you want to remove association with \"" + selectedPart.getName() + "\"?")) {
            associatedTable.getItems().remove(selectedPart);
            associatedTable.refresh();
            associatedTable.sort();
        }

        associatedTable.getSelectionModel().clearSelection();
    }

    /**
     * Updates the selected product and any associated parts.
     * Called when the user clicks the save button.
     * Validates input and provides errors if necessary, otherwise replace the selected product with the new product and associated parts.
     * If product is successfully updated, return to the MainScreen view.
     *
     * @param actionEvent
     * @throws IOException
     */
    public void saveProduct(ActionEvent actionEvent) throws IOException {
        if (DialogHandler.confirm("Modify Product", "Do you want to save the changes to the product?")) {
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

            Product newProduct = new Product(selectedProduct.getId(), name, price, stock, min, max);
            for (Part p : associatedTable.getItems())
                newProduct.addAssociatedPart(p);
            Inventory.updateProduct(Inventory.getAllProducts().indexOf(selectedProduct), newProduct);

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
        if (DialogHandler.confirm("Modify Product", "Do you want to cancel modifying this product?")) {
            Parent parent = FXMLLoader.load(getClass().getResource("/view/MainScreen.fxml"));
            Scene scene = new Scene(parent);
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        }
    }
}
