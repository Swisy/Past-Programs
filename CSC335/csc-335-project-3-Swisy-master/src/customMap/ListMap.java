package customMap;
import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.Iterator;
import java.util.Set;

/**
 * 
 * @author Saul Weintraub
 *
 * This class is a map that was designed to be used with Madlibs.java. This map uses a linked list
 * to store the key-value mappings added to it. This map is a templated generic type that supports
 * multiple types for key-value mappings. This map does not support the remove() method.
 */
public class ListMap<K, V> extends AbstractMap<K,V>{
	private ListMapNode<K, V> beginning;
	private ListMapNode<K, V> end;

	/**
	 *  This method will add the key value pairing to the map. If the key is already in the map,
	 *  this method will replace the existing value with the new value and return the old value.
	 *  
	 *  If the map is empty, the method will update the private variables beginning and end. The
	 *  method will use the containsKey method to determine if the key is already in the map. If it
	 *  is then the method will iterate through the linked list until it finds the node with the
	 *  key and will then save the old value and replace it with the new value before finally
	 *  returning the old value. If the key is not already in the map, then the method will create
	 *  a new node with the key and value and place it at the end of the linked list.
	 *  
	 *  
	 *  @param key key with which the specified value is to be associated
	 *  @param value value to be associated with the specified key
	 *  @return the previous value associated with key, or null if there was no mapping for key
	 */
	@Override
	public V put(K key, V value) {
		// If there are no elements in the map
		if(size() == 0) {
			beginning = new ListMapNode<K, V>(key, value, null);
			end = beginning;
			return null;
		}
		
		// If the key is already in the map, then find the node and replace the value
		if(containsKey(key)) {
			ListMapNode<K, V> cur = beginning;
			while(cur != null) {
				if(cur.key.equals(key)) {
					V oldVal = cur.value;
					cur.value = value;
					return oldVal;
				}
				cur = cur.next;
			}
		}
		
		// The key is not in the map and end is the end of the linked list
		ListMapNode<K, V> newElement = new ListMapNode<K, V>(key, value, null);
		end.next = newElement;
		end = end.next;
		
		return null;
		
	}
	
	/**
	 * Returns the number of key-value mappings in this map.
	 * 
	 * This method will iterate through all the nodes in the linked list and will count how many
	 * nodes are in the linked list.
	 * 
	 * @return the number of key-value mappings in this map
	 */
	@Override
	public int size() {
		int size = 0;
		ListMapNode<K, V> cur = beginning;
		while(cur != null) {
			size += 1;
			cur = cur.next;
		}
		return size;
	}
	
	/**
	 * Returns a Set view of the mappings contained in this map.The set is backed by the map, so
	 * changes to the map are reflected in the set, and vice-versa.
	 * 
	 * This method will create a ListMapEntrySet() object and will return it
	 * 
	 * @return a set view of the mappings contained in this map
	 */
	@Override
	public Set<Entry<K, V>> entrySet() {
		Set<Entry<K, V>> entrySet = new ListMapEntrySet();
		
		return entrySet;
	}
	
	/**
	 * 
	 * @author Saul Weintraub
	 * 
	 * This class is a node of the linked list that holds the key-value mappings in the map.
	 * This class has three public variables. The key and the value of the mapping, and a reference
	 * to the next node in the linked list.
	 *
	 * @param <A> The type of the Keys in the map
	 * @param <B> The type of the Values in the map
	 */
	private class ListMapNode<A, B>{
		public A key;
		public B value;
		public ListMapNode<A, B> next;
		
		public ListMapNode(A key, B value, ListMapNode<A, B> next) {
			this.key = key;
			this.value = value;
			this.next = next;
		}
	}
	
	/**
	 * 
	 * @author Saul Weintraub
	 * 
	 * This class is a concrete set of map entries that extends the AbstractSet class. This class
	 * has methods that will be used by the methods that were not overwritten to correctly perform
	 * their functions.
	 *
	 */
	private class ListMapEntrySet extends AbstractSet<Entry<K,V>>{
		
		/**
		 * Returns the number of elements in this set (and by extension the number of key-value
		 * mappings in the map).
		 * 
		 * This method will count all the nodes in the linked list to find the size of the set.
		 * 
		 * @return the number of elements in this set
		 */
		@Override
		public int size() {
			int size = 0;
			ListMapNode<K, V> cur = beginning;
			while(cur != null) {
				size += 1;
				cur = cur.next;
			}
			return size;
		}
		
		/**
		 * This method will check if the passed object is in the set.
		 * 
		 * This method will return true if the set contains an Entry equal to passed object. If the
		 * passed object is not an Entry then the method will return false. If the passed object is
		 * an Entry then the method will check if the Entry is in the Set.
		 * 
		 * @parameter o element whose presence in this set is to be tested
		 * 
		 * @return true if the Entry o is in the Set
		 */
		@SuppressWarnings("unchecked")
		@Override
		public boolean contains(Object o) {
			if(o instanceof Entry<?, ?>) {
				Entry<K, V> entry = (Entry<K, V>) o;
				K key = entry.getKey();
				V value = entry.getValue();
				
				ListMapNode<K, V> cur = beginning;
				while(cur != null) {
					if(cur.key.equals(key)) {
						if(cur.value.equals(value)) {
							return true;
						}
						return false;
					}
					cur = cur.next;
				}
				return false;
			} else {
				return false;
			}
			
		}
		
		/**
		 * This method will return an iterator over the elements in this set.
		 * 
		 * This method will create and return a ListMapEntrySetIterator object that will be used to
		 * iterate over the elements in this set.
		 * 
		 * @return an iterator over the elements in this set
		 */
		@Override
		public Iterator<Entry<K,V>> iterator(){
			Iterator<Entry<K, V>> iter = new ListMapEntrySetIterator<Entry<K, V>>();
			return iter;
			
		}
		
		
	}
	
	/**
	 * 
	 * @author Saul Weintraub
	 *
	 * This class is an iterator that will be used to iterate over the elements of the
	 * ListMapEntrySet class. This class has two methods that will allow the ListMapEntrySet to
	 * iterate over it's elements.
	 */
	private class ListMapEntrySetIterator<T> implements Iterator<T>{
		private ListMapNode<K, V> cur = beginning;
		
		/**
		 * This method will return whether or not there is another element to iterate over.
		 * 
		 * This method will return true if the next() method would return an element.
		 * 
		 * @return true if the iteration has more elements
		 */
		@Override
		public boolean hasNext() {
			if(cur != null) {
				return true;
			} else {
				return false;
			}
			
		}
		
		/**
		 * This method will return the next element in the iteration.
		 * 
		 * @return the next element in the iteration
		 */
		@SuppressWarnings("unchecked")
		@Override
		public T next() {
			SimpleEntry<K, V> entry = new SimpleEntry<K, V>(cur.key, cur.value);
			cur = cur.next;
			return (T) entry;
		}
	}

}
