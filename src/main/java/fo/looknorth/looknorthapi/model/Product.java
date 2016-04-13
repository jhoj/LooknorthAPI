package fo.looknorth.looknorthapi.model;

/**
 * Created by jakup on 3/17/16.
 */
public class Product {
    public int id;
    public String name;
    public int quantity;

    public Product(){}

    public Product(int id, String name, int quantity)
    {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return name;
    }
}
