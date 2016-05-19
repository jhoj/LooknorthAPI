package fo.looknorth.looknorthapi.database;

import fo.looknorth.looknorthapi.model.AverageOilConsumption;
import fo.looknorth.looknorthapi.model.Machine;
import fo.looknorth.looknorthapi.model.OilConsumption;
import fo.looknorth.looknorthapi.model.Product;
import fo.looknorth.looknorthapi.model.Production;
import fo.looknorth.utilities.Organiser;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.HashMap;

/**
 * @since 24.02.2016
 * @author jakuphoj
 
 This Class implements the LooknorthDAO methods,
 and overwrites them so they work in Mariadb.
 */
public class MariadbLooknorthDAO implements LooknorthDAO {
    
    @Override
    public boolean saveMessage(Organiser organiser) {
        boolean result = false;
        String insertStatement;
        boolean fromMachines = false;
        
        if (organiser.getSUBJECT().contains("machines")) {
            fromMachines = true;
            
            insertStatement = "INSERT looknorth.production (m_id, p_id) "
                    + "SELECT products_active.m_id, products_active.p_id "
                    + "FROM products_active WHERE products_active.m_id= (?)";            
        }
        else {
            insertStatement = "insert into looknorth.oil_usage (m_id, liters) " +
                    "values (?,?)";
        }
        
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        
        try {
            connection = MariadbDAOFactory.createConnection();

            preparedStatement = connection.prepareStatement(insertStatement);

            if (fromMachines) {

                preparedStatement.setInt(1, Integer.parseInt(organiser.getSENSOR()));
            }
            else {
                preparedStatement.setInt(1, Integer.parseInt(organiser.getSENSOR()));
                preparedStatement.setFloat(2, Float.parseFloat(organiser.getMESSAGE()));
                System.out.println(organiser.getMESSAGE());
            }
            
            int returnValue = preparedStatement.executeUpdate();
            
            result = (returnValue == 1) ? true: false;
          
        } catch (SQLException ex) {
            Logger.getLogger(MariadbLooknorthDAO.class.getName()).log(Level.SEVERE, null, ex);
        }  finally {
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException ex) {
                    Logger.getLogger(MariadbLooknorthDAO.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException ex) {
                    Logger.getLogger(MariadbLooknorthDAO.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        
        return result;
    }

  @Override
  public boolean saveProductCombination(List<Product> products) {
//    
//    boolean result = false;
//    //check if the list already exists
//    Object[] obj = checkForProductCombination(products);
//    boolean found = (Boolean) obj[1];
//    
//    if(!found) {
//      //insert list into database
//      //get the names in the list of productsCombination, already sorted in 'checkForProductCombination' method.
//      String productCombination = (String) obj[0];
//      String insertStatement = "INSERT INTO looknorth.average_oil_usage (product_combination) values (?)";                           
//      Connection connection = null;
//      PreparedStatement preparedStatement = null;
//
//      try {
//          connection = MariadbDAOFactory.createConnection();
//
//          preparedStatement = connection.prepareStatement(insertStatement);
//          preparedStatement.setString(1, productCombination);
//          int returnValue = preparedStatement.executeUpdate();
//          
//          result = (returnValue == 1) ? true:false;
//          System.out.println("combination added");
//
//      } catch (SQLException ex) {
//          Logger.getLogger(MariadbLooknorthDAO.class.getName()).log(Level.SEVERE, null, ex);
//      }  finally {
//          if (preparedStatement != null) {
//              try {
//                  preparedStatement.close();
//              } catch (SQLException ex) {
//                  Logger.getLogger(MariadbLooknorthDAO.class.getName()).log(Level.SEVERE, null, ex);
//              }
//          }
//          if (connection != null) {
//              try {
//                  connection.close();
//              } catch (SQLException ex) {
//                  Logger.getLogger(MariadbLooknorthDAO.class.getName()).log(Level.SEVERE, null, ex);
//              }
//          }
//      }
//    }
//    else 
//    {
//      System.out.println("Combination already added.");
//    }
//    
//    return result;
      return false;
  }

  @Override
  public void setActiveProduct(Machine machine) {
        String updateStatement = "update looknorth.products_active set p_id = (?) where m_id = ?;";
        Connection connection = null;
        PreparedStatement preparedStatement = null;
    try {
        connection = MariadbDAOFactory.createConnection();
        preparedStatement = connection.prepareStatement(updateStatement);
        preparedStatement.setInt(1, machine.currentProduct.id);
        preparedStatement.setInt(2, machine.machineNumber);
        preparedStatement.executeQuery();
      } catch (SQLException ex) {
        Logger.getLogger(MariadbLooknorthDAO.class.getName()).log(Level.SEVERE, null, ex);
      } finally {
        if (preparedStatement != null) {
            try {
                preparedStatement.close();
            } catch (SQLException ex) {
                Logger.getLogger(MariadbLooknorthDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException ex) {
                Logger.getLogger(MariadbLooknorthDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
      }
  }

  @Override
  public Product getActiveProduct(int machineId) {
    Product result = null;
        
    String query = "select * from looknorth.products_active where m_id = ?";

    Connection connection = null;
    PreparedStatement preparedStatement = null;
      try {
        connection = MariadbDAOFactory.createConnection();
        preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, machineId);
        ResultSet resultSet = preparedStatement.executeQuery();
        //gets product list from db.
        List<Product> wholeProductList = getProductList();
        //insert into map to make it faster to search
        HashMap<Integer, Product> productMap = new HashMap<Integer, Product>();
        for (Product product : wholeProductList) {
          productMap.put(product.id, product);
        }
        
        if (resultSet.next()) {
          //find the product from productList, which has the same id as found in 
          //active products
          int id = resultSet.getInt("p_id");
          result = productMap.get(id);
        }
      } catch (SQLException ex) {
        Logger.getLogger(MariadbLooknorthDAO.class.getName()).log(Level.SEVERE, null, ex);
      } finally {
        if (preparedStatement != null) {
            try {
                preparedStatement.close();
            } catch (SQLException ex) {
                Logger.getLogger(MariadbLooknorthDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException ex) {
                Logger.getLogger(MariadbLooknorthDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    return result;
  }
  
  @Override
  public List<Product> getActiveProductList()
  {
    List<Product> list = new ArrayList<>();
        
    String query = "select * from looknorth.products_active";

    Connection connection = null;
    PreparedStatement preparedStatement = null;
      try {
        connection = MariadbDAOFactory.createConnection();
        preparedStatement = connection.prepareStatement(query);
        ResultSet resultSet = preparedStatement.executeQuery();
        //gets product list from db.
        List<Product> wholeProductList = getProductList();
        //insert into map to make it faster to search
        HashMap<Integer, Product> productMap = new HashMap<Integer, Product>();
        for (Product product : wholeProductList) {
          productMap.put(product.id, product);
        }
        
        while (resultSet.next()) {
          //find product from productList, which has the same id as found in 
          //active products
          int id = resultSet.getInt("p_id");
          
          Product p = productMap.get(id);
          list.add(p);
        }
      } catch (SQLException ex) {
        Logger.getLogger(MariadbLooknorthDAO.class.getName()).log(Level.SEVERE, null, ex);
      } finally {
        if (preparedStatement != null) {
            try {
                preparedStatement.close();
            } catch (SQLException ex) {
                Logger.getLogger(MariadbLooknorthDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException ex) {
                Logger.getLogger(MariadbLooknorthDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    return list;
  }

  @Override
  public List<Product> getProductList() {      
        List<Product> list = new ArrayList<>();
        
        String query = "select * from looknorth.products";
        
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
        connection = MariadbDAOFactory.createConnection();
        preparedStatement = connection.prepareStatement(query);
        ResultSet resultSet = preparedStatement.executeQuery();
        
        while (resultSet.next())
        {
          //create Product
          int id = resultSet.getInt("p_id");
          String name = resultSet.getString("description");
          int quantity = resultSet.getInt("quantity");
          Product p = new Product(id, name, quantity);
          //add to list
          list.add(p);
        } } catch (SQLException ex) {
        Logger.getLogger(MariadbLooknorthDAO.class.getName()).log(Level.SEVERE, null, ex);
      } finally {
          if (preparedStatement != null) {
            try {
                preparedStatement.close();
            } catch (SQLException ex) {
                Logger.getLogger(MariadbLooknorthDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException ex) {
                Logger.getLogger(MariadbLooknorthDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        }
      return list; 
  }

  @Override
  public void setActiveProducts(List<Machine> machines) {
        StringBuilder sb = new StringBuilder();
     
        //building insert string.
        for (int i = 1; i <= machines.size(); i++) {
          int p_id = machines.get(i).currentProduct.id;
          int m_id = machines.get(i).machineNumber;
          sb.append("update looknorth.products_active set p_id = " + p_id + " where m_id = " + m_id + ";");
    } 
        String update = sb.toString();
        
        Connection connection = null;
        PreparedStatement preparedStatement = null; 
    try {       
        connection = MariadbDAOFactory.createConnection();
        preparedStatement = connection.prepareStatement(update);
        
          preparedStatement.execute();
        } catch (SQLException ex) {
        Logger.getLogger(MariadbLooknorthDAO.class.getName()).log(Level.SEVERE, null, ex);
      } 
      finally {
      if (preparedStatement != null) {
            try {
                preparedStatement.close();
            } catch (SQLException ex) {
                Logger.getLogger(MariadbLooknorthDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException ex) {
                Logger.getLogger(MariadbLooknorthDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
  }

  @Override
  public Product getProduct(int productId) {
    Product result = null;
    String query = "select * from products where p_id = ?";
    Connection connection = null;
    PreparedStatement preparedStatement = null;  
    
    try {       
        connection = MariadbDAOFactory.createConnection();
        preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, productId);
        ResultSet resultSet = preparedStatement.executeQuery();
        
        if (resultSet.next())
        {
          int id = resultSet.getInt("p_id");
          String name = resultSet.getString("description");
          int quantity = resultSet.getInt("quantity");
          result = new Product(id, name, quantity);
        } } catch (SQLException ex) {
        Logger.getLogger(MariadbLooknorthDAO.class.getName()).log(Level.SEVERE, null, ex);
      }
    return result;
  }

  @Override
  public List<Machine> getMachineList() {
    List<Machine> machines = new ArrayList<>();
    String query = "select a.m_id, b.p_id, c.description, c.quantity " +
                    "from machines a " +
                    "join products_active b " +
                    "on a.m_id = b.m_id " +
                    "join products c " +
                    "on b.p_id = c.p_id";
    Connection connection = null;
    PreparedStatement preparedStatement = null;  
    
    try {       
        connection = MariadbDAOFactory.createConnection();
        preparedStatement = connection.prepareStatement(query);
        ResultSet resultSet = preparedStatement.executeQuery();
        
        while (resultSet.next())
        {
          int machineNumber = resultSet.getInt("m_id");
          int productNumber = resultSet.getInt("p_id");
          String productName = resultSet.getString("description");
          int productQuantity = resultSet.getInt("quantity");
          Machine result = new Machine(machineNumber, new Product(productNumber, productName, productQuantity));
          machines.add(result);
        } 
    } catch (SQLException ex) {
        Logger.getLogger(MariadbLooknorthDAO.class.getName()).log(Level.SEVERE, null, ex);
      }
    return machines;
  }

  @Override
  public Machine getMachine(int machineId) {
    Machine result = null;
    
    String query = "select a.m_id, b.p_id, c.description, c.quantity " + 
                   "from machines a " + 
                   "join products_active b " + 
                   "on a.m_id = b.m_id " + 
                   "join products c " + 
                   "on b.p_id = c.p_id " + 
                   "where a.m_id = ?";
    Connection connection = null;
    PreparedStatement preparedStatement = null;  
    
    try {       
        connection = MariadbDAOFactory.createConnection();
        preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, machineId);
        ResultSet resultSet = preparedStatement.executeQuery();
        
        if (resultSet.next())
        {
          int machineNumber = resultSet.getInt("m_id");
          int productNumber = resultSet.getInt("p_id");
          String productName = resultSet.getString("description");
          int productQuantity = resultSet.getInt("quantity");
          result = new Machine(machineNumber, new Product(productNumber, productName, productQuantity));
        } } catch (SQLException ex) {
        Logger.getLogger(MariadbLooknorthDAO.class.getName()).log(Level.SEVERE, null, ex);
      }
    return result;
  }

  @Override
  public List<Production> getProductionList() {
    List<Production> productions = new ArrayList<>();
    String query = "select * from production";
    Connection connection = null;
    PreparedStatement preparedStatement = null;
    
    try {       
        connection = MariadbDAOFactory.createConnection();
        preparedStatement = connection.prepareStatement(query);
        ResultSet resultSet = preparedStatement.executeQuery();
        
        while (resultSet.next())
        {
          int id = resultSet.getInt("id");
          int productId = resultSet.getInt("p_id");
          int machineId = resultSet.getInt("m_id");
          Timestamp recorded = resultSet.getTimestamp("recorded");
          productions.add(new Production(id, machineId, productId, recorded));
        } } catch (SQLException ex) {
        Logger.getLogger(MariadbLooknorthDAO.class.getName()).log(Level.SEVERE, null, ex);
      }
    return productions;
  }

  @Override
  public List<Production> getProductionList(Timestamp date) {
    List<Production> productions = new ArrayList<>();
    String query = "SELECT * FROM production " +
                   "WHERE recorded >= ? " +
                   "AND recorded < ? + INTERVAL 1 DAY;";
    Connection connection = null;
    PreparedStatement preparedStatement = null;
    
    try {       
        connection = MariadbDAOFactory.createConnection();
        preparedStatement = connection.prepareStatement(query);
        preparedStatement.setTimestamp(1, date);
        preparedStatement.setTimestamp(2, date);
        ResultSet resultSet = preparedStatement.executeQuery();
        
        while (resultSet.next())
        {
          int id = resultSet.getInt("id");
          int productId = resultSet.getInt("p_id");
          int machineIdd = resultSet.getInt("m_id");
          Timestamp recorded = resultSet.getTimestamp("recorded");
          productions.add(new Production(id, machineIdd, productId, recorded));
        } } catch (SQLException ex) {
        Logger.getLogger(MariadbLooknorthDAO.class.getName()).log(Level.SEVERE, null, ex);
      }
    return productions;
  }

  @Override
  public Production getLastProduction() {
    Production production = null;
    
    String query = "SELECT * FROM looknorth.production " + 
    "ORDER BY recorded DESC " +
    "LIMIT 1;";
    
    Connection connection = null;
    PreparedStatement preparedStatement = null;
    
    try {       
        connection = MariadbDAOFactory.createConnection();
        preparedStatement = connection.prepareStatement(query);
        ResultSet resultSet = preparedStatement.executeQuery();
        
        if (resultSet.next())
        {
          int id = resultSet.getInt("id");
          int productId = resultSet.getInt("p_id");
          int machineId = resultSet.getInt("m_id");
          Timestamp recorded = resultSet.getTimestamp("recorded");
          production = new Production(id, machineId, productId, recorded);
        } } catch (SQLException ex) {
        Logger.getLogger(MariadbLooknorthDAO.class.getName()).log(Level.SEVERE, null, ex);
      }
    return production;
  }

  @Override
  public Production getLastProductionForProduct(int productId) {
    Production production = null;
    
    String query = "SELECT * FROM looknorth.production " + 
    "WHERE p_id = ? " +
    "ORDER BY recorded DESC " +
    "LIMIT 1;";
    
    Connection connection = null;
    PreparedStatement preparedStatement = null;
    
    try {       
        connection = MariadbDAOFactory.createConnection();
        preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, productId);
        ResultSet resultSet = preparedStatement.executeQuery();
        
        if (resultSet.next())
        {
          int id = resultSet.getInt("id");
          int productIdd = resultSet.getInt("p_id");
          int machineId = resultSet.getInt("m_id");
          Timestamp recorded = resultSet.getTimestamp("recorded");
          production = new Production(id, machineId, productIdd, recorded);
        } } catch (SQLException ex) {
        Logger.getLogger(MariadbLooknorthDAO.class.getName()).log(Level.SEVERE, null, ex);
      }
    return production;
  }

  @Override
  public Production getLastProductionForMachine(int machineNumber) {
    Production production = null;
    
    String query = "SELECT * FROM looknorth.production " + 
    "WHERE m_id = ? " +
    "ORDER BY recorded DESC " +
    "LIMIT 1;";
    
    Connection connection = null;
    PreparedStatement preparedStatement = null;
    
    try {       
        connection = MariadbDAOFactory.createConnection();
        preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, machineNumber);
        ResultSet resultSet = preparedStatement.executeQuery();
        
        if (resultSet.next())
        {
          int id = resultSet.getInt("id");
          int productId = resultSet.getInt("p_id");
          int machineId = resultSet.getInt("m_id");
          Timestamp recorded = resultSet.getTimestamp("recorded");
          production = new Production(id, machineId, productId, recorded);
        } } catch (SQLException ex) {
        Logger.getLogger(MariadbLooknorthDAO.class.getName()).log(Level.SEVERE, null, ex);
      }
    return production;
  }

  @Override
  public List<Production> getProductionListForMachine(int machineId, Timestamp date) {
    List<Production> productions = new ArrayList<>();
    String query = "SELECT * FROM production " +
                   "WHERE m_id = ? AND recorded >= ? " +
                   "AND recorded < ? + INTERVAL 1 DAY;";
    Connection connection = null;
    PreparedStatement preparedStatement = null;
    
    try {       
        connection = MariadbDAOFactory.createConnection();
        preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, machineId);
        preparedStatement.setTimestamp(2, date);
        preparedStatement.setTimestamp(3, date);
        ResultSet resultSet = preparedStatement.executeQuery();
        
        while (resultSet.next())
        {
          int id = resultSet.getInt("id");
          int productId = resultSet.getInt("p_id");
          int machineIdd = resultSet.getInt("m_id");
          Timestamp recorded = resultSet.getTimestamp("recorded");
          productions.add(new Production(id, machineIdd, productId, recorded));
        } } catch (SQLException ex) {
        Logger.getLogger(MariadbLooknorthDAO.class.getName()).log(Level.SEVERE, null, ex);
      }
    return productions;
  }
  
  @Override
  public List<Production> getProductionListForProduct(int productId, Timestamp date) {
    List<Production> productions = new ArrayList<>();
    String query = "SELECT * FROM production " +
                   "WHERE p_id = ? AND recorded >= ? " +
                   "AND recorded < ? + INTERVAL 1 DAY;";
    Connection connection = null;
    PreparedStatement preparedStatement = null;
    
    try {       
        connection = MariadbDAOFactory.createConnection();
        preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, productId);
        preparedStatement.setTimestamp(2, date);
        preparedStatement.setTimestamp(3, date);
        ResultSet resultSet = preparedStatement.executeQuery();
        
        while (resultSet.next())
        {
          int id = resultSet.getInt("id");
          int productIdd = resultSet.getInt("p_id");
          int machineIdd = resultSet.getInt("m_id");
          Timestamp recorded = resultSet.getTimestamp("recorded");
          productions.add(new Production(id, machineIdd, productIdd, recorded));
        } } catch (SQLException ex) {
        Logger.getLogger(MariadbLooknorthDAO.class.getName()).log(Level.SEVERE, null, ex);
      }
    return productions;
  }

  @Override
  public List<Production> getProductionListForMachine(int machineId) {
    List<Production> productions = new ArrayList<>();
    String query = "SELECT * FROM production " +
                   "WHERE m_id = ?;";
    Connection connection = null;
    PreparedStatement preparedStatement = null;
    
    try {       
        connection = MariadbDAOFactory.createConnection();
        preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, machineId);
        ResultSet resultSet = preparedStatement.executeQuery();
        
        while (resultSet.next())
        {
          int id = resultSet.getInt("id");
          int productId = resultSet.getInt("p_id");
          int machineIdd = resultSet.getInt("m_id");
          Timestamp recorded = resultSet.getTimestamp("recorded");
          productions.add(new Production(id, machineIdd, productId, recorded));
        } } catch (SQLException ex) {
        Logger.getLogger(MariadbLooknorthDAO.class.getName()).log(Level.SEVERE, null, ex);
      }
    return productions;
  }

  @Override
  public List<Production> getProductionListForProduct(int productId) {
    List<Production> productions = new ArrayList<>();
    String query = "SELECT * FROM production " +
                   "WHERE p_id = ?;";
    Connection connection = null;
    PreparedStatement preparedStatement = null;
    
    try {       
        connection = MariadbDAOFactory.createConnection();
        preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, productId);
        ResultSet resultSet = preparedStatement.executeQuery();
        
        while (resultSet.next())
        {
          int id = resultSet.getInt("id");
          int productIdd = resultSet.getInt("p_id");
          int machineIdd = resultSet.getInt("m_id");
          Timestamp recorded = resultSet.getTimestamp("recorded");
          productions.add(new Production(id, machineIdd, productIdd, recorded));
        } } catch (SQLException ex) {
        Logger.getLogger(MariadbLooknorthDAO.class.getName()).log(Level.SEVERE, null, ex);
      }
    return productions;
  }

  @Override
  public List<OilConsumption> getOilConsumption() {     
        List<OilConsumption> list = new ArrayList<>();
        
        String query = "select * from looknorth.oil_usage";
        
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
        connection = MariadbDAOFactory.createConnection();
        preparedStatement = connection.prepareStatement(query);
        ResultSet resultSet = preparedStatement.executeQuery();
        
        while (resultSet.next())
        {
          //create OilConsumption object
          int id = resultSet.getInt("id");
          int machineNumber = resultSet.getInt("m_id");
          float liters = resultSet.getFloat("liters");
          Timestamp recorded = resultSet.getTimestamp("recorded");
          String s = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(recorded);
          list.add(new OilConsumption(id, machineNumber, liters, s));
        } } catch (SQLException ex) {
        Logger.getLogger(MariadbLooknorthDAO.class.getName()).log(Level.SEVERE, null, ex);
      } finally {
          if (preparedStatement != null) {
            try {
                preparedStatement.close();
            } catch (SQLException ex) {
                Logger.getLogger(MariadbLooknorthDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException ex) {
                Logger.getLogger(MariadbLooknorthDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        }
      return list; 
  }

  @Override
  public List<OilConsumption> getOilConsumptionByDate(Timestamp date) {
    List<OilConsumption> list = new ArrayList<>();
    String query = "SELECT * FROM oil_usage " +
                   "WHERE recorded >= ? " +
                   "AND recorded < ? + INTERVAL 1 DAY;";
    Connection connection = null;
    PreparedStatement preparedStatement = null;
    
    try {       
        connection = MariadbDAOFactory.createConnection();
        preparedStatement = connection.prepareStatement(query);
        preparedStatement.setTimestamp(1, date);
        preparedStatement.setTimestamp(2, date);
        ResultSet resultSet = preparedStatement.executeQuery();
        
        while (resultSet.next())
        {
          //create OilConsumption object
          int id = resultSet.getInt("id");
          int machineNumber = resultSet.getInt("m_id");
          float liters = resultSet.getFloat("liters");
          Timestamp recorded = resultSet.getTimestamp("recorded");
          String s = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(recorded);
          list.add(new OilConsumption(id, machineNumber, liters, s));
        } 
    } catch (SQLException ex) {
        Logger.getLogger(MariadbLooknorthDAO.class.getName()).log(Level.SEVERE, null, ex);
      }
    return list;
  }

  @Override
  public OilConsumption getLastOilConsumption() {
    OilConsumption oilConsumption = null;
    
    String query = "SELECT * FROM looknorth.oil_usage " + 
    "ORDER BY recorded DESC " +
    "LIMIT 1;";
    
    Connection connection = null;
    PreparedStatement preparedStatement = null;
    
    try {       
        connection = MariadbDAOFactory.createConnection();
        preparedStatement = connection.prepareStatement(query);
        ResultSet resultSet = preparedStatement.executeQuery();
        
        if (resultSet.next())
        {
          //create OilConsumption object
          int id = resultSet.getInt("id");
          int machineNumber = resultSet.getInt("m_id");
          float liters = resultSet.getFloat("liters");
          Timestamp recorded = resultSet.getTimestamp("recorded");
          String s = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(recorded);
          oilConsumption = new OilConsumption(id, machineNumber, liters, s);
        } } catch (SQLException ex) {
        Logger.getLogger(MariadbLooknorthDAO.class.getName()).log(Level.SEVERE, null, ex);
      }
    return oilConsumption;
  }

  @Override
  public List<AverageOilConsumption> getAverageOilConsumption() {
   List<AverageOilConsumption> list = new ArrayList<>();
        
        String query = "select * from looknorth.average_oil_usage";
        
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
        connection = MariadbDAOFactory.createConnection();
        preparedStatement = connection.prepareStatement(query);
        ResultSet resultSet = preparedStatement.executeQuery();
        
        while (resultSet.next())
        {
          //create AverageOilConsumption object
          
          int id = resultSet.getInt("id");
          String machineCombination = resultSet.getString("machine_combination");
          String productCombination = resultSet.getString("product_combination");
          float average = resultSet.getFloat("average");
          
          //add to list
          list.add(new AverageOilConsumption(id, machineCombination, productCombination, average));
        } } catch (SQLException ex) {
        Logger.getLogger(MariadbLooknorthDAO.class.getName()).log(Level.SEVERE, null, ex);
      } finally {
          if (preparedStatement != null) {
            try {
                preparedStatement.close();
            } catch (SQLException ex) {
                Logger.getLogger(MariadbLooknorthDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException ex) {
                Logger.getLogger(MariadbLooknorthDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        }
      return list;  
  }

  @Override
  public AverageOilConsumption getAverageOilConsumption(String productCombination) {
        AverageOilConsumption averageOilConsumption = null;
        
        String query = "SELECT * FROM looknorth.average_oil_usage where product_combination = ?";
        
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
        connection = MariadbDAOFactory.createConnection();
        preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, productCombination);
        ResultSet resultSet = preparedStatement.executeQuery();
        
        if (resultSet.next())
        {
          //create AverageOilConsumption object
          
          int id = resultSet.getInt("id");
          String machineCombination = resultSet.getString("machine_combination");
          String productCombinationn = resultSet.getString("product_combination");
          float average = resultSet.getFloat("average");
          averageOilConsumption = new AverageOilConsumption(id, machineCombination, productCombinationn, average);
        } } catch (SQLException ex) {
        Logger.getLogger(MariadbLooknorthDAO.class.getName()).log(Level.SEVERE, null, ex);
      } finally {
          if (preparedStatement != null) {
            try {
                preparedStatement.close();
            } catch (SQLException ex) {
                Logger.getLogger(MariadbLooknorthDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException ex) {
                Logger.getLogger(MariadbLooknorthDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        }
      return averageOilConsumption;  
  }
  
  private String generateAverageOilConsumptionStringMachine(int i) throws IllegalArgumentException {
    String generatedString = null;
    
    if (i < 1) throw new IllegalArgumentException("number is lower than at actual size of machines.");
    else if (i > 5) throw new IllegalArgumentException("Higher than the number of machines");
    else {
      switch (i) {
        case 1: generatedString = "1,-,-,-,-,";
          break;
        case 2: generatedString = "-,2,-,-,-,";
          break;
        case 3: generatedString = "-,-,3,-,-,";
          break;
        case 4: generatedString = "-,-,-,4,-,";
          break;
        case 5: generatedString = "-,-,-,-,5,";
          break;
        default:
          //do nothing, String returns null.
      }
    }
    
    return generatedString;
  }
  private String generateAverageOilConsumptionStringProduct(int machine, int product) throws IllegalArgumentException {
    String generatedString = null;
    
    if (machine < 1) throw new IllegalArgumentException("number is lower than at actual size of machines.");
    else if (machine > 5) throw new IllegalArgumentException("Higher than the number of machines");
    else {
      switch (machine) {
        case 1: generatedString = product + ",-,-,-,-,";
          break;
        case 2: generatedString = "-," + product + ",-,-,-,";
          break;
        case 3: generatedString = "-,-," + product + ",-,-,";
          break;
        case 4: generatedString = "-,-,-," + product + ",-,";
          break;
        case 5: generatedString = "-,-,-,-," + product + ",";
          break;
        default:
          //do nothing, String returns null.
      }
    }
    
    return generatedString;
  } 

  @Override
  public List<AverageOilConsumption> getAverageOilConsumption(int machineId) {
    List<AverageOilConsumption> list = new ArrayList<>();
        
        String query = "select * from looknorth.average_oil_usage where machine_combination = ?";
        
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
        connection = MariadbDAOFactory.createConnection();
        preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, generateAverageOilConsumptionStringMachine(machineId));
        ResultSet resultSet = preparedStatement.executeQuery();
        
        while (resultSet.next())
        {
          //create AverageOilConsumption object
          
          int id = resultSet.getInt("id");
          String  machineCombination = resultSet.getString("machine_combination");
          String productCombination = resultSet.getString("product_combination");
          float average = resultSet.getFloat("average");
          
          //add to list
          list.add(new AverageOilConsumption(id, machineCombination, productCombination, average));
        } } catch (SQLException ex) {
        Logger.getLogger(MariadbLooknorthDAO.class.getName()).log(Level.SEVERE, null, ex);
      } finally {
          if (preparedStatement != null) {
            try {
                preparedStatement.close();
            } catch (SQLException ex) {
                Logger.getLogger(MariadbLooknorthDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException ex) {
                Logger.getLogger(MariadbLooknorthDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        }
      return list;  
  }

  @Override
  public AverageOilConsumption getAverageOilConsumption(int machineId, int productId) {
    AverageOilConsumption averageOilConsumption = null;
        
        String query = "select * from looknorth.average_oil_usage "
                +       "where machine_combination = ? and product_combination = ?;";
        
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
        connection = MariadbDAOFactory.createConnection();
        preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, generateAverageOilConsumptionStringMachine(machineId));
        preparedStatement.setString(2, generateAverageOilConsumptionStringProduct(machineId, productId));
        ResultSet resultSet = preparedStatement.executeQuery();
        
        if (resultSet.next())
        {
          //create AverageOilConsumption object
          
          int id = resultSet.getInt("id");
          String machineCombination = resultSet.getString("machine_combination");
          String productCombination = resultSet.getString("product_combination");
          float average = resultSet.getFloat("average");
          
          //add to list
          averageOilConsumption = new AverageOilConsumption(id, machineCombination, productCombination, average);
        } } catch (SQLException ex) {
        Logger.getLogger(MariadbLooknorthDAO.class.getName()).log(Level.SEVERE, null, ex);
      } finally {
          if (preparedStatement != null) {
            try {
                preparedStatement.close();
            } catch (SQLException ex) {
                Logger.getLogger(MariadbLooknorthDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException ex) {
                Logger.getLogger(MariadbLooknorthDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        }
      return averageOilConsumption;  
  }
 
}
