package gui;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;


public class MainWindow extends JFrame implements MouseMotionListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3880026026104218593L;
	private JLabel coordinateLabel;
	
	public MainWindow() {
		super("Geometrijski algoritmi - Presjek dva prosta poligona");
		
		Toolkit kit = Toolkit.getDefaultToolkit();
		
		int locY = kit.getScreenSize().height/3, locX = kit.getScreenSize().width/4;
		
		
		
		ButtonGroup polygonGroup = new ButtonGroup();
		
		JRadioButton firstButton = new JRadioButton("Prvi mnogougao");
		firstButton.setSelected(true);
		JRadioButton secondButton = new JRadioButton("Drugi mnogougao");
		
		polygonGroup.add(firstButton);
		polygonGroup.add(secondButton);
		
		
		JPanel polygonPanel = new JPanel();
		
		JButton nextStepButton = new JButton("Next");
		
		
		polygonPanel.setLayout(new FlowLayout());
		coordinateLabel = new JLabel("");
		
		polygonPanel.add(coordinateLabel);
		polygonPanel.add(firstButton);
		polygonPanel.add(secondButton);
		polygonPanel.add(nextStepButton);
		
		Framework framework = new Framework(firstButton);
		add(polygonPanel, BorderLayout.SOUTH);
		add(framework, BorderLayout.CENTER);
		
		nextStepButton.addActionListener(framework);
		framework.addMouseMotionListener(this);
		
		setLocation(locX, locY);
		setSize(800, 600);
		
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}


	@Override
	public void mouseDragged(MouseEvent e) {
	}


	@Override
	public void mouseMoved(MouseEvent e) {
		coordinateLabel.setText(e.getPoint().x + " , " + e.getPoint().y);
	}

}
