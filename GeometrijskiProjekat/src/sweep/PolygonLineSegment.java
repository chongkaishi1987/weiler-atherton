package sweep;

import utils.MathUtils;
import algorithm.PolygonNode;

public class PolygonLineSegment implements Comparable<PolygonLineSegment> {
	public PolygonNode start, end;
	public PolygonLineSegmentType type;

	public PolygonLineSegment(PolygonNode a, PolygonNode b) {
		switch (a.getType()) {
		case CLIP:
			type = PolygonLineSegmentType.CLIP;
			break;
		case SUBJECT:
			type = PolygonLineSegmentType.SUBJECT;
			break;
		default:
			switch (b.getType()) {
			case CLIP:
				type = PolygonLineSegmentType.CLIP;
				break;
			case SUBJECT:
				type = PolygonLineSegmentType.SUBJECT;
				break;
			default:
				break;
			}
			break;
		}
		if (a.y > b.y) {
			start = a;
			end = b;
		} else if (a.y < b.y) {
			start = b;
			end = a;
		} else {
			if (a.x <= b.x) // = ??
			{
				start=a;
				end=b;
			}
			else
			{
				start=b;
				end=a;
			}
		}
	}

	public double getA() {
		return end.y - start.y;
	}

	public double getB() {
		return start.x - end.x;
	}

	public double getC() {
		return getA() * start.x + getB() * start.y;
	}

	private int comp(PolygonLineSegment o) {
		//doublecheck n
		double x=start.x,ox=o.start.x;
		double y=IntersectionFinder.getInstance().getSweepY();
		if (getA()!=0)
		{
			x=(getC()-getB()*y)/getA();
		}
		if (o.getA()!=0)
		{
			ox=(o.getC()-o.getB()*y)/o.getA();
		}
		if (!MathUtils.Equal(x,ox))
			return Double.valueOf(x).compareTo(ox);
		if (MathUtils.Equal(getA(), 0))
		{
			if (MathUtils.Equal(o.getA(), 0)) return Double.compare(end.x, o.end.x);
			return 1;
		}
		if (MathUtils.Equal(o.getA(), 0)) return -1;
		return Double.valueOf(getB()/getA()).compareTo(o.getB()/o.getA());
		}

	@Override
	public int compareTo(PolygonLineSegment o) {
		int r = comp(o);
		if (r==0 && this==o) return 0;
		if (r == 0)
			if (type == PolygonLineSegmentType.SUBJECT)
				return -1;
			else
				return 1;
		return r;
	}
	@Override
	public String toString() {
		return type + " ("+start.x+" , "+start.y+")" +" - " + " ("+end.x+" , "+end.y+")";
	}
}
