package algorithm;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

import sweep.PolygonLineSegment;
import utils.MathUtils;

public class PolygonNode implements Comparable<PolygonNode>{
	public double x, y;
	private PolygonNodeType type;
	public PolygonNode nextSubject, nextClip;

	public PolygonNode(double x, double y, PolygonNodeType type) {
		this.x = x;
		this.y = y;
		this.type = type;
	}

	public void insertAsNextSubject(PolygonNode node) {
		node.nextSubject = nextSubject;
		nextSubject = node;
	}

	public void insertAsNextClip(PolygonNode node) {
		node.nextClip = nextClip;
		nextClip = node;
	} 
	public PolygonNode next(PolygonNodeType type)
	{
		if (type == PolygonNodeType.SUBJECT) return nextSubject;
		if (type == PolygonNodeType.CLIP) return nextClip;
		return null;
	}
	public PolygonNodeType getType() {
		return type;
	}

	public static PolygonNode createSubject(ArrayList<Point2D> points)
    {
        //enforce clockwise if needed later
        PolygonNode starting = new PolygonNode(points.get(0).getX(), points.get(0).getY(), PolygonNodeType.SUBJECT);
        PolygonNode current = starting;
        for (int i=1;i<points.size();i++)
        {
            current.nextSubject =  new PolygonNode(points.get(i).getX(), points.get(i).getY(), PolygonNodeType.SUBJECT);
            current = current.nextSubject;
        }
        current.nextSubject = starting;
        return starting;
    }
    public static PolygonNode createClip(ArrayList<Point2D> points)
    {
        //enforce clockwise if needed later
        PolygonNode starting = new PolygonNode(points.get(0).getX(), points.get(0).getY(), PolygonNodeType.CLIP);
        PolygonNode current = starting;
        for (int i=1;i<points.size();i++)
        {
            current.nextClip =  new PolygonNode(points.get(i).getX(), points.get(i).getY(), PolygonNodeType.CLIP);
            current = current.nextClip;
        }
        current.nextClip = starting;
        return starting;
    }
    public void printPolygon()
    {
    	printNodes(this);
    }
    private void printNodes(PolygonNode seed)
    {
    	System.out.println(type + " (" + x + ", " +y +")");
    	if (seed.type == PolygonNodeType.CLIP) if (nextClip!=seed) nextClip.printNodes(seed);
    	if (seed.type == PolygonNodeType.SUBJECT) if (nextSubject!=seed) nextSubject.printNodes(seed);
    }
    private void addPointsToSubjects(PolygonNode seed, Collection<Point2D> subjects)
    {
    	if (type == PolygonNodeType.SUBJECT) subjects.add(new Point2D.Double(x,y));
    	if (seed.type == PolygonNodeType.CLIP) if (nextClip!=seed) nextClip.addPointsToSubjects(seed, subjects);
    	if (seed.type == PolygonNodeType.SUBJECT) if (nextSubject!=seed) nextSubject.addPointsToSubjects(seed,subjects);
    }
    public void addPointsToSubjects(Collection<Point2D> subjects)
    {
    	addPointsToSubjects(this, subjects);
    }
    
    private void addPointsToIntersectionsEntering(PolygonNode seed, Set<Point2D> intersectionsEntering)
    {
    	if (type == PolygonNodeType.ENTERING) intersectionsEntering.add(new Point2D.Double(x,y));
    	if (seed.type == PolygonNodeType.CLIP) if (nextClip!=seed) nextClip.addPointsToIntersectionsEntering(seed, intersectionsEntering);
    	if (seed.type == PolygonNodeType.SUBJECT) if (nextSubject!=seed) nextSubject.addPointsToIntersectionsEntering(seed,intersectionsEntering);
    }
    public void addPointsToIntersectionEnterings(Set<Point2D> intersectionsEntering)
    {
    	addPointsToIntersectionsEntering(this, intersectionsEntering);
    }
    
    private void addPointsToIntersectionsExiting(PolygonNode seed, Set<Point2D> intersectionsExiting)
    {
    	if (type == PolygonNodeType.EXITING) intersectionsExiting.add(new Point2D.Double(x,y));
    	if (seed.type == PolygonNodeType.CLIP) if (nextClip!=seed) nextClip.addPointsToIntersectionsExiting(seed, intersectionsExiting);
    	if (seed.type == PolygonNodeType.SUBJECT) if (nextSubject!=seed) nextSubject.addPointsToIntersectionsExiting(seed,intersectionsExiting);
    }
    public void addPointsToIntersectionExitings(Set<Point2D> intersectionsExiting)
    {
    	addPointsToIntersectionsExiting(this, intersectionsExiting);
    }
    
    private void addPointsToClips(PolygonNode seed, Collection<Point2D> clips)
    {
    	if (type == PolygonNodeType.CLIP) clips.add(new Point2D.Double(x,y));
    	if (seed.type == PolygonNodeType.CLIP) if (nextClip!=seed) nextClip.addPointsToClips(seed, clips);
    	if (seed.type == PolygonNodeType.SUBJECT) if (nextSubject!=seed) nextSubject.addPointsToClips(seed,clips);
    }
    public void addPointsToClips(Collection<Point2D> clips)
    {
    	addPointsToClips(this, clips);
    }
    
    private void addNodesToIntersectionsEntering(PolygonNode seed, Set<PolygonNode> intersectionsEntering)
    {
    	if (type == PolygonNodeType.ENTERING) intersectionsEntering.add(this);
    	if (seed.type == PolygonNodeType.CLIP) if (nextClip!=seed) nextClip.addNodesToIntersectionsEntering(seed, intersectionsEntering);
    	if (seed.type == PolygonNodeType.SUBJECT) if (nextSubject!=seed) nextSubject.addNodesToIntersectionsEntering(seed,intersectionsEntering);
    }
    public void addNodesToIntersectionsEntering(Set<PolygonNode> intersectionsEntering)
    {
    	addNodesToIntersectionsEntering(this, intersectionsEntering);
    }
    
    public ArrayList<PolygonLineSegment> getLineSegments()
    {
    	ArrayList<PolygonLineSegment> res= new ArrayList<PolygonLineSegment>();
    	getLineSegments(this,res);
    	return res;
    }
    private void getLineSegments(PolygonNode seed,ArrayList<PolygonLineSegment> res)
    {	PolygonNode next;
    	if (seed.type == PolygonNodeType.CLIP) next=nextClip;
    	else next=nextSubject;
    	res.add(new PolygonLineSegment(this, next));
    	if (next!=seed) next.getLineSegments(seed,res);
    }

	@Override
	public int compareTo(PolygonNode o) {
		if (!MathUtils.Equal(y, o.y)) return Double.valueOf(y).compareTo(o.y);
		if (!MathUtils.Equal(x, o.x)) return Double.valueOf(x).compareTo(o.x);
		return 0;
	}
	public void setType(PolygonNodeType type) {
		this.type = type;
	}
    
}
