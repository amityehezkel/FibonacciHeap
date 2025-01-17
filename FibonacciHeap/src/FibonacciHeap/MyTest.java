package FibonacciHeap;



import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MyTest {
	public static void main(String[] args) { test();}
	public static void test() {
		Random rand = new Random();
		int size = rand.nextInt(1, 100);
		List<Integer> options = new ArrayList<>();
		for (int i = 1 ; i <= 500 ; i++) {
			options.add(i);
		}
		
		FibonacciHeap heap = new FibonacciHeap();
		int min = 501;
		for (int i = 1 ; i <= size ; i++) {
			int j = rand.nextInt(200);
			int insert = options.get(j);
			options.remove(j);
			if (insert < min) {min = insert;}
			heap.insert(insert, "info");
		}
		
		System.out.println("expected size: " + size);
		System.out.println("heap size: " + heap.size);
		System.out.println("expected min: " + min);
		System.out.println("heap min: " + heap.min.key);
		
		heap.deleteMin();
		System.out.println("deleting min");
		System.out.println("new size: " + heap.size);
		System.out.println("new min: " + heap.min.key);
		heap.printHeap2();
		
	}
	
	public static void test2() {
		FibonacciHeap heap = new FibonacciHeap();
		for (int i = 1 ; i <= 5 ; i++) {
			heap.insert(i, "info");
		}
		

		System.out.println("heap size: " + heap.size);

		System.out.println("heap min: " + heap.min.key);
		
		heap.deleteMin();
		System.out.println("deleting min");
		System.out.println("new size: " + heap.size);
		System.out.println("new min: " + heap.min.key);
		heap.printHeap2();
		
	}
	

}
