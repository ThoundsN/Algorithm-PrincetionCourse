import java.util.Iterator;
import edu.princeton.cs.algs4.StdRandom;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private Item[] array;
    private int size;
    private int capacity;

    public RandomizedQueue(){
        size = 0 ;
        capacity = 1;
        update();
    }                 // construct an empty randomized queue
    public boolean isEmpty(){
        return size == 0;
    }                 // is the randomized queue empty?
    public int size(){
        return  size;
    }                        // return the number of items on the randomized queue
    public void enqueue(Item item){
        if(item == null){
            throw new java.lang.IllegalArgumentException();
        }
        if (size == capacity){
            capacity = capacity* 2 ;
            update();
        }
        array[size++] = item;

    }           // add the item
    public Item dequeue(){
        if (isEmpty()){
            throw new java.util.NoSuchElementException();
        }
        if (size == capacity/4){
            capacity = capacity/2;
            update();
        }
        int random = StdRandom.uniform(size);
        Item item = array[random];
        array[random] = array[--size];
        array[size] = null;


        return item;
    }                    // remove and return a random item
    public Item sample(){
        if (isEmpty()){
            throw new java.util.NoSuchElementException();
        }
        int id = StdRandom.uniform(size);
        return array[id];
    }
    // return a random item (but do not remove it)
    private void update(){
        Item[] temp = (Item[]) new Object[capacity];
        for (int i = 0; i < size; i++) {
            temp[i] = array[i];
        }
        for (int i = size; i < capacity; i++){
            temp[i] = null;
        }
        array = temp;
    }
    public Iterator<Item> iterator(){
        return new RandomizedQueueIterator();
    }
    // return an independent iterator over items in random order

    private class RandomizedQueueIterator implements Iterator<Item>{
        private int[] random;
        private int current;

        public RandomizedQueueIterator(){
            random = new int[size];
            for (int i = 0; i < size; i++){
                random[i] = i;
            }
            StdRandom.shuffle(random);
            current = 0;
        }
        public Item next(){
            if (!hasNext()){
                throw new java.util.NoSuchElementException();
            }
            return array[random[current++]];

        }
        public boolean hasNext(){
            return current!= size;
        }
        public void remove(){
            throw new java.lang.UnsupportedOperationException();
        }

    }
//    public static void main(String[] args)   // unit testing (optional)
}