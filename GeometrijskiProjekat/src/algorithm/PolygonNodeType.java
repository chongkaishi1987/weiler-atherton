package algorithm;

public enum PolygonNodeType {
	SUBJECT,CLIP,INTERSECTION,ENTERING,EXITING;
	
	public static PolygonNodeType flipSubjectClip(PolygonNodeType type)
	{
		if (type==SUBJECT) type=CLIP;
		else if (type==CLIP) type=SUBJECT;
		return type;
	}
	public static PolygonNodeType flipEnteringExiting(PolygonNodeType type)
	{
		if (type==ENTERING) type=EXITING;
		else if (type==EXITING) type=ENTERING;
		return type;
	}
}
