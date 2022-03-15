/**
 * Implementation of LootTable interface
 * 
 */
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

class LootTable implements ILootTable {

  //--| Internal Data Structure |------------------------------------------------------------------

  List<lootRecord> table = new ArrayList<lootRecord>();

  /**
   * Internal bean for each loot drop
   */
  public class lootRecord{
    String name  = "[empty drop]";
    int    tries = 0;
    double dropChance = 0.01;

    /**
     * @return string representation of loot drop
     */
    public String toString(){
      return String.format("%9.10s, %2d tries, drops %4.1f%%", 
        this.name, this.tries, 100 * this.dropChance);  
    }

  } // lootRecord

  double confidence = 0.95;
  static Random rng = new Random();

  //--| Primary Interface |------------------------------------------------------------------------

  /**
   * Constructor
   * @param fileName CSV file to import
   */
  LootTable(String fileName){
    load(fileName);
  }
  
  /**
   * get a stringified randomly selected loot record from the table
   */
  public String get(){
    double target = rng.nextDouble();
    double sum = 0.0;
    for(lootRecord record : table){
      sum += record.dropChance;
      if(target <= sum){
        return record.toString();
      }
    } // else RNG rolled higher than our total drop chances
    return new lootRecord().toString();
  }

  /**
   * @return count of records in loot table
   */
  public int size(){
    return table.size();
  }

  /**
   * @return total of loot drop chances
   */
  public double total(){
    double total = 0.0;
    for (lootRecord record : table) {
      total += record.dropChance;
    }
    return total;
  }

  /**
   * Load a set of loot items into loot table from a Comma Separated Value (CSV) file
   * @param fileName expected CSV format: | name | tries |
   * @throws NumberFormatException if 2nd column isn't an integer (extra space?)
   * @throws FileNotFoundException if can't find the CSV file (path & extension?)
   * @throws IOException other I/O & storage problems
   */
  public boolean load(String fileName){
    try {
      BufferedReader fileReader = new BufferedReader(new FileReader(fileName));
      String lootRecord;

      while ((lootRecord = fileReader.readLine()) != null) {
          String[] lootDetails = lootRecord.split(",");

          lootRecord data = new lootRecord();

          data.name = lootDetails[0];
          data.tries = Integer.valueOf(lootDetails[1]);
          data.dropChance = calcDropChance(data.tries);

          table.add(data);
      }
      fileReader.close();
      return true;
    }
    catch (NumberFormatException e) {
      System.out.println("Invalid String: Integer expected");
      e.printStackTrace();
    } 
    catch (FileNotFoundException e) {
      e.printStackTrace();
    } 
    catch (IOException e) {
      e.printStackTrace();
    }
    return false;
  }

  //--| Utility Methods |--------------------------------------------------------------------------

  /**
   *  @return string representation of loot table
   */
  public String toString(){
    String stringBeans = new String();
    for (lootRecord bean : table) {
      stringBeans = stringBeans + bean + "\n";
    }
    return stringBeans;
  }

  /**
   * Change loot table confidence (1 - risk)
   * Default: 95% certainty (5% risk of misadventure)
   * @param confidence [0..1] 
   */
  public void setConfidence(double confidence){
    this.confidence = confidence;
  }

  /**
   * get confidence (1 - risk)
   * @return confidence [0..1]
   */
  public double getConfidence(){
    return this.confidence;
  }

  //--| Internal Methods |-------------------------------------------------------------------------

  /**
   * Calculate drop chance
   * Given tries & confidence (1-risk), calculate success (1-failure)
   *  failure = risk^(1/tries)
   *  failure = (1-confidence)^(1/tries)
   *  success = 1 - (1-confidence)^(1/tries)
   * @param tries before it's confidence% certain the loot has dropped
   */
  double calcDropChance(int tries){
    return 1.0 - Math.pow(1.0 - confidence, 1.0 / tries);
  }
}