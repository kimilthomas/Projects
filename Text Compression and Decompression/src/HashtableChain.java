/*This program is a modified hashmap class that uses separate chaining with *linked list. It has various methods for searching , modifying, and *rehashing.
 *@author Shayna Bello
 *@author Kimil Thomas
 *since 05-11-2023
 */

import java.util.*;

public class HashtableChain<K, V> implements KWHashMap<K, V> {
    //insert inner class Entry <K, V> here

    private static class Entry<K, V> {
        private final K key; //make final so it cant change after initialization
        private V value;

        //Create new key-value pair
        public Entry (K key, V value) {
            this.key = key;
            this.value = value;
        }

        //gets the key
        public K getKey() {
            return key;
        }

        //gets the value
        public V getValue() {
            return value;
        }

        //sets the value
        public V setValue(V val) {
            V oldVal = value;
            value = val;
            return oldVal;
        }

        @Override
        public String toString() {
            return key + "=" + value;
        }
    }

    //the table, creates an array of linked lists
    private LinkedList<Entry<K, V>>[] table;

    //the number of keys/table entries
    private static int numKeys;

    //the rehash count
    public static int rehashCount;

    //the capacity
    private static final int CAPACITY = makePrime(numKeys); //make prime number;

    //maximum load factor 10 to 15 is good
    private static final double LOAD_THRESHOLD = 0.75;

    //constructor
    public HashtableChain() {
        table = new LinkedList[CAPACITY];
        numKeys = 0;
    }

    //nondefault constructor
    public HashtableChain(int cap) {
        int capacity = makePrime(cap);
        table = new LinkedList[capacity];
        numKeys = 0;
    }

    //helper method to get the next prime number
    private static int makePrime(int n) {
        if (n < 2) {
            return 2;
        }

        int prime = n;
        boolean foundPrime = false;

        while (!foundPrime) {
            prime++;

            if (isPrime(prime)) {
                foundPrime = true;
            }
        }

        return prime;
    }

    //helper method to check if a number is prime
    private static boolean isPrime(int n) {
        if (n == 2 || n == 3) { return true;}
        if (n == 1 || n % 2 == 0) {return false;}

        for (int i = 3; i <= Math.sqrt(n); i += 2) {
            if (n % i == 0) {return false;}
        }
        return true;
    }

    //get method
    @Override
    public V get (Object key) {
        int index = key.hashCode() % table.length;
        if (index < 0)
            index += table.length;
        if (table[index] == null)
            return null; // key isnt in the table

        //search list at table[index] to find key
        for (Entry<K, V> nextItem: table[index]){
            if (nextItem.getKey().equals(key))
                return nextItem.getValue();
        }
        return null; //if key isnt in the table
    }

    //put method.
    @Override
    public V put(K key, V value) {
        int index = key.hashCode() % table.length;
        if (index < 0)
            index += table.length;
        if (table[index] == null) {
            table[index] = new LinkedList<>(); //create new linked list at table[index]
        }

        //search list at  table[index] to find key
        for (Entry<K, V> nextItem: table[index]){
            //if search is successful, replace the old value.
            if (nextItem.getKey().equals(key)) {
                //replace value for this key
                V oldVal = nextItem.getValue();
                nextItem.setValue(value);
                return oldVal;
            }
        }
        table[index].addFirst(new Entry<>(key, value));
        numKeys++;
        if (numKeys > (LOAD_THRESHOLD * table.length))
            rehash();
        return null;
    }

    //remove method algorithm
    /* set index to key.hashCode() % table.length
    if index is neg, add table.length
    if table[i] is null
        key is not in the table; return null
    search the list at table[i] to find the key
    if the search is successful
        remove entry with his key & decrement numkKeys
            if the list at table[i] is empty
                set table[i] to null
            return the value associated with this key
    the key is not in the table; return null
     *
     */
    //can’t use for loop, needs to use Iterator next/remove
    @Override
    public V remove(Object key) {
        V value = null; // entry that will be removed
        int index = key.hashCode() % table.length;
        if (index < 0)
            index += table.length;
        if (table[index] == null)
            return null; //key isn’t in the table

        //search list at table[index] to find key
        Iterator<Entry<K, V>> iter = table[index].iterator();
        while (iter.hasNext()) {
            Entry<K, V> nextItem = iter.next();
            if (nextItem.getKey().equals(key)) {
                value = nextItem.getValue();
                iter.remove();
                numKeys --;
                if (table[index].isEmpty()) {
                    table[index] = null;
                }
            }
            return value;
        }
        return null; //if key isn’t in the table
    }

    @Override
    public int size() {
        return numKeys;
    }

    @Override
    public boolean isEmpty() {
        return numKeys == 0;
    }

    //create to String method
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < table.length; i++) {
            if (table[i] != null) {
                for (Entry<K, V> entry : table[i]) {
                    sb.append(entry.getKey() + "=" + entry.getValue() + "  ");
                }
            }
        }
        return sb.toString().trim();
    }

    //rehash method
    public void rehash() {
        // allocate a new hashtable with twice the capacity as original
        int newCapacity = makePrime(table.length * 2);
        LinkedList<Entry<K, V>>[] newTable = new LinkedList[newCapacity];

        rehashCount ++;
        //reinsert each old table entry into the new hash table
        for (LinkedList<Entry<K, V>> list : table) {
            if (list != null) {
                for (Entry<K, V> entry : list) {
                    int index = entry.getKey().hashCode() % newCapacity;
                    if (index < 0) {
                        index += newCapacity;
                    }
                    if (newTable[index] == null) {
                        newTable[index] = new LinkedList<>();
                    }
                    newTable[index].add(entry);
                }
            }//end if
        }
        //reference the new table instead of the original
        table = newTable;
    }
}

