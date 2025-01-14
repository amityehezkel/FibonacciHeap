package FibonacciHeap;

/**
 * FibonacciHeap
 *
 * An implementation of Fibonacci heap over positive integers.
 *
 */
public class FibonacciHeap {
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
	public HeapNode insert(int key, String info) {    
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

	
	/* inserting a tree to a nonempty heap
	 * 
	 * 
	 * pre: this is not empty
	 */
	public void insertTree(HeapNode root) {
		root.prev = this.min;
		root.next = this.min.next;
		root.next.prev = root;
		this.min.next = root;
		
		if (root.key<this.min.key) {
			this.min = root;
		}
		
		this.numTrees++;
		return;
	}
	
	
	/**
	 * 
	 * Return the minimal HeapNode, null if empty.
	 *
	 */
	public HeapNode findMin() {
		return this.min;
	}

	
	
	/**
	 * 
	 * Delete the minimal item
	 *
	 */
	public void deleteMin() {
		deleteMin(true);
		return;
	}
	
	
	/**
	 * 
	 * 
	 * @param realDeleteMin: if true -> need to find new min and consolidate
	 */
	public void deleteMin(boolean realDeleteMin) {			
		while(this.min.child != null) {			//cutting min from it's children
			HeapNode child = this.min.child;
			cut(child);
			insertTree(child);				//adding each children s a new tree to the heap
		}
		HeapNode minNext  = this.min.next;
		deleteTree(this.min);
		this.size--;
		if (realDeleteMin) {	
			consolidating(minNext);
		}
		return;
	}

	
	public HeapNode cut(HeapNode node) {
		this.totalCuts++;
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
		
		if (parent.mark == true) {		//parent had already lost a child before
			return parent;
		}
		
		else {
			if (!parent.isRoot()) {
				parent.mark = true;
			}
			return null;
		}
	}
	
	
	public void deleteTree(HeapNode root) {
		this.numTrees--;
		if (root.next == root) {
			this.min = null;
		}
		
		else {
			root.prev.next = root.next;
			root.next.prev = root.prev;
			root.prev = null;
			root.next = null;
		}
	}
	
	
	public void consolidating(HeapNode startPoint) {
		HeapNode[] ranksArray = new HeapNode[(int) Math.floor(Math.log(this.size)/Math.log(2))+1];
		HeapNode root = startPoint;
		
		while (this.numTrees != 0) {
			HeapNode tmpNextRoot = root.next;
			int rank = root.rank;
			if (ranksArray[rank] == null) {
				ranksArray[rank] = root;
			}
			else {
				successiveLink(root, ranksArray[rank], ranksArray);
			}
			deleteTree(root);
			root = tmpNextRoot;
		}
		
		this.min = startPoint;
		
		for(HeapNode curCell : ranksArray) {
			if (curCell != null) {
				insertTree(curCell);
			}
		}
		return;
	}
	
	
	/*param: 2 roots with same rank to be linked
	 * 
	 * 
	 */
	public void successiveLink(HeapNode root1, HeapNode root2, HeapNode[] array) {
		this.totalLinks++;
		
		HeapNode newRoot;
		HeapNode newChild;
		
		if (root1.key<root2.key) {
			newRoot = root1;
			newChild = root2;
		}
		else {
			newRoot = root2;
			newChild = root1;
		}
		
		newChild.next = newRoot.child;
		newChild.prev = newRoot.child.prev;
		newChild.next.prev = newChild;
		newChild.prev.next = newChild;
		newRoot.child = newChild;
		newChild.parent = newRoot;
		array[newRoot.rank] = null;
		newRoot.rank++;
		
		
		if (array[newRoot.rank] == null) {
			array[newRoot.rank] = newRoot;
			return;
		}
		else {
			successiveLink(newRoot, array[newRoot.rank], array);
			return;
		}
	}
	
	
	/**
	 * 
	 * pre: 0<diff<x.key
	 * 
	 * Decrease the key of x by diff and fix the heap. 
	 * 
	 */
	public void decreaseKey(HeapNode x, int diff) {    
		x.key-=diff;
		if (x.isRoot()) {		//not violating heap rule
			if (x.key<this.min.key) {
				this.min = x;
			}
			
			return;
		}
		
		if (x.key < x.parent.key) {			//violating heap rule
			cascadingCut(x);
		}
		
		return;
	}
	
	
	public void cascadingCut(HeapNode node) {
		HeapNode cutRes = cut(node);
		insertTree(node);
		if (cutRes != null) {
			cascadingCut(cutRes);
		}
		return;
	}

	/**
	 * 
	 * Delete the x from the heap.
	 *
	 */
	public void delete(HeapNode x) {    
		if (x == this.min) {
			deleteMin();
			return;
		}
		else{
			HeapNode originalMin = this.min;
			decreaseKey(x, x.key+1);			//turning x key to -1: x is new min
			deleteMin(false);
			this.min = originalMin;
			return; 	
		}
	}


	/**
	 * 
	 * Return the total number of links.
	 * 
	 */
	public int totalLinks() {
		return this.totalLinks;
	}


	/**
	 * 
	 * Return the total number of cuts.
	 * 
	 */
	public int totalCuts() {
		return this.totalCuts;
	}


	/**
	 * 
	 * Meld the heap with heap2
	 *
	 */
	public void meld(FibonacciHeap heap2) {
		this.size += heap2.size;
		this.numTrees += heap2.numTrees;
		this.totalCuts += heap2.totalCuts;
		this.totalLinks += heap2.totalLinks;
		
		HeapNode min1 = this.min;
		HeapNode min2 = heap2.min;
		HeapNode prev1 = this.min.prev;
		HeapNode next2 = heap2.min.next;
		
		min2.next = min1;
		prev1.next = next2;
		next2.prev = prev1;
		min1.prev = min2;
		
		if (heap2.min.key<this.min.key) {
			this.min = heap2.min;
		}
		return;		
	}

	/**
	 * 
	 * Return the number of elements in the heap
	 *   
	 */
	public int size() {
		return this.size;
	}


	/**
	 * 
	 * Return the number of trees in the heap.
	 * 
	 */
	public int numTrees() {
		return this.numTrees;
	}

	/**
	 * Class implementing a node in a Fibonacci Heap.
	 *  
	 */
	public static class HeapNode {
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

