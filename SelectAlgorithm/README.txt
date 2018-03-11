Implemented 3 versions of the Select algorithm (finding k largest elements)

1. In order to find the largest k elements in an array we create a priority queue (max heap) of the n elements, and use remove() k times and add it to the result array and return 

2. Create a min heap of size k as we traverse over the array. When a value larger than the 
head of the queue is faced we remove the top element from queue and add the new element. Hence the priority queue has k largest numbers in an array which is copied to result array and returned.

3.A linear time algorithm to find the k largest elements in array. 
 * If k is less than 0, return empty array.
 * If k> length of array, return the entire array.
 * else call the select algorithm and copy the values of the array from the index returned by the algorithm to result array and return the new array.