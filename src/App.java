public class App {

  public static void main(String[] args) throws Exception {

    // Create a loot table, loaded from a CSV file
    ILootTable myLootTable = new LootTable("loot.csv");

    // print the contents of the loot table
    System.out.println( 
      String.format("%d records in loot table, total drop chance: %4.1f%%\n", 
        myLootTable.size(), 100 * myLootTable.total() 
      ) + myLootTable.toString() + "\n"
    );

    // get several random loot drops from the table
    int several = 20;
    System.out.println( String.format("Getting %d loot drops", several) );

    for(int i = 1; i <= several; i++){
      System.out.println( myLootTable.get() );
    }

  } // main
} // App