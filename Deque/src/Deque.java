import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> 
{
	private int size;
	private Node first, last;
	private class Node
	{
		Item value;
		Node next;
		Node previous;
	}
	
	public Deque()  // construct an empty deque
	{
		first = null;
		last = null;
		size = 0;
	}
	
	public boolean isEmpty()  // is the deque empty?
	{
		if (size == 0)
			return true;
		else
			return false;
	}
	
	public int size()   // return the number of items on the deque
	{
		return size;
	}
	
	public void addFirst(Item item)  // insert the item at the front
	{
		if (item == null)
			throw new NullPointerException("Tring to inserting a null item.");
		
		if (first == null)
		{
			first = new Node();
			first.value = item;
			first.next = null;
			//previous
			first.previous = null;
			last = first;
		}
		else
		{
			Node temp = new Node();
			temp.value = item;
			temp.next = first;
			temp.previous = null;
			//previous
			first.previous = temp;
			first = temp;
		}
		size++;
	}
	
	public void addLast(Item item)  // insert the item at the end
	{
		if (item == null)
			throw new NullPointerException("Tring to inserting an null item.");
		
		if (last == null)
		{
			last = new Node();
			last.value = item;
			last.next = null;
			//previous
			last.previous = null;
			
			first = last;
		}
		else
		{
			Node temp = new Node();
			temp.value = item;
			temp.next = null;
			//previous
			temp.previous = last;
			
			last.next = temp;
			last = temp;
		}
		size++;
	}
	
	public Item removeFirst()  // delete and return the item at the front
	{
		if (size == 0)
			throw new NoSuchElementException("Tring to remove item that not exist.");
		
		Node temp = first;
		Item value = first.value;
		
		if (first.next != null)
			first.next.previous = null;
		else
			last = null;
		first = first.next;
		size--;
		
		temp.next = null;
		temp.previous = null;
		temp.value = null;
		temp = null;
		
		return value;
	}
	
	public Item removeLast()  // delete and return the item at the end
	{
		if (size == 0)
			throw new NoSuchElementException("");
		
		Node temp = last;
		Item value = last.value;
		if (last.previous != null)
			last.previous.next = null;
		else
			first = null;
		last = last.previous;
		size--;
		
		temp.next = null;
		temp.previous = null;
		temp.value = null;
		temp = null;		
		
		return value;
	}
	
	@Override
	public Iterator<Item> iterator()         // return an iterator over items in order from front to end
	{
		return new ListIterator();
	}
	
	private class ListIterator implements Iterator<Item>
	{
		private Node current = first;
		
		@Override
		public boolean hasNext()
		{
			if (this.current != null)
				return true;
			else
				return false;
		}

		@Override
		public Item next() 
		{
			if (this.current == null)
				throw new NoSuchElementException("No such element.");
			
			Item value = this.current.value;
			this.current = this.current.next;
			return value;
		}

		@Override
		public void remove() 
		{
			throw new UnsupportedOperationException("Unsupported operation.");
		}
	}
	
	public static void main(String[] args)   // unit testing
	{
		
		Deque<Integer> dq = new Deque();
		
		dq.addFirst(1);
		int t = dq.removeLast();
		System.out.printf("%d\t", t);
		dq.addFirst(2);
		dq.addFirst(3);
		System.out.printf("%d\t", dq.removeLast());
		System.out.printf("%d\t", dq.removeLast());
		/*
		Iterator i = dq.iterator();
		while(i.hasNext())
		{
			int s = (int) i.next();
			System.out.printf("%d\t", s);
		}
		
		
		System.out.printf("\n%d\t", dq.removeFirst());
		System.out.printf("%d\t", dq.removeFirst());
		System.out.printf("%d\t", dq.removeLast());
		System.out.printf("%d\t", dq.removeFirst());
		System.out.printf("%d\t", dq.removeFirst());
		System.out.printf("%d\t", dq.removeLast());*/
	
	}
}
