Multi-dimensional search: Consider the web site of a seller like Amazon.  
They carry tens of thousands of products, and each product has many
attributes (Name, Size, Description, Keywords, Manufacturer, Price, etc.).  
The search engine allows users to specify attributes of products that
they are seeking, and shows products that have most of those
attributes.  To make search efficient, the data is organized using
appropriate data structures, such as balanced trees.  But, if products
are organized by Name, how can search by price implemented efficiently?
The solution, called indexing in databases, is to create a new set of
references to the objects for each search field, and organize them to
implement search operations on that field efficiently.  As the objects
change, these access structures have to be kept consistent.

We have a set of items available for purchase.  Each item is
identified by an id (Long), and has a description (one or more Longs).
There are a number of suppliers, and each supplier is identified by a
vendor id (Long).  Each supplier charges a price for each item they
sell (int).  The following operations are supported:

   a. add(id, description): add a new item.  If an entry with the same
      id already exists, the new description is merged with the
      existing description of the item.
      Returns true if the item is new, and false otherwise.

   b. add(supplier, reputation): add a new supplier (Long) and their
      reputation (float in [0.0-5.0], single decimal place). If the
      supplier exists, their reputation is replaced by the new value.
      Return true if the supplier is new, and false otherwise.

   c. add(supplier, Pairs(id, price)): add products and their prices
      at which the supplier sells the product.  If there is an entry
      for the price of an id by the same supplier, then the price is
      replaced by the new price.  Returns the number of new entries
      created.
   
   d. description(id): return an array with the description of id.
      Return null if there is no item with this id.

   e. findItem(arr): given an array of Longs, return an array of items
      whose description contains one or more elements of the array,
      sorted by the number of elements of the array that are in the
      item's description (non-increasing order).

   f. findItem(n, minPrice, maxPrice, minReputation): given a Long n,
      return an array of items whose description contains n, which
      have one or more suppliers whose reputation meets or exceeds the
      given minimum reputation, that sell that item at a price that
      falls within the price range [minPrice, maxPrice] given.  Items
      should be sorted in order of their minimum price charged by a
      supplier for that item (non-decreasing order).

   g. findSupplier(id): given an id, return an array of suppliers who
      sell that item, ordered by the price at which they sell the item
      (non-decreasing order).

   h. findSupplier(id, minReputation): given an id and a minimum
      reputation, return an array of suppliers who sell that item,
      whose reputation meets or exceeds the given reputation.  The
      array should be ordered by the price at which they sell the item
      (non-decreasing order).

   i. identical(): find suppliers selling 5 or more products, who have
      the same identical profile as another supplier: same reputation,
      and, sell the same set of products, at identical prices.  This
      is a rare operation, so do not do additional work in the other
      operations so that this operation is fast.  Creative solutions
      that are elegant and efficient will be awarded excellence credit.
      Return array of suppliers satisfying above condition.  Make sure
      that each supplier appears only once in the returned array.

   j. invoice(arr, minReputation): given an array of ids, find the
      total price of those items, if those items were purchased at the
      lowest prices, but only from sellers meeting or exceeding the
      given minimum reputation.  Each item can be purchased from a
      different seller.

   k. purge(maxReputation): remove all items, all of whose suppliers
      have a reputation that is equal or lower than the given maximum
      reputation.  Returns an array with the items removed.

   l. remove(id): remove item from storage.  Returns the sum of the
      Longs that are in the description of the item deleted (or 0, if
      such an id did not exist).

   m. remove(id, arr): remove from the given id's description those
      elements that are in the given array.  It is possible that some
      elements of the array are not part of the item's description.
      Return the number of elements that were actually removed from
      the description.

   n. removeAll(arr): remove the elements of the array from the
      description of all items.  Return the number of items that lost
      one or more terms from their descriptions.
   

Implement the operations using data structures that are best suited
for the problem.  It is recommended that you use the data structures
from Java's library when possible.


AI  1  10  100 101 102 103 104 105 106 107 108 109
AI  2  5   100 	   102 	   104 	   106 	   108
AI  3  6   100 101     	   104 105 	   108 109
AI  4  9   100 101 102 103 104 105 106 107 108
AI  5  5       101     103     105     107     109
AR  205	   5.0
AR  204	   4.0
AR  203	   3.0
AR  202	   2.0
AR  201	   1.0
AS  205	4  1 100   2 200  3 300  4 400
AS  204	4  1 98    2 205  3 294  4 392
AS  203	2    	   2 180    	 4 360
AS  202	3  1 80	          3 250         5 450
AS  201	1    		  3 200
D   5
D   8
FIA 3	100 102 109
FIA 2	500 600
FIP 109	0   999	2.5
FSR 1 	4.0
INV 3	1   2	5    2.9
R   5
FIA 3	100 102 109
RD  3	3   107	108  109
FIA 3	100 102	109
RA  2	102 103
End

Output:
1805
Timer output

Output with VERBOSE = 1:
1 : AI : 1
2 : AI : 1
3 : AI : 1
4 : AI : 1
5 : AI : 1
6 : AR : 1
7 : AR : 1
8 : AR : 1
9 : AR : 1
10 : AR : 1
11 : AS : 4
12 : AS : 4
13 : AS : 2
14 : AS : 3
15 : AS : 1
16 : D : 525
17 : D : 0
[18 : FIA : 1 4 3 2 5 ]
18 : FIA : 15
[19 : FIA : ]
19 : FIA : 0
[20 : FIP : 1 3 ]
20 : FIP : 4
[21 : FSR : 204 205 ]
21 : FSR : 409
Invoice: Skipping id 5: not available from seller with at least 2.9 reputation
22 : INV : 278
23 : R : 525
[24 : FIA : 1 4 3 2 ]
24 : FIA : 10
25 : RD : 2
[26 : FIA : 1 4 2 3 ]
26 : FIA : 10
27 : RA : 3
1805
Timer output
