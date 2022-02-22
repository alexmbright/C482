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
 * The controller for the MainScreen view.
 *
 * @author Alex Bright
 */
public class MainScreenController implements Initializable {

    @FXML private TextField partSearchField;
    @FXML private TextField prodSearchField;

    @FXML private TableView<Part> partTable;
    @FXML private TableColumn<Part, Integer> partIdCol;
    @FXML private TableColumn<Part, String> partNameCol;
    @FXML private TableColumn<Part, Integer> partInvCol;
    @FXML private TableColumn<Part, Double> partPriceCol;

    @FXML private TableView<Product> productTable;
    @FXML private TableColumn<Product, Integer> prodIdCol;
    @FXML private TableColumn<Product, String> prodNameCol;
    @FXML private TableColumn<Product, Integer> prodInvCol;
    @FXML private TableColumn<Product, Double> prodPriceCol;

    /**
     * Initializes the controller.
     * The tables are populated with parts and products.
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

        productTable.setItems(Inventory.getAllProducts());
        prodIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        prodNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        prodInvCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        prodPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        productTable.getSortOrder().add(prodIdCol);
    }

    /**
     * Opens the add part screen.
     * Called when the user clicks the add button under the products table.
     *
     * @param actionEvent
     * @throws IOException
     */
    public void addPart(ActionEvent actionEvent) throws IOException {
        Parent parent = FXMLLoader.load(getClass().getResource("/view/AddPart.fxml"));
        Scene scene = new Scene(parent);
        Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Opens the modify part screen.
     * Called when the user clicks the modify button under the parts table.
     * Provides selection validation and sends selected part criteria to the ModifyPart view.
     *
     * @param actionEvent
     * @throws IOException
     */
    public void modifyPart(ActionEvent actionEvent) throws IOException {
        if (partTable.getItems().size() == 0) {
            DialogHandler.inform("Modify Part", "There are no parts to modify.");
            return;
        } else if (partTable.getSelectionModel().isEmpty()) {
            DialogHandler.inform("Modify Part", "Please select a part to modify.");
            return;
        }

        Part selectedPart = partTable.getSelectionModel().getSelectedItem();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/ModifyPart.fxml"));
        ModifyPartController controller = new ModifyPartController(selectedPart);
        loader.setController(controller);
        Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        stage.setScene(new Scene(loader.load()));
        stage.show();
    }

    /**
     * Deletes a part from the inventory.
     * Called when the user clicks the delete button under the parts table.
     * Provides selection validation.
     *
     * @param actionEvent
     */
    public void deletePart(ActionEvent actionEvent) {
        if (partTable.getItems().size() == 0) {
            DialogHandler.inform("Delete Part", "There are no parts to delete.");
            return;
        } else if (partTable.getSelectionModel().isEmpty()) {
            DialogHandler.inform("Delete Part", "Please select a part to delete.");
            return;
        }

        Part selectedPart = partTable.getSelectionModel().getSelectedItem();
        if (DialogHandler.confirm("Delete Part", "Do you want to delete \"" + selectedPart.getName() + "\" with ID " + selectedPart.getId() + "?"))
            if (Inventory.deletePart(selectedPart)) {
                partTable.refresh();
                partTable.sort();
            } else DialogHandler.error("Delete Part", "\"" + selectedPart.getName() + "\" with ID " + selectedPart.getId() + " could not be deleted!");

        partTable.getSelectionModel().clearSelection();
    }

    /**
     * Opens the add product screen.
     * Called when the user clicks the add button under the products table.
     *
     * @param actionEvent
     * @throws IOException
     */
    public void addProduct(ActionEvent actionEvent) throws IOException {
        Parent parent = FXMLLoader.load(getClass().getResource("/view/AddProduct.fxml"));
        Scene scene = new Scene(parent);
        Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Opens the modify product screen.
     * Called when the user clicks the modify button under the products table.
     * Provides selection validation and sends the selected product criteria to the ModifyProduct view.
     *
     * @param actionEvent
     * @throws IOException
     */
    public void modifyProduct(ActionEvent actionEvent) throws IOException {
        if (productTable.getItems().size() == 0) {
            DialogHandler.inform("Modify Product", "There are no products to modify.");
            return;
        } else if (productTable.getSelectionModel().isEmpty()) {
            DialogHandler.inform("Modify Product", "Please select a product to modify.");
            return;
        }

        Product selectedProduct = productTable.getSelectionModel().getSelectedItem();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/ModifyProduct.fxml"));
        ModifyProductController controller = new ModifyProductController(selectedProduct);
        loader.setController(controller);
        Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        stage.setScene(new Scene(loader.load()));
        stage.show();
    }

    /**
     * Deletes a product from the inventory.
     * Called when the user clicks the delete button under the products table.
     * Provides selection validation.
     *
     * @param actionEvent
     */
    public void deleteProduct(ActionEvent actionEvent) {
        if (productTable.getItems().size() == 0) {
            DialogHandler.inform("Delete Product", "There are no products to delete.");
            return;
        } else if (productTable.getSelectionModel().isEmpty()) {
            DialogHandler.inform("Delete Product", "Please select a product to delete.");
            return;
        }

        Product selectedProd = productTable.getSelectionModel().getSelectedItem();
        if (DialogHandler.confirm("Delete Product", "Do you want to delete \"" + selectedProd.getName() + "\" with ID " + selectedProd.getId() + "?"))
            if (!selectedProd.getAllAssociatedParts().isEmpty()) {
                DialogHandler.warn("Delete Product", "Please remove all associated parts from \"" + selectedProd.getName() + "\" before deleting!");
                return;
            }
            if (Inventory.deleteProduct(selectedProd)) {
                productTable.refresh();
                productTable.sort();
            } else
                DialogHandler.error("Delete Product", "\"" + selectedProd.getName() + "\" with ID " + selectedProd.getId() + " could not be deleted!");

        productTable.getSelectionModel().clearSelection();
    }

    /**
     * Searches for a part when the user presses "enter" in the search box above the parts table.
     * Allows the use of ID, partial name, or full name.
     * If input is an integer, searches both ID and name simultaneously.
     *
     * @param keyEvent
     */
    public void searchPartKeyPressed(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.ENTER) {
            String searchStr = partSearchField.getText().trim();
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
     * Searches for a product when the user presses "enter" in the search box above the products table.
     * Allows the use of ID, partial name, or full name.
     * If input is an integer, searches both ID and name simultaneously.
     *
     * @param keyEvent
     */
    public void searchProductKeyPressed(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.ENTER) {
            String searchStr = prodSearchField.getText().trim();
            if (searchStr.isEmpty()) {
                productTable.setItems(Inventory.getAllProducts());
                productTable.getSortOrder().add(prodIdCol);
            } else {
                ObservableList<Product> searchedProducts = FXCollections.observableArrayList();
                if (Main.isInt(searchStr)) {
                    Product p = Inventory.lookupProduct(Integer.parseInt(searchStr));
                    if (p != null) searchedProducts.add(p);
                }
                searchedProducts.addAll(Inventory.lookupProduct(searchStr));
                if (searchedProducts.isEmpty()) {
                    DialogHandler.inform("Search Product", "No products found using that search criteria.");
                    productTable.setItems(Inventory.getAllProducts());
                    productTable.getSortOrder().add(prodIdCol);
                    return;
                }
                productTable.setItems(searchedProducts);
            }
        }
    }

    /**
     * Exits the system.
     * If the user confirms, the stage closes and the program exits.
     *
     * @param actionEvent
     */
    public void exit(ActionEvent actionEvent) {
        if (DialogHandler.confirm("Exit Program", "Do you want to exit the Inventory Management System?")) {
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.close();
            System.exit(0);
        }
    }

}
