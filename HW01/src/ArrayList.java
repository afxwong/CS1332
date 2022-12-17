import java.util.NoSuchElementException;

/**
 * Your implementation of an ArrayList.
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
public class ArrayList<T> {

    /**
     * The initial capacity of the ArrayList.
     *
     * DO NOT MODIFY THIS VARIABLE!
     */
    public static final int INITIAL_CAPACITY = 9;

    // Do not add new instance variables or modify existing ones.
    private T[] backingArray;
    private int size;

    /**
     * Constructs a new ArrayList.
     *
     * Java does not allow for regular generic array creation, so you will have
     * to cast an Object[] to a T[] to get the generic typing.
     */
    public ArrayList() {
        this.backingArray = (T[]) new Object[INITIAL_CAPACITY];
    }

    /**
     * Adds the element to the specified index.
     *
     * Remember that this add may require elements to be shifted.
     *
     * Must be amortized O(1) for index size and O(n) for all other cases.
     *
     * @param index the index at which to add the new element
     * @param data  the data to add at the specified index
     * @throws java.lang.IndexOutOfBoundsException if index < 0 or index > size
     * @throws java.lang.IllegalArgumentException  if data is null
     */
    public void addAtIndex(int index, T data) {
        if (index < 0 || index > this.size) {
            throw new IndexOutOfBoundsException("The arraylist does not contain the index");
        }
        if (data == null) {
            throw new IllegalArgumentException("Data passed in was null");
        }
        if (index == this.size) {
            addToBack(data);
        } else {
            if (this.size == this.backingArray.length) {
                T[] newarray = (T[]) new Object[this.backingArray.length * 2];
                for (int i = this.size; i >= 0; i--) {
                    if (i > index) {
                        newarray[i] = this.backingArray[i - 1];
                    } else if (i < index) {
                        newarray[i] = this.backingArray[i];
                    } else {
                        newarray[i] = data;
                    }
                }
                this.backingArray = newarray;
            } else {
                for (int i = this.size; i >= 0; i--) {
                    if (i > index) {
                        this.backingArray[i] = this.backingArray[i - 1];
                    } else if (i == index) {
                        this.backingArray[i] = data;
                    }
                }
            }
            size++;
        }
    }

    /**
     * Adds the element to the front of the list.
     *
     * Remember that this add may require elements to be shifted.
     *
     * Must be O(n).
     *
     * @param data the data to add to the front of the list
     * @throws java.lang.IllegalArgumentException if data is null
     */
    public void addToFront(T data) {
        if (data == null) {
            throw new IllegalArgumentException("Data passed in was null");
        } else {
            addAtIndex(0, data);
        }
    }

    /**
     * Adds the element to the back of the list.
     *
     * Must be amortized O(1).
     *
     * @param data the data to add to the back of the list
     * @throws java.lang.IllegalArgumentException if data is null
     */
    public void addToBack(T data) {
        if (data == null) {
            throw new IllegalArgumentException("Data passed in was null");
        } else {
            if (this.size == this.backingArray.length) {
                T[] newarray = (T[]) new Object[this.backingArray.length * 2];
                for (int i = 0; i < this.size; i++) {
                    newarray[i] = this.backingArray[i];
                }
                this.backingArray = newarray;
            }
            this.backingArray[this.size] = data;
            size++;
        }
    }

    /**
     * Removes and returns the element at the specified index.
     *
     * Remember that this remove may require elements to be shifted.
     *
     * Must be O(1) for index size - 1 and O(n) for all other cases.
     *
     * @param index the index of the element to remove
     * @return the data formerly located at the specified index
     * @throws java.lang.IndexOutOfBoundsException if index < 0 or index >= size
     */
    public T removeAtIndex(int index) {
        if (index < 0 || index > this.size) {
            throw new IndexOutOfBoundsException("The arraylist does not contain the index");
        }
        if (index == this.size - 1) {
            return removeFromBack();
        } else {
            T removedData = this.backingArray[index];
            for (int i = index + 1; i < this.size; i++) {
                this.backingArray[i - 1] = this.backingArray[i];
            }
            this.backingArray[this.size - 1] = null;
            this.size--;
            return removedData;
        }
    }

    /**
     * Removes and returns the first element of the list.
     *
     * Remember that this remove may require elements to be shifted.
     *
     * Must be O(n).
     *
     * @return the data formerly located at the front of the list
     * @throws java.util.NoSuchElementException if the list is empty
     */
    public T removeFromFront() {
        if (isEmpty()) {
            throw new NoSuchElementException("Cannot remove from empty list");
        }
        return removeAtIndex(0);
    }

    /**
     * Removes and returns the last element of the list.
     *
     * Must be O(1).
     *
     * @return the data formerly located at the back of the list
     * @throws java.util.NoSuchElementException if the list is empty
     */
    public T removeFromBack() {
        if (isEmpty()) {
            throw new NoSuchElementException("Cannot remove from empty list");
        }
        T removedData = this.backingArray[this.size - 1];
        this.backingArray[this.size - 1] = null;
        this.size--;
        return removedData;
    }

    /**
     * Returns the element at the specified index.
     *
     * Must be O(1).
     *
     * @param index the index of the element to get
     * @return the data stored at the index in the list
     * @throws java.lang.IndexOutOfBoundsException if index < 0 or index >= size
     */
    public T get(int index) {
        if (index < 0 || index > this.size) {
            throw new IndexOutOfBoundsException("Index is not within ArrayList size");
        }
        return backingArray[index];
    }

    /**
     * Returns whether or not the list is empty.
     *
     * Must be O(1).
     *
     * @return true if empty, false otherwise
     */
    public boolean isEmpty() {
        return this.size == 0;
    }

    /**
     * Clears the list.
     *
     * Resets the backing array to a new array of the initial capacity and
     * resets the size.
     *
     * Must be O(1).
     */
    public void clear() {
        backingArray = (T[]) new Object[INITIAL_CAPACITY];
        this.size = 0;
    }

    /**
     * Returns the backing array of the list.
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
     * Returns the size of the list.
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
