package dungeon.ai.neural;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;

import dungeon.ai.ai_code.FQ_learner;
import dungeon.ai.ai_code.Transition;

public class GraphFQ extends JPanel {
	public static Graphics2D g2;
	static double[][] qtable;
	double action;

	public void initGraphFQ(double[][] q_in, double action_in) {
		
			qtable= q_in;
		action = action_in;
	}

	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);

		double energy, distance,scaledVal;

		int horizSize = 100;
		int index = 0;
		float v;
		int boxsize = 5;
		for (int y = 0; y < horizSize; y++) {
			for (int x = 0; x < horizSize; x++) {

				energy = (double) x / (double) horizSize;// x axis is my
															// energy
				distance = (double) y / (double) horizSize;// y axis is the
															// distance
				scaledVal=(10.0+FQ_learner.nearestValue(energy,distance,action))/20.0;
				v =  (float) scaledVal;
				if(v<0)v=0;
//				System.out.println(v);
				g2.setPaint(new Color(v, v, v, 1));
				g2.fill(new Rectangle2D.Double(x * boxsize, y * boxsize,
						boxsize, boxsize));
				index++;
			}
		}
//		System.out.println("painted");
	}
}
