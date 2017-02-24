package shell;

public class Point
{
	public final int x, y;
	
	public Point(int x, int y)
	{
		this.x = x;
		this.y = y;
	}
	
	public Point(java.awt.Point p)
	{
		this.x = p.x;
		this.y = p.y;
	}
	
	public java.awt.Point toAwt()
	{
		return new java.awt.Point(x, y);
	}
	
	public Point translate(int dx, int dy)
	{
		return new Point(x + dx, y + dy);
	}
	
	public boolean equals(int x, int y)
	{
		return this.x == x && this.y == y;
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if(obj instanceof Point)
			return(((Point)obj).x == x && ((Point)obj).y == y);
		else
			return false;
	}
	
	@Override
	public int hashCode()
	{
		return x ^ y ^ (40523436 % x) ^ (39549645 % y);
	}
	
	@Override
	public String toString()
	{
		return "(" + x + ", " + y + ")";
	}
	
	public static Point[] merge(Point[]... arrays)
	{
		int size = 0;
		for(Point[] array : arrays)
			size += array.length;
		
		Point[] mergedGroup = new Point[size];
		int index = 0;
		for(Point[] array : arrays)
			for(Point point : array)
				mergedGroup[index++] = point;
		
		return mergedGroup;
	}
	
	public static Point midPoint(Point p1, Point p2)
	{
		return new Point((p1.x + p2.x) / 2, (p1.y + p2.y) / 2);
	}
}