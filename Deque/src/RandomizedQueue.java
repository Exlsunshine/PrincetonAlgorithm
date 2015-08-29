import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Random;


public class RandomizedQueue<Item> implements Iterable<Item>
{
	private Item [] queue;
	private int size;
	
	/*
	public void test()
	{
		for (int i = 0;i < size; i++)
			System.out.printf("%d\t", ((Integer)queue[i]).intValue());
		System.out.printf("\n");
	}*/
	
	public RandomizedQueue()                 // construct an empty randomized queue
	{
		queue = (Item []) new Object[1];
		size = 0;
	}
	
	public boolean isEmpty()                 // is the queue empty?
	{
		if (size == 0)
			return true;
		else
			return false;
	}
	
	public int size()                        // return the number of items on the queue
	{
		return size;
	}
	
	public void enqueue(Item item)           // add the item
	{
		if (item == null)
			throw new NullPointerException("Tring to insert an empty item.");
		
		if (size == queue.length)
			resize(2 * queue.length, -1);

		queue[size++] = item;
	}
	
	private void resize(int capacity, int randIndex)
	{
		if (-1 == randIndex)
		{
			//System.out.printf("\n\nTotal is %d,use %d ,expand to %d",queue.length,size,capacity);
			Item [] copy = (Item []) new Object[capacity];
			for (int i = 0; i < size; i++)
				copy[i] = queue[i];
			
			queue = copy;
		}
		else
		{
			//System.out.printf("\n\nTotal is %d,use %d ,shrink to %d",queue.length,size,capacity);
			Item [] copy = (Item []) new Object[capacity];
			for (int i = 0; i < randIndex; i++)
				copy[i] = queue[i];
			for (int i = randIndex + 1; i < size; i++)
				copy[i - 1] = queue[i];
			
			queue = copy;
		}
	}
	
	private int random()
	{
		Random rand = new Random();
		return rand.nextInt(size);
	}
	
	public Item dequeue()     // delete and return a random item
	{
		if (size == 0)
			throw new NoSuchElementException("Tring to remove item that not exist.");
		
		int randIndex = random();
		Item value = queue[randIndex];
		
		if (size > 0 && queue.length >= size * 4)
		{
			resize(queue.length / 2 , randIndex);
			size--;
		}
		else
		{
			queue[randIndex] = queue[--size];
			queue[size] = null;
			//for (int i = randIndex; i < size - 1; i++)
			//	queue[i] = queue[i+1];
			//queue[--size] = null;
		}
		return value;
	}
	
	public Item sample()                     // return (but do not delete) a random item
	{
		if (size == 0)
			throw new NoSuchElementException("Tring to remove item that not exist.");
		
		int randIndex = random();
		return queue[randIndex];
	}

	
	@Override
	public Iterator<Item> iterator() // return an independent iterator over items in random order
	{
		return new ListIterator();
	}
	
	private class ListIterator implements Iterator<Item>
	{
		private Item [] items;
		private int sz;
		private int index;
		public ListIterator()
		{
			items = (Item []) new Object[size];
			sz = size;
			index = 0;
			
			for (int i = 0; i < sz; i++)
				items[i] = queue[i];
			StdRandom.shuffle(items);
		}
		
		@Override
		public boolean hasNext()
		{
			if (index < sz)
				return true;
			else
				return false;
		}

		@Override
		public Item next()
		{
			if (index < 0 || index >= size)
				throw new NoSuchElementException("No such element.");
			return items[index++];
		}

		@Override
		public void remove() 
		{
			throw new UnsupportedOperationException("Unsupported operation.");
		}
		
	}

	public static void main(String[] args)   // unit testing
	{
		/*
		RandomizedQueue<Integer> rq = new RandomizedQueue();
		
		for (int i = 0;i < 5 ; i++)
			rq.enqueue(i);
		rq.test();
		
		
		for (Integer i : rq)
		{
			System.out.printf("%d\t", i.intValue());
		}
		//rq.test();
		 */
	}
}
