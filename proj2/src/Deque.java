import edu.princeton.cs.algs4.StdOut;

import java.util.Iterator;

public class Deque<Item> implements Iterable<Item> {
    private class Node{
         Node prev;
         Item item;
         Node next;

         Node(Node prev, Item item, Node next){
            this.prev = prev;
            this.next = next;
            this.item = item;
        }
    }
    private int size;
    private Node first;
    private Node last;
    private Node nil;

    public Deque(){
        size = 0;
        nil = new Node(null,null,null);
        first = nil;
        last = nil;

    }                           // construct an empty deque
    public boolean isEmpty() {

        return first.item == null && last.item == null;
    }      // is the deque empty?
    public int size(){
        return size;
    }                        // return the number of items on the deque
    public void addFirst(Item item){
        if (item == null){
            throw new java.lang.IllegalArgumentException();
        }
        Node newfirst = new Node(nil,item,first);
        if (isEmpty()){
            first = newfirst;
            last = newfirst;
        }
        else {
            first.prev = newfirst;
            first = newfirst;
        }
        size ++;

    }          // add the item to the front
    public void addLast(Item item){
        if (item == null){
            throw new java.lang.IllegalArgumentException();
        }
        Node newlast = new Node(last, item, nil);
        if (isEmpty()){
            last = newlast;
            first =newlast;
        }
        else {
            last.next = newlast;
            last = newlast;
        }
        size ++;

    }           // add the item to the end
    public Item removeFirst(){
        if ( isEmpty()){
            throw new java.util.NoSuchElementException();
        }
        Item item = first.item;
        first = first.next;
        if (first.item == null){
            last = nil;
        }
        else {
            first.prev = nil ;
        }
        size --;
        return item;


    }                // remove and return the item from the front
    public Item removeLast(){
        if ( isEmpty()){
            throw new java.util.NoSuchElementException();
        }
        Item item = last.item;
        last = last.prev;
        if (last.item == null){
            first = nil;
        }
        else {
            last.next = nil;
        }
        size --;
        return item;
    }                 // remove and return the item from the end
    public Iterator<Item> iterator(){
        return new DequeIterator();
    }

    private class DequeIterator implements Iterator<Item>{
        private Node current = first;
        public Item next(){
            if (size == 0 ){
                throw new java.util.NoSuchElementException();
            }
            Item item = current.item;
            current = current.next;
            return item;
        }
        public boolean hasNext(){
            return current.next != null;
        }
        public void remove(){
            throw new java.lang.UnsupportedOperationException();
        }

    }
    // return an iterator over items in order from front to end
    public static void main(String[] args) {
        Deque<Integer> deque = new Deque<Integer>();
        for (int i = 0; i < 6; i += 2) {
            deque.addFirst(i);
            deque.addLast(i+1);
        }
        Iterator<Integer> it = deque.iterator();
        while (it.hasNext()) StdOut.print(it.next() + " ");
        StdOut.println(" size: " + deque.size());

        for (int i = 0; i < 3; ++i) {
            deque.removeLast();
            deque.removeFirst();

        }
        StdOut.println("size: " + deque.size());

        for (int i = 0; i < 6; i += 2) {
            deque.addLast(i);
            deque.addFirst(i+1);
        }
        it = deque.iterator();
        while (it.hasNext()) StdOut.print(it.next() + " ");
        StdOut.println(" size: " + deque.size());
    }
}