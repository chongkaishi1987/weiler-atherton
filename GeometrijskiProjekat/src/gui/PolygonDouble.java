package gui;

import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;


public class PolygonDouble {
	private ArrayList<Point2D> points;
	private boolean finished;
	
	public PolygonDouble(){
		points = new ArrayList<>();
		finished = false;
	}
	
	public void setPoints(ArrayList<Point2D> points) {
		this.points = points;
	}
	
	public void addPoint(double x, double y){
		points.add(new Point2D.Double(x, y));
		
	}
	
	public int getPointsCount(){
		return points.size();
	}
	
	public void setFinished(boolean finished) {
		this.finished = finished;
	}
	public boolean isFinished() {
		return finished;
	}
	public void finish(double x, double y){
		points.remove(getPointsCount()-1);
		finished = true;
	}
	
	public void replaceLast(double x, double y){
		points.set(points.size()-1, new Point2D.Double(x, y));
	}
	
	public ArrayList<Point2D> getPointsList(){
		return points;
	}
	
	public ArrayList<Point2D> getPointsClockwise(){
		ArrayList<Point2D> ret = new ArrayList<>();
		
		int minPointIndex = 0;
		Point2D minPoint = points.get(0);
		for(int i = 1; i < points.size(); i++){
			Point2D point = points.get(i);
			if(point.getX() < minPoint.getX() || (point.getX() == minPoint.getX() && point.getY() < minPoint.getY())){
				minPointIndex = i;
				minPoint = point;
			}
			
		}
		
		Point2D
				prevPoint = points.get(minPointIndex - 1 < 0 ? points.size() - 1 : minPointIndex - 1),
				nextPoint = points.get(minPointIndex + 1 > points.size() - 1 ?  0 : minPointIndex + 1);
		
		boolean isClockwise = prevPoint.getY() > nextPoint.getY();
		int c = 0;
		for(int i = minPointIndex; c < getPointsCount(); i = (((isClockwise ? i+1 : i-1) % getPointsCount()) + getPointsCount()) % getPointsCount(), c++){
			ret.add(points.get(i));
		}   
		
		return ret;
	}
	
	public Point2D getFirstPoint(){
		if(points.size() > 0)
			return points.get(0);
		return null;
	}
	
	public Path2D asPath(){
		Path2D ret = new Path2D.Double();
		if(getPointsCount() > 1){
			Point2D current = points.get(0);
			ret.moveTo(current.getX(), current.getY());
			
			for(int i = 1; i < getPointsCount(); i++){
				current = points.get(i);
				ret.lineTo(current.getX(), current.getY());
			}
			if(finished)
				ret.closePath();
		}
		return ret;
	}
	
	public boolean intersects(Point2D a, Point2D b, Point2D c, Point2D d){
		double c1, c2, length1, length2;
		int status1, status2;
		double epsilon=0.0000000001;

		length1= distance(b, a); //FloatMath.sqrt((b.getX()-a.getX())*(b.getX()-a.getX())+(b.getY()-a.getY())*(b.getY()-a.getY()));
		length2= distance(d, c); //FloatMath.sqrt((d.getX()-c.getX())*(d.getX()-c.getX())+(d.getY()-c.getY())*(d.getY()-c.getY()));

		c1=b.getX()*a.getY()-b.getY()*a.getX(); // c first line parameter //

		// checking if lines are colinear //
		if(Math.abs((b.getY()-a.getY())*c.getX()-(b.getX()-a.getX())*c.getY()+c1) < (length1* length2 * epsilon))
			if(Math.abs((b.getY()-a.getY())*d.getX()-(b.getX()-a.getX())*d.getY()+c1) < (length1* length2 * epsilon)){
				// if yes, are they intersecting //
				if(((a.getX()<=c.getX())&&(c.getX()<=b.getX())||(a.getX()>=c.getX())&&(c.getX()>=b.getX()))&&(a.getX()!=b.getX()))
					return true; 
				if(((a.getX()<=d.getX())&&(d.getX()<=b.getX())||(a.getX()>=d.getX())&&(d.getX()>=b.getX()))&&(a.getX()!=b.getX()))
					return true; 
				if(((a.getY()<=c.getY())&&(c.getY()<=b.getY())||(a.getY()>=c.getY())&&(c.getY()>=b.getY()))&&(a.getY()!=b.getY()))
					return true; 
				if(((a.getY()<=d.getY())&&(d.getY()<=b.getY())||(a.getY()>=d.getY())&&(d.getY()>=b.getY()))&&(a.getY()!=b.getY()))
					return true; 
				return false; 
			}

		if((b.getY()-a.getY())*c.getX()-(b.getX()-a.getX())*c.getY()+c1>0) status1=1;
		else status1=-1;

		if((b.getY()-a.getY())*d.getX()-(b.getX()-a.getX())*d.getY()+c1>0) status2=1;
		else status2=-1;

		if ((status1>0 && status2>0) || (status1<0 && status2<0))
			// if both ends of the second line are on the same side of first one//
			return false;

		// else continue //
		c2=d.getX()*c.getY()-d.getY()*c.getX(); // c second line parameter //
		if((d.getY()-c.getY())*a.getX()-(d.getX()-c.getX())*a.getY()+c2>0) status1=1;
		else status1=-1;

		if((d.getY()-c.getY())*b.getX()-(d.getX()-c.getX())*b.getY()+c2>0) status2=1; 
		else status2=-1;

		if ((status1>0 && status2>0) || (status1<0 && status2<0))
			// if both ends of the first line are on the same side of second one //
			return false; 
		return true;
	}
	private double distance(Point2D start, Point2D end){
		double dist = Math.sqrt((start.getX() - end.getX())*(start.getX() - end.getX()) + 
				(start.getY() - end.getY())*(start.getY() - end.getY()));
		return dist;
	}

	public boolean selfIntersects(int x, int y) {
		Point2D previous = null, last = null;
		if(getPointsCount() < 2) return false;
		for(Point2D point : points.subList(0, getPointsCount()-2)){
			if(previous == null){
				previous = point;
				continue;
			}
			if(last == null) last = points.get(getPointsCount()-2);
			if(intersects(previous, point, last, new Point2D.Double(x, y))) return true;
			previous = point;
		}
		return false;
	}

}
