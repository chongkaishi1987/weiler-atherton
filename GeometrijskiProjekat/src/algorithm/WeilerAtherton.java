package algorithm;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import output.AlgorithmOutput;
import sweep.IntersectionFinder;

public class WeilerAtherton {

	private static WeilerAtherton instance;
	private TreeSet<Point2D> pointsCheck;
	public static WeilerAtherton getInstance() {
		if (instance == null) instance = new WeilerAtherton();
		return instance;
	}
	private WeilerAtherton ()
	{
		pointsCheck = new TreeSet<Point2D>(new PointCheckComparator());
	}
	public AlgorithmOutput doAlgorithms(ArrayList<Point2D> s, ArrayList<Point2D> c, boolean sInC, boolean cInS)
	{
		
		System.out.println(sInC+ "  c in s: " +cInS);
		pointsCheck.clear();
		checkAndCorrectPoints(s);
		checkAndCorrectPoints(c);
		
		AlgorithmOutput toReturn = new AlgorithmOutput();
		PolygonNode subject = PolygonNode.createSubject(s);
		PolygonNode clip = PolygonNode.createClip(c);
		IntersectionFinder.getInstance().findIntersections(subject, clip);
		Set<Point2D> intersectionsEntering = new HashSet<Point2D>();
		Set<Point2D> intersectionsExiting = new HashSet<Point2D>();
		subject.addPointsToIntersectionEnterings(intersectionsEntering);
		clip.addPointsToIntersectionExitings(intersectionsExiting);
		toReturn.setIntersectionsEntering(new ArrayList<Point2D>(intersectionsEntering));
		toReturn.setIntersectionsExiting(new ArrayList<Point2D>(intersectionsExiting));
		toReturn.setFinalIntersections(PolygonIntersectionsFinder.getInstance().getPolygonIntersections(subject, clip));
		return toReturn;
	}
	private void checkAndCorrectPoints(ArrayList<Point2D> points)
	{
		for (Point2D point : points)
		{
			while (pointsCheck.contains(point))
			{
				point.setLocation(point.getX()+(Math.random()-0.5) * 2e-3, point.getY()+(Math.random()-0.5) * 2e-3);
			}
			pointsCheck.add(point);
		}
	}
	private class PointCheckComparator implements Comparator<Point2D>
	{

		@Override
		public int compare(Point2D o1, Point2D o2) {
			if (Math.abs(o1.getX()-o2.getX()) > 1e-5)  return Double.compare(o1.getX(), o2.getX());
			if (Math.abs(o1.getY()-o2.getY()) > 1e-5)  return Double.compare(o1.getY(), o2.getY());
			return 0;
		}
		
	}
}
