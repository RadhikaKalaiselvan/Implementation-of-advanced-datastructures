package cs6301.g23;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.NoSuchElementException;
import java.util.Scanner;

//Sp2 Question 2
/**
 * @author Radhika Kalaiselvan
 *
 *Version 1.0 -9/6/17
 */
public class SortableList<T extends Comparable<? super T>> extends SinglyLinkedList<T>{
/*
 * If either of the left or right Entry<T> is null, return the other Entry<T>
 * else append the smallest of left and right entry to the resultHead and return the resultHead.
 */
	Entry<T> sortedMerge(Entry<T> left,Entry<T> right) { 
		Entry<T> resultHead=null;
		if(left== null)
			return right;
		if(right== null)
			return left;
		if(left.element.compareTo(right.element)<0){
			resultHead=left;
			resultHead.next=sortedMerge(left.next,right);
		}else{
			resultHead=right;
			resultHead.next=sortedMerge(left,right.next);
		}
		return resultHead;
	}
	
	void updateHead(Entry<T> n){
		super.head.next=n;
	}
	/*
	 * Performs mergeSort and returns the head of the new sorted list.
	 */
	Entry<T> mergeSort(Entry<T> head) {
		//If head is NULL or there is only one element in the Linked List 
	   // then return.
		if (head == null || head.next == null)
		{
			return head;
		}
		//Else divide the linked list into two halves. One half = head to middleElement
		//Second half middElement+1 to end.
		//Sort each half and call merge
		Entry<T> middleElement=getMiddleElement(head);
		Entry<T> middleNextElement=middleElement.next;
		middleElement.next=null;
		Entry<T> left=mergeSort(head);
		Entry<T> right=mergeSort(middleNextElement);
		return sortedMerge(left,right);
	}
	
 /*
  * This method performs merge sort on the given list and updates the head of the list.
  */
	void mergeSort() { 
		updateHead(this.mergeSort(this.head.next));
	}
	public static<T extends Comparable<? super T>> void mergeSort(SortableList<T> list) {
		list.mergeSort();
	}
	/*
	 * This method returns mid element of the list.
	 * It uses two runner nodes: slowrunner and fastrunner
	 * fastrunner passes double the number of nodes as slowrunner.
	 * When fastnunner reaches the end, the  slowrunner node is
	 * in the middle,so slowrunner is returned.
	 */
	public Entry<T> getMiddleElement(Entry<T> head){
		if (head == null)
			return head;
		Entry<T> slowRunner=head;
		Entry<T> fastRunner=slowRunner.next;
		while(fastRunner!=null){
			fastRunner=fastRunner.next;
			if(fastRunner!=null){
				slowRunner=slowRunner.next;
				fastRunner=fastRunner.next;
			}
		}
		return slowRunner;
	}
//Might have to change the main method in SinglyLinkedList.java as given below
// public static void main(String[] args) throws Exception {
	public static void main(String[] args) throws FileNotFoundException {
		Scanner in;
		if (args.length > 0) {
			File inputFile = new File(args[0]);
			in = new Scanner(inputFile);
		} else {
			in = new Scanner(System.in); 
		}
		int n=in.nextInt();
		SortableList<Integer> lst = new SortableList<>();
		for(int i=1; i<=n; i++) {
			lst.add(in.nextInt());
		}
		lst.mergeSort();
		System.out.println("After merge sort :");
		lst.printList();     
	}
}
/*
 * Sample input: 4
 * 6
90
89
11
10
2
3
After merge sort :
6: 2 3 10 11 89 90 
 * 
 */
