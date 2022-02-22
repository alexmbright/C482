package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 *
 * @author Alex Bright
 */
public class Product {

    private ObservableList<Part> associatedParts = FXCollections.observableArrayList();
    private int id;
    private String name;
    private double price;
    private int stock;
    private int min;
    private int max;

    /**
     * Creates a new product.
     *
     * @param id    ID
     * @param name  name
     * @param price price/cost
     * @param stock inventory level
     * @param min   minimum
     * @param max   maximum
     */
    public Product(int id, String name, double price, int stock, int min, int max) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.min = min;
        this.max = max;
    }

    /**
     * Sets the ID of the product.
     *
     * @param id    ID of the product
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Sets the name of the product.
     *
     * @param name  name of the product
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Sets the price of the product.
     *
     * @param price price of the product
     */
    public void setPrice(double price) {
        this.price = price;
    }

    /**
     * Sets the minimum of the product.
     *
     * @param min   minimum
     */
    public void setMin(int min) {
        this.min = min;
    }

    /**
     * Sets the maximum of the product.
     *
     * @param max   maximum
     */
    public void setMax(int max) {
        this.max = max;
    }

    /**
     * @return  ID of the product
     */
    public int getId() {
        return id;
    }

    /**
     * @return  name of the product
     */
    public String getName() {
        return name;
    }

    /**
     * @return  price of the product
     */
    public double getPrice() {
        return price;
    }

    /**
     * @return  inventory level of the product
     */
    public int getStock() {
        return stock;
    }

    /**
     * @return  minimum of the product
     */
    public int getMin() {
        return min;
    }

    /**
     * @return  maximum of the product
     */
    public int getMax() {
        return max;
    }

    /**
     * Adds an associated part to the product.
     *
     * @param part  the part to associate
     */
    public void addAssociatedPart(Part part) {
        associatedParts.add(part);
    }

    /**
     * Deletes an associated part to the product.
     *
     * @param selectedAssociatedPart    the part to disassociate
     * @return                          true if the part could be removed from the associated list, otherwise false
     */
    public boolean deleteAssociatedPart(Part selectedAssociatedPart) {
        return associatedParts.remove(selectedAssociatedPart);
    }

    /**
     * @return  an ObservableList of all associated parts
     */
    public ObservableList<Part> getAllAssociatedParts() {
        return associatedParts;
    }
}
