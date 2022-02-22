package main;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.Optional;

/**
 * Handles the use of dialog when needed.
 *
 * @author Alex Bright
 */
public class DialogHandler {

    /**
     * Opens a confirm dialog.
     *
     * @param title     dialog box title
     * @param message   dialog box header text
     * @return          true if the user wishes to perform action, otherwise false
     */
    public static boolean confirm(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(message);
        alert.setContentText("Click OK to confirm.");
        Optional<ButtonType> option = alert.showAndWait();
        return option.get() == ButtonType.OK;
    }

    /**
     * Opens a warning dialog.
     *
     * @param title     dialog box title
     * @param message   dialog box header text
     */
    public static void warn(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(message);
        alert.setContentText("Click OK to close this window.");
        alert.show();
    }

    /**
     * Opens an information dialog.
     *
     * @param title     dialog box title
     * @param message   dialog box header text
     */
    public static void inform(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(message);
        alert.setContentText("Click OK to close this window.");
        alert.show();
    }

    /**
     * Opens an error dialog.
     *
     * @param title     dialog box title
     * @param message   dialog box header text
     */
    public static void error(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(message);
        alert.setContentText("Click OK to close this window.");
        alert.show();
    }

}
