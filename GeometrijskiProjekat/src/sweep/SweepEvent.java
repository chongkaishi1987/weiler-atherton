package sweep;

import utils.MathUtils;

public class SweepEvent implements Comparable<SweepEvent> {
	PolygonLineSegment primarySegment, intersectSegment;
	SweepEventType type;
	private double eventX, eventY;
	private boolean feasable;

	/**
	 * Creates an insert/delete event
	 * 
	 * @param segment
	 *            - the segment
	 * @param insert
	 *            - true if inserting the segment, false if deleting
	 */
	public SweepEvent(PolygonLineSegment segment, boolean insert) {
		if (insert) {
			type = SweepEventType.INSERT;
			eventX = segment.start.x;
			eventY = segment.start.y;
		} else {
			type = SweepEventType.DELETE;
			eventX = segment.end.x;
			eventY = segment.end.y;
		}
		feasable = true;
		primarySegment = segment;
	}

	/**
	 * 
	 * @param leftSegment
	 *            - the left intersecting segment
	 * @param rightSegment
	 *            - the right intersecting segment
	 */
	public SweepEvent(PolygonLineSegment leftSegment,
			PolygonLineSegment rightSegment) {
		type = SweepEventType.INTERSECT;
		primarySegment = leftSegment;
		intersectSegment = rightSegment;
		if (leftSegment.type == rightSegment.type
				|| leftSegment.start == rightSegment.start)
			feasable = false;
		else {
			feasable = false;
			double A1 = leftSegment.getA(), B1 = leftSegment.getB(), C1 = leftSegment
					.getC();
			double A2 = rightSegment.getA(), B2 = rightSegment.getB(), C2 = rightSegment
					.getC();
			double det = A1 * B2 - A2 * B1;
			if (MathUtils.Equal(det, 0)) {
				if (!(MathUtils.Equal(A1, 0) && MathUtils.Equal(B1, 0)) && !(MathUtils.Equal(A2, 0) && MathUtils.Equal(B2, 0))) {
					if (!MathUtils.Equal(B1, 0)) {
						if (MathUtils.Equal(C1 / B1, C2 / B2))
							feasable = true;
					} else {
						if (MathUtils.Equal(C1 / A1, C2 / A2))
							feasable = true;
					}
				}
				if (feasable) {
					double L1 = leftSegment.start.y, L2 = leftSegment.end.y, R1 = rightSegment.start.y, R2 = rightSegment.end.y;
					if (MathUtils.Equal(B1, 0)) {
						L1 = leftSegment.start.x;
						L2 = leftSegment.end.x;
						if (L1 > L2) {
							double tmp = L1;
							L1 = L2;
							L2 = tmp;
						}
						R1 = rightSegment.start.x;
						R2 = rightSegment.end.x;
						if (R1 > R2) {
							double tmp = R1;
							R1 = R2;
							R2 = tmp;
						}
					}
					if (!MathUtils.Equal(L2, R1) && L2 < R1)
						feasable = false;
					else {
						if (MathUtils.Equal(L1, R1)) {
							eventX = leftSegment.end.x;
							eventY = leftSegment.end.y;
						} else {
							eventX = rightSegment.start.x;
							eventY = rightSegment.start.y;
						}
					}
				}
			} else {
				eventX = (B2 * C1 - B1 * C2) / det;
				eventY = (A1 * C2 - A2 * C1) / det;
				if (eventY < rightSegment.start.y
						+ MathUtils.DOUBLE_EQUALITY_TOLERANCE
						&& eventY < leftSegment.start.y
								+ MathUtils.DOUBLE_EQUALITY_TOLERANCE)
				{
					//checks needed if the line destruction is on same y coordinate
					if (MathUtils.Equal(A1, 0))
					{
						//System.out.println("Usao1 za "+eventX +"," + eventY);
						//System.out.println(leftSegment.start.x + "," + leftSegment.end.x);
						if (MathUtils.Equal(eventX, leftSegment.start.x) || MathUtils.Equal(eventX, leftSegment.end.x))
							feasable=true;
						else if (eventX > leftSegment.start.x && eventX < leftSegment.end.x) feasable=true;
						else if (eventX > leftSegment.end.x && eventX < leftSegment.start.x) feasable=true;
					}
					else if (MathUtils.Equal(A2, 0))
					{
						//System.out.println("Usao2 za "+eventX +"," + eventY);
						if (MathUtils.Equal(eventX, rightSegment.start.x) || MathUtils.Equal(eventX, rightSegment.end.x))
							feasable=true;
						else if (eventX > rightSegment.start.x && eventX < rightSegment.end.x) feasable=true;
						else if (eventX > rightSegment.end.x && eventX < rightSegment.start.x) feasable=true;
					}
					else feasable = true;
				}
					
			}
		}
	}

	public boolean isFeasable() {
		return feasable;
	}

	@Override
	public int compareTo(SweepEvent o) {
		if (primarySegment == o.primarySegment
				&& intersectSegment == o.intersectSegment)
			return 0;
		if (!MathUtils.Equal(eventY, o.eventY))
			return Double.compare(eventY, o.eventY);
		if (!MathUtils.Equal(eventX, o.eventX))
			return Double.compare(o.eventX, eventX);
		if (type == SweepEventType.INSERT) {
			if (o.type == SweepEventType.INSERT
					&& primarySegment.type == PolygonLineSegmentType.CLIP)
				return -1;
			return 1;
		} else if (type == SweepEventType.INTERSECT) {
			if (o.type == SweepEventType.INSERT)
				return -1;
			if (o.type == SweepEventType.DELETE)
				return 1;
			if (primarySegment.type == PolygonLineSegmentType.SUBJECT)
				return 1;
			return -1;
		} else {
			if (o.type == SweepEventType.DELETE
					&& primarySegment.type == PolygonLineSegmentType.SUBJECT)
				return 1;
			return -1;
		}
	}

	public double getEventX() {
		return eventX;
	}

	public double getEventY() {
		return eventY;
	}
}
