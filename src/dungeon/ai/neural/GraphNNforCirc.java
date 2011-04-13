package dungeon.ai.neural;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import javax.swing.JPanel;

import dungeon.ai.ai_code.Experience;

public class GraphNNforCirc extends JPanel {
	public static Graphics2D g2;
	double action;
	ArrayList<Experience> transitions;

	public void initGraphQ(ArrayList<Experience> tList) {
		transitions=tList;
	}

	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		int boxsize = 5;		int horizSize = 100;int x,y;float v;
//		System.out.println("helo:"+transitions.size());
		for (int index = 0; index < transitions.size(); index++) {
			Experience t = transitions.get(index);
			x = (int) (t.getCurrentState().getOgreEnergy() * 100.0);
			y = (int) (t.getCurrentState().getEnemyDistance() * 100.0);
			
			v = (float) t.getAction();
//			System.out.println("v:"+v);
			if (v<0.5)
				g2.setPaint(new Color(1.0f, 0, 0, 1));
			else
				g2.setPaint(new Color(0, 0, 1.0f, 1));

			g2.fill(new Rectangle2D.Double(x * boxsize, y * boxsize, boxsize,
					boxsize));
		}




//		System.out.println("painted");
	}
}
