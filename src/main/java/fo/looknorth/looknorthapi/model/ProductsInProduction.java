package fo.looknorth.looknorthapi.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jakup on 3/17/16.
 */
public class ProductsInProduction {
    public ArrayList<Machine> machines;
    public ArrayList<Product> productList;

    public ProductsInProduction() {

        Product product = new Product(1, "4L box", 1);

        machines = new ArrayList<>();

        machines.add(new Machine(1, product));
        machines.add(new Machine(2, product));
        machines.add(new Machine(3, product));
        machines.add(new Machine(4, product));
        machines.add(new Machine(5, product));


        productList = new ArrayList<>();
        productList.add(new Product(0, "Not active", 9));
        productList.add(new Product(2001, "Kassi - 20 kg",1));
        productList.add(new Product(2003, "Kassi - 3 kg, 30 x 40 cm", 1));
        productList.add(new Product(2004, "Lok - 3 og 5 kg, 30 x 40 cm", 1));
        productList.add(new Product(2005, "Kassi - 5 kg, 30 x 40 cm", 1));
        productList.add(new Product(2008, "Kassi - 40 x 60 cm",1));
        productList.add(new Product(2009, "Lok - til 40 x 60 cm kassa",1));
        productList.add(new Product(2026, "Flogkassi - 20 kg",1));
        productList.add(new Product(2027, "Lok - til flogkassa 20 kg",1));
        productList.add(new Product(2030, "Lok - til hummarakassa",1));
        productList.add(new Product(2033, "Hummarakassi - (AIR300)",1));
        productList.add(new Product(2340, "Kassi - 340 litur",1));
        productList.add(new Product(2341, "Lok - 340 litur",1));
        productList.add(new Product(3001, "Flot - 90 cm O270",1));
        productList.add(new Product(3004, "Flot - 120 cm O370 - halv",1));
        productList.add(new Product(10025, "Plata - L150H 25 mm",1));
        productList.add(new Product(10050, "Plata - L150H 50 mm",1));
        productList.add(new Product(10075, "Plata - L150H 75 mm",1));
        productList.add(new Product(10100, "Plata - L150H 100 mm",1));
        productList.add(new Product(10125, "Plata - L150H 125 mm",1));
        productList.add(new Product(10150, "Plata - L150H 150 mm",1));
        productList.add(new Product(20050, "Plata - L120H 50 mm",1));
        productList.add(new Product(20075, "Plata - L120H 75 mm",1));
        productList.add(new Product(20100, "Plata - L120H 100 mm",1));
        productList.add(new Product(20150, "Plata - L120H 150 mm",1));
        productList.add(new Product(30025, "Plata - L80H 25 mm",1));
        productList.add(new Product(30050, "Plata - L80H 50 mm",1));
        productList.add(new Product(30100, "Plata - L80H 100 mm",1));
        productList.add(new Product(31075, "Plata - L80H 75 mm",1));
        productList.add(new Product(32125, "Plata - L80H 125 mm",1));
        productList.add(new Product(32150, "Plata - L80H 150 mm",1));
        productList.add(new Product(39050, "Plata - L250H 50 mm",1));
        productList.add(new Product(39100, "Plata - L250H 100 mm",1));
    }

}
