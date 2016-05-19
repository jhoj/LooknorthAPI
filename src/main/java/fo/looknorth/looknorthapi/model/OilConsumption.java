/*
 * This project is owned by Jákup Høj, redistribution is not allowed.
 */
package fo.looknorth.looknorthapi.model;

/**
 *
 * @author jakup
 */
public class OilConsumption {
  
  private int id;
  private int machineId;
  private float liters;
  private String recorded;

  public OilConsumption(int id, int machineId, float liters, String recorded) {
    this.id = id;
    this.machineId = machineId;
    this.liters = liters;
    this.recorded = recorded;
  }

  public OilConsumption() {
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public int getMachineId() {
    return machineId;
  }

  public void setMachineId(int machineId) {
    this.machineId = machineId;
  }

  public float getLiters() {
    return liters;
  }

  public void setLiters(float liters) {
    this.liters = liters;
  }

  public String getRecorded() {
    return recorded;
  }

  public void setRecorded(String recorded) {
    this.recorded = recorded;
  }
  
}
