
//Implement 3 versions of the Select algorithm (finding k largest elements)
package cs6301.g23;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Collections;
import java.util.PriorityQueue;
import java.util.Scanner;
/**
 * @author radhikakalaiselvan
 *
 */
public class SelectAlgorithm {
/*
 * In order to find the largest k elements in an array we create a priority queue (max heap)
 *  of the n elements, and use remove() k times and add it to the result array and return 
 */
	public static int[] getKGreatestPriorityQueue(int[] arr,int k){
		int result[]=new int[k];
		PriorityQueue<Integer> pq=new PriorityQueue<Integer>(Collections.reverseOrder()); //max heap
		for(int p:arr){
			pq.add(p);
		}
		for(int i=0;i<k;i++){
			result[i]=pq.remove();
		}
		return result;
	}
/*
 * To find the k largest elements we create a min heap of size k as we traverse over the array. When a value larger than the 
 * head of the queue is faced we remove the top element from queue and add the new element. Hence the priority queue has k 
 * largest numbers in an array which is copied to result array and returned.
 */
	private static int[] getKGreatestMinHeap(int[] arr, int k) {
		PriorityQueue<Integer> pq=new PriorityQueue<Integer>();
		int result[]=new int[k];
		for(int p:arr){
			if(pq.size()<k ){
				pq.add(p);

			}else {
				if(pq.peek()<p){
					pq.poll();
					pq.add(p);
				}
			}
		}
		for(int m=0;m<k;m++){
			result[m]=pq.remove();
		}
		return result;
	}

/*
 * A linear time algorithm to find the k largest elements in array. 
 * If k is less than 0, return empty array.
 * If k> length of array, return the entire array.
 * else call the select algorithm and copy the values of the array from the index returned by the algorithm to result array 
 * and return.
 */
	private static int[] getKGreatestByPartition(int[] arr, int k) {
		int result[]=new int[k];
		if(k<=0){
			return result;
		} 
		if(k>arr.length){
			return arr;
		}
		int index= select(arr,0,arr.length-1,k)+1;
		for(int i=0;i<k;i++){
			result[i]=arr[index];
			index++;
		}
		return result;
	}
	
	/*
	 * If the number of elements in the array is less than 17,then call insertionsort and return the index and n-k;
	 * else call partition algorithm. 
	 * q=partition(arr,p,n);
	 * q means all the values in array are greater than arr[q];
	 * If there are k elements to the right of q then return q
	 * else if the number of elements on the right is greater than k,
	 *  recursively call select for right portion of array to find k elements.
	 * else recursively call select for left portion of array to find k-length-1 elements(-1 is for arr[q] itself to be counted)
	 */

	private static int select(int[] arr,int p, int n,int k) {
		if(n<17){
			insertionSort(arr,p,n);
			return n-k;
		}else {
		int q=partition(arr,p,n);
		int length=n-q; //length of right side
		if(k==length){
			return q;
		}
		else if(k<length){
			return select(arr,q+1,n,k);
		}
		else {
			return select(arr,p,q-1,k-length-1);
		}
		}
	}

/*
 * Swaps the values in the given index i and j in arr.
 */
	private static void swap(int[] arr, int i,int j){
		int temp=arr[i];
		arr[i]=arr[j];
		arr[j]=temp;

	}
/*
 * Randomly partitions the given array such that all the values below the returned index are less than the val at the
 * returned index and all the values are greater than the value at returned index.
 */
	private static int partition(int[] arr,int start,int end ) {
		int pivot = arr[end];
		int i = start - 1;
		for (int j = start; j <end; j++)
		{
			if (arr[j] <= pivot)
			{
				i++;
				swap(arr,i,j);
			}
		}
		swap(arr,i + 1,end);	
		return i+1 ;
	}
/*
 * Performs insertion sort on the given array
 */
	public static void insertionSort(int[] arr,int start,int end){
		for(int i=start;i<=end;i++){
			for(int j=i;j>start;j--){
				if(arr[j]<arr[j-1]){
					int temp=arr[j];
					arr[j]=arr[j-1];
					arr[j-1]=temp;
				}
			}
		}
	}
/*
 * Prints the given array
 */
	static public void printArray(int[] n){
		System.out.println("Array : ");
		for(int val:n){
			System.out.print(val+" ");
		}
	}


	public static void main(String[] args) throws FileNotFoundException {
		// TODO Auto-generated method stub
		Scanner in;
		if (args.length > 0) {
			File inputFile = new File(args[0]);
			in = new Scanner(inputFile);
		} 
		else {
			in = new Scanner(System.in); 
		}
		int n = in.nextInt();
		int[] arr=new int[n];
		for(int i=1;i<=n; ++i){
			arr[i-1]=i;
		}
		Shuffle.shuffle(arr);
		System.out.println("Enter k");
		int k=in.nextInt();
		Timer t = new Timer();
		System.out.println(" Get K Greatest PriorityQueue: ");
		t.start();
		printArray(getKGreatestPriorityQueue(arr,k));
		System.out.println(t.end());

		Shuffle.shuffle(arr);
		System.out.println(" Get K Greatest Min Heap: ");
		t.start();
		printArray(getKGreatestMinHeap(arr,k));
		System.out.println(t.end());

		Shuffle.shuffle(arr);
		System.out.println(" Get K Greatest By Partition: ");
		t.start();
		printArray(getKGreatestByPartition(arr,k));
		System.out.println(t.end());

	}
}
/*
200000
Enter k
3
 Get K Greatest PriorityQueue: 
Array : 
200000 199999 199998 Time: 24 msec.
Memory: 8 MB / 123 MB.
 Get K Greatest Min Heap: 
Array : 
199998 199999 200000 Time: 13 msec.
Memory: 8 MB / 123 MB.
 Get K Greatest By Partition: 
Array : 
199998 199999 200000 Time: 6 msec.
Memory: 8 MB / 123 MB.

 */