public class PointSET
{
	private SET<Point2D> pointSets = null;
	
	// construct an empty set of points 
	public PointSET()
	{
		pointSets = new SET<Point2D>();
	}
	
	// is the set empty? 
	public boolean isEmpty()
	{
		return pointSets.isEmpty();
	}
	
	// number of points in the set 
	public int size()
	{
		return pointSets.size();
	}
	
	// add the point to the set (if it is not already in the set)
	public void insert(Point2D p)
	{
		if (!pointSets.contains(p))
			pointSets.add(p);
	}
	
	// does the set contain point p? 
	public boolean contains(Point2D p) 
	{
		return pointSets.contains(p);
	}
	
	// draw all points to standard draw 
	public void draw()
	{
		for (Point2D p2d : pointSets)
		{
			//System.out.printf("%f,%f\n", p2d.x(),p2d.y());
			p2d.draw();
		}
	}
	
	// all points that are inside the rectangle 
	public Iterable<Point2D> range(RectHV rect)
	{
		Stack<Point2D> stkPoint2d = new Stack<Point2D>();
		
		for (Point2D p2d : pointSets)
		{
			if (rect.contains(p2d))
				stkPoint2d.push(p2d);
		}
		
		return stkPoint2d;
	}
	
	// a nearest neighbor in the set to point p; null if the set is empty 
	public Point2D nearest(Point2D p)
	{
		double minDistance = Double.POSITIVE_INFINITY;
		Point2D minPoint = null;
		
		for (Point2D p2d : pointSets)
		{
			double dis = p.distanceSquaredTo(p2d);
			if (dis < minDistance)
			{
				minDistance = dis;
				minPoint = p2d;
			}
		}
		
		//System.out.printf("%f,%f",minPoint.x(),minPoint.y());
		
		return minPoint;
	}
	
	public static void main(String[] args)
	{
		PointSET p = new PointSET();
		p.insert(new Point2D(0.1, 0.1));
		p.insert(new Point2D(0.4, 0.2));
		p.insert(new Point2D(0.2, 0.2));
		p.insert(new Point2D(0.3, 0.3));
		p.insert(new Point2D(0.4, 0.4));
		
		
		//StdDraw.setPenRadius(0.01);
		//StdDraw.setPenRadius();
		StdDraw.setXscale(0, 10);
		StdDraw.setYscale(0, 10);
		p.draw();
	}
}
