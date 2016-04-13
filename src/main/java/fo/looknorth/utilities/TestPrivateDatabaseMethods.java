package fo.looknorth.utilities;

/**
 *
 * @author jakup
 */
public class TestPrivateDatabaseMethods {
  
  private String generateAverageOilUsageString(int i) throws IllegalArgumentException {
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
  
  public static void main(String[] args) {
    TestPrivateDatabaseMethods t = new TestPrivateDatabaseMethods();
    for (int i = 1; i < 6; i++) {
      System.out.println("Number " + i + " " + t.generateAverageOilUsageString(i)); 
    }
  }
}
