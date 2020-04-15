package deques;

public class LinkedDeque<T> extends AbstractDeque<T> {
    private int size;
    // IMPORTANT: Do not rename these fields or change their visibility.
    // We access these during grading to test your code.
    Node<T> front;
    Node<T> back; // may be the same as front, if you're using circular sentinel nodes


    public LinkedDeque() {
        front = new Node((T) new Object());
        back = new Node((T) new Object());
        front.next = back;
        back.prev = front;
        size = 0;
        //throw new UnsupportedOperationException("Not implemented yet.");
    }

    static class Node<T> {
        // IMPORTANT: Do not rename these fields or change their visibility.
        // We access these during grading to test your code.
        T value;
        Node<T> next;
        Node<T> prev;

        Node(T value) {
            this.value = value;
            this.next = null;
            this.prev = null;
        }
    }

    public void addFirst(T item) {
        Node addItem = new Node(item);
        front.next.prev = addItem;
        addItem.next = front.next;
        addItem.prev = front;
        front.next = addItem;

        size += 1;
        //throw new UnsupportedOperationException("Not implemented yet.");
    }

    public void addLast(T item) {
        Node addItem = new Node(item);
        back.prev.next = addItem;
        addItem.next = back;
        addItem.prev = back.prev;
        back.prev = addItem;
        size += 1;
        //throw new UnsupportedOperationException("Not implemented yet.");
    }

    public T removeFirst() {
        if (size == 0) {
            return null;
        }
        Node rem = front.next;
        front.next = front.next.next;
        front.next.prev = front;
        size -= 1;
        return (T) rem.value;
    }

    public T removeLast() {
        if (size == 0) {
            return null;
        }
        Node rem = back.prev;
        back.prev = back.prev.prev;
        back.prev.next = back;
        size -= 1;
        return (T) rem.value;
        //throw new UnsupportedOperationException("Not implemented yet.");
    }

    public T get(int index) {
        if ((index >= size) || (index < 0)) {
            return null;
        }
        Node n = front.next;
        for (int i = 0; i < index; i++) {
            n = n.next;
        }
        return (T) n.value;
        //throw new UnsupportedOperationException("Not implemented yet.");
    }

    public int size() {
        return size;
    }
}
