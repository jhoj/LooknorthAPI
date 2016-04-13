/*
 * This project is owned by Jákup Høj, redistribution is not allowed.
 */
package fo.looknorth.looknorthapi;

import static spark.Spark.*;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import fo.looknorth.looknorthapi.database.DAOFactory;
import fo.looknorth.looknorthapi.database.LooknorthDAO;
import fo.looknorth.looknorthapi.model.*;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author jakup
 */
public class LooknorthAPI {
  
    public static void main(String[] args) {
    
    final DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.MARIADB);
    final LooknorthDAO dao = daoFactory.getLooknorthDAO();
    final Gson gson = new Gson();

    //Web-api  
      
    //active products
      get("/active-product", (req, res) -> {        
        // create the type for the collection. In this case define that the collection is of type Machine
        List<Product> activeProducts = dao.getActiveProductList();
        Type datasetListType = new TypeToken<Collection<Product>>() {}.getType();
        return gson.toJson(activeProducts, datasetListType);
      });

      get("/active-product/:machineId", (req, res) -> {
        int id = Integer.parseInt(req.params(":machineId"));
        Product p =  dao.getActiveProduct(id);
        return gson.toJson(p);
      });
      
      //products
      get("/product", (req, res) -> {
        List<Product> products = dao.getProductList();
        Type productType = new TypeToken<Collection<Product>>(){}.getType();
        return gson.toJsonTree(products, productType);
      });
      
      get("/product/:productId", (req, res) -> {
        int productId = Integer.parseInt(req.params(":productId"));
        Product p = dao.getProduct(productId);
        return gson.toJson(p);
      });
      
    //machine
    get("/machine", (req, res) -> {
      List<Machine> machines = dao.getMachineList();
      Type machineType = new TypeToken<Collection<Machine>>(){}.getType();
      return gson.toJson(machines, machineType);
    });
    
    get("/machine/:machineId", (req, res) -> {
      int machineId = Integer.parseInt(req.params(":machineId"));
      Machine machine = dao.getMachine(machineId);
      return gson.toJson(machine);
    });
    
    //production
    get("/production", (req, res) -> {
      List<Production> productions = dao.getProductionList();
      Type productionType = new TypeToken<Collection<Production>>(){}.getType();
      return gson.toJson(productions, productionType);
    });
    
    get("/production/:date", (req, res) -> {
      SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
      Date parsedDate = dateFormat.parse(req.params(":date"));
      Timestamp date = new java.sql.Timestamp(parsedDate.getTime());
      Type productionType = new TypeToken<Collection<Production>>(){}.getType();
      List<Production> productions = dao.getProductionList(date); 
      return gson.toJson(productions, productionType);
    });
    
    get("/production/machine/:machineId/:date", (req, res) -> {
      int machineId = Integer.parseInt(req.params(":machineId"));
      SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
      Date parsedDate = dateFormat.parse(req.params(":date"));
      Timestamp date = new java.sql.Timestamp(parsedDate.getTime());
      Type productionType = new TypeToken<Collection<Production>>(){}.getType();
      List<Production> productions = dao.getProductionListForMachine(machineId, date); 
      return gson.toJson(productions, productionType);
    });
    
        get("/production/product/:productId/:date", (req, res) -> {
      int productId = Integer.parseInt(req.params(":productId"));
      SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
      Date parsedDate = dateFormat.parse(req.params(":date"));
      Timestamp date = new java.sql.Timestamp(parsedDate.getTime());
      Type productionType = new TypeToken<Collection<Production>>(){}.getType();
      List<Production> productions = dao.getProductionListForProduct(productId, date); 
      return gson.toJson(productions, productionType);
    });
        
    get("/production/product/:productId", (req, res) -> {
      int productId = Integer.parseInt(req.params(":productId"));
      SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
      Date parsedDate = dateFormat.parse(req.params(":date"));
      Timestamp date = new java.sql.Timestamp(parsedDate.getTime());
      Type productionType = new TypeToken<Collection<Production>>(){}.getType();
      List<Production> productions = dao.getProductionListForProduct(productId, date); 
      return gson.toJson(productions, productionType);
    });
    
    get("/production/machine/:machineId", (req, res) -> {
      int productId = Integer.parseInt(req.params(":productId"));
      SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
      Date parsedDate = dateFormat.parse(req.params(":date"));
      Timestamp date = new java.sql.Timestamp(parsedDate.getTime());
      Type productionType = new TypeToken<Collection<Production>>(){}.getType();
      List<Production> productions = dao.getProductionListForProduct(productId, date); 
      return gson.toJson(productions, productionType);
    });
    
        
    get("/production/machine/:machineId/last", (req, res) -> {
      int machineId = Integer.parseInt(req.params(":machineId"));
      Production production = dao.getLastProductionForMachine(machineId);
      return gson.toJson(production);
    });

    get("/production/product/:productId/last", (req, res) -> {
      int productId = Integer.parseInt(req.params(":productId"));
      Production production = dao.getLastProductionForProduct(productId);
      return gson.toJson(production);
    });
    
    //oil usage
    get("/oil-usage", (req, res) -> {
      List<OilUsage> list = dao.getOilUsage();
       Type oilUsageType = new TypeToken<Collection<OilUsage>>(){}.getType();
      return gson.toJson(list, oilUsageType);
    });
    
    get("/oil-usage/date/:date", (req, res) -> {
      SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
      Date parsedDate = dateFormat.parse(req.params(":date"));
      Timestamp date = new java.sql.Timestamp(parsedDate.getTime());
      Type oilUsageType = new TypeToken<Collection<OilUsage>>(){}.getType();
      List<OilUsage> list = dao.getOilUsageByDate(date);
      return gson.toJson(list, oilUsageType);
    });
    
    get("/oil-usage/last", (req, res) -> {
      OilUsage oilUsage = dao.getLastOilUsage();
      return gson.toJson(oilUsage);
    });
    
    //average oil usage
    get("/average-oil-usage", (req, res) -> {
      List<AverageOilUsage> list = dao.getAverageOilUsage();
      Type averageOilUsageType = new TypeToken<Collection<AverageOilUsage>>(){}.getType();
      return gson.toJson(list, averageOilUsageType);
    });
    
    get("/average-oil-usage/product-combination/:combination", (req, res) -> {
      String combination = req.params(":combination");
      AverageOilUsage averageOilUsage = dao.getAverageOilUsage(combination);
      return gson.toJson(averageOilUsage);
    });
    
    get("/average-oil-usage/machine/:machineId", (req, res) -> {
      int machineId = Integer.parseInt(req.params(":machineId"));
      List<AverageOilUsage> list = dao.getAverageOilUsage(machineId);
      Type averageOilUsageType = new TypeToken<Collection<AverageOilUsage>>(){}.getType();
      return gson.toJson(list, averageOilUsageType);
    });
    
    get("/average-oil-usage/machine/:machineId/product/:productId", (req, res) -> {
      int machineId = Integer.parseInt(req.params(":machineId"));
      int productId = Integer.parseInt(req.params(":productId"));
      AverageOilUsage averageOilUsage = dao.getAverageOilUsage(machineId, productId);
      return gson.toJson(averageOilUsage);
    });
    
    
    }
}
