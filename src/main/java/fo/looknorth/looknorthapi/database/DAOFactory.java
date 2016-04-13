package fo.looknorth.looknorthapi.database;

/**
 *
 * @author jakuphoj
 * @version 06/04/2016
 * 
 * DAOFactory implements the factory pattern. This is an Abstract class 
 * containing only one method, for getting the Data Access Object needed for 
 * comunicating with the database.
 */
public abstract class DAOFactory {

  // List of DAO types supported by the factory
  public static final int MARIADB = 1;

  // There will be a method for each DAO that can be 
  // created. The concrete factories will have to 
  // implement these methods.
  public abstract LooknorthDAO getLooknorthDAO();

  public static DAOFactory getDAOFactory(int whichFactory) {
  
    switch (whichFactory) {
      case MARIADB: 
          return new MariadbDAOFactory();
      default: 
          return null;
    }
  }
}
