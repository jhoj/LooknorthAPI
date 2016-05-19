/*
 * This project is owned by Jákup Høj, redistribution is not allowed.
 */
package fo.looknorth.looknorthapi.model;

/**
 *
 * @author jakup
 */
public class AverageOilConsumption {
  
  private int id;
  private String machineCombination;
  private String productCombination;
  private float average;

  public AverageOilConsumption() {
  }

  public AverageOilConsumption(int id, String machineCombination, String productCombination, float average) {
    this.id = id;
    this.machineCombination = machineCombination;
    this.productCombination = productCombination;
    this.average = average;
  }

  public float getAverage() {
    return average;
  }

  public void setAverage(float average) {
    this.average = average;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getProductCombination() {
    return productCombination;
  }

  public void setProductCombination(String productCombination) {
    this.productCombination = productCombination;
  }

  public String getMachineCombination() {
    return machineCombination;
  }

  public void setMachineCombination(String machineCombination) {
    this.machineCombination = machineCombination;
  }
  
  
}
