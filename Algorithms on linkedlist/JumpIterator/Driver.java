import java.util.Iterator;
import java.util.LinkedList;
import java.util.NoSuchElementException;
/*
* @author Radhika Kalaiselvan
*/
public class Driver {
	

public static void main(String[] args){
	LinkedList<Integer> ll=new LinkedList<Integer>();
	ll.add(10);
	ll.add(20);
	ll.add(22);
	ll.add(50);
	JumpIterator jit=new JumpIterator(ll);
	//Iterator<Integer> it=ll.iterator();
	jit.hasNext();
	jit.hasNext();
	while(jit.hasNext()){
		System.out.println(jit.next());	
	}
	
}
}
class JumpIterator implements Iterator<Integer>{
	Iterator<Integer> it;
	LinkedList<Integer> ll;
	int flag=0;
	JumpIterator(LinkedList<Integer> list){
		this.it=list.iterator();
		flag=1;
		ll=list;
	}

	@Override
	public boolean hasNext() {
		// TODO Auto-generated method stub
		System.out.println("size "+ll.size());
		return it.hasNext();
	}

	@Override
	public Integer next() {
		if(flag==1){
			flag=0;
			if(it.hasNext()){
				return it.next();
			} else{
			throw new NoSuchElementException();
		}
		}else{
			it.next();
			if(it.hasNext()){
				return it.next();
			}else{
				throw new NoSuchElementException();
			}
		}
}
}
