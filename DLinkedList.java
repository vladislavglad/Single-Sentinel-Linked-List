import java.util.*;

public class DLinkedList<E> implements List<E> {

    //anonymous Node class for doubly linked list.
    private static class Node<E> {
        private E data;
        private Node<E> prev;
        private Node<E> next;

        //arg. constructor.
        public Node(E d, Node<E> p, Node<E> n) {
            data = d;
            prev = p;
            next = n;
        }

        //no-arg. constructor.
        public Node() {
            this(null, null, null);
        }

        //getters.
        public E getData() {
            return data;
        }

        public Node<E> getPrev() {
            return prev;
        }

        public Node<E> getNext() {
            return next;
        }

        //setters.
        public void setData(E d) {
            data = d;
        }

        public void setPrev(Node<E> p) {
            prev = p;
        }

        public void setNext(Node<E> n) {
            next = n;
        }
    }

    //data fields for doubly linked list implementation.
    private Node<E> sentinel;
    private int size;

    public DLinkedList() {
        //create a Node with no arg. constructor.
        sentinel = new Node<>();

        /*initialize fields one by one, otherwise causes NullPointerException
          as sentinel needs to point to itself that was never created before.
          (this is what I think at least.) */
        sentinel.setData(null);
        sentinel.setPrev(sentinel);
        sentinel.setNext(sentinel);

        size = 0;
    }

    //utility method that adds in-between.
    private void addBetween(Node<E> prevNode, Node<E> nextNode, E d) {
        //create a new node with arg. constructor.
        Node<E> newNode = new Node<>(d, prevNode, nextNode);

        //include this new node in List.
        prevNode.setNext(newNode);
        nextNode.setPrev(newNode);

        //increment size.
        size++;
    }

    //utility method that removes current node.
    private void remove(Node<E> node) {
        //get previous and next nodes of a current node.
        Node<E> prevNode = node.getPrev();
        Node<E> nextNode = node.getNext();

        //skip the current node.
        prevNode.setNext(nextNode);
        nextNode.setPrev(prevNode);

        //set current node's pointers to null.
        //node.setPrev(null);
        //node.setNext(null);

        //decrement size of List.
        size--;
    }

    public void addFirst(E d) {
        addBetween(sentinel, sentinel.getNext(), d);
    }

    public void addLast(E d) {
        addBetween(sentinel.getPrev(), sentinel, d);
    }

    public E removeFirst() {
        E data = sentinel.getNext().getData();
        remove(sentinel.getNext());
        return data;
    }

    public E removeLast() {
        E data = sentinel.getPrev().getData();
        remove(sentinel.getPrev());
        return data;
    }

    public E getFirst() {
        return sentinel.getNext().getData();
    }

    public E getLast() {
        return sentinel.getPrev().getData();
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public boolean add(E d) {
        addLast(d);
        return true;
    }

    public void add(int index, E data) {
        Node<E> node = advanceToIndex(index);
        Node<E> nextNode = node.getNext();
        addBetween(node, nextNode, data);
    }

    public E get(int index) {
        Node<E> node = advanceToIndex(index);
        return node.getData();
    }

    public int hashCode() {
        return sentinel.hashCode();
    }

    public E set(int index, E newElm) {
        if (isOutOfBounds(index))
            throw new IndexOutOfBoundsException();

        Node<E> node = advanceToIndex(index);
        E old = node.getData();
        node.setData(newElm);
        return old;
    }

    public void clear() {
        sentinel.setPrev(sentinel);
        sentinel.setNext(sentinel);
        size = 0;
    }

    //itterator() method.
    public Iterator<E> iterator() {
        return new DLinkedListIterator();
    }

    //private inner Iterator class that has access to data fields
    private class DLinkedListIterator implements Iterator<E> {
        private Node<E> iter;

        public DLinkedListIterator() {
            //iter is the first element after the sentinel.
            iter = sentinel.getNext();
        }

        public E next() {
            //get data to return
            E data = iter.getData();

            //advance iter to the next element.
            iter = iter.getNext();
            return data;
        }

        public boolean hasNext() {
            //if iter's data is null, then it is sentinel.
            return iter.getData() != null;
        }
    }

    //utility method that checks the bounds.
    private boolean isOutOfBounds(int index) {
        return index < 0 || index >= size;
    }

    //method that moves to a specified index in the list.
    private Node<E> advanceToIndex(int index) {

        //first checks if it is out of bounds.
        if(isOutOfBounds(index))
            throw new IndexOutOfBoundsException();

        //0th index element initialization.
        int currentIndex = 0;
        Node<E> element = sentinel.getNext();

        //advancec to the specified index.
        while(currentIndex<index) {
            element = element.getNext();
            currentIndex++;
        }

        //returns element at the index.
        return element;
    }

    public boolean equals(Object obj) {

        //check if the same instance.
        if(! (obj instanceof DLinkedList))
            return false;

        //else type cast object to be DLinkedList...
        DLinkedList<E> another = (DLinkedList<E>)obj;

        //and initialize both pointers/iterators.
        Node<E> iter = sentinel.getNext();
        Node<E> anotherIter = another.sentinel.getNext();

        //check List's elements one by one
        while(iter.getData()!=null) {

            //catch an element that is different.
            if( !(iter.getData().equals(anotherIter.getData())) )
                return false;

            //advance to the next element.
            iter = iter.getNext();
            anotherIter = anotherIter.getNext();
        }

        //return ture for equality.
        return true;
    }

    //same idea as equals method.
    public int indexOf(Object obj) {
        int index = 0;
        Node<E> iter = sentinel.getNext();

        //looking for an index of specified element.
        while (iter.getData() != null) {
            if (iter.getData().equals(obj))
                return index;
            index++;
            iter = iter.getNext();
        }
        return -1;
    }

    public int lastIndexOf(Object o) {
        int index = size;

        //iterate through the list from right.
        for (Node<E> iter = sentinel.getPrev(); iter.getData() != null;
             iter = iter.prev) {

            index--;

            //if obj. is found, return index.
            if (o.equals(iter.getData()))
                return index;
        }
        return -1;
    }

    //call indexOf() method to find an object.
    public boolean contains(Object obj) {
        return indexOf(obj) != -1;
    }

    public boolean containsAll(Collection<?> c) {
        for (Object e : c) {
            if (!contains(e))
                return false;
        }
        return true;
    }

    public boolean remove(Object obj) {
        //find index of obj.
        int index = indexOf(obj);

        //if its -1, the obj. is not in the List.
        if (index==-1)
            return false;

        //advance to this index and remove this node.
        Node<E> node = advanceToIndex(index);
        remove(node);
        return true;
    }

    public E remove(int index) {
        Node<E> node = advanceToIndex(index);
        E data = node.getData();

        remove(node);
        return data;
    }

    public List<E> subList(int fromIndex, int toIndex) {
        DLinkedList<E> list = new DLinkedList<>();
        Node<E> node = advanceToIndex(fromIndex);

        int index = fromIndex;
        while (index < toIndex) {
            list.add(node.getData());

            node.getNext();
            index++;
        }
        return (List<E>)list;
    }

    public Object[] toArray() {

        //create an array of Object[]
        Object[] arr = new Object[size];
        int index = 0;

        //ittereate through the entire list and populate the array.
        for (Node<E> x = sentinel.getNext(); x.getData()!=null; x.getNext()) {
            arr[index] = x.getData();
            index++;
        }

        //return the referance to now populated array.
        return arr;
    }
    
    //----------- All Unsupported Operations follow... -----------
    public <T> T[] toArray(T[] arr) {
        throw new UnsupportedOperationException();
    }

    public ListIterator<E> listIterator() {
        throw new UnsupportedOperationException();
    }

    public ListIterator<E> listIterator(int index) {
        throw new UnsupportedOperationException();
    }

    public boolean addAll(Collection<? extends E> c) {
        throw new UnsupportedOperationException();
    }

    public boolean addAll(int i, Collection<? extends E> c) {
        throw new UnsupportedOperationException();
    }

    public boolean removeAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }
    //----------- End of Unsupported Operations -----------

    public static void main(String[] args) {
        DLinkedList<String> list = new DLinkedList<>();

        list.add("apple");
        list.add("banana");
        list.addFirst("phone");

        System.out.println(list.getFirst());
        System.out.println(list.getLast());

        list.removeLast();
        System.out.println(list.getLast());
    }
}
