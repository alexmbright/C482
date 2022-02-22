package model;

/**
 * This class defines an In-House part.
 * Inherits the Part class.
 *
 * @author Alex Bright
 */
public class InHouse extends Part {

    private int machineId;

    /**
     * Creates a new in-house part.
     *
     * @param id        ID
     * @param name      name
     * @param price     price/cost
     * @param stock     inventory level
     * @param min       minimum
     * @param max       maximum
     * @param machineId in-house machine ID
     */
    public InHouse(int id, String name, double price, int stock, int min, int max, int machineId) {
        super(id, name, price, stock, min, max);
        this.machineId = machineId;
    }

    /**
     * Sets the in-house machine ID of the part.
     *
     * @param machineId in-house machine ID
     */
    public void setMachineId(int machineId) {
        this.machineId = machineId;
    }

    /**
     * Retrieves the in-house machine ID of the part.
     *
     * @return in-house machine ID
     */
    public int getMachineId() {
        return machineId;
    }
}
