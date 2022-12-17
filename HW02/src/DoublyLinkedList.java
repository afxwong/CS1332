import java.util.NoSuchElementException;

/**
 * Your implementation of a non-circular DoublyLinkedList with a tail pointer.
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
public class DoublyLinkedList<T> {

    // Do not add new instance variables or modify existing ones.
    private DoublyLinkedListNode<T> head;
    private DoublyLinkedListNode<T> tail;
    private int size;

    // Do not add a constructor.

    /**
     * Adds the element to the specified index. Don't forget to consider whether
     * traversing the list from the head or tail is more efficient!
     *
     * Must be O(1) for indices 0 and size and O(n) for all other cases.
     *
     * @param index the index at which to add the new element
     * @param data  the data to add at the specified index
     * @throws java.lang.IndexOutOfBoundsException if index < 0 or index > size
     * @throws java.lang.IllegalArgumentException  if data is null
     */
    public void addAtIndex(int index, T data) {
        if (index < 0 || index > this.size) {
            throw new IndexOutOfBoundsException("Index does not exist within the range of the linkedlist");
        }
        if (data == null) {
            throw new IllegalArgumentException("Data is null");
        }
        if (index == 0) {
            addToFront(data);
        } else if (index == this.size) {
            addToBack(data);
        } else {
            int middle = (this.size - 1) / 2;
            if (index <= middle) {
                DoublyLinkedListNode<T> cur = this.head;
                int pos = 0;
                while (cur != null) {
                    if (index == pos) {
                        DoublyLinkedListNode<T> newNode = new DoublyLinkedListNode<>(data, cur.getPrevious(), cur);
                        cur.getPrevious().setNext(newNode);
                        cur.setPrevious(newNode);
                        break;
                    }
                    pos++;
                    cur = cur.getNext();
                }
            } else {
                DoublyLinkedListNode<T> cur = this.tail;
                int pos = this.size - 1;
                while (cur != null) {
                    if (index == pos) {
                        DoublyLinkedListNode<T> newNode = new DoublyLinkedListNode<>(data, cur.getPrevious(), cur);
                        cur.getPrevious().setNext(newNode);
                        cur.setPrevious(newNode);
                        break;
                    }
                    pos--;
                    cur = cur.getPrevious();
                }
            }
            this.size++;
        }
    }

    /**
     * Adds the element to the front of the list.
     *
     * Must be O(1).
     *
     * @param data the data to add to the front of the list
     * @throws java.lang.IllegalArgumentException if data is null
     */
    public void addToFront(T data) {
        if (data == null) {
            throw new IllegalArgumentException("Data is null");
        }
        DoublyLinkedListNode<T> newNode = new DoublyLinkedListNode<>(data);
        if (this.head == null) {
            this.head = newNode;
            this.tail = newNode;
        } else {
            this.head.setPrevious(newNode);
            newNode.setNext(this.head);
            this.head = newNode;
        }
        this.size++;
    }

    /**
     * Adds the element to the back of the list.
     *
     * Must be O(1).
     *
     * @param data the data to add to the back of the list
     * @throws java.lang.IllegalArgumentException if data is null
     */
    public void addToBack(T data) {
        if (data == null) {
            throw new IllegalArgumentException("Data is null");
        }
        DoublyLinkedListNode<T> newNode = new DoublyLinkedListNode<>(data);
        if (this.head == null) {
            this.head = newNode;
        } else {
            this.tail.setNext(newNode);
            newNode.setPrevious(this.tail);
        }
        this.tail = newNode;
        this.size++;
    }

    /**
     * Removes and returns the element at the specified index. Don't forget to
     * consider whether traversing the list from the head or tail is more
     * efficient!
     *
     * Must be O(1) for indices 0 and size - 1 and O(n) for all other cases.
     *
     * @param index the index of the element to remove
     * @return the data formerly located at the specified index
     * @throws java.lang.IndexOutOfBoundsException if index < 0 or index >= size
     */
    public T removeAtIndex(int index) {
        if (index < 0 || index >= this.size) {
            throw new IndexOutOfBoundsException("Index does not exist within the range of the linkedlist");
        }
        if (index == 0) {
            return removeFromFront();
        } else if (index == this.size - 1) {
            return removeFromBack();
        } else {
            int middle = (this.size - 1) / 2;
            if (index <= middle) {
                DoublyLinkedListNode<T> cur = this.head;
                int pos = 0;
                while (cur != null) {
                    if (index == pos) {
                        cur.getPrevious().setNext(cur.getNext());
                        cur.getNext().setPrevious(cur.getPrevious());
                        this.size--;
                        return cur.getData();
                    }
                    pos++;
                    cur = cur.getNext();
                }
            } else {
                DoublyLinkedListNode<T> cur = this.tail;
                int pos = this.size - 1;
                while (cur != null) {
                    if (index == pos) {
                        cur.getPrevious().setNext(cur.getNext());
                        cur.getNext().setPrevious(cur.getPrevious());
                        this.size--;
                        return cur.getData();
                    }
                    pos--;
                    cur = cur.getPrevious();
                }
            }
        }
        return null;
    }

    /**
     * Removes and returns the first element of the list.
     *
     * Must be O(1).
     *
     * @return the data formerly located at the front of the list
     * @throws java.util.NoSuchElementException if the list is empty
     */
    public T removeFromFront() {
        if (this.size == 0) {
            throw new NoSuchElementException("Cannot remove from empty list");
        } else {
            T retData = this.head.getData();
            this.head = this.head.getNext();
            if (this.head != null) {
                this.head.setPrevious(null);
            }
            this.size--;
            if (this.size == 0) {
                this.tail = null;
            }
            return retData;
        }
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
        if (this.size == 0) {
            throw new NoSuchElementException("Cannot remove from empty list");
        } else {
            T retData = this.tail.getData();
            this.tail = this.tail.getPrevious();
            if (this.tail != null) {
                this.tail.setNext(null);
            }
            this.size--;
            if (this.size == 0) {
                this.head = null;
            }
            return retData;
        }
    }

    /**
     * Returns the element at the specified index. Don't forget to consider
     * whether traversing the list from the head or tail is more efficient!
     *
     * Must be O(1) for indices 0 and size - 1 and O(n) for all other cases.
     *
     * @param index the index of the element to get
     * @return the data stored at the index in the list
     * @throws java.lang.IndexOutOfBoundsException if index < 0 or index >= size
     */
    public T get(int index) {
        if (index < 0 || index >= this.size) {
            throw new IndexOutOfBoundsException("Index does not exist within the range of the linkedlist");
        }
        if (index == 0) {
            return this.head.getData();
        } else if (index == this.size - 1) {
            return this.tail.getData();
        } else {
            int middle = (this.size - 1) / 2;
            if (index <= middle) {
                DoublyLinkedListNode<T> cur = this.head;
                int pos = 0;
                while (cur != null) {
                    if (index == pos) {
                        return cur.getData();
                    }
                    pos++;
                    cur = cur.getNext();
                }
            } else {
                DoublyLinkedListNode<T> cur = this.tail;
                int pos = this.size - 1;
                while (cur != null) {
                    if (index == pos) {
                        return cur.getData();
                    }
                    pos--;
                    cur = cur.getPrevious();
                }
            }
        }
        return null;
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
     * Clears all data and resets the size.
     *
     * Must be O(1).
     */
    public void clear() {
        this.head = null;
        this.tail = null;
        this.size = 0;
    }

    /**
     * Removes and returns the last copy of the given data from the list.
     *
     * Do not return the same data that was passed in. Return the data that
     * was stored in the list.
     *
     * Must be O(1) if data is in the tail and O(n) for all other cases.
     *
     * @param data the data to be removed from the list
     * @return the data that was removed
     * @throws java.lang.IllegalArgumentException if data is null
     * @throws java.util.NoSuchElementException   if data is not found
     */
    public T removeLastOccurrence(T data) {
        if (data == null) {
            throw new IllegalArgumentException("Data is null");
        }
        if (this.size == 0) {
            throw new NoSuchElementException("Data was not found in the list");
        }
        if (data == this.tail.getData()) {
            return removeFromBack();
        } else {
            DoublyLinkedListNode<T> cur = this.tail;
            int pos = this.size - 1;
            while (cur != null) {
                if (cur.getData().equals(data) || cur.getData() == data) {
                    return removeAtIndex(pos);
                }
                pos--;
                cur = cur.getPrevious();
            }
        }
        throw new NoSuchElementException("Data was not found in the list");
    }

    /**
     * Returns an array representation of the linked list. If the list is
     * size 0, return an empty array.
     *
     * Must be O(n) for all cases.
     *
     * @return an array of length size holding all of the objects in the
     * list in the same order
     */
    public Object[] toArray() {
        if (this.size == 0) {
            return new Object[0];
        } else {
            Object[] retarr = new Object[this.size];
            DoublyLinkedListNode<T> cur = this.head;
            int pos = 0;
            while (cur != null) {
                retarr[pos] = cur.getData();
                pos++;
                cur = cur.getNext();
            }
            return retarr;
        }
    }

    /**
     * Returns the head node of the list.
     *
     * For grading purposes only. You shouldn't need to use this method since
     * you have direct access to the variable.
     *
     * @return the node at the head of the list
     */
    public DoublyLinkedListNode<T> getHead() {
        // DO NOT MODIFY!
        return head;
    }

    /**
     * Returns the tail node of the list.
     *
     * For grading purposes only. You shouldn't need to use this method since
     * you have direct access to the variable.
     *
     * @return the node at the tail of the list
     */
    public DoublyLinkedListNode<T> getTail() {
        // DO NOT MODIFY!
        return tail;
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
        // DO NOT MODIFY!
        return size;
    }
}
