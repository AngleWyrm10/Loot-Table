/**
 * LootTable Interface
 */

interface ILootTable {
  boolean load(String filename); // load data from CSV file
  String  toString();            // Stringified version of table
  String  get();                 // get a random loot record
  int     size();                // get count of records in loot table
  double  total();               // get total of loot drop chances
}