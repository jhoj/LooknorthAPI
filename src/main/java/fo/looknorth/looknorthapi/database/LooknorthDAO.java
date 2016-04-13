package fo.looknorth.looknorthapi.database;

import fo.looknorth.looknorthapi.model.AverageOilUsage;
import fo.looknorth.looknorthapi.model.Machine;
import fo.looknorth.looknorthapi.model.OilUsage;
import fo.looknorth.looknorthapi.model.Product;
import fo.looknorth.looknorthapi.model.Production;
import fo.looknorth.utilities.Organiser;
import java.sql.Timestamp;
import java.util.List;

/**
 *
 * @author Jakup
 * @since 24.02.16
 DAO = DatabaseAccessObject
 Interface defining what database methods need to be implemented to use the full system.
 */

public interface LooknorthDAO {
  /**
   *saves the mqtt messages to the database
   * @param organiser sorts the mqtt message into smaller pieces.
   */
  public abstract boolean saveMessage(Organiser organiser);
  
  /**
   * Saves the current product combination, so that it can be found later when
   * an average has to be found.
   * This method needs to handle already existing combinations too, so they 
   * don't get overwritten in the database.
   * @param products list of product active in the machines.
   */
  public abstract boolean saveProductCombination(List<Product> products);
  
  /**
   * Gets the oil usage for the whole system
   * @return the oil usage in L/min, as a float.
   */
  public abstract List<OilUsage> getOilUsage();
  
   /**
   * Gets the oil usage for the given date
   * @return a list with oil usage objects.
   */
  public abstract List<OilUsage> getOilUsageByDate(Timestamp date);
  
  /**
   * Gets the last inserted oil usage entry
   * @return the oil usage object.
   */
  public abstract OilUsage getLastOilUsage();
  
  /**
   * Finds the list with average oil usage 
   * @return a list of AverageOilUsage objects
   */
  public abstract List<AverageOilUsage> getAverageOilUsage();
  
  /**
   * Finds the average oil usage for the given combination
   * @param productCombination String with the product id's
   * @return AverageOilUsage object
   * N.B. products are separated by '-'!
   */
  public abstract AverageOilUsage getAverageOilUsage(String productCombination);
  
  /**
   * Finds a list of average oil usage details for a given machine
   * @param machineId the machine identification number.
   * @return a List of AverageOilUsage objects
   */
  public abstract List<AverageOilUsage> getAverageOilUsage(int machineId);
  
  /**
   * Finds an average oil usage object for the given machine and product
   * @param machineId
   * @param productId
   * @return 
   */
  public abstract AverageOilUsage getAverageOilUsage(int machineId, int productId);
  /**
   * tells which product is active in the system
   * @param machine the machine in the system
   */
  public abstract void setActiveProduct(Machine machine);
  
  /**
   * Sets the products that are active for all the machines.
   * @param machines 
   */
  public abstract void setActiveProducts(List<Machine> machines);
  
  /**
   * Gets the product active in the machine
   * @param machine the machine in the system.
   * @return the product which is currently in production.
   */
  public abstract Product getActiveProduct(int machineId);
  
  /**
   * Gets the list of the products active in the current production
   * @return a list with Products 
   */
  public abstract List<Product> getActiveProductList();
  
  /**
   * @return a list with all the products the Looknorth produce. 
   */
  public abstract List<Product> getProductList();
  
  /**
   * @param productId is the identification number for the product
   * @return an object of class Product.
   */
  public abstract Product getProduct(int productId);
  
    /**
   * @return a list with all the machines at Looknorth. 
   */
  public abstract List<Machine> getMachineList();
  
  /**
   * @param machineId is the identification number for the machine
   * @return an object of class Machine.
   */
  public abstract Machine getMachine(int machineId);
  
  /**
   * Gets all entries for production
   * @return List of production entries.
   */
  public abstract List<Production> getProductionList();
  
  /**
   * Gets the production for today
   * @param today Date object for the current day.
   * @return List of Production objects
   */
  public abstract List<Production> getProductionList(Timestamp today);
  
  /**
   * The last production cycle
   * @return Production object from the last cycle.
   */
  public abstract Production getLastProduction();
  
  /**
   * last cycle for the given product
   * @param product will use the productId to find last entry
   * @return Production object
   */
  public abstract Production getLastProductionForProduct(int productId);
  
  /**
   * last cycle based on the machine 
   * @param machineId the machineId number representing a machine in the production
   * @return the last entered Production object.
   */
  public abstract Production getLastProductionForMachine(int machineId);

  /**
   * 
   * @param machineId the machine number
   * @return A list with production objects
   */
  public abstract List<Production> getProductionListForMachine(int machineId);
  
   /**
   * 
   * @param productId the product number
   * @return A list with production objects
   */
  public abstract List<Production> getProductionListForProduct(int productId);
  
  /**
   * 
   * @param machineId the machine number
   * @param date the date to get productions, e.g. today.
   * @return A list with production objects
   */
  public abstract List<Production> getProductionListForMachine(int machineId, Timestamp date);
  
   /**
   * 
   * @param productId the product number
   * @param date the date to get productions, e.g. today.
   * @return A list with production objects
   */
  public abstract List<Production> getProductionListForProduct(int productId, Timestamp date);
  
}
