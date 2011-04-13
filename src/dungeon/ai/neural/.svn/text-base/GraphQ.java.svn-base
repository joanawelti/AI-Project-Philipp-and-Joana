package dungeon.ai.neural;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;

import dungeon.ai.ai_code.Transition;

public class GraphQ extends JPanel {
	public static Graphics2D g2;
	static double[][] qtable;
	double action;
	ArrayList<Transition> transitions;

	public void initGraphQ(double[][] q_in, ArrayList<Transition> tList) {
		qtable= q_in;
		transitions=tList;
	}

	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		int boxsize = 5;		int horizSize = 100;int x,y;float v;
		for (int index = 0; index < transitions.size(); index++) {
			Transition t = transitions.get(index);
			x = (int) (t.getCurrentState().getOgreEnergy() * 100.0);
			y = (int) (t.getCurrentState().getEnemyDistance() * 100.0);
			v = (float) ((qtable[index][0]+10.0)/20.0);
			g2.setPaint(new Color(0, v, 0, 1));
			g2.fill(new Rectangle2D.Double(x * boxsize, y * boxsize, boxsize,
					boxsize));
		}




//		System.out.println("painted");
	}
}
