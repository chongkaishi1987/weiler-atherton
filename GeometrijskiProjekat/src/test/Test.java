package test;

import java.awt.geom.Point2D;
import java.util.ArrayList;

import output.AlgorithmOutput;

import sweep.IntersectionFinder;

import algorithm.PolygonNode;
import algorithm.WeilerAtherton;

public class Test {

	public static void main(String[] args) {
		ArrayList<Point2D> s = new ArrayList<Point2D>();
		/*s.add(new Point2D.Double(1,9));
		s.add(new Point2D.Double(3,9));
		s.add(new Point2D.Double(7,7));
		s.add(new Point2D.Double(7,2));
		s.add(new Point2D.Double(6,1));
		s.add(new Point2D.Double(4,2));
		s.add(new Point2D.Double(2,1));
		s.add(new Point2D.Double(0,2));
		s.add(new Point2D.Double(1,5));
		*/
		s.add(new Point2D.Double(0,0));
		s.add(new Point2D.Double(0,2));
		s.add(new Point2D.Double(2,0));
		ArrayList<Point2D> c = new ArrayList<Point2D>();
		/*c.add(new Point2D.Double(1,9));
		c.add(new Point2D.Double(5,9));
		c.add(new Point2D.Double(4,7));
		c.add(new Point2D.Double(8,4));
		c.add(new Point2D.Double(5,2));
		c.add(new Point2D.Double(2,1));
		c.add(new Point2D.Double(1,3));
		*/
		c.add(new Point2D.Double(-0.5,-0.5));
		c.add(new Point2D.Double(-0.5,0.5));
		c.add(new Point2D.Double(0.5,0.5));
		c.add(new Point2D.Double(0.5,-0.5));
		AlgorithmOutput output = WeilerAtherton.getInstance().doAlgorithms(c, s);
		System.out.println(output);
	}

}
