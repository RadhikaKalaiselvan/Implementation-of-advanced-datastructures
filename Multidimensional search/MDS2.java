package cs6301.g23;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;

import java.util.Objects;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * @author Radhika Kalaiselvan
 *Version 1.0 -11/26/17
 */
public class MDS {
	/*
	 * productIDDescMap - used to store the prodId and HashSet of descriptions
	 * descProductIDMap - used to store the description and the HashSet of prodId
	 * supplierReptMap - used to store the supplierId and reputation
	 * reptSupplierMap - used to store the reputation and HashSet of supplierId
	 * supplierProdMap - used to store the supplierId and HashSet of Pairs
	 * prodSupplierMap - used to store the prodId and HashSet of SupplierPair class(which contains supplierId and price at which this product is sold)
	 */
	HashMap<Long,HashSet<Long>> productIDDescMap;
	HashMap<Long,HashSet<Long>> descProductIDMap;

	HashMap<Long,Float> supplierReptMap;
	TreeMap<Float,HashSet<Long>> reptSupplierMap;

	HashMap<Long,HashSet<Pair>> supplierProductMap;
	HashMap<Long,TreeSet<SupplierPair>> productSupplierMap;


	public MDS() {
		this.productIDDescMap=new HashMap<Long,HashSet<Long>>();
		this.descProductIDMap=new HashMap<Long,HashSet<Long>>();

		this.supplierReptMap=new HashMap<Long,Float>();
		this.reptSupplierMap=new TreeMap<Float,HashSet<Long>>();

		this.supplierProductMap=new HashMap<Long,HashSet<Pair>>();
		this.productSupplierMap=new HashMap<Long,TreeSet<SupplierPair>>();

	}

	/*
	 * SupplierPair has two attributes price of the product and supplierID (suppID) which is stored
	 * in productSupplierMap with productId as key.
	 */
	public static class SupplierPair{
		int price;
		long suppID;
		public SupplierPair(int price,long suppID){
			this.price=price;
			this.suppID=suppID;
		}

		@Override public int hashCode() {
			return Objects.hash(this.price,this.suppID);
		}

		@Override
		public boolean equals(Object obj){
			if (obj instanceof SupplierPair) {
				SupplierPair sp = (SupplierPair) obj;
				return (sp.price == this.price) && (sp.suppID== this.suppID);
			} else {
				return false;
			}
		}
	}
 /*
  * User-defined class used to store the productID and price. Overrides hashCode method and equals method to used in HashSet.
  */
	public static class Pair {
		long id;
		int price;

		public Pair(long id, int price) {
			this.id = id;
			this.price = price;

		}

		public Pair(long id, int price,long supplierID) {
			this.id = id;
			this.price = price;

		}
		@Override public int hashCode() {
			return Objects.hash(this.id);
		}

		@Override
		public boolean equals(Object obj){
			if (obj instanceof Pair) {
				Pair pair = (Pair) obj;
				return (pair.id == this.id) && (pair.price== this.price);
			} else {
				return false;
			}
		}
	}



	/*
	 * SupplierPair comparator is used TreeSet to compare the SupplierPair
	 */
	class SupplierPairComparator implements Comparator<SupplierPair>{

		@Override
		public int compare(SupplierPair o1, SupplierPair o2) {
			Integer price=o1.price;
			if(price.compareTo(o2.price)==0){
				Long l=o2.suppID;
				return l.compareTo(o1.suppID);
			}else{
				return price.compareTo(o2.price);
			}
		}

	}

	/* add a new item.  If an entry with the same id already exists,
       the new description is merged with the existing description of
       the item.  Returns true if the item is new, and false otherwise.
	 */
	public boolean add(Long id, Long[ ] description) {
		boolean newItem=false;
		HashSet<Long> hs=this.productIDDescMap.get(id);
		if(hs==null){
			newItem=true;
			hs=new HashSet<Long>();
		}
		for(Long desc:description){
			hs.add(desc);
			HashSet<Long> idSet=this.descProductIDMap.get(desc);
			if(idSet==null){
				idSet=new HashSet<Long>();
				idSet.add(id);
				this.descProductIDMap.put(desc, idSet);
			}else{
				idSet.add(id);
			}
		}
		this.productIDDescMap.put(id,hs);
		return newItem;
	}
/*
 * To print all the values in all data structures used.Never called.
 */
	void testPrint(){
		System.out.println("Supplier Product Map");
		for (Map.Entry<Long,HashSet<Pair>> entry : this.supplierProductMap.entrySet()) {
			System.out.println("Supplier "+entry.getKey());
			for(Pair v:entry.getValue()){
				System.out.println("ID "+v.id+" price"+v.price);
			}
		}
		System.out.println("Product Supplier Map");
		for (Map.Entry<Long,TreeSet<SupplierPair>> entry : this.productSupplierMap.entrySet()) {
			System.out.println(" price ="+entry.getKey());
			for(SupplierPair v:entry.getValue()){
				System.out.println(" suuplier "+v.suppID+" prod ID "+v.price);
			}
		}
		System.out.println("Product Desc");
		for(Map.Entry<Long,HashSet<Long>> entry : this.productIDDescMap.entrySet()){
			System.out.println(" ID="+entry.getKey());
			for(Long desc:entry.getValue()){
				System.out.println(" DESC "+desc);
			}
		}

		System.out.println(" Desc Product Map");
		for(Map.Entry<Long,HashSet<Long>> entry : this.productIDDescMap.entrySet()){
			System.out.println(" Desc="+entry.getKey());
			for(Long desc:entry.getValue()){
				System.out.println(" ID "+desc);
			}
		}

	}


	/* add a new supplier (Long) and their reputation (float in
       [0.0-5.0], single decimal place). If the supplier exists, their
       reputation is replaced by the new value.  Return true if the
       supplier is new, and false otherwise.
	 */
	public boolean add(Long supplier, float reputation) {
		boolean newSupplier=false;
		Float rept=this.supplierReptMap.get(supplier);
		if(rept==null){
			newSupplier=true;
			this.supplierReptMap.put(supplier,reputation);
		}else{
			HashSet<Long> supplierHashSet=this.reptSupplierMap.get(rept);
			supplierHashSet.remove(supplier); 
			if(supplierHashSet.size()==0){
				this.reptSupplierMap.remove(rept);
			}
			this.supplierReptMap.put(supplier,reputation);
		}

		HashSet<Long> suppliers=this.reptSupplierMap.get(reputation);
		if(suppliers==null){
			suppliers=new HashSet<Long>();
		}
		suppliers.add(supplier);
		this.reptSupplierMap.put(reputation,suppliers);
		return newSupplier;
	}

	/* add products and their prices at which the supplier sells the
      product.  If there is an entry for the price of an id by the
      same supplier, then the price is replaced by the new price.
      Returns the number of new entries created.
	 */
	public int add(Long supplier, Pair[ ] idPrice) {
		int count=0;

		HashSet<Pair> productPairs=this.supplierProductMap.get(supplier);
		if(productPairs==null){
			productPairs=new HashSet<Pair>();
		}	
		for(Pair pair:idPrice){
			Pair removedPair=contains(productPairs,pair);
			if(removedPair!=null){
				productPairs.remove(removedPair);
				productPairs.add(new Pair(pair.id,pair.price));
			}else{
				count++;
				productPairs.add(new Pair(pair.id,pair.price));
				this.supplierProductMap.put(supplier,productPairs);	
			}	
			TreeSet<SupplierPair> suppliers=this.productSupplierMap.get(pair.id);
			if(suppliers==null){
				suppliers=new TreeSet<SupplierPair>(new SupplierPairComparator());
				suppliers.add(new SupplierPair(pair.price,supplier));
				this.productSupplierMap.put(pair.id, suppliers);
			}else{
				suppliers.add(new SupplierPair(pair.price,supplier));
			}

			if(removedPair!=null){
				TreeSet<SupplierPair> suppliers_pair=this.productSupplierMap.get(removedPair.id);
				suppliers_pair.remove(new SupplierPair(removedPair.price,supplier));
				if(suppliers_pair.size()==0){
					this.productSupplierMap.remove(removedPair.id);
				}
			}
		}
		return count;
	}
/*
 * Returns the pair object if given HashSet contains the given pair. 
 */
	private Pair contains(HashSet<Pair> productPairs, Pair pair) {
		Pair removedPair=null;
		for(Pair vp:productPairs){
			if(vp.id==pair.id){
				removedPair=vp;
				break;
			}
		}
		return removedPair;
	}

	/* return an array with the description of id.  Return null if
      there is no item with this id.
	 */
	public Long[ ] description(Long id) {
		HashSet<Long> hs=this.productIDDescMap.get(id);
		Long[] result=(hs==null)?null:hs.toArray(new Long[hs.size()]);
		return result;
	}

	/* given an array of Longs, return an array of items whose
      description contains one or more elements of the array, sorted
      by the number of elements of the array that are in the item's
      description (non-increasing order).
	 */
	public Long[ ] findItem(Long[ ] arr) {
		HashMap<Long,Integer> hm=new HashMap<Long,Integer>();
		for(Long desc:arr){
			HashSet<Long> prodIds=this.descProductIDMap.get(desc);
			if(prodIds==null){
				continue;
			}
			for(Long prodID:prodIds){
				Integer count=hm.get(prodID);
				if(count==null){
					hm.put(prodID,1);
				}else{
					hm.put(prodID,count+1);
				}
			}
		}
		Set<Entry<Long,Integer>> set = hm.entrySet();
		ArrayList<Entry<Long,Integer>> list = new ArrayList<Entry<Long,Integer>>(set);
		Collections.sort(list,new Comparator<Map.Entry<Long,Integer>>()
		{
			public int compare(Map.Entry<Long,Integer> o1,Map.Entry<Long,Integer> o2)
			{
				return (o2.getValue()).compareTo(o1.getValue());
			}
		});
		Long[] result=new Long[list.size()];
		int i=0;
		for(Entry<Long,Integer> e:list){
			result[i]=e.getKey();
			i++;
		}
		return result;
	}

	/* given a Long n, return an array of items whose description
      contains n, which have one or more suppliers whose reputation
      meets or exceeds the given minimum reputation, that sell that
      item at a price that falls within the price range [minPrice,
      maxPrice] given.  Items should be sorted in order of their
      minimum price charged by a supplier for that item
      (non-decreasing order).
	 */
	public Long[ ] findItem(Long n, int minPrice, int maxPrice, float minReputation) {
		HashSet<Long> prods=this.descProductIDMap.get(n);
		LinkedList<Long> result=new LinkedList<Long>();
		for(Long prodId:prods){
			TreeSet<SupplierPair> sp_set=this.productSupplierMap.get(prodId);
			for(SupplierPair sp:sp_set){
				if(this.supplierReptMap.get(sp.suppID)>=minReputation){
					result.add(prodId);
					break;
				}
			}
		}
		return result.toArray(new Long[result.size()]);
	}

	/* given an id, return an array of suppliers who sell that item,
      ordered by the price at which they sell the item (non-decreasing order).
	 */
	public Long[ ] findSupplier(Long id) {
		//this.testPrint();
		TreeSet<SupplierPair> sp_set=this.productSupplierMap.get(id);
		LinkedList<Long> result=new LinkedList<Long>();
		for(SupplierPair sp:sp_set){

			result.add(sp.suppID);
		}	
		return result.toArray(new Long[result.size()]);
	}

	/* given an id and a minimum reputation, return an array of
      suppliers who sell that item, whose reputation meets or exceeds
      the given reputation.  The array should be ordered by the price
      at which they sell the item (non-decreasing order).
	 */
	public Long[ ] findSupplier(Long id, float minReputation) {
		Long[] result=this.findSupplier(id);
		LinkedList<Long> listSuppliers=new LinkedList<Long>();
		for(Long supplierID:result){
			Float supplier_rept=this.supplierReptMap.get(supplierID);
			if(minReputation<=supplier_rept){
				listSuppliers.add(supplierID);
			}
		}
		return listSuppliers.toArray(new Long[listSuppliers.size()]);
	}

	/*
	 * returns the lowest price of the given productID from supplier with minimum given reputation
	 */
	public int getLowestPriceWithMinReputation(Long id, float minReputation) {
		int result=0;	
		TreeSet<SupplierPair> sp_set=this.productSupplierMap.get(id);
		for(SupplierPair sp:sp_set){
			Float supplier_rept=this.supplierReptMap.get(sp.suppID);
			if(minReputation<=supplier_rept){
				result=sp.price;
				break;
			}	
		}
		return result;
	}


	/* find suppliers selling 5 or more products, who have the same
       identical profile as another supplier: same reputation, and,
       sell the same set of products, at identical prices.  This is a
       rare operation, so do not do additional work in the other
       operations so that this operation is fast.  Creative solutions
       that are elegant and efficient will be awarded excellence credit.
       Return array of suppliers satisfying above condition.  Make sure
       that each supplier appears only once in the returned array.
	 */
	public Long[ ] identical() {
		HashSet<Long> result=new HashSet<Long>();
		for (Map.Entry<Float,HashSet<Long>> entry : this.reptSupplierMap.entrySet()) {
			HashSet<Long> suppliersToCheck=new HashSet<Long>();
			HashSet<Long> suppliers=entry.getValue();
			for(Long suppId: suppliers){
				HashSet<Pair> pairSet=this.supplierProductMap.get(suppId);
				if(pairSet.size()>=5){
					suppliersToCheck.add(suppId);
				}
			}
			findIdenticalSuppiers(suppliersToCheck.toArray(new Long[suppliersToCheck.size()]),result);
		}
		return result.toArray(new Long[result.size()]);
	}
/*
 * Given list of suppliers within each reputation, find identical suppliers and added it to the HashSet result.
 */
	private void findIdenticalSuppiers(Long[] suppliersToCheck, HashSet<Long> result) {
		for(int i=0;i<suppliersToCheck.length-1;i++){
			for(int j=i+1;j<suppliersToCheck.length;j++){
				if(j!=i){

					HashSet<Pair> prodSet=this.supplierProductMap.get(suppliersToCheck[i]);
					HashSet<Pair> prodSetToCheck=this.supplierProductMap.get(suppliersToCheck[j]);
					if(prodSet.size()!=prodSetToCheck.size()){
						continue;
					}else{
						int temp = 0;
						for(Pair p:prodSetToCheck){
							if(prodSet.contains(p)) {
								temp++;
							}
						}

						if(prodSet.size()==temp){
							result.add(suppliersToCheck[i]);
							result.add(suppliersToCheck[j]);
						}
					}
				}
			}
		}
	}

	/* given an array of ids, find the total price of those items, if
       those items were purchased at the lowest prices, but only from
       sellers meeting or exceeding the given minimum reputation.
       Each item can be purchased from a different seller.
	 */
	public int invoice(Long[ ] arr, float minReputation) {

		int totalCost=0;
		for(Long prodId:arr){
			totalCost+=this.getLowestPriceWithMinReputation(prodId,minReputation);
		}
		return totalCost;
	}

	/* remove all items, all of whose suppliers have a reputation that
       is equal or lower than the given maximum reputation.  Returns
       an array with the items removed.
	 */
	public Long[ ] purge(float maxReputation) {
		SortedMap<Float,HashSet<Long>> suppliersWithLowRept = this.reptSupplierMap.subMap(0F,true,maxReputation,true);
		HashSet<Long> removedItems=new HashSet<Long>();
		HashSet<Long> suppList=new HashSet<Long>();
		HashSet<Long> productsToRemove=new HashSet<Long>();
		for (Map.Entry<Float,HashSet<Long>> entry : suppliersWithLowRept.entrySet()) {
			HashSet<Long> suppliers=entry.getValue();
			for(Long id:suppliers){
				suppList.add(id);
			}
		}

		for(Long supplierID:suppList){   
			HashSet<Pair> pset=this.supplierProductMap.get(supplierID);
			if(pset !=null){
				for(Pair p:pset){
					productsToRemove.add(p.id);
				}
			} 
		}

		for(Long prodID:productsToRemove) {
			TreeSet<SupplierPair> tp=this.productSupplierMap.get(prodID);
			boolean allSuppliersLowRept=true;
			for(SupplierPair sp: tp){
				if(!suppList.contains(sp.suppID)){
					allSuppliersLowRept=false;
					break;
				}
			}
			if(allSuppliersLowRept){
				this.remove(prodID);
				removedItems.add(prodID);
			}

		}

		return removedItems.toArray(new Long[removedItems.size()]);
	}





	/* remove item from storage.  Returns the sum of the Longs that
       are in the description of the item deleted (or 0, if such an id
       did not exist).
	 */
	public Long remove(Long id) {
		HashSet<Long> descs=this.productIDDescMap.remove(id);
		Long sum=0L;
		if(descs==null){
			return sum;
		} 
		for(Long descId:descs){
			sum+=descId;
			HashSet<Long> prodId=this.descProductIDMap.get(descId);
			if(prodId!=null){
				prodId.remove(id);
				if(prodId.size()==0){
					this.descProductIDMap.remove(descId);
				}
			}
		}

		TreeSet<SupplierPair> sp_set=this.productSupplierMap.remove(id);
		for(SupplierPair sp: sp_set){
			HashSet<Pair> pairs=this.supplierProductMap.get(sp.suppID);
			pairs.remove(new Pair(id,sp.price));
			if(pairs.size()==0){
				this.supplierProductMap.remove(sp.suppID);
			}
		}
		//  testPrint();
		return sum;
	}

	/* remove from the given id's description those elements that are
       in the given array.  It is possible that some elements of the
       array are not part of the item's description.  Return the
       number of elements that were actually removed from the description.
	 */
	public int remove(Long id, Long[ ] arr) {
		HashSet<Long> desc=this.productIDDescMap.get(id);
		int count=0;
		for(Long givenDesc:arr){
			if(desc.contains(givenDesc)){
				count++;
				desc.remove(givenDesc);
				if(desc.size()==0){
					this.productIDDescMap.remove(id);
					TreeSet<SupplierPair> spt=this.productSupplierMap.remove(id);
					for(SupplierPair sp:spt){
						HashSet<Pair> pairs=this.supplierProductMap.get(sp.suppID);
						pairs.remove(new Pair(id,sp.price));
					}

				}
				HashSet<Long> prodIds=this.descProductIDMap.get(givenDesc);
				prodIds.remove(id);
				if(prodIds.size()==0){
					this.descProductIDMap.remove(givenDesc);
				}	
			}
		}
		return count;
	}

	/* remove the elements of the array from the description of all
       items.  Return the number of items that lost one or more terms
       from their descriptions.
	 */
	public int removeAll(Long[ ] arr) {
		HashSet<Long> products=new HashSet<Long>();
		for(Long descID:arr){
			HashSet<Long> prods=this.descProductIDMap.remove(descID);
			if(prods==null){
				continue;
			}
			for(Long prodId:prods){
				products.add(prodId);
				HashSet<Long> desc=this.productIDDescMap.get(prodId);
				desc.remove(descID);
				if(desc.size()==0){
					this.productIDDescMap.remove(prodId);
					TreeSet<SupplierPair> spt=this.productSupplierMap.remove(prodId);
					for(SupplierPair sp:spt){
						HashSet<Pair> pairs=this.supplierProductMap.get(sp.suppID);
						pairs.remove(new Pair(prodId,sp.price));
					}
				}
			}
		} 
		return products.size();
	}
}
