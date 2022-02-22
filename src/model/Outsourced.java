package model;

/**
 * This class defines an Outsourced part.
 * Inherits the Part class.
 *
 * @author Alex Bright
 */
public class Outsourced extends Part {

    private String companyName;

    /**
     * Creates a new outsourced part.
     *
     * @param id            ID
     * @param name          name
     * @param price         price/cost
     * @param stock         inventory level
     * @param min           minimum
     * @param max           maximum
     * @param companyName   outsourced company name
     */
    public Outsourced(int id, String name, double price, int stock, int min, int max, String companyName) {
        super(id, name, price, stock, min, max);
        this.companyName = companyName;
    }

    /**
     * Sets the outsourced company name of the part.
     *
     * @param companyName   outsourced company name
     */
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    /**
     * Retrieves the outsourced company name of the part.
     *
     * @return  outsourced company name
     */
    public String getCompanyName() {
        return companyName;
    }

}
