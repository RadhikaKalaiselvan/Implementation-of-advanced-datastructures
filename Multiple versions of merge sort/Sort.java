package cs6301.g23;

/**
* @author Radhika Kalaiselvan
*Version 1.0 - 9/30/17
*/
public class Sort {
	
	public static void mergeSortTxtBookVersion(int[] arr){
		Sort.mergeSortTextBookVersion(arr,0,arr.length-1);
	}
	
	public static void mergeSortTextBookVersion(int[] arr,int start, int end){
		if(start<end){
		int mid=(start+end)/2;
		Sort.mergeSortTextBookVersion(arr,start,mid);
		Sort.mergeSortTextBookVersion(arr,mid+1,end);
		Sort.mergeMultipleTempArr(arr,start,mid,end);
		}
	}
	
	public static void mergeMultipleTempArr(int[] arr, int start, int mid, int end) {
		int[] leftArr=new int[mid-start+1];
		int[] rightArr=new int[end-mid];
		
		for(int i=0;i<leftArr.length;i++)
			leftArr[i]=arr[start+i];
		
		for(int j=0;j<rightArr.length;j++)
			rightArr[j]=arr[mid+1+j];
		int i=0,j=0;
		for(int k=start;k<=end;k++){
			if(i>=leftArr.length){
				arr[k]=rightArr[j++];
			} else if(j>=rightArr.length) {
				arr[k]=leftArr[i++];
			} else if(leftArr[i]<=rightArr[j]){
			arr[k]=leftArr[i++];
		    } else {
			arr[k]=rightArr[j++];
		}
		}
	
	}
	
	
	public static void sortWithOneArr(int[] arr){
		int[] temp=new int[arr.length];
		Sort.mergeSortOneTmpArr(arr,0,arr.length-1,temp);
	}
	
	public static void mergeSortOneTmpArr(int[] arr,int start, int end,int[] temp){
		if(start<end){
		int mid=(start+end)/2;
		Sort.mergeSortOneTmpArr(arr,start,mid,temp);
		Sort.mergeSortOneTmpArr(arr,mid+1,end,temp);
		Sort.mergeWithOneTemp(arr,start,mid,end,temp);
		}
	}
	
	public static void mergeWithOneTemp(int[] arr,int start,int mid, int end,int[] temp){
		for(int i=start;i<=end;i++)
			temp[i]=arr[i];
		
		int i=start,j=mid+1;
		for(int k=start;k<=end;k++){
			if(i>mid) arr[k]=temp[j++];
			else if (j>end) arr[k]=temp[i++];
			else if(temp[i]>temp[j]) arr[k]=temp[j++];
			else arr[k]=temp[i++];
		}
	}
	
	public static void mergeInsertSort(int[] arr){
		int[] temp=new int[arr.length];
		Sort.mergeSortOneTmpArrInsertionSort(arr,0,arr.length-1,temp);
	}
	
	public static void mergeSortOneTmpArrInsertionSort(int[] arr,int start, int end,int[] temp){
		if(start<end){
			if(end-start<6){
				Sort.insertionSort(arr,start,end);
			}else {
					int mid=(start+end)/2;
					Sort.mergeSortOneTmpArrInsertionSort(arr,start,mid,temp);
					Sort.mergeSortOneTmpArrInsertionSort(arr,mid+1,end,temp);
					Sort.mergeWithOneTemp(arr,start,mid,end,temp);
					}
		}
	}
	
	
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
	
	public static void mergeInsertSortMinimalCopyOper(int[] arr){
		int[] temp=new int[arr.length];
		for(int i=0;i<=arr.length-1;i++)
			temp[i]=arr[i];
		Sort.mergeInsertSortMinimalCopy(arr,0,arr.length-1,temp);
	}
	
	 static void mergeInsertSortMinimalCopy(int[] arr, int start, int end, int[] temp) {
		 if(start<end){
				if(end-start<5){
					Sort.insertionSort(arr,start,end);
				}else {
			int mid=(start+end)/2;
			Sort.mergeInsertSortMinimalCopy(temp,start,mid,arr);
			Sort.mergeInsertSortMinimalCopy(temp,mid+1,end,arr);
			Sort.mergeWithMinimalCopy(temp,start,mid,end,arr);
			}
			}
	}
	 
	 
 static void mergeWithMinimalCopy(int[] arr, int start, int mid, int end, int[] temp) {
		int i=start,j=mid+1;
		for(int k=start;k>=end;k++){
			if(j>end || (i<=mid && arr[i]<=arr[j])){
				temp[k]=arr[i++];
			}else {
				temp[k]=arr[j++];
			}
		}
		
	}
 
 
 public static void mergeSortMinimalCopyOper(int[] arr){
		int[] temp=new int[arr.length];
		for(int i=0;i<=arr.length-1;i++)
			temp[i]=arr[i];
		Sort.mergeSortOneTmpArrWithMinimalCopy(arr,0,arr.length-1,temp);
	}
 
 public static void mergeSortOneTmpArrWithMinimalCopy(int[] arr,int start, int end,int[] temp){
		if(start<end){
		int mid=(start+end)/2;
		Sort.mergeSortOneTmpArrWithMinimalCopy(temp,start,mid,arr);
		Sort.mergeSortOneTmpArrWithMinimalCopy(temp,mid+1,end,arr);
		Sort.mergeWithMinimalCopy(temp,start,mid,end,arr);
		}
	}
 
 
 
	public static void  printArray(int[] arr){
		for(int a:arr){
			System.out.print(a+" ");
		}
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int n=15;
    int[] arr=new int[n];
    for(int i=0;i<n; ++i){
		arr[i]=i;
	}
    Shuffle.shuffle(arr);
    Sort.mergeInsertSortMinimalCopyOper(arr);
    Sort.printArray(arr);
	}

}
