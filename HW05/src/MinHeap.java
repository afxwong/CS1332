import java.util.ArrayList;
import java.util.NoSuchElementException;

/**
 * Your implementation of a MinHeap.
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
public class MinHeap<T extends Comparable<? super T>> {

    /**
     * The initial capacity of the MinHeap when created with the default
     * constructor.
     *
     * DO NOT MODIFY THIS VARIABLE!
     */
    public static final int INITIAL_CAPACITY = 13;

    // Do not add new instance variables or modify existing ones.
    private T[] backingArray;
    private int size;

    /**
     * Constructs a new MinHeap.
     *
     * The backing array should have an initial capacity of INITIAL_CAPACITY.
     */
    public MinHeap() {
        this.backingArray = (T[]) new Comparable[INITIAL_CAPACITY];
    }

    /**
     * Creates a properly ordered heap from a set of initial values.
     *
     * You must use the BuildHeap algorithm that was taught in lecture! Simply
     * adding the data one by one using the add method will not get any credit.
     * As a reminder, this is the algorithm that involves building the heap
     * from the bottom up by repeated use of downHeap operations.
     *
     * Before doing the algorithm, first copy over the data from the
     * ArrayList to the backingArray (leaving index 0 of the backingArray
     * empty). The data in the backingArray should be in the same order as it
     * appears in the passed in ArrayList before you start the BuildHeap
     * algorithm.
     *
     * The backingArray should have capacity 2n + 1 where n is the
     * number of data in the passed in ArrayList (not INITIAL_CAPACITY).
     * Index 0 should remain empty, indices 1 to n should contain the data in
     * proper order, and the rest of the indices should be empty.
     *
     * @param data a list of data to initialize the heap with
     * @throws IllegalArgumentException if data or any element in data
     *                                            is null
     */
    public MinHeap(ArrayList<T> data) {
        if (data == null) {
            throw new IllegalArgumentException("Data is null");
        }
        this.backingArray = (T[]) new Comparable[(2 * data.size()) + 1];
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i) == null) {
                throw new IllegalArgumentException("Data is null");
            }
            this.backingArray[i + 1] = data.get(i);
            this.size++;
        }
        for (int i = this.size / 2; i > 0; i--) {
            heapDown(i);
        }
    }

    /**
     * Method recursively heapifies down
     * @param parentidx index of the parent
     */
    private void heapDown(int parentidx) {
        if (parentidx > this.size / 2) {
            return;
        }
        int lchild = parentidx * 2;
        int rchild = lchild + 1;
        boolean heapleft = rchild > this.size
                || this.backingArray[lchild].compareTo(this.backingArray[rchild]) <= 0;
        if (heapleft && this.backingArray[parentidx].compareTo(backingArray[lchild]) > 0) {
            swap(parentidx, lchild);
            heapDown(lchild);
        } else if (rchild <= this.size
                && backingArray[parentidx].compareTo(backingArray[rchild]) > 0) {
            swap(parentidx, rchild);
            heapDown(rchild);
        } else {
            return;
        }
    }

    /**
     * Swaps elements in backing array
     * @param idx1 index of larger element
     * @param idx2 index of smaller element
     */
    private void swap(int idx1, int idx2) {
        T temp = this.backingArray[idx1];
        this.backingArray[idx1] = this.backingArray[idx2];
        this.backingArray[idx2] = temp;
    }

    /**
     * Adds an item to the heap. If the backing array is full (except for
     * index 0) and you're trying to add a new item, then double its capacity.
     * The order property of the heap must be maintained after adding.
     *
     * @param data the data to add
     * @throws IllegalArgumentException if data is null
     */
    public void add(T data) {
        if (data == null) {
            throw new IllegalArgumentException("Data is null");
        }
        this.size++;
        if (this.size == this.backingArray.length) {
            T[] newarr = (T[]) new Comparable[this.backingArray.length * 2];
            for (int i = 1; i < this.size; i++) {
                newarr[i] = this.backingArray[i];
            }
            this.backingArray = newarr;
        }
        this.backingArray[this.size] = data;
        heapUp();
    }

    /**
     * Method heapifies up
     */
    private void heapUp() {
        int pos = this.size;
        while (pos > 1 && this.backingArray[pos / 2].compareTo(this.backingArray[pos]) > 0) {
            T temp = backingArray[pos];
            backingArray[pos] = backingArray[pos / 2];
            backingArray[pos / 2] = temp;
            pos /= 2;
        }
    }

    /**
     * Removes and returns the min item of the heap. As usual for array-backed
     * structures, be sure to null out spots as you remove. Do not decrease the
     * capacity of the backing array.
     * The order property of the heap must be maintained after adding.
     *
     * @return the data that was removed
     * @throws java.util.NoSuchElementException if the heap is empty
     */
    public T remove() {
        if (isEmpty()) {
            throw new NoSuchElementException("Heap is empty");
        }
        T retdata = this.backingArray[1];
        this.backingArray[1] = this.backingArray[this.size];
        this.backingArray[this.size] = null;
        this.size--;
        heapDown(1);
        return retdata;
    }

    /**
     * Returns the minimum element in the heap.
     *
     * @return the minimum element
     * @throws java.util.NoSuchElementException if the heap is empty
     */
    public T getMin() {
        if (isEmpty()) {
            throw new NoSuchElementException("Heap cannot be empty");
        }
        return this.backingArray[1];
    }

    /**
     * Returns whether or not the heap is empty.
     *
     * @return true if empty, false otherwise
     */
    public boolean isEmpty() {
        return this.size == 0;
    }

    /**
     * Clears the heap.
     *
     * Resets the backing array to a new array of the initial capacity and
     * resets the size.
     */
    public void clear() {
        this.backingArray = (T[]) new Comparable[INITIAL_CAPACITY];
        this.size = 0;
    }

    /**
     * Returns the backing array of the heap.
     *
     * For grading purposes only. You shouldn't need to use this method since
     * you have direct access to the variable.
     *
     * @return the backing array of the list
     */
    public T[] getBackingArray() {
        // DO NOT MODIFY THIS METHOD!
        return backingArray;
    }

    /**
     * Returns the size of the heap.
     *
     * For grading purposes only. You shouldn't need to use this method since
     * you have direct access to the variable.
     *
     * @return the size of the list
     */
    public int size() {
        // DO NOT MODIFY THIS METHOD!
        return size;
    }
}
