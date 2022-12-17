import java.util.Collection;
import java.util.List;
import java.util.ArrayList;
import java.util.Queue;
import java.util.LinkedList;
import java.util.NoSuchElementException;

/**
 * Your implementation of a BST.
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
public class BST<T extends Comparable<? super T>> {

    /*
     * Do not add new instance variables or modify existing ones.
     */
    private BSTNode<T> root;
    private int size;

    /**
     * Constructs a new BST.
     *
     * This constructor should initialize an empty BST.
     *
     * Since instance variables are initialized to their default values, there
     * is no need to do anything for this constructor.
     */
    public BST() {
        // DO NOT IMPLEMENT THIS CONSTRUCTOR!
    }

    /**
     * Constructs a new BST.
     *
     * This constructor should initialize the BST with the data in the
     * Collection. The data should be added in the same order it is in the
     * Collection.
     *
     * Hint: Not all Collections are indexable like Lists, so a regular for loop
     * will not work here. However, all Collections are Iterable, so what type
     * of loop would work?
     *
     * @param data the data to add
     * @throws IllegalArgumentException if data or any element in data
     *                                            is null
     */
    public BST(Collection<T> data) {
        if (data == null) {
            throw new IllegalArgumentException("Data is null");
        }
        for (T item : data) {
            if (item == null) {
                throw new IllegalArgumentException("Data is null");
            } else {
                add(item);
            }
        }
    }

    /**
     * Adds the data to the tree.
     *
     * This must be done recursively.
     *
     * The data becomes a leaf in the tree.
     *
     * Traverse the tree to find the appropriate location. If the data is
     * already in the tree, then nothing should be done (the duplicate
     * shouldn't get added, and size should not be incremented).
     *
     * Must be O(log n) for best and average cases and O(n) for worst case.
     *
     * @param data the data to add
     * @throws IllegalArgumentException if data is null
     */
    public void add(T data) {
        if (data == null) {
            throw new IllegalArgumentException("Data is null");
        }
        this.root = rAdd(data, this.root);
    }

    /**
     * Recursive function that adds node to BST
     * @param data data to add
     * @param current current node in BST
     * @return pointer reinforcement of BST
     */
    private BSTNode<T> rAdd(T data, BSTNode<T> current) {
        if (current == null) {
            this.size++;
            current = new BSTNode<>(data);
        } else if (data.compareTo(current.getData()) < 0) {
            current.setLeft(rAdd(data, current.getLeft()));
        } else if (data.compareTo(current.getData()) > 0) {
            current.setRight(rAdd(data, current.getRight()));
        }
        return current;
    }

    /**
     * Removes and returns the data from the tree matching the given parameter.
     *
     * This must be done recursively.
     *
     * There are 3 cases to consider:
     * 1: The node containing the data is a leaf (no children). In this case,
     * simply remove it.
     * 2: The node containing the data has one child. In this case, simply
     * replace it with its child.
     * 3: The node containing the data has 2 children. Use the successor to
     * replace the data. You MUST use recursion to find and remove the
     * successor (you will likely need an additional helper method to
     * handle this case efficiently).
     *
     * Do not return the same data that was passed in. Return the data that
     * was stored in the tree.
     *
     * Hint: Should you use value equality or reference equality?
     *
     * Must be O(log n) for best and average cases and O(n) for worst case.
     *
     * @param data the data to remove
     * @return the data that was removed
     * @throws IllegalArgumentException if data is null
     * @throws java.util.NoSuchElementException   if the data is not in the tree
     */
    public T remove(T data) {
        if (data == null) {
            throw new IllegalArgumentException("Data is null");
        }
        BSTNode<T> dummy = new BSTNode<>(null);
        this.root = rRemove(data, this.root, dummy);
        return dummy.getData();
    }

    /**
     * Traverses BST to remove node
     * @param data data to remove
     * @param current current node in BST
     * @param dummy node with returned data
     * @return pointer reinforcement
     */
    private BSTNode<T> rRemove(T data, BSTNode<T> current, BSTNode<T> dummy) {
        if (current == null) {
            throw new NoSuchElementException("Data not in BST");
        } else if (data.compareTo(current.getData()) < 0) {
            current.setLeft(rRemove(data, current.getLeft(), dummy));
        } else if (data.compareTo(current.getData()) > 0) {
            current.setRight(rRemove(data, current.getRight(), dummy));
        } else {
            dummy.setData(current.getData());
            this.size--;
            if (current.getLeft() == null && current.getRight() == null) {
                return null;
            } else if (current.getLeft() != null && current.getRight() == null) {
                return current.getLeft();
            } else if (current.getLeft() == null && current.getRight() != null) {
                return current.getRight();
            } else {
                BSTNode<T> successor = new BSTNode<>(null);
                current.setRight(getSuccessor(current.getRight(), successor));
                current.setData(successor.getData());
            }
        }
        return current;
    }

    /**
     * Get successor and remove the node
     * @param current current node in BST
     * @param successor holds successor data
     * @return subtree on thr right of removed node
     */
    private BSTNode<T> getSuccessor(BSTNode<T> current, BSTNode<T> successor) {
        if (current.getLeft() == null) {
            successor.setData(current.getData());
            return current.getRight();
        } else {
            current.setLeft(getSuccessor(current.getLeft(), successor));
        }
        return current;
    }

    /**
     * Returns the data from the tree matching the given parameter.
     *
     * This must be done recursively.
     *
     * Do not return the same data that was passed in. Return the data that
     * was stored in the tree.
     *
     * Hint: Should you use value equality or reference equality?
     *
     * Must be O(log n) for best and average cases and O(n) for worst case.
     *
     * @param data the data to search for
     * @return the data in the tree equal to the parameter
     * @throws IllegalArgumentException if data is null
     * @throws java.util.NoSuchElementException   if the data is not in the tree
     */
    public T get(T data) {
        if (data == null) {
            throw new IllegalArgumentException("Data is null");
        }
        BSTNode<T> searchedNode = rSearch(this.root, data);
        if (searchedNode == null) {
            throw new NoSuchElementException("Data not in BST");
        } else {
            return searchedNode.getData();
        }
    }

    /**
     * Recursively search BST for data
     * @param current current node in BST
     * @param target data to find
     * @return node of data or null if it is not in BST
     */
    private BSTNode<T> rSearch(BSTNode<T> current, T target) {
        if (current == null || current.getData().equals(target)) {
            return current;
        } else if (current.getData().compareTo(target) < 0) {
            return rSearch(current.getRight(), target);
        }
        return rSearch(current.getLeft(), target);
    }

    /**
     * Returns whether or not data matching the given parameter is contained
     * within the tree.
     *
     * This must be done recursively.
     *
     * Hint: Should you use value equality or reference equality?
     *
     * Must be O(log n) for best and average cases and O(n) for worst case.
     *
     * @param data the data to search for
     * @return true if the parameter is contained within the tree, false
     * otherwise
     * @throws IllegalArgumentException if data is null
     */
    public boolean contains(T data) {
        if (data == null) {
            throw new IllegalArgumentException("Data is null");
        }
        return rSearch(this.root, data) != null;
    }

    /**
     * Generate a pre-order traversal of the tree.
     *
     * This must be done recursively.
     *
     * Must be O(n).
     *
     * @return the preorder traversal of the tree
     */
    public List<T> preorder() {
        List<T> retlist = new ArrayList<>(this.size);
        traversePreOr(this.root, retlist);
        return retlist;
    }

    /**
     * Recursively traverse preorder
     * @param current current node in BST
     * @param list list of elements in preorder stack
     */
    private void traversePreOr(BSTNode<T> current, List<T> list) {
        if (current == null) {
            return;
        }
        list.add(current.getData());
        traversePreOr(current.getLeft(), list);
        traversePreOr(current.getRight(), list);
    }

    /**
     * Generate an in-order traversal of the tree.
     *
     * This must be done recursively.
     *
     * Must be O(n).
     *
     * @return the inorder traversal of the tree
     */
    public List<T> inorder() {
        List<T> retlist = new ArrayList<>(this.size);
        traverseInOr(this.root, retlist);
        return retlist;
    }

    /**
     * Recursively traverse inorder
     * @param current current node in BST
     * @param list list of elements in inorder stack
     */
    private void traverseInOr(BSTNode<T> current, List<T> list) {
        if (current == null) {
            return;
        }
        traverseInOr(current.getLeft(), list);
        list.add(current.getData());
        traverseInOr(current.getRight(), list);
    }

    /**
     * Generate a post-order traversal of the tree.
     *
     * This must be done recursively.
     *
     * Must be O(n).
     *
     * @return the postorder traversal of the tree
     */
    public List<T> postorder() {
        List<T> retlist = new ArrayList<>(this.size);
        traversePosOr(this.root, retlist);
        return retlist;
    }

    /**
     * Recursively traverse postorder
     * @param current current node in BST
     * @param list list of elements in postorder stack
     */
    private void traversePosOr(BSTNode<T> current, List<T> list) {
        if (current == null) {
            return;
        }
        traversePosOr(current.getLeft(), list);
        traversePosOr(current.getRight(), list);
        list.add(current.getData());
    }

    /**
     * Generate a level-order traversal of the tree.
     *
     * This does not need to be done recursively.
     *
     * Hint: You will need to use a queue of nodes. Think about what initial
     * node you should add to the queue and what loop / loop conditions you
     * should use.
     *
     * Must be O(n).
     *
     * @return the level order traversal of the tree
     */
    public List<T> levelorder() {
        List<T> retlist = new ArrayList<>(this.size);
        Queue<BSTNode<T>> queue = new LinkedList<>();
        queue.add(this.root);
        while (!queue.isEmpty()) {
            BSTNode<T> current = queue.remove();
            if (current != null) {
                retlist.add(current.getData());
                queue.add(current.getLeft());
                queue.add(current.getRight());
            }
        }
        return retlist;
    }

    /**
     * Returns the height of the root of the tree.
     *
     * This must be done recursively.
     *
     * A node's height is defined as max(left.height, right.height) + 1. A
     * leaf node has a height of 0 and a null child has a height of -1.
     *
     * Must be O(n).
     *
     * @return the height of the root of the tree, -1 if the tree is empty
     */
    public int height() {
        if (this.size == 0) {
            return -1;
        }
        return rFindHeight(this.root);
    }

    /**
     * Recursively find height of BST
     * @param current current node in BST
     * @return height at current node - 1
     */
    private int rFindHeight(BSTNode<T> current) {
        if (current == null) {
            return -1;
        }
        int right = rFindHeight(current.getRight());
        int left = rFindHeight(current.getLeft());
        if (left > right) {
            return left + 1;
        } else {
            return right + 1;
        }
    }
    /**
     * Clears the tree.
     *
     * Clears all data and resets the size.
     *
     * Must be O(1).
     */
    public void clear() {
        this.size = 0;
        this.root = null;
    }

    /**
     * Finds and retrieves the k-largest elements from the BST in sorted order,
     * least to greatest.
     *
     * This must be done recursively.
     *
     * In most cases, this method will not need to traverse the entire tree to
     * function properly, so you should only traverse the branches of the tree
     * necessary to get the data and only do so once. Failure to do so will
     * result in an efficiency penalty.
     *
     * EXAMPLE: Given the BST below composed of Integers:
     *
     *                50
     *              /    \
     *            25      75
     *           /  \
     *          12   37
     *         /  \    \
     *        10  15    40
     *           /
     *          13
     *
     * kLargest(5) should return the list [25, 37, 40, 50, 75].
     * kLargest(3) should return the list [40, 50, 75].
     *
     * Should have a running time of O(log(n) + k) for a balanced tree and a
     * worst case of O(n + k).
     *
     * @param k the number of largest elements to return
     * @return sorted list consisting of the k largest elements
     * @throws IllegalArgumentException if k > n, the number of data
     *                                            in the BST
     */
    public List<T> kLargest(int k) {
        if (k > this.size) {
            throw new IllegalArgumentException("K is larger than BST");
        }
        List<T> retlist = new LinkedList<>();
        createIncreasingList((LinkedList<T>) retlist, this.root, k);
        return retlist;
    }

    /**
     * Create k-largest elements in increasing order
     * @param linkedList increasing order stack
     * @param current current node in BST
     * @param k largest number of elements
     */
    private void createIncreasingList(LinkedList<T> linkedList, BSTNode<T> current, int k) {
        if (current == null || linkedList.size() == k) {
            return;
        } else {
            createIncreasingList(linkedList, current.getRight(), k);
            if (linkedList.size() < k) {
                linkedList.addFirst(current.getData());
            }
            if (linkedList.size() < k) {
                createIncreasingList(linkedList, current.getLeft(), k);
            }
        }
    }

    /**
     * Returns the root of the tree.
     *
     * For grading purposes only. You shouldn't need to use this method since
     * you have direct access to the variable.
     *
     * @return the root of the tree
     */
    public BSTNode<T> getRoot() {
        // DO NOT MODIFY THIS METHOD!
        return root;
    }

    /**
     * Returns the size of the tree.
     *
     * For grading purposes only. You shouldn't need to use this method since
     * you have direct access to the variable.
     *
     * @return the size of the tree
     */
    public int size() {
        // DO NOT MODIFY THIS METHOD!
        return size;
    }
}
