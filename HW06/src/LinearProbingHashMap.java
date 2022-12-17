import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.ArrayList;

/**
 * Your implementation of a LinearProbingHashMap.
 *
 * @author Anthony Wong
 * @version 1.0
 * @userid awong307
 * @GTID 903579250
 *
 * Collaborators: LIST ALL COLLABORATORS YOU WORKED WITH HERE
 *
 * Resources: LIST ALL NON-COURSE RESOURCES YOU CONSULTED HERE
 */
public class LinearProbingHashMap<K, V> {

    /**
     * The initial capacity of the LinearProbingHashMap when created with the
     * default constructor.
     *
     * DO NOT MODIFY THIS VARIABLE!
     */
    public static final int INITIAL_CAPACITY = 13;

    /**
     * The max load factor of the LinearProbingHashMap
     *
     * DO NOT MODIFY THIS VARIABLE!
     */
    public static final double MAX_LOAD_FACTOR = 0.67;

    // Do not add new instance variables or modify existing ones.
    private LinearProbingMapEntry<K, V>[] table;
    private int size;

    /**
     * Constructs a new LinearProbingHashMap.
     *
     * The backing array should have an initial capacity of INITIAL_CAPACITY.
     *
     * Use constructor chaining.
     */
    public LinearProbingHashMap() {
        this(INITIAL_CAPACITY);
    }

    /**
     * Constructs a new LinearProbingHashMap.
     *
     * The backing array should have an initial capacity of initialCapacity.
     *
     * You may assume initialCapacity will always be positive.
     *
     * @param initialCapacity the initial capacity of the backing array
     */
    public LinearProbingHashMap(int initialCapacity) {
        this.table = (LinearProbingMapEntry<K, V>[]) new LinearProbingMapEntry[initialCapacity];
    }

    /**
     * Adds the given key-value pair to the map. If an entry in the map
     * already has this key, replace the entry's value with the new one
     * passed in.
     *
     * In the case of a collision, use linear probing as your resolution
     * strategy.
     *
     * Before actually adding any data to the HashMap, you should check to
     * see if the array would violate the max load factor if the data was
     * added. For example, let's say the array is of length 5 and the current
     * size is 3 (LF = 0.6). For this example, assume that no elements are
     * removed in between steps. If another entry is attempted to be added,
     * before doing anything else, you should check whether (3 + 1) / 5 = 0.8
     * is larger than the max LF. It is, so you would trigger a resize before
     * you even attempt to add the data or figure out if it's a duplicate. Be
     * careful to consider the differences between integer and double
     * division when calculating load factor.
     *
     * When regrowing, resize the length of the backing table to
     * 2 * old length + 1. You must use the resizeBackingTable method to do so.
     *
     * Return null if the key was not already in the map. If it was in the map,
     * return the old value associated with it.
     *
     * @param key   the key to add
     * @param value the value to add
     * @return null if the key was not already in the map. If it was in the
     * map, return the old value associated with it
     * @throws IllegalArgumentException if key or value is null
     */
    public V put(K key, V value) {
        return putHelper(key, value, true);
    }

    /**
     * Helper function for put function
     * @param key key to map
     * @param value value to put
     * @param resize resize or not
     * @return null if the key was not already in the map. If it was in the
     * map, return the old value associated with it
     */
    private V putHelper(K key, V value, boolean resize) {
        if (key == null || value == null) {
            throw new IllegalArgumentException("Key or Value is null");
        }
        if (resize && ((this.size + 1) / (float) this.table.length) > MAX_LOAD_FACTOR) {
            resizeBackingTable((2 * this.table.length) + 1);
        }
        int index = Math.abs(key.hashCode() % this.table.length);
        int delindex = -1;
        int currentindex = index;
        LinearProbingMapEntry<K, V> entry = this.table[currentindex];
        if (entry == null) {
            return insert(key, value, currentindex, true);
        } else {
            while (entry != null) {
                if (entry.isRemoved() && delindex == -1) {
                    delindex = currentindex;
                } else if (entry.getKey().equals(key) && !entry.isRemoved()) {
                    return insert(key, value, currentindex, false);
                } else {
                    currentindex = (currentindex + 1) % this.table.length;
                    entry = this.table[currentindex];
                }
            }
        }
        if (delindex != -1) {
            return insert(key, value, delindex, true);
        }
        this.table[currentindex] = new LinearProbingMapEntry<>(key, value);
        this.size++;
        return null;
    }

    /**
     * Insert value into Hash Table
     * @param key key to enter
     * @param value value to enter
     * @param index where to enter value
     * @param incsize increment size
     * @return value if index is occupied
     */
    private V insert(K key, V value, int index, boolean incsize) {
        V oldvalue = (this.table[index] == null
                || this.table[index].isRemoved()) ? null : this.table[index].getValue();
        if (this.table[index] == null) {
            this.table[index] = new LinearProbingMapEntry<>(key, value);
        } else {
            this.table[index].setValue(value);
            this.table[index].setKey(key);
            this.table[index].setRemoved(false);
        }
        this.size = incsize ? this.size + 1 : this.size;
        return oldvalue;
    }

    /**
     * Removes the entry with a matching key from map by marking the entry as
     * removed.
     *
     * @param key the key to remove
     * @return the value previously associated with the key
     * @throws IllegalArgumentException if key is null
     * @throws java.util.NoSuchElementException   if the key is not in the map
     */
    public V remove(K key) {
        int index = findElementLocation(key);
        V value = this.table[index].getValue();
        this.size--;
        this.table[index].setRemoved(true);
        return value;
    }

    /**
     * Gets the value associated with the given key.
     *
     * @param key the key to search for in the map
     * @return the value associated with the given key
     * @throws IllegalArgumentException if key is null
     * @throws java.util.NoSuchElementException   if the key is not in the map
     */
    public V get(K key) {
        return this.table[findElementLocation(key)].getValue();
    }

    /**
     * Find element with correct key in Hash Map
     * @param key key to find
     * @return index of key
     */
    private int findElementLocation(K key) {
        if (key == null) {
            throw new IllegalArgumentException("Key cannot be null");
        }
        int probeIndex = Math.abs(key.hashCode() % this.table.length);
        int probes = 0;
        int returnIndex = -1;
        LinearProbingMapEntry<K, V> entry = this.table[probeIndex];
        if (entry == null) {
            throw new NoSuchElementException("Key not found");
        }
        if (entry.getKey().equals(key) && !entry.isRemoved()) {
            return probeIndex;
        } else {
            while (probes < this.table.length && entry != null) {
                if (entry.getKey().equals(key) && !entry.isRemoved()) {
                    returnIndex = probeIndex;
                    break;
                } else if (entry.getKey().equals(key) && entry.isRemoved()) {
                    throw new NoSuchElementException("Key not found");
                }
                probes++;
                probeIndex = (probeIndex + 1) % this.table.length;
                entry = this.table[probeIndex];
            }
        }
        if (returnIndex == -1) {
            throw new NoSuchElementException("Key is not in the map");
        } else {
            return returnIndex;
        }
    }

    /**
     * Returns whether or not the key is in the map.
     *
     * @param key the key to search for in the map
     * @return true if the key is contained within the map, false
     * otherwise
     * @throws IllegalArgumentException if key is null
     */
    public boolean containsKey(K key) {
        if (key == null) {
            throw new IllegalArgumentException("Key is null");
        }
        try {
            return get(key) != null;
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    /**
     * Returns a Set view of the keys contained in this map.
     *
     * Use java.util.HashSet.
     *
     * @return the set of keys in this map
     */
    public Set<K> keySet() {
        HashSet<K> hashSet = new HashSet<>();
        for (LinearProbingMapEntry<K, V> entry : this.table) {
            if (hashSet.size() == this.size) {
                break;
            } else if (entry != null && !entry.isRemoved()) {
                hashSet.add(entry.getKey());
            }
        }
        return hashSet;
    }

    /**
     * Returns a List view of the values contained in this map.
     *
     * Use java.util.ArrayList or java.util.LinkedList.
     *
     * You should iterate over the table in order of increasing index and add
     * entries to the List in the order in which they are traversed.
     *
     * @return list of values in this map
     */
    public List<V> values() {
        ArrayList<V> arrayList = new ArrayList<>();
        for (LinearProbingMapEntry<K, V> entry : this.table) {
            if (arrayList.size() == this.size) {
                break;
            } else if (entry != null && !entry.isRemoved()) {
                arrayList.add(entry.getValue());
            }
        }
        return arrayList;
    }

    /**
     * Resize the backing table to length.
     *
     * Disregard the load factor for this method. So, if the passed in length is
     * smaller than the current capacity, and this new length causes the table's
     * load factor to exceed MAX_LOAD_FACTOR, you should still resize the table
     * to the specified length and leave it at that capacity.
     *
     * You should iterate over the old table in order of increasing index and
     * add entries to the new table in the order in which they are traversed.
     * You should NOT copy over removed elements to the resized backing table.
     *
     * Since resizing the backing table is working with the non-duplicate
     * data already in the table, you shouldn't explicitly check for
     * duplicates.
     *
     * Hint: You cannot just simply copy the entries over to the new array.
     *
     * @param length new length of the backing table
     * @throws IllegalArgumentException if length is less than the
     *                                            number of items in the hash
     *                                            map
     */
    public void resizeBackingTable(int length) {
        if (length < this.size) {
            throw new IllegalArgumentException("Length is less than size");
        }
        LinearProbingMapEntry<K, V>[] oldtable = this.table;
        this.table = (LinearProbingMapEntry<K, V>[]) new LinearProbingMapEntry[length];
        int oldsize = this.size;
        int count = 0;
        this.size = 0;
        for (int i = 0; i < oldtable.length; i++) {
            if (count > oldsize) {
                break;
            }
            LinearProbingMapEntry<K, V> entry = oldtable[i];
            if (entry != null && !entry.isRemoved()) {
                putHelper(entry.getKey(), entry.getValue(), false);
                count++;
            }
        }
    }

    /**
     * Clears the map.
     *
     * Resets the table to a new array of the INITIAL_CAPACITY and resets the
     * size.
     *
     * Must be O(1).
     */
    public void clear() {
        this.table = (LinearProbingMapEntry<K, V>[]) new LinearProbingMapEntry[INITIAL_CAPACITY];
        this.size = 0;
    }

    /**
     * Returns the table of the map.
     *
     * For grading purposes only. You shouldn't need to use this method since
     * you have direct access to the variable.
     *
     * @return the table of the map
     */
    public LinearProbingMapEntry<K, V>[] getTable() {
        // DO NOT MODIFY THIS METHOD!
        return table;
    }

    /**
     * Returns the size of the map.
     *
     * For grading purposes only. You shouldn't need to use this method since
     * you have direct access to the variable.
     *
     * @return the size of the map
     */
    public int size() {
        // DO NOT MODIFY THIS METHOD!
        return size;
    }
}
