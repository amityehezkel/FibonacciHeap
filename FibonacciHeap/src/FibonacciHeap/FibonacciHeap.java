package FibonacciHeap;

import java.util.ArrayList;
import java.util.List;

/**
 * FibonacciHeap
 *
 * An implementation of Fibonacci heap over positive integers.
 *
 */
public class FibonacciHeap
{
	public HeapNode min;
	public int size;
	public int totalLinks;
	public int totalCuts;
	public int numTrees;
	
	/**
	 *
	 * Constructor to initialize an empty heap.
	 *
	 */
	public FibonacciHeap()
	{
		this.min = null;
		this.size = 0;
		this.totalLinks = 0;
		this.totalCuts = 0;
		this.numTrees = 0;
	}

	
	
	/**
	 * 
	 * pre: key > 0
	 *
	 * Insert (key,info) into the heap and return the newly generated HeapNode.
	 *
	 */
	public HeapNode insert(int key, String info) 
	{    
		HeapNode newNode = new HeapNode(key, info);

		if(this.size == 0) {			//heap is empty
			this.min = newNode;
			newNode.prev = newNode;
			newNode.next = newNode;
		}
		
		else {
			newNode.prev = this.min;
			newNode.next = this.min.next;
			newNode.next.prev = newNode;
			this.min.next = newNode;
			
			if(this.min.key>key) {
				this.min = newNode;
			}
		}
		
		this.numTrees++;
		this.size++;
		return newNode;
	}

	
	
	/**
	 * 
	 * Return the minimal HeapNode, null if empty.
	 *
	 */
	public HeapNode findMin()
	{
		return this.min;
	}

	
	
	/**
	 * 
	 * Delete the minimal item
	 *
	 */
	public void deleteMin()
	{
		deleteMin(true);
		
		return; // should be replaced by student code

	}
	
	
	/**
	 * 
	 * 
	 * @param withMinUpdate: if true -> need to find new min and consolidate
	 */
	public void deleteMin(boolean withMinUpdate) {			
		
	}

	
	public void cut(HeapNode node) {
		HeapNode parent = node.parent;
		node.parent = null;
		node.mark = false;
		parent.rank--;
		if (node.next==node) {
			parent.child = null;
		}
		else {
			parent.child = node.next;
			node.prev.next = node.next;
			node.next.prev= node.prev;
		}
		this.totalCuts++;
	}
	
	/**
	 * 
	 * pre: 0<diff<x.key
	 * 
	 * Decrease the key of x by diff and fix the heap. 
	 * 
	 */
	public void decreaseKey(HeapNode x, int diff) 
	{    
		return; // should be replaced by student code
	}

	/**
	 * 
	 * Delete the x from the heap.
	 *
	 */
	public void delete(HeapNode x) 
	{    
		return; // should be replaced by student code
	}


	/**
	 * 
	 * Return the total number of links.
	 * 
	 */
	public int totalLinks()
	{
		return 0; // should be replaced by student code
	}


	/**
	 * 
	 * Return the total number of cuts.
	 * 
	 */
	public int totalCuts()
	{
		return 0; // should be replaced by student code
	}


	/**
	 * 
	 * Meld the heap with heap2
	 *
	 */
	public void meld(FibonacciHeap heap2)
	{
		return; // should be replaced by student code   		
	}

	/**
	 * 
	 * Return the number of elements in the heap
	 *   
	 */
	public int size()
	{
		return 42; // should be replaced by student code
	}


	/**
	 * 
	 * Return the number of trees in the heap.
	 * 
	 */
	public int numTrees()
	{
		return 0; // should be replaced by student code
	}

	/**
	 * Class implementing a node in a Fibonacci Heap.
	 *  
	 */
	public static class HeapNode{
		public int key;
		public String info;
		public HeapNode child;
		public HeapNode next;
		public HeapNode prev;
		public HeapNode parent;
		public int rank;
		public boolean mark;
		
		public HeapNode(int key, String info) {
			this.key = key;
			this.info = info;
			this.child = null;
			this.next = null;
			this.prev = null;
			this.parent = null;
			this.rank = 0;
			this.mark = false;
		}
		
		public boolean isRoot() {
			return (this.parent == null);
		}
	}
}

