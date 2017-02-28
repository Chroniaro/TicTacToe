package shell;

import java.util.Comparator;

public class RunInfo
{
	private final Point p1, p2;
	private final Point[] gaps;
	private final byte key;
	
	public RunInfo(Point p1, Point p2, byte key, Point... holes)
	{
		this.p1 = p1;
		this.p2 = p2;
		this.key = key;
		gaps = new Point[holes.length];
		for (int i = 0; i < holes.length; i++)
			gaps[i] = holes[i];
	}
	
	public Point[] getEnds()
	{
		return new Point[] {new Point(p1.x, p1.y), new Point(p2.x, p2.y)};
	}
	
	public Point[] getHoles()
	{
		return gaps;
	}
	
	public byte getKey()
	{
		return key;
	}
	
	public int getLength()
	{  
		return 1 + Math.max(Math.abs(p1.x - p2.x), Math.abs(p1.y - p2.y)) - getNumberOfGaps();
	}
	
	public static class sizeComparator implements Comparator<RunInfo>
	{
		@Override
		public int compare(RunInfo o1, RunInfo o2)
		{
			return 2 * (o2.getLength() - o1.getLength());
		}
	}
	
	public int getNumberOfGaps()
	{
		return gaps.length;
	}
	
	public Point getGap(int gapNumber)
	{
		return gaps[gapNumber];
	}
	
	public Point getGap()
	{
		return getGap(0);
	}
	
	public Point getExtension(int length)
	{
		if(length == 0)
			return Point.midPoint(p1, p2);
		if(p1.equals(p2))
			return p1;
		
		int dx = (int)Math.signum(p1.x - p2.x);
		int dy = (int)Math.signum(p1.y - p2.y);
		
		Point reference = length > 0 ? p1 : p2;
		
		return new Point(reference.x + length * dx, reference.y + length * dy);
	}
	
	@Override
	public String toString()
	{
		return "[" + p1 + ", " + p2 + "]";
	}
}
