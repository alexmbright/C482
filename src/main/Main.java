package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.*;

/**
 * The main class of the application.
 * <p>
 *     <b>FUTURE ENHANCEMENT:</b> Allow the user to set their own IDs for parts and products, but validate that it is unique.
 * </p>
 * <p>
 *     <b>RUNTIME ERRORS:</b> I did not encounter any errors while testing logic.
 * </p>
 *
 * @author Alex Bright
 */
public class Main extends Application {

    /**
     * Launches the application.
     * Populates the sample data and opens the MainScreen view.
     *
     * @param stage
     * @throws Exception
     */
    @Override
    public void start(Stage stage) throws Exception {
        populateData();

        Parent root = FXMLLoader.load(getClass().getResource("/view/MainScreen.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    /**
     * The entry point of the Java program.
     * Javadocs can be found inside the "/javadoc" folder.
     *
     * @param args
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Populates the sample data.
     */
    public void populateData() {
        Part wheel = new InHouse(1, "Wheel", 249.99, 24, 1, 48, 15);
        Part tire = new Outsourced(2, "Tire", 149.99, 16, 1, 32, "Bright Tire Shop");
        Part spring = new InHouse(5, "Spring", 49.99, 48, 1, 96, 20);
        Part shock = new Outsourced(6, "Shock", 99.99, 144, 1, 288, "The Suspension Experts, LLC");

        Inventory.addPart(wheel);
        Inventory.addPart(tire);
        Inventory.addPart(spring);
        Inventory.addPart(shock);

        Product coilover = new Product(1, "Coilover", 249.99, 48, 1, 96);
        Product combo = new Product(4, "Wheel/Tire Combo", 499.99, 16, 1, 32);

        Inventory.addProduct(coilover);
        Inventory.addProduct(combo);

        coilover.addAssociatedPart(spring);
        coilover.addAssociatedPart(shock);

        combo.addAssociatedPart(wheel);
        combo.addAssociatedPart(tire);
    }

    /**
     * Tests if provided string is an integer.
     *
     * @param testStr   string to test
     * @return          true if provided string is an integer, otherwise false
     */
    public static boolean isInt(String testStr) {
        if (testStr == null) return false;
        try {
            int i = Integer.parseInt(testStr);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    /**
     * Tests if provided string is a double.
     *
     * @param testStr   string to test
     * @return          true if provided string is a double, otherwise false
     */
    public static boolean isDouble(String testStr) {
        if (testStr == null) return false;
        try {
            double d = Double.parseDouble(testStr);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

}