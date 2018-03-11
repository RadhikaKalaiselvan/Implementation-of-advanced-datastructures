package cs6301.g23;

import java.math.BigInteger;
/**
* @author Radhika Kalaiselvan
*Version 1.0 - 9/30/17
*/
public class FibonacciNumbers {
    
	static BigInteger linearFibonacci(int n) {
		
		if(n==0 || n==1){
			return new BigInteger(n+"");
		} else {
			return linearFibonacci(n-1).add(linearFibonacci(n-2));
		}
	}
	
	static BigInteger[][] matrixMultiplication(BigInteger[][] a,BigInteger[][] b) throws Exception{
	BigInteger[][] result=new BigInteger[a.length][b[0].length];
	for(int i=0;i<a.length;i++){
		for(int j=0;j<b[0].length;j++)
			{
			result[i][j]=new BigInteger("0");
			}
	}
		
	if(a[0].length!=b.length){
		throw new Exception("Cannot perform multiplication");
	} else {
		//assumption square matrix
		for (int i = 0; i < a.length  ; i++)
            for (int j = 0; j < b[0].length; j++)
                for (int k = 0; k < b.length; k++)
                    result[i][j]=result[i][j].add(a[i][k].multiply(b[k][j]));
	}
	return result;
	}
	
	static BigInteger[][] power(BigInteger a[][],long n) throws Exception{
		
		if(n==0){
			BigInteger[][] result=new BigInteger[a.length][a[0].length];
			for(int i=0;i<a.length;i++){
				for(int j=0;j<a.length;j++){
					if(i==j){
					result[i][j]=new BigInteger("1");
					}else {
						result[i][j]=new BigInteger("0");
					}
				}
			}
		return result;
		}
		else if(n==1){
			BigInteger[][] result=new BigInteger[a.length][a[0].length];
			for(int i=0;i<a.length;i++){
				for(int j=0;j<a.length;j++){
					result[i][j]=new BigInteger(a[i][j].toString());
				}
			}
			return result;
		}
		else {
			BigInteger[][] s=power(a,n/2);
			if(n%2==0){
				return FibonacciNumbers.matrixMultiplication(s,s);
			} else {
				return FibonacciNumbers.matrixMultiplication(s,FibonacciNumbers.matrixMultiplication(s,a));
			}
		}
	}
	
	static void  printArray(BigInteger[][] a){
		for(int i=0;i<a.length;i++){
			for(int j=0;j<a[0].length;j++){
				System.out.println(a[i][j]+" ");
			}
			System.out.println();
		}
	}
	
	static BigInteger logFibonacci(int n) throws Exception {
		BigInteger[][] a=new BigInteger[2][2];
		    a[0][0]=new BigInteger("1");
	        a[0][1]=new BigInteger("1");
	        a[1][0]=new BigInteger("1");
	        a[1][1]=new BigInteger("0");
	        
	       BigInteger[][] r= FibonacciNumbers.power(a,n-1);  
	       
	       BigInteger[][] b=new BigInteger[2][1];
	       b[0][0]=new BigInteger("1");
	       b[1][0]=new BigInteger("0");
	      
	       BigInteger[][] fib=FibonacciNumbers.matrixMultiplication(r,b);
	       return fib[0][0];
	}
	
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		int n=40;
	  System.out.println(" log fib "+logFibonacci(n));
      System.out.println(" linear fib "+linearFibonacci(n));
     	
	}

}
