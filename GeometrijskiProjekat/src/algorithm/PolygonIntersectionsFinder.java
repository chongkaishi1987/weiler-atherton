package algorithm;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.TreeSet;

public class PolygonIntersectionsFinder {

	private static PolygonIntersectionsFinder instance;
	private TreeSet<PolygonNode> enteringNodes;
	private PolygonIntersectionsFinder()
	{
		enteringNodes = new TreeSet<PolygonNode>();
	}
	public static PolygonIntersectionsFinder getInstance() {
		if (instance==null) instance = new PolygonIntersectionsFinder();
		return instance;
	}
	public ArrayList<ArrayList<Point2D>> getPolygonIntersections(PolygonNode subject,PolygonNode clip, boolean sInC, boolean cInS)
	{
		ArrayList<ArrayList<Point2D>> toReturn = new ArrayList<ArrayList<Point2D>>();
		enteringNodes.clear();
		subject.addNodesToIntersectionsEntering(enteringNodes);
		clip.addNodesToIntersectionsEntering(enteringNodes);
		if (enteringNodes.isEmpty())
		{
			ArrayList <Point2D> inter = new ArrayList<Point2D>();
			if (sInC) subject.addPointsToSubjects(inter);
			else if (cInS) clip.addPointsToClips(inter);
			toReturn.add(inter);
		}
		while (!enteringNodes.isEmpty())
		{
			toReturn.add(getPolygonIntersection(enteringNodes.first()));
		}
		return toReturn;
	}
	private ArrayList<Point2D> getPolygonIntersection(PolygonNode intersection)
	{
		ArrayList<Point2D> toReturn = new ArrayList<Point2D>();
		PolygonNodeType currentlyTraversing = PolygonNodeType.SUBJECT;
		enteringNodes.remove(intersection);
		toReturn.add(new Point2D.Double(intersection.x,intersection.y));
		PolygonNode start = intersection;
		while (intersection.next(currentlyTraversing) != start)
		{
			intersection = intersection.next(currentlyTraversing);
			toReturn.add(new Point2D.Double(intersection.x,intersection.y));
			if (intersection.getType() == PolygonNodeType.ENTERING) enteringNodes.remove(intersection);
			if (intersection.getType() == PolygonNodeType.ENTERING || intersection.getType() == PolygonNodeType.EXITING)
				currentlyTraversing = PolygonNodeType.flipSubjectClip(currentlyTraversing);
		}
		return toReturn;
	}
}
