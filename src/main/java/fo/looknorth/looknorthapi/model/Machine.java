package fo.looknorth.looknorthapi.model;

/**
 * Created by jakup on 3/17/16.
 */
public class Machine {

    public int machineNumber;
    public Product currentProduct;

    public Machine() {}

    public Machine(int machineNumber, Product product)
    {
        this.machineNumber = machineNumber;
        this.currentProduct = product;
    }
}
