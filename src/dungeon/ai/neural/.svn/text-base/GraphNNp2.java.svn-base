package dungeon.ai.neural;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class GraphNNp2 extends JPanel {
	public static Graphics2D g2;
	static Network NN;
	double action;

	public void initGraphNN(Network nn_in, double action_in) {
		NN = nn_in;
		action = action_in;
	}

	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);

		double[] input = new double[2];
		input[0] = 0.01;
		input[1] = 1.0;

		int horizSize = 100;
		int index = 0;
		float v;
		int boxsize = 5;
		for (int y = 0; y < horizSize; y++) {
			for (int x = 0; x < horizSize; x++) {

				input[0] = (double) x / (double) horizSize;// x axis is my
															// energy
				input[1] = (double) y / (double) horizSize;// y axis is the
															// distance
				v = (float) NN.computeOutputs(input)[0];
				if(v<0)v=0;
//				System.out.println(v);
				g2.setPaint(new Color(1-v, 0, v, 1));
				g2.fill(new Rectangle2D.Double(x * boxsize, y * boxsize,
						boxsize, boxsize));
				index++;
			}
		}
//		System.out.println("painted");
	}
}
