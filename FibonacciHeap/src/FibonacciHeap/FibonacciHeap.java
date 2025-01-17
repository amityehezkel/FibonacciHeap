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
		if (this.numTrees == 0) {
			this.min = root;
			root.next = root;
			root.prev = root;
		}
		
		else {
			root.prev = this.min;
			root.next = this.min.next;
			root.next.prev = root;
			this.min.next = root;
			
			if (root.key<this.min.key) {
				this.min = root;
			}
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
		if (realDeleteMin && min != null) {	
			//System.out.println("delete min " + min.key);
		}
		if (this.size == 0) {
			return;
		}
		
		if (this.size == 1) {
			this.min = null;
			this.size = 0;
			this.numTrees = 0;
			return;
		}

		
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
			//printHeap2();
			//printHeap();
		}
		return;
	}

	
	public HeapNode cut(HeapNode node) {
		/*System.out.println("cut " + node.key);
		System.out.println(node.parent == null);
		System.out.println("next " + node.next.key);*/
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
			//System.out.println("parent " + parent.key);
			//System.out.println("new child "+parent.child.key);
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
			node.parent = null;
			node.next = node;
			node.prev = node;
			return null;
		}
	}
	
	
	public void deleteTree(HeapNode root) {
		this.numTrees--;
		if (root.next == root) {
			this.min = null;
			return;
		}
		
		else {
			root.prev.next = root.next;
			root.next.prev = root.prev;
			root.prev = root;
			root.next = root;
		}
	}
	
	
	public void consolidating(HeapNode startPoint) {
		
		HeapNode[] ranksArray = new HeapNode[(int) Math.floor(Math.log(this.size)/Math.log(2))+1];
		HeapNode root = startPoint;
		
		while (this.numTrees != 0) {
			HeapNode tmpNextRoot = root.next;
			int rank = root.rank;
			deleteTree(root);
			if (ranksArray[rank] == null) {
				ranksArray[rank] = root;
			}
			else {
				successiveLink(root, ranksArray[rank], ranksArray);
			}
			root = tmpNextRoot;
		}
		
		
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
		//System.out.println("successiveLink " + root1.key + " " + root2.key + " rank: " + root1.rank);
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
		
		if (newRoot.rank != 0) {
			newChild.next = newRoot.child;
			newChild.prev = newRoot.child.prev;
			newChild.next.prev = newChild;
			newChild.prev.next = newChild;
		}
		
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
		if (x == null) {
			return;
		}
		//System.out.println("decrease key " + x.key + " by " + diff + ", to: " + (x.key-diff));
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
		if (x == null) {return;}
		//System.out.println("delete " + x.key);
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
		if (heap2 == null) {
			return;
		}
		
		this.size += heap2.size;
		this.numTrees += heap2.numTrees;
		this.totalCuts += heap2.totalCuts;
		this.totalLinks += heap2.totalLinks;
		
		if (this.size == 0) {
			this.min = heap2.min;
			return;
		}
		
		if (heap2.size == 0) {
			return;
		}
		
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
			this.next = this;
			this.prev = this;
			this.parent = null;
			this.rank = 0;
			this.mark = false;
		}
		
		public boolean isRoot() {
			return (this.parent == null);
		}
	}
	
	
	
	public void printHeap() {
		HeapNode root = this.min;
		int i = 0;
		boolean bool = true;		//after root=root.next -> if root=this.min ----> bool=false
		while (bool) {
			System.out.println("------------------"+i+"------------------");
			if (root == this.min) {System.out.println(root.key + " (MIN)");}
			else {System.out.println(root.key);}
			
			HeapNode child = root.child;
			while (child != null) {
				//System.out.println("↓");
				System.out.println("\t"+child.key);
				HeapNode node = child.next;
				while (node != child) {
					System.out.print("→");
					if (node.next == child) {
						System.out.println(node.key);
					}
					else {
						System.out.print(node.key);
					}
					node = node.next;
				}
				child = child.child;
			}
			root = root.next;
			i++;
			if (root == this.min) {bool = false;}
		}
	}
	
	public void printHeap2() {
		HeapNode root = this.min;
		int i = 1;
		boolean bool = true;		//after root=root.next -> if root=this.min ----> bool=false
		while (bool) {
			System.out.println("------------------"+i+"------------------");
			printSubTree(root, 0);
			root = root.next;
			i++;
			if (root == this.min) {bool = false;}
		}
	}
	
	public void printSubTree(HeapNode subRoot, int tabs) {
		//System.out.println("input- subRoot: " + subRoot.key + " , tabs: "+tabs);
		if (tabs == 0) {
			System.out.println(subRoot.key);
		}
		else {
			System.out.println("\t".repeat(tabs) + subRoot.key);
		}
		
		if (subRoot.child != null) {
			//System.out.println("subRoot: " + subRoot.key + " , tabs: " + tabs);
			//System.out.println("calling with subRoot: " + subRoot.child.key + " , tabs: "+tabs++);

			printSubTree(subRoot.child, tabs+1);
			HeapNode child = subRoot.child.next;
			while (child != subRoot.child) {
				printSubTree(child, tabs+1);
				child = child.next;
			}
		}
		else { return; }
		}
		
		

		
	
}

