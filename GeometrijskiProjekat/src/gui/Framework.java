package gui;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.JRadioButton;

import output.AlgorithmOutput;

import algorithm.WeilerAtherton;


public class Framework extends JPanel implements MouseListener, MouseMotionListener, ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1308621525053772803L;
 
	private PolygonDouble _poly1;
	private PolygonDouble _poly2;
	
	private PolygonDouble _intersectionPoly;
	
	private JRadioButton _firstPolyButton;
	
	private ArrayList<Point2D> _intersectionsIn;
	private ArrayList<Point2D> _intersectionsOut;
	
	public Framework(JRadioButton firstPolyButton){
		super();
		
		
		
		_firstPolyButton = firstPolyButton;
		
		addMouseListener(this);
		addMouseMotionListener(this);
	
	}

	@Override
	protected void paintComponent(Graphics g) {
		Graphics2D g2D = (Graphics2D)g;
		
		g2D.setColor(Color.WHITE);
		g2D.fillRect(0, 0, getWidth(), getHeight());
		
		g2D.setColor(Color.BLUE);
		if(_poly1 != null){
			g2D.draw(_poly1.asPath()); 
			drawVertices(g2D, _poly1, false);
		}
		g2D.setColor(Color.MAGENTA);
		if(_poly2 != null){
			
			g2D.draw(_poly2.asPath());
			drawVertices(g2D, _poly2, true);
		}
		
		g2D.setColor(Color.LIGHT_GRAY);
		
		if(_intersectionPoly != null){
			g2D.draw(_intersectionPoly.asPath());
			g2D.fill(_intersectionPoly.asPath());
		}
		
		drawIntersections(g2D, _intersectionsIn, true);
		drawIntersections(g2D, _intersectionsOut, false);
		
	}



	private void drawIntersections(Graphics2D g2D, ArrayList<Point2D> intersections,
			boolean isIn) {
		if(intersections == null) return;
		int count = 0;
		
		g2D.setColor(isIn ? Color.GREEN : Color.RED);
		for(Point2D point : intersections){
			count++;
			
			g2D.drawOval((int)point.getX() - 5, (int) point.getY() - 5, 10, 10);
			g2D.fillOval((int)point.getX() - 5, (int) point.getY() - 5, 10, 10);
			g2D.drawString("I"+ count, (int)point.getX() - 5, (int)point.getY()+20);
		}
		
	}

	private void drawVertices(Graphics2D g2D, PolygonDouble poly, boolean isSubject) {
		String mark = isSubject ? "S" : "C";
		int radius = isSubject ? 14 : 16;
		int count = 0;
		for(Point2D point : poly.getPointsClockwise()){
			count++;
			g2D.drawOval((int)point.getX() - radius/2, (int)point.getY() - radius/2, radius, radius);
			if(poly.isFinished())
				g2D.drawString(mark + count, (int)point.getX() - 7, (int)point.getY()+20);
		}
	}
	
	private void drawCurrent(Graphics2D g2D, int x, int y) {
		g2D.setColor(Color.BLACK);
		
		g2D.drawOval(x-5, y-5, 10, 10);
		g2D.fillOval(x-5, y-5, 10, 10);
		
		g2D.drawString("Current", x-20, y-10);
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if(_firstPolyButton.isSelected()){
			if(e.getButton() == MouseEvent.BUTTON3){
				_poly1 = null;
			}
			else { 
				if(_poly1 == null || _poly1.isFinished()){
					_poly1 = new PolygonDouble();
					_poly1.addPoint(e.getX(), e.getY());
				}
				checkPoly(_poly1, e.getX(), e.getY());
			}
		}else{
			if(e.getButton() == MouseEvent.BUTTON3){
				_poly2 = null;
			}
			else {
				if(_poly2 == null || _poly2.isFinished()){
					_poly2 = new PolygonDouble();
					_poly2.addPoint(e.getX(), e.getY());
				}
				checkPoly(_poly2, e.getX(), e.getY());
			}
		}
		repaint();
	}

	private void checkPoly(PolygonDouble poly, int x, int y) {
		Point2D.Double current = new Point2D.Double(x, y);
		
		if(poly.selfIntersects(x, y)) return;
		
		if(poly.getPointsCount() > 1 && poly.getFirstPoint().distance(current) < 10 && poly.getPointsCount() > 3){
			poly.finish(x, y);
			if(!_firstPolyButton.isSelected()) _firstPolyButton.setSelected(true);
			return;
		}
	
		poly.addPoint(x, y);
		
	}
	
	

	@Override
	public void mouseMoved(MouseEvent e) {
		PolygonDouble poly = _firstPolyButton.isSelected() ? _poly1 : _poly2;
		if(poly != null && !poly.isFinished() ){
			poly.replaceLast(e.getX(), e.getY());
			repaint();
		}
		
	}

	@Override
	public void mousePressed(MouseEvent e) {}
	
	@Override
	public void mouseDragged(MouseEvent e) { }
	@Override
	public void mouseClicked(MouseEvent e) { }
	@Override
	public void mouseEntered(MouseEvent e) { }
	@Override
	public void mouseExited(MouseEvent e) { }
	
	private AlgorithmOutput _output = null;
	private int currentStep = 0;
	@Override
	public void actionPerformed(ActionEvent e) {
		// pravljenje novog algoritam autputa ili opaljivanje sljedeceg stanja
		
		if(_poly1 == null || _poly2 == null) return;
		
		if(_output == null){
			_output = WeilerAtherton.getInstance().doAlgorithms(_poly2.getPointsClockwise(), _poly1.getPointsClockwise());
			
			_intersectionsIn = _output.getIntersectionsEntering();
			_intersectionsOut = _output.getIntersectionsExiting();
			
			_intersectionPoly = new PolygonDouble();
		}
		
		if(_output.getFinalIntersections().size() > currentStep){
			_intersectionPoly.setPoints(_output.getFinalIntersections().get(currentStep++));
			_intersectionPoly.setFinished(true);
		}else{
			currentStep = 0;
			_intersectionsIn = _intersectionsOut = null;
			_intersectionPoly = null;
			_output = null;
		}
		
		repaint();
	}

	

}
