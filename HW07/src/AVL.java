import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Your implementation of an AVL Tree.
 *
 * @author Anthony Wong
 * @userid awong307
 * @GTID 903579250
 * @version 1.0
 */
public class AVL<T extends Comparable<? super T>> {
    // DO NOT ADD OR MODIFY INSTANCE VARIABLES.
    private AVLNode<T> root;
    private int size;

    /**
     * A no-argument constructor that should initialize an empty AVL.
     *
     * Since instance variables are initialized to their default values, there
     * is no need to do anything for this constructor.
     */
    public AVL() {
        // DO NOT IMPLEMENT THIS CONSTRUCTOR!
    }

    /**
     * Initializes the AVL tree with the data in the Collection. The data
     * should be added in the same order it appears in the Collection.
     *
     * @throws IllegalArgumentException if data or any element in data is null
     * @param data the data to add to the tree
     */
    public AVL(Collection<T> data) {
        if (data == null) {
            throw new IllegalArgumentException("Data is null");
        }
        for (T element : data) {
            if (element == null) {
                throw new IllegalArgumentException("Data is null");
            }
            add(element);
        }
    }

    /**
     * Adds the data to the AVL. Start by adding it as a leaf like in a regular
     * BST and then rotate the tree as needed.
     *
     * If the data is already in the tree, then nothing should be done (the
     * duplicate shouldn't get added, and size should not be incremented).
     *
     * Remember to recalculate heights and balance factors going up the tree,
     * rebalancing if necessary.
     *
     * @throws IllegalArgumentException if the data is null
     * @param data the data to be added
     */
    public void add(T data) {
        if (data == null) {
            throw new IllegalArgumentException("Data is null");
        }
        this.root = rAdd(data, this.root);
    }

    /**
     * Recursively add node to tree and rotate accordingly
     * @param data data to add
     * @param current current node in recursion
     * @return ptr reinforcement
     */
    private AVLNode<T> rAdd(T data, AVLNode<T> current) {
        if (current == null) {
            this.size++;
            return new AVLNode<>(data);
        } else if (current.getData().compareTo(data) > 0) {
            current.setLeft(rAdd(data, current.getLeft()));
        } else if (current.getData().compareTo(data) < 0) {
            current.setRight(rAdd(data, current.getRight()));
        }
        updateNode(current);
        return checkRotation(current);
    }

    /**
     * Update height and balance factor of node
     * @param current node to update
     */
    private void updateNode(AVLNode<T> current) {
        int leftheight = -1;
        int rightheight = -1;
        int height;
        if (current.getRight() != null) {
            rightheight = current.getRight().getHeight();
        }
        if (current.getLeft() != null) {
            leftheight = current.getLeft().getHeight();
        }
        current.setBalanceFactor(leftheight - rightheight);
        height = leftheight > rightheight ? ++leftheight : ++rightheight;
        current.setHeight(height);
    }

    /**
     * Check if rotation is needed
     * @param current node to check
     * @return ptr reinforcement
     */
    private AVLNode<T> checkRotation(AVLNode<T> current) {
        int leftchildbf = -1;
        int rightchildbf = -1;
        if (current.getLeft() != null) {
            leftchildbf = current.getLeft().getBalanceFactor();
        }
        if (current.getRight() != null) {
            rightchildbf = current.getRight().getBalanceFactor();
        }
        if (current.getBalanceFactor() < -1) {
            if (rightchildbf > 0) {
                current.setRight(rotateRight(current.getRight()));
            }
            current = rotateLeft(current);
        } else if (current.getBalanceFactor() > 1) {
            if (leftchildbf < 0) {
                current.setLeft(rotateLeft(current.getLeft()));
            }
            current = rotateRight(current);
        }
        return current;
    }

    /**
     * Perform right rotation
     * @param node node to perform on
     * @return ptr reinforcement
     */
    private AVLNode<T> rotateRight(AVLNode<T> node) {
        AVLNode<T> leftnode = node.getLeft();
        node.setLeft(leftnode.getRight() != null ? leftnode.getRight() : null);
        leftnode.setRight(node);
        updateNode(node);
        updateNode(leftnode);
        return leftnode;
    }

    /**
     * Perform left rotation
     * @param node node to rotate
     * @return ptr reinforcement
     */
    private AVLNode<T> rotateLeft(AVLNode<T> node) {
        AVLNode<T> rightnode = node.getRight();
        node.setRight(rightnode.getLeft() != null ? rightnode.getLeft() : null);
        rightnode.setLeft(node);
        updateNode(node);
        updateNode(rightnode);
        return rightnode;
    }

    /**
     * Removes the data from the tree. There are 3 cases to consider:
     *
     * 1: the data is a leaf. In this case, simply remove it.
     * 2: the data has one child. In this case, simply replace it with its
     * child.
     * 3: the data has 2 children. Use the successor to replace the data,
     * not the predecessor. As a reminder, rotations can occur after removing
     * the successor node.
     *
     * Remember to recalculate heights going up the tree, rebalancing if
     * necessary.
     *
     * @throws IllegalArgumentException if the data is null
     * @throws java.util.NoSuchElementException if the data is not found
     * @param data the data to remove from the tree.
     * @return the data removed from the tree. Do not return the same data
     * that was passed in.  Return the data that was stored in the tree.
     */
    public T remove(T data) {
        if (data == null) {
            throw new IllegalArgumentException("Data is null");
        }
        AVLNode<T> dummy = new AVLNode<>(null);
        this.root = rRemove(data, this.root, dummy);
        return dummy.getData();
    }

    /**
     * Recursively remove node from tree
     * @param data data to remove
     * @param current current node in recursion
     * @param dummy storage node
     * @return ptr reinforcement
     */
    private AVLNode<T> rRemove(T data, AVLNode<T> current, AVLNode<T> dummy) {
        if (current == null) {
            throw new NoSuchElementException("Data not in tree");
        } else if (current.getData().compareTo(data) > 0) {
            current.setLeft(rRemove(data, current.getLeft(), dummy));
        } else if (current.getData().compareTo(data) < 0) {
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
                AVLNode<T> successor = new AVLNode<>(null);
                current.setRight(findSuccessor(current.getRight(), successor));
                current.setData(successor.getData());
            }
        }
        updateNode(current);
        return checkRotation(current);
    }

    /**
     * Find successor node
     * @param current current node in recursion
     * @param storage storage node
     * @return ptr reinforcement
     */
    private AVLNode<T> findSuccessor(AVLNode<T> current, AVLNode<T> storage) {
        if (current.getLeft() == null) {
            storage.setData(current.getData());
            return current.getRight();
        } else {
            current.setLeft(findSuccessor(current.getLeft(), storage));
        }
        updateNode(current);
        return checkRotation(current);
    }

    /**
     * Returns the data in the tree matching the parameter passed in (think
     * carefully: should you use value equality or reference equality?).
     *
     * @throws IllegalArgumentException if the data is null
     * @throws java.util.NoSuchElementException if the data is not found
     * @param data the data to search for in the tree.
     * @return the data in the tree equal to the parameter. Do not return the
     * same data that was passed in.  Return the data that was stored in the
     * tree.
     */
    public T get(T data) {
        if (data == null) {
            throw new IllegalArgumentException("Data is null");
        }
        AVLNode<T> retnode = rSearch(data, this.root);
        if (retnode == null) {
            throw new NoSuchElementException("Data not found");
        }
        return retnode.getData();
    }

    /**
     * Recursively search for data
     * @param data data to find
     * @param current current node in recursion
     * @return ptr reinforcement
     */
    private AVLNode<T> rSearch(T data, AVLNode<T> current) {
        if (current == null) {
            return null;
        } else if (current.getData().compareTo(data) > 0) {
            return rSearch(data, current.getLeft());
        } else if (current.getData().compareTo(data) < 0) {
            return rSearch(data, current.getRight());
        } else {
            return current;
        }
    }

    /**
     * Returns whether or not data equivalent to the given parameter is
     * contained within the tree. The same type of equality should be used as
     * in the get method.
     *
     * @throws IllegalArgumentException if the data is null
     * @param data the data to search for in the tree.
     * @return whether or not the parameter is contained within the tree.
     */
    public boolean contains(T data) {
        if (data == null) {
            throw new IllegalArgumentException("Data is null");
        }
        return rSearch(data, this.root) != null;
    }

    /**
     * Returns the data on branches of the tree with the maximum depth. If you
     * encounter multiple branches of maximum depth while traversing, then you
     * should list the remaining data from the left branch first, then the
     * remaining data in the right branch. This is essentially a preorder
     * traversal of the tree, but only of the branches of maximum depth.
     *
     * Your list should not duplicate data, and the data of a branch should be
     * listed in order going from the root to the leaf of that branch.
     *
     * Should run in worst case O(n), but you should not explore branches that
     * do not have maximum depth. You should also not need to traverse branches
     * more than once.
     *
     * Hint: How can you take advantage of the balancing information stored in
     * AVL nodes to discern deep branches?
     *
     * Example Tree:
     *                           10
     *                       /        \
     *                      5          15
     *                    /   \      /    \
     *                   2     7    13    20
     *                  / \   / \     \  / \
     *                 1   4 6   8   14 17  25
     *                /           \          \
     *               0             9         30
     *
     * Returns: [10, 5, 2, 1, 0, 7, 8, 9, 15, 20, 25, 30]
     *
     * @return the list of data in branches of maximum depth in preorder
     * traversal order
     */
    public List<T> deepestBranches() {
        List<T> branchlist = new ArrayList<>();
        rDeepestBranches(this.root, branchlist);
        return branchlist;
    }

    /**
     * Recursively traverse tree for deepest branches
     * @param current current node in recursion
     * @param branchlist list of deepest branches
     */
    private void rDeepestBranches(AVLNode<T> current, List<T> branchlist) {
        if (this.size == 0) {
            return;
        }
        if (current.getHeight() == 0) {
            branchlist.add(current.getData());
        } else if (current.getBalanceFactor() > 0) {
            branchlist.add(current.getData());
            rDeepestBranches(current.getLeft(), branchlist);
        } else if (current.getBalanceFactor() < 0) {
            branchlist.add(current.getData());
            rDeepestBranches(current.getRight(), branchlist);
        } else {
            branchlist.add(current.getData());
            rDeepestBranches(current.getLeft(), branchlist);
            rDeepestBranches(current.getRight(), branchlist);
        }
    }

    /**
     * Returns a sorted list of data that are within the threshold bounds of
     * data1 and data2. That is, the data should be > data1 and < data2.
     *
     * Should run in worst case O(n), but this is heavily dependent on the
     * threshold data. You should not explore branches of the tree that do not
     * satisfy the threshold.
     *
     * Example Tree:
     *                           10
     *                       /        \
     *                      5          15
     *                    /   \      /    \
     *                   2     7    13    20
     *                  / \   / \     \  / \
     *                 1   4 6   8   14 17  25
     *                /           \          \
     *               0             9         30
     *
     * sortedInBetween(7, 14) returns [8, 9, 10, 13]
     * sortedInBetween(3, 8) returns [4, 5, 6, 7]
     * sortedInBetween(8, 8) returns []
     *
     * @throws IllegalArgumentException if data1 or data2 are null
     * @param data1 the smaller data in the threshold
     * @param data2 the larger data in the threshold
     * or if data1 > data2
     * @return a sorted list of data that is > data1 and < data2
     */
    public List<T> sortedInBetween(T data1, T data2) {
        if (data1 == null || data2 == null) {
            throw new IllegalArgumentException("Bounds cannot be null");
        }
        if (data1.compareTo(data2) > 0) {
            throw new IllegalArgumentException("Lower bound cannot be larger than upper bound");
        }
        List<T> sortedlist = new ArrayList<>();
        rSortedInBetween(data1, data2, this.root, sortedlist);
        return sortedlist;
    }

    /**
     * Recursively search for nodes between bounds
     * @param data1 lower bound
     * @param data2 upper bound
     * @param current current node in recursion
     * @param sortedlist sorted list between bounds
     */
    private void rSortedInBetween(T data1, T data2, AVLNode<T> current, List<T> sortedlist) {
        if (current == null) {
            return;
        } else if (data1.compareTo(current.getData()) < 0 && data2.compareTo(current.getData()) > 0) {
            rSortedInBetween(data1, data2, current.getLeft(), sortedlist);
            sortedlist.add(current.getData());
            rSortedInBetween(data1, data2, current.getRight(), sortedlist);
        } else if (data1.compareTo(current.getData()) >= 0) {
            rSortedInBetween(data1, data2, current.getRight(), sortedlist);
        } else if (data2.compareTo(current.getData()) <= 0) {
            rSortedInBetween(data1, data2, current.getLeft(), sortedlist);
        }
    }

    /**
     * Clears the tree.
     */
    public void clear() {
        this.root = null;
        this.size = 0;
    }

    /**
     * Returns the height of the root of the tree.
     *
     * Since this is an AVL, this method does not need to traverse the tree
     * and should be O(1)
     *
     * @return the height of the root of the tree, -1 if the tree is empty
     */
    public int height() {
        return this.size == 0 ? -1 : this.root.getHeight();
    }

    /**
     * Returns the size of the AVL tree.
     *
     * For grading purposes only. You shouldn't need to use this method since
     * you have direct access to the variable.
     *
     * @return number of items in the AVL tree
     */
    public int size() {
        // DO NOT MODIFY THIS METHOD
        return size;
    }

    /**
     * Returns the root of the AVL tree.
     *
     * For grading purposes only. You shouldn't need to use this method since
     * you have direct access to the variable.
     *
     * @return the root of the AVL tree
     */
    public AVLNode<T> getRoot() {
        // DO NOT MODIFY THIS METHOD!
        return root;
    }
}