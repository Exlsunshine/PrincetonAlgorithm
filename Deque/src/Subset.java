import java.util.Iterator;


public class Subset
{
	public static void main(String[] args)
	{
		int k;
		k = Integer.parseInt(args[0]);
		RandomizedQueue<String> rq = new RandomizedQueue<String>();
		
		
		while (!StdIn.isEmpty())
		{
			String str = StdIn.readString();
			rq.enqueue(str);
		}
		
		/*
		for (int i = 1; i < args.length; i++)
		{
			rq.enqueue(args[i]);
		}*/
		
		Iterator<String> rqIterator = rq.iterator();
		while (rqIterator.hasNext())
		{
			if (k < 1)
				break;
			String str = (String) rqIterator.next();
			StdOut.printf("%s\n", str);
			k--;
		}
	}
}
