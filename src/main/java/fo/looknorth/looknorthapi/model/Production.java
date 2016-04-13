/*
 * This project is owned by Jákup Høj, redistribution is not allowed.
 */
package fo.looknorth.looknorthapi.model;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author jakup
 */
public class Production {
  
  private int id;
  private int machineId;
  private int productId;
  private String recorded;
  
  public Production() {}
  public Production(int id, int machineId, int productId, Timestamp recorded) {
    this.id = id;
    this.machineId = machineId;
    this.productId = productId;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MMM-dd HH:MM:SS");
    this.recorded = sdf.format(new Date(recorded.getTime()));
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

  public int getProductId() {
    return productId;
  }

  public void setProductId(int productId) {
    this.productId = productId;
  }

  public String getRecorded() {
    return recorded;
  }

  public void setRecorded(String recorded) {
    this.recorded = recorded;
  }
  
}

