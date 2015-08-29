// txt 2
public class KdTree
{
	private static class Node 
	{
		private Point2D p;      // the point
		private RectHV rect;    // the axis-aligned rectangle corresponding to this node
		private Node lb;        // the left/bottom subtree
		private Node rt;        // the right/top subtree
		private int orient;     // 0 - horizontal; 1 - vertical
	}
	

	private Node root;
	private int size;
	private final static int HORIZONTAL = 0;
	private final static int VERTICAL = 1;
	
	// construct an empty set of points
	public KdTree()
	{
		root = null;
		size = 0;
	}
	
	// is the set empty? 
	public boolean isEmpty()
	{
		if (size == 0)
			return true;
		else
			return false;
	}
	
	// number of points in the set
	public int size()
	{
		return size;
	}
	
	// add the point to the set (if it is not already in the set)
	public void insert(Point2D p)
	{
		Point2D leftBottom = new Point2D(0, 0);
		Point2D rightTop = new Point2D(1, 1);
		
		if (!contains(p))
			root = insert(root, p, VERTICAL, leftBottom, rightTop);
	}
	
	private Node insert(Node pRoot, Point2D pAdd, int ori, Point2D leftBottom, Point2D rightTop)
	{
		if (pRoot == null)
		{
			Node nAdd = new Node();
			nAdd.p = pAdd;
			nAdd.lb = null;
			nAdd.rt = null;
			nAdd.orient = ori;
			nAdd.rect = new RectHV(leftBottom.x(), leftBottom.y(), rightTop.x(), rightTop.y());
			size++;
			return nAdd;
		}
		
		// vertical
		if (pRoot.orient == VERTICAL)
		{
			double cmp = pAdd.x() - pRoot.p.x();
			if (cmp < 0)
				pRoot.lb = insert(pRoot.lb, pAdd, HORIZONTAL, leftBottom, new Point2D(pRoot.p.x(), rightTop.y()));
			else if (cmp >= 0)
				pRoot.rt = insert(pRoot.rt, pAdd, HORIZONTAL, new Point2D(pRoot.p.x(), leftBottom.y()), rightTop);
		}
		else // horizontal
		{
			double cmp = pAdd.y() - pRoot.p.y();
			if (cmp < 0)
				pRoot.lb = insert(pRoot.lb, pAdd, VERTICAL, leftBottom, new Point2D(rightTop.x(), pRoot.p.y()));
			else if (cmp >= 0)
				pRoot.rt = insert(pRoot.rt, pAdd, VERTICAL, new Point2D(leftBottom.x(), pRoot.p.y()), rightTop);
		}
		
		return pRoot;
	}
	
	// does the set contain point p? 
	public boolean contains(Point2D p)
	{
		Node sn = root;
		while (sn != null)
		{
			if (sn.p.equals(p))
				return true;
			
			if (sn.orient == VERTICAL)
			{
				double cmp = p.x() - sn.p.x();
				if (cmp < 0)
					sn = sn.lb;
				else if (cmp >= 0)
					sn = sn.rt;
			}
			else
			{
				double cmp = p.y() - sn.p.y();
				if (cmp < 0)
					sn = sn.lb;
				else if (cmp >= 0)
					sn = sn.rt;
			}
		}
		return false;
	}
	
	public void draw()                         // draw all points to standard draw 
	{
		preorder(root);
	}
	
	private void preorder(Node sn)
	{
		if (sn != null)
		{
			StdDraw.point(sn.p.x(), sn.p.y());
			//System.out.printf("%s,%s,%d\n", sn.p.toString(),sn.rect.toString(),sn.orient);
			preorder(sn.lb);
			preorder(sn.rt);
		}
	}
	
	public Iterable<Point2D> range(RectHV rect)             // all points that are inside the rectangle 
	{
		Stack<Point2D> stkPoint2D = new Stack<Point2D>();
		range(root, rect, stkPoint2D);
		return stkPoint2D;
	}
	
	private void range(Node sn, RectHV rect, Stack<Point2D> stkPoint2D)
	{
		if (sn != null)
		{
			if (sn.rect.intersects(rect))
			{
				if (rect.contains(sn.p))
					stkPoint2D.push(sn.p);
				range(sn.lb, rect, stkPoint2D);
				range(sn.rt, rect, stkPoint2D);
			}
		}
	}
	
	private double minDistance;
	private Point2D minPoint;
	private Point2D p;
	
	private void findNear(Node sn)
	{
		if (sn == null)
			return;
		
		double temp = sn.rect.distanceSquaredTo(this.p);
		if (temp < this.minDistance)
		{
			temp = sn.p.distanceSquaredTo(this.p);
			if (temp < this.minDistance)
			{
				this.minDistance = temp;
				this.minPoint = sn.p;
			}
			
			if (sn.orient == VERTICAL)
			{
				if (this.p.x() < sn.p.x())
				{
					findNear(sn.lb);
					findNear(sn.rt);
				}
				else
				{
					findNear(sn.rt);
					findNear(sn.lb);
				}
			}
			else
			{
				if (this.p.y() < sn.p.y())
				{
					findNear(sn.lb);
					findNear(sn.rt);
				}
				else
				{
					findNear(sn.rt);
					findNear(sn.lb);
				}
			}
		}
	}
	
	// a nearest neighbor in the set to point p; null if the set is empty 
	public Point2D nearest(Point2D p)
	{
		this.minDistance = Double.POSITIVE_INFINITY;
		this.minPoint = null;
		this.p = p;
		
		findNear(root);
		
		return this.minPoint;
		
		/*
		double minDistance = Double.POSITIVE_INFINITY;
		Point2D minPoint = null;
		Stack<Node> stkNode = new Stack<Node>();
		
		double temp = 0;
		stkNode.push(root);		
		while (!stkNode.isEmpty())
		{
			Node sn = stkNode.pop();
			if (sn == null)
				break;
			
			temp = sn.rect.distanceSquaredTo(p);
			if (temp < minDistance)
			{
				temp = sn.p.distanceSquaredTo(p);
				if (temp < minDistance)
				{
					minDistance = temp;
					minPoint = sn.p;
				}
				
				*/
				
				/*
				if (sn.lb != null)
					stkNode.push(sn.lb);
				if (sn.rt != null)
					stkNode.push(sn.rt);
				*/
				
				
				/*
				if (sn.orient == VERTICAL)
				{
					if (p.x() < sn.p.x())
					{
						if (sn.lb != null)
							stkNode.push(sn.lb);
						else if (sn.rt != null)
							stkNode.push(sn.rt);
					}
					else if (p.x() > sn.p.x())
					{
						if (sn.rt != null)
							stkNode.push(sn.rt);
						else if (sn.lb != null)
							stkNode.push(sn.lb);
					}
					else
					{
						if (sn.rt != null)
							stkNode.push(sn.rt);
						if (sn.lb != null)
							stkNode.push(sn.lb);
					}
				}
				else
				{
					if (p.y() < sn.p.y())
					{
						if (sn.lb != null)
							stkNode.push(sn.lb);
						else if (sn.rt != null)
							stkNode.push(sn.rt);
					}
					else if (p.y() > sn.p.y())
					{
						if (sn.rt != null)
							stkNode.push(sn.rt);
						else if (sn.lb != null)
							stkNode.push(sn.lb);
					}
					else
					{
						if (sn.rt != null)
							stkNode.push(sn.rt);
						if (sn.lb != null)
							stkNode.push(sn.lb);
					}
				}
				*/
	/*		}
		}
		return minPoint;*/
	}
	
	// unit testing of the methods (optional) 
	public static void main(String[] args)
	{
		KdTree kd = new KdTree();
		
		Point2D p1 = new Point2D(0.1, 0.1);
		Point2D p2 = new Point2D(0.2, 0.2);
		Point2D p3 = new Point2D(0.3, 0.3);
		kd.insert(p1);
		kd.insert(p2);
		kd.insert(p3);
		
		System.out.printf("%s\n", kd.contains(p1));
		System.out.printf("%s\n", kd.contains(p2));
		System.out.printf("%s\n", kd.contains(p3));
	}
}
