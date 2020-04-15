package priorityqueues;

// import jdk.nashorn.api.tree.Tree;

// import jdk.nashorn.api.tree.Tree;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import java.util.TreeMap;
import java.util.NoSuchElementException;
// import java.util.Optional;

public class ArrayHeapMinPQ<T extends Comparable<T>> implements ExtrinsicMinPQ<T> {
    // IMPORTANT: Do not rename these fields or change their visibility.
    // We access these during grading to test your code.
    static final int START_INDEX = 0;
    List<PriorityNode<T>> items;
    TreeMap<T, Integer> ordItems = new TreeMap<T, Integer>();
    private int SIZE;

    public ArrayHeapMinPQ() {
        items = new ArrayList<>();

        // replace this with your code
        //throw new UnsupportedOperationException("Not implemented yet.");
    }

    // Here's a method stub that may be useful. Feel free to change or remove it, if you wish.
    // You'll probably want to add more helper methods like this one to make your code easier to read.
    /**
     * A helper method for swapping the items at two indices of the array heap.
     */
    private void swap(int a, int b) {
        // replace this with your code
        // throw new UnsupportedOperationException("Not implemented yet.");
        Collections.swap(items, a, b);
        // PriorityNode tmp = new PriorityNode(items.get(a).getItem(), items.get(a).getPriority());
    }

    /**
     * Adds an item with the given priority value.
     * Runs in O(log N) time (except when resizing).
     *
     * @throws IllegalArgumentException if item is null or is already present in the PQ
     */
    @Override
    public void add(T item, double priority) {
        // replace this with your code
        // throw new UnsupportedOperationException("Not implemented yet.");
        if (item == null || contains(item)) {
            throw new IllegalArgumentException();
        }
        // add item at the end of arrayHeap
        PriorityNode newItem = new PriorityNode(item, priority);
        items.add(SIZE, newItem);
        // Recursively percolate up
        int cap = percolateUp(SIZE, false);
        // Update TreeMap by adding the item
        // int index = items.indexOf(newItem);

        ordItems.put(item, cap);

        // Update Size
        SIZE++;
    }

    private int percolateUp(int idx, boolean changePr) {
        //if (idx > 0) { idx = idx - 1; }
        double tmp = items.get(idx).getPriority();
        int tmpIdx = idx;
        int cap = idx;
        for (; idx > 0;) {
            if (idx > 1) {
                idx = (idx - 1) / 2;
            } else {
                idx = idx / 2;
            }
            if (items.get(idx).getPriority() > tmp) {
                ordItems.replace(items.get(idx).getItem(), tmpIdx);
                if (changePr) {
                    ordItems.replace(items.get(tmpIdx).getItem(), idx);
                }
                swap(idx, tmpIdx);
                tmpIdx = (tmpIdx - 1) / 2;

                cap = (cap - 1) / 2;
            }
        }
        return cap;
    }

    private void percolateDown(int idx, boolean changePr) {
        if (SIZE > 1) {
            double tmp = items.get(idx).getPriority();

            for (; (idx + 1) * 2 <= items.size();) {
                if (idx == 0) {
                    idx = 1;
                } else {
                    idx = (idx * 2) + 1;
                }
                if (SIZE - 2 > idx) {
                    if (items.get(idx).getPriority() > items.get(idx + 1).getPriority()) {
                        idx++;
                    }
                }
                if (items.get(idx).getPriority() < tmp) {
                    ordItems.replace(items.get(idx).getItem(), (idx - 1) / 2);
                    //if (changePr) {
                    ordItems.replace(items.get((idx - 1) / 2).getItem(), idx);
                    //}
                    swap(idx, (idx - 1) / 2);
                }
            }
        }
    }

    /**
     * Returns true if the PQ contains the given item; false otherwise.
     * Runs in O(log N) time.
     */
    @Override
    public boolean contains(T item) {
        // replace this with your code
        // throw new UnsupportedOperationException("Not implemented yet.");
        return ordItems.containsKey(item);
    }


    /**
     * Returns the item with the least-valued priority.
     * Runs in O(log N) time.
     *
     * @throws NoSuchElementException if the PQ is empty
     */
    @Override
    public T peekMin() {
        // replace this with your code
        // throw new UnsupportedOperationException("Not implemented yet.");
        if (SIZE < 1) {
            throw new NoSuchElementException("PQ is empty");
        }
        return items.get(0).getItem();
    }

    /**
     * Removes and returns the item with the least-valued priority.
     * Runs in O(log N) time (except when resizing).
     *
     * @throws NoSuchElementException if the PQ is empty
     */
    @Override
    public T removeMin() {
        // replace this with your code
        // throw new UnsupportedOperationException("Not implemented yet.");
        if (items.size() <= 0) {
            throw new NoSuchElementException("PQ is empty...");
        }
        // find min value, and store in tmp to return at the end
        T min = items.get(0).getItem();
        // update ordItems by removing the node
        ordItems.remove(min);
        // swap in the to be removed slot using the val at the end of the list
        swap(0, SIZE - 1);
        // remove the min which is now at the end of the list
        items.remove(SIZE - 1);
        // recursively percolate down
        percolateDown(0, false);
        // update size
        SIZE--;
        return min;
    }


    /**
     * Changes the priority of the given item.
     * Runs in O(log N) time.
     *
     * @throws NoSuchElementException if the item is not present in the PQ
     */
    @Override
    public void changePriority(T item, double priority) {
        // replace this with your code
        // throw new UnsupportedOperationException("Not implemented yet.");
        if (ordItems.get(item) == null) {
            throw new NoSuchElementException();
        }

        int index = ordItems.get(item);
        double oldPriority = items.get(index).getPriority();
        items.get(index).setPriority(priority);
        double downPriority = items.get((index - 1) / 2).getPriority();
        if (oldPriority != priority) {
            if (downPriority > priority) {
                percolateUp(index, true);
            } else {
                percolateDown(index, true);
            }
        }


    }

    /**
     * Returns the number of items in the PQ.
     * Runs in O(log N) time.
     */
    @Override
    public int size() {
        // replace this with your code
        // throw new UnsupportedOperationException("Not implemented yet.");
        return SIZE;
    }
}
