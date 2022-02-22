package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Handles the inventory management tasks.
 *
 * @author Alex Bright
 */
public class Inventory {

    private static ObservableList<Part> allParts = FXCollections.observableArrayList();
    private static ObservableList<Product> allProducts = FXCollections.observableArrayList();

    /**
     * Adds a new part to the parts list.
     *
     * @param newPart   the part to be added
     */
    public static void addPart(Part newPart) {
        allParts.add(newPart);
    }

    /**
     * Adds a new product to the products list.
     *
     * @param newProduct    the product to be added
     */
    public static void addProduct(Product newProduct) {
        allProducts.add(newProduct);
    }

    /**
     * Searches for part by ID.
     *
     * @param partId    ID of the requested part
     * @return          the requested part, otherwise null
     */
    public static Part lookupPart(int partId) {
        for (Part p : allParts)
            if (p.getId() == partId)
                return p;
        return null;
    }

    /**
     * Searches for part by name.
     *
     * @param partName  name of the requested part(s)
     * @return          an ObservableList of the requested part(s)
     */
    public static ObservableList<Part> lookupPart (String partName) {
        ObservableList<Part> parts = FXCollections.observableArrayList();
        for (Part p : allParts)
            if (p.getName().toLowerCase().contains(partName.toLowerCase()))
                parts.add(p);
        return parts;
    }

    /**
     * Searches for product by ID.
     *
     * @param productId ID of the requested product
     * @return          the requested product, otherwise null
     */
    public static Product lookupProduct(int productId) {
        for (Product p : allProducts)
            if (p.getId() == productId)
                return p;
        return null;
    }

    /**
     * Searches for product by name.
     *
     * @param productName   name of the requested product(s)
     * @return              an ObservableList of the requested product(s)
     */
    public static ObservableList<Product> lookupProduct(String productName) {
        ObservableList<Product> products = FXCollections.observableArrayList();
        for (Product p : allProducts)
            if (p.getName().toLowerCase().contains(productName.toLowerCase()))
                products.add(p);
        return products;
    }

    /**
     * Updates a specific part in the inventory.
     *
     * @param index         index of part to update
     * @param selectedPart  updated part
     */
    public static void updatePart(int index, Part selectedPart) {
        allParts.set(index, selectedPart);
    }

    /**
     * Updates a specific product in the inventory.
     *
     * @param index         index of product to update
     * @param newProduct    updated product
     */
    public static void updateProduct(int index, Product newProduct) {
        allProducts.set(index, newProduct);
    }

    /**
     * Deletes a specific part from the inventory.
     *
     * @param selectedPart  part to delete
     * @return              true if part was removed from inventory, otherwise false
     */
    public static boolean deletePart(Part selectedPart) {
        return allParts.remove(selectedPart);
    }

    /**
     * Deletes a specific product from the inventory.
     *
     * @param selectedProduct   product to delete
     * @return                  true if product was removed from inventory, otherwise false
     */
    public static boolean deleteProduct(Product selectedProduct) {
        return allProducts.remove(selectedProduct);
    }

    /**
     * Retrieves list of all parts in the inventory.
     *
     * @return  an ObservableList of all parts in the inventory
     */
    public static ObservableList<Part> getAllParts() {
        return allParts;
    }

    /**
     * Retrieves list of all products in the inventory.
     *
     * @return  an ObservableList of all products in the inventory.
     */
    public static ObservableList<Product> getAllProducts() {
        return allProducts;
    }

    /**
     * Prioritizes missing gaps in the ID list, or returns a new highest ID. If no ID is present, an ID of 1 is returned.
     *
     * @return  available part ID
     */
    public static int getNextPartId() {
        int id = 1;
        if (!allParts.isEmpty()) {
            ArrayList<Integer> ids = new ArrayList<Integer>();
            for (Part p : allParts) ids.add(p.getId());
            Collections.sort(ids);
            for (int i = 0; i < ids.size(); i++) {
                if ((i == 0 && ids.get(i) > 1) || (i > 0 && ids.get(i - 1) + 1 < ids.get(i))) break;
                id = ids.get(i) + 1;
            }
        }
        return id;
    }

    /**
     * Prioritizes missing gaps in the ID list, or returns a new highest ID. If no ID is present, an ID of 1 is returned.
     *
     * @return  available product ID
     */
    public static int getNextProductId() {
        int id = 1;
        if (!allParts.isEmpty()) {
            ArrayList<Integer> ids = new ArrayList<Integer>();
            for (Product p : allProducts) ids.add(p.getId());
            Collections.sort(ids);
            for (int i = 0; i < ids.size(); i++) {
                if ((i == 0 && ids.get(i) > 1) || (i > 0 && ids.get(i - 1) + 1 < ids.get(i))) break;
                id = ids.get(i) + 1;
            }
        }
        return id;
    }

}
