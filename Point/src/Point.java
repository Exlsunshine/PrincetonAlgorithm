import java.util.Comparator;


public class Point implements Comparable<Point>
{
	private final int x;
	private final int y;
	public final Comparator<Point> SLOPE_ORDER = new BySlopeOrder();        // compare points by slope to this point
	
	// construct the point (x, y)
	public Point(int x, int y)
	{
		this.x = x;
		this.y = y;
	}
	
	// draw this point
	public void draw()
	{
		StdDraw.point(this.x, this.y);
	}
	
	// draw the line segment from this point to that point
	public void drawTo(Point that)
	{
		StdDraw.line(this.x, this.y, that.x, that.y);
	}
	
	// string representation
	public String toString()
	{
		return "(" + x + "," + y + ")";
	}
	
	
	//==================================================//
	private class BySlopeOrder implements Comparator<Point>
	{
		@Override
		public int compare(Point arg0, Point arg1) 
		{
			if (arg0 == null || arg1 == null)
				throw new NullPointerException("Null pointer.");
			
			
			double slope0 = slopeTo(arg0);
			double slope1 = slopeTo(arg1);
			
			//deal with particular situation
			if (slope0 == Double.NEGATIVE_INFINITY)
			{
				if (slope1 == Double.NEGATIVE_INFINITY)
					return 0;
				else
					return -1;
			}
			if (slope1 == Double.NEGATIVE_INFINITY)
				return 1;
			
			if (slope0 == Double.POSITIVE_INFINITY)
			{
				if (slope1 == Double.POSITIVE_INFINITY)
					return 0;
				else
					return 1;
			}
			if (slope1 == Double.POSITIVE_INFINITY)
				return -1;
			
			//deal the normal situation
			if (slope0 > slope1)
				return 1;
			else if (slope0 == slope1)
				return 0;
			else
				return -1;
		}
	}
	
	public double slopeTo(Point that)                  // the slope between this point and that point
	{
		//Double.POSITIVE_INFINITY
		double deltaX = that.x - this.x;
		double deltaY = that.y - this.y;

		if (deltaY == 0 && deltaX == 0)//overlap
			return Double.NEGATIVE_INFINITY;
		if (deltaY == 0)//horizontal
			return (1.0 - 1.0) / 1.0; 
		if (deltaX == 0)//vertical
			return Double.POSITIVE_INFINITY;
		return deltaY / deltaX;
	}
	
	// is this point lexicographically smaller than that point?
	@Override
	public int compareTo(Point that) 
	{
		if (this.y < that.y)
			return -1;
		
		if (this.y == that.y)
		{
			if (this.x < that.x)
				return -1;
			else if (this.x == that.x)
				return 0;
			else
				return 1;
		}
		else
			return 1;
	}
	
	public static void main(String [] args)
	{
		Point p = new Point(1, 1);
		Point q = new Point(1, 1);
		Point r = new Point(1, 3);
		
		int restult = p.SLOPE_ORDER.compare(q, r); // compareTo(q);
		System.out.printf("%s\n", Double.toString(p.slopeTo(q)));
		int restult2 = p.SLOPE_ORDER.compare(r, q); // compareTo(q);
		System.out.printf("%s\n", Double.toString(p.slopeTo(r)));
	}
	

}
