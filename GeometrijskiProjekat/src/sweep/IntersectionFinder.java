package sweep;

import java.util.TreeSet;

import algorithm.PolygonNode;
import algorithm.PolygonNodeType;

public class IntersectionFinder {
	private static IntersectionFinder instance;
	private TreeSet<PolygonLineSegment> status;
	private TreeSet<SweepEvent> queue;
	private TreeSet<PolygonNode> intersections;

	private double sweepY = Double.MAX_VALUE;

	private IntersectionFinder() {
		status = new TreeSet<PolygonLineSegment>();
		queue = new TreeSet<SweepEvent>();
		intersections = new TreeSet<PolygonNode>();
	}

	public static IntersectionFinder getInstance() {
		if (instance == null)
			instance = new IntersectionFinder();
		return instance;
	}

	private void addEvents(PolygonNode subject, PolygonNode clip) {
		for (PolygonLineSegment pls : subject.getLineSegments()) {
			addEvent(new SweepEvent(pls, true));
		}
		for (PolygonLineSegment pls : clip.getLineSegments()) {
			addEvent(new SweepEvent(pls, true));
		}
	}

	private void processEvent(SweepEvent e) {
		//System.out.println(status);
		sweepY = e.getEventY();
		switch (e.type) {
		case DELETE:
			processDeleteEvent(e);
			break;
		case INSERT:
			processInsertEvent(e);
			break;
		case INTERSECT:
			processIntersectEvent(e);
			break;
		}
	}

	private void addEvent(SweepEvent e) {
		if (e.isFeasable())
			queue.add(e);
	}

	private void removeEvent(SweepEvent e) {
		if (e.isFeasable())
			queue.remove(e);
	}

	private void insertSegment(PolygonLineSegment segment) {
		System.out.println("Insert " + segment);
		PolygonLineSegment left = status.lower(segment);
		PolygonLineSegment right = status.higher(segment);
		if (left != null && right != null)
			removeEvent(new SweepEvent(left, right));
		status.add(segment);
		if (left != null)
			addEvent(new SweepEvent(left, segment));
		if (right != null)
			addEvent(new SweepEvent(segment, right));
		addEvent(new SweepEvent(segment, false));
	}

	private void deleteSegment(PolygonLineSegment segment) {
		System.out.println("Delete " + segment);
		PolygonLineSegment left = status.lower(segment);
		PolygonLineSegment right = status.higher(segment);
		if (left != null)
			removeEvent(new SweepEvent(left, segment));
		if (right != null)
			removeEvent(new SweepEvent(segment, right));
		status.remove(segment);
		if (left != null && right != null)
			addEvent(new SweepEvent(left, right));
		removeEvent(new SweepEvent(segment, false));
	}

	private void processInsertEvent(SweepEvent e) {
		insertSegment(e.primarySegment);
	}

	private void processDeleteEvent(SweepEvent e) {
		deleteSegment(e.primarySegment);
	}

	private void processIntersectEvent(SweepEvent e) {
		System.out.println("Intersected at " + e.getEventX() + " , "
				+ e.getEventY());
		PolygonNode intersection = new PolygonNode(e.getEventX(),
				e.getEventY(), PolygonNodeType.INTERSECTION);
		if (intersections.floor(intersection) == null
				|| intersections.floor(intersection).compareTo(intersection) != 0) {
			intersections.add(intersection);

			if (e.primarySegment.type == PolygonLineSegmentType.SUBJECT) {
				if (e.primarySegment.start.nextSubject == e.primarySegment.end)
					e.primarySegment.start.insertAsNextSubject(intersection);
				else
					e.primarySegment.end.insertAsNextSubject(intersection);
			}
			if (e.primarySegment.type == PolygonLineSegmentType.CLIP) {
				if (e.primarySegment.start.nextClip == e.primarySegment.end)
					e.primarySegment.start.insertAsNextClip(intersection);
				else
					e.primarySegment.end.insertAsNextClip(intersection);
			}

			if (e.intersectSegment.type == PolygonLineSegmentType.SUBJECT) {
				if (e.intersectSegment.start.nextSubject == e.intersectSegment.end)
					e.intersectSegment.start.insertAsNextSubject(intersection);
				else
					e.intersectSegment.end.insertAsNextSubject(intersection);
			}
			if (e.intersectSegment.type == PolygonLineSegmentType.CLIP) {
				if (e.intersectSegment.start.nextClip == e.intersectSegment.end)
					e.intersectSegment.start.insertAsNextClip(intersection);
				else
					e.intersectSegment.end.insertAsNextClip(intersection);
			}
		} else {
			intersection = intersections.floor(intersection);
		}
		deleteSegment(e.primarySegment);
		deleteSegment(e.intersectSegment);
		insertSegment(new PolygonLineSegment(intersection, e.primarySegment.end));
		insertSegment(new PolygonLineSegment(intersection,
				e.intersectSegment.end));
	}

	public void findIntersections(PolygonNode subject, PolygonNode clip, boolean sInC) {
		status.clear();
		queue.clear();
		intersections.clear();
		sweepY=Double.MAX_VALUE;
		addEvents(subject, clip);
		while (!queue.isEmpty()) {
			processEvent(queue.pollLast());
		}
		
		
	    PolygonNodeType nextNodeMark = sInC?PolygonNodeType.EXITING:PolygonNodeType.ENTERING;
	    
	    PolygonNode iterator = subject;
	    while (!(iterator.nextSubject == subject))
	    {
	    	iterator = iterator.nextSubject;
	    	if (iterator.getType() == PolygonNodeType.INTERSECTION)
	    	{
	    		iterator.setType(nextNodeMark);
	    		nextNodeMark=PolygonNodeType.flipEnteringExiting(nextNodeMark);
	    	}
	    }
	}

	public double getSweepY() {
		return sweepY;
	}
}
