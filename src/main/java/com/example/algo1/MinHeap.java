package com.example.algo1;

public class MinHeap {

    private CostEntry[] heap;
    private int size;
    private int capacity;



    // Constructor
    public MinHeap(int capacity) {
        this.capacity = capacity;
        this.size = 0;
        heap = new CostEntry[capacity + 1]; // +1 to avoid using index 0
    }

    // Utility function to get left child index
    private int left(int parent) {
        return 2 * parent;
    }

    // Utility function to get right child index
    private int right(int parent) {
        return 2 * parent + 1;
    }

    // Utility function to get parent index
    private int parent(int child) {
        return child / 2;
    }

    // Checks if the heap is empty
    public boolean isEmpty() {
        return size == 0;
    }

    // Inserts an element into the heap
    public void insert(CostEntry data) {
        if (size == capacity) {
            System.out.println("Heap overflow");
            return;
        }

        // Initially place element at the end
        heap[++size] = data;

        // Heapify bottom-up to maintain heap property
        int current = size;
        while (current > 1 && heap[current].getCost() < heap[parent(current)].getCost()) {
            swap(current, parent(current));
            current = parent(current);
        }
    }

    //Swaps two elements in the heap (for internal heap)
    private void swap(int i, int j) {
        CostEntry temp = heap[i];
        heap[i] = heap[j];
        heap[j] = temp;
    }

    // Extracts the minimum element from the heap (root)
    public CostEntry extractMin() {
        if (isEmpty()) {
            System.out.println("Heap underflow");
            return null;
        }

        CostEntry min = heap[1]; // root element

        // Move the last element to the root
        heap[1] = heap[size];
        size--;

        // Heapify top-down to maintain heap property
        heapify(1);

        return min;
    }

    // Heapify function to maintain heap property
    private void heapify(int index) {
        int smallest = index;
        int leftChild = left(index);
        int rightChild = right(index);

        // Check if left child is smaller
        if (leftChild <= size && heap[leftChild].getCost() < heap[smallest].getCost()) {
            smallest = leftChild;
        }

        // Check if right child is smaller
        if (rightChild <= size && heap[rightChild].getCost() < heap[smallest].getCost()) {
            smallest = rightChild;
        }

        // If root is not the smallest, swap and recursively heapify
        if (smallest != index) {
            swap(index, smallest);
            heapify(smallest);
        }
    }





}