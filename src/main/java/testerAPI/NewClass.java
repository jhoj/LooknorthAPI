/*
 * This project is owned by Jákup Høj, redistribution is not allowed.
 */
package testerAPI;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import fo.looknorth.looknorthapi.model.Machine;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 *
 * @author jakup
 */
public class NewClass {
  
  public List<Machine> getMachines() {
        List<Machine> machines = null;

        try {
            InputStream is = new URL("http://localhost:4567/machine").openStream();

            byte b[] = new byte[is.available()]; // kun små filer
            is.read(b);
            String str = new String(b, "UTF-8");
            Gson gson = new Gson();
            Type machineType = new TypeToken<Collection<Machine>>(){}.getType();
            machines =  gson.fromJson(str, machineType);

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return machines;
    }
  
  public static void main(String[] args) {
    NewClass nc = new NewClass();
      List<Machine> machines = nc.getMachines();
      
      for (Machine machine : machines) {
        System.out.println(machine.currentProduct.name);
        System.out.println(machine.machineNumber);
    }
  }
}
