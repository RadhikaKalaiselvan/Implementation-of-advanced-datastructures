package cs6301.g23;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
//Sp2 Question 1
/**
 * @author Radhika Kalaiselvan
 *
 *Version 1.0 -9/6/17
 */
public class ListUtil {
	/*
	 * 
	 * Return elements common to l1 and l2, in sorted order.
	 * Iterator through L1 and L2 till both have elements and add common elements
	 * outList.
	 */
	public static<T extends Comparable<? super T>>
	void intersect(List<T> l1, List<T> l2, List<T> outList) {  
	//	assert isSorted(l1) && isSorted(l2);
		Iterator<T> list1it = l1.iterator(); 
		Iterator<T> list2it = l2.iterator();
		T elementFromList1=list1it.next();
		T elementFromList2=list2it.next();
		while(elementFromList1!=null && elementFromList2!=null){
			if(elementFromList1.compareTo(elementFromList2)<0){
				elementFromList1=next(list1it);
			} else if(elementFromList1.compareTo(elementFromList2)>0){
				elementFromList2=next(list2it);
			} else {
				outList.add(elementFromList1);
				elementFromList1=next(list1it);
				elementFromList2=next(list2it);
			}

		}
	}

	public static<T extends Comparable<? super T>> T next(Iterator<T> it){
		return it.hasNext()?it.next():null;
	}

	/*
	 * Return elements from l1 and l2, in sorted order without duplicates.
	 * Iterator through L1 and L2 till both have elements and add the elements to 
	 * outList. Then if l1 or l2 is not completely iteratored, add the remaining 
	 * elements to outList.
	 */
	 
	public static<T extends Comparable<? super T>>
	void union(List<T> l1, List<T> l2, List<T> outList) {
	//	assert isSorted(l1) && isSorted(l2);
		Iterator<T> list1it = l1.iterator(); 
		Iterator<T> list2it = l2.iterator();
		T elementFromList1=list1it.next();
		T elementFromList2=list2it.next();
		while(elementFromList1!=null && elementFromList2!=null){
			if(elementFromList1.compareTo(elementFromList2)<0){
				outList.add(elementFromList1);
				elementFromList1=next(list1it);
			} else if(elementFromList1.compareTo(elementFromList2)>0){
				outList.add(elementFromList2);
				elementFromList2=next(list2it);
			} else {
				outList.add(elementFromList1);
				elementFromList1=next(list1it);
				elementFromList2=next(list2it);
			}	 
		}
		while(elementFromList1!=null){
			outList.add(elementFromList1);
			elementFromList1=next(list1it);
		}
		while(elementFromList2!=null){
			outList.add(elementFromList2);
			elementFromList2=next(list2it);
		}
	}

	/*
	 * Return elements common from l1 and not in l2.
	 * Iterator through L1 and L2 till both have elements and add the elements to 
	 * outList if not found in l2.
	 * Finally add the elements of l1 to outlist.
	 */
	public static<T extends Comparable<? super T>>
	void difference(List<T> l1, List<T> l2, List<T> outList) {
	//	assert isSorted(l1) && isSorted(l2);
		Iterator<T> list1it = l1.iterator(); 
		Iterator<T> list2it = l2.iterator();
		T elementFromList1=list1it.next();
		T elementFromList2=list2it.next();
		while(elementFromList1!=null && elementFromList2!=null){
			if(elementFromList1.compareTo(elementFromList2)<0){
				outList.add(elementFromList1);
				elementFromList1=next(list1it);
			} else if(elementFromList1.compareTo(elementFromList2)>0){
				elementFromList2=next(list2it);
			} else {
				elementFromList1=next(list1it);
				elementFromList2=next(list2it);
			}	 
		}
		while(elementFromList1!=null){
			outList.add(elementFromList1);
			elementFromList1=next(list1it);
		}
	}
	
	/*
	 * Prints the given list
	 */
	public static<T extends Comparable<? super T>> void printList(List<T> list){
		for(T i:list){
			System.out.print(" "+i);
		}
		System.out.println();
	}
	
	/*
	*Input list1 size,list2 size, and enter list elements.
	*This program will print the intersection,union and difference of two lists
	*/
	public static void main(String[] args) throws FileNotFoundException{
		List<Integer> l1=new LinkedList<Integer>();
		LinkedList<Integer> l2=new LinkedList<Integer>();
		Scanner in;
		if (args.length > 0) {
			File inputFile = new File(args[0]);
			in = new Scanner(inputFile);
		} 
		else {
			in = new Scanner(System.in); 
		}
		int list1Size=in.nextInt();
		int list2Size=in.nextInt();
		for(int i=0;i<list1Size;i++){
			l1.add(in.nextInt());
		}
		for(int i=0;i<list2Size;i++){
			l2.add(in.nextInt());
		}
		LinkedList<Integer> outList=new LinkedList<Integer>();
		System.out.println("Intersection :");
		ListUtil.intersect(l1, l2, outList);
		printList(outList);
		System.out.println("Union :");
		outList=new LinkedList<Integer>();
		ListUtil.union(l1, l2, outList);
		printList(outList);
		System.out.println("Difference :");
		outList=new LinkedList<Integer>();
		ListUtil.difference(l1, l2, outList);
		printList(outList);
	}
}
/**
 * Sample input: 4
4
1
2
3
4

3
4
5
6
Output:
Intersection :
 3 4
Union :
 1 2 3 4 5 6
Difference :
 1 2 
 */
