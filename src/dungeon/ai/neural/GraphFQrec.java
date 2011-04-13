package dungeon.ai.neural;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;

import javax.swing.JFrame;
import javax.swing.JPanel;

import dungeon.ai.ai_code.FQ_learner;

public class GraphFQrec extends JPanel {
	public static Graphics2D g2;
	static double[][] qtable;
	double action;

	public void initGraphFQ(double[][] q_in) {
		
		qtable= q_in;
}

	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);

		int horizSize = 100;
		int index = 0;
		double energy, distance;
		float v,vAttack,vEvade;
		int boxsize = 5;
		for (int y = 0; y < horizSize; y++) {
			for (int x = 0; x < horizSize; x++) {
		
				energy = (double) x / (double) horizSize;// x axis is my
				// energy
				distance = (double) y / (double) horizSize;// y axis is the
				// distance

				 vAttack=(float) FQ_learner.nearestValue(energy,distance,0);
				 vEvade=(float) FQ_learner.nearestValue(energy,distance,1);
				if(vAttack>vEvade)
				v = 0.0f; else v=1.0f;
				g2.setPaint(new Color(v, v, v, 1));
				g2.fill(new Rectangle2D.Double(x * boxsize, y * boxsize,
						boxsize, boxsize));
				index++;
			}
		}
//		System.out.println("painted");
	}
}
