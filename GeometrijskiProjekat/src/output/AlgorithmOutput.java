package output;

import java.awt.geom.Point2D;
import java.util.ArrayList;

public class AlgorithmOutput {

	private ArrayList<Point2D> intersectionsEntering;
	private ArrayList<Point2D> intersectionsExiting;
	private ArrayList<ArrayList<Point2D>> finalIntersections;
	public AlgorithmOutput ()
	{
		intersectionsEntering = new ArrayList<Point2D>();
		intersectionsExiting = new ArrayList<Point2D>();
		finalIntersections = new ArrayList<ArrayList<Point2D>>();
	}
	public void setIntersectionsEntering(
			ArrayList<Point2D> intersectionsEntering) {
		this.intersectionsEntering = intersectionsEntering;
	};
	public void setIntersectionsExiting(ArrayList<Point2D> intersectionsExiting) {
		this.intersectionsExiting = intersectionsExiting;
	}
	public ArrayList<Point2D> getIntersectionsEntering() {
		return intersectionsEntering;
	}
	public ArrayList<Point2D> getIntersectionsExiting() {
		return intersectionsExiting;
	}
	public ArrayList<ArrayList<Point2D>> getFinalIntersections() {
		return finalIntersections;
	}
	public void setFinalIntersections(
			ArrayList<ArrayList<Point2D>> finalIntersections) {
		this.finalIntersections = finalIntersections;
	}
	public void addFinalIntersection(ArrayList<Point2D> finalIntersection)
	{
		finalIntersections.add(finalIntersection);
	}
	@Override
	public String toString() {
		return finalIntersections.toString();
	}
}
