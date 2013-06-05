package de.unihalle.sim.main;

import java.awt.Color;
import java.awt.Graphics;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import de.unihalle.sim.entities.Bee;
import de.unihalle.sim.entities.BeeHive;
import de.unihalle.sim.entities.Flower;

public class VisualisationCanvas extends JFrame {

	static int iFieldSizeX = BeeSimulation.getEnvironment().getMaxX();
	static int iFieldSizeY = BeeSimulation.getEnvironment().getMaxY();
	static final int iFieldScaleFactor = 10;

	private static final long serialVersionUID = 1L;

	public VisualisationCanvas() {
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setSize((iFieldSizeX * iFieldScaleFactor * 2) + 50, (iFieldSizeX * iFieldScaleFactor * 2) + 50);
		setLocationRelativeTo(null);
	}

	public void drawit() {

		Sheet sh = new Sheet();
		sh.removeAll();
		sh.updateUI();
		add(sh);
		repaint();
		setVisible(true);
	}

	public void showit() {
		setVisible(true);
	}

}

class Sheet extends JPanel {

	private static final long serialVersionUID = 1L;

	/*
	 * static Position pAktuell = Position.createFromCoordinates(0, 0); static
	 * Position pAltzumUeberschreiben = Position.createFromCoordinates(0, 0);
	 * String className = ""; static int beeValue = 0; static int beeValueOld =
	 * 0;
	 */

	/*
	 * public Sheet(int x, int y, String clName) { className = clName;
	 * pAktuell.x = x; pAktuell.y = y; }
	 */

	private void drawArrow(Graphics g, int x1, int y1, int x2, int y2) {
		g.drawLine(x1, y1, x2, y2);
		double directionCorrection = 1;
		if (x2 > x1) {
			directionCorrection = -1;
		}
		double alpha = Math.atan((double) (y2 - y1) / (double) (x2 - x1));
		double theta1 = alpha + Math.PI / 8d;
		double theta2 = alpha - Math.PI / 8d;
		int r = (int) Math.ceil(1 * VisualisationCanvas.iFieldScaleFactor);
		double linie1x = x2 + (r * Math.cos(theta1)) * directionCorrection;
		double linie1y = y2 + (r * Math.sin(theta1)) * directionCorrection;
		double linie2x = x2 + (r * Math.cos(theta2)) * directionCorrection;
		double linie2y = y2 + (r * Math.sin(theta2)) * directionCorrection;
		g.drawLine(x2, y2, (int) linie1x, (int) linie1y);
		g.drawLine(x2, y2, (int) linie2x, (int) linie2y);
	}

	@Override
	public void paintComponent(Graphics g) {

		List<BeeHive> beeHiveList = BeeSimulation.getEnvironment().getBeeHives();
		for (BeeHive f : beeHiveList) {
			g.setColor(new Color(255, 0, 0));
			g.drawRect((VisualisationCanvas.iFieldSizeX) * VisualisationCanvas.iFieldScaleFactor
					+ (((f.getPosition().x * VisualisationCanvas.iFieldScaleFactor) - 5) + 25),
					(VisualisationCanvas.iFieldSizeY) * VisualisationCanvas.iFieldScaleFactor
							+ (((f.getPosition().y * VisualisationCanvas.iFieldScaleFactor) - 5) + 25), 10, 10);

		}

		List<Flower> flowerList = BeeSimulation.getEnvironment().getFlowers();
		for (Flower f : flowerList) {
			g.setColor(new Color(0, 0, 255));
			g.drawRect((VisualisationCanvas.iFieldSizeX) * VisualisationCanvas.iFieldScaleFactor
					+ ((((f.getPosition().x) * VisualisationCanvas.iFieldScaleFactor) - 2) + 25),
					(VisualisationCanvas.iFieldSizeY) * VisualisationCanvas.iFieldScaleFactor
							+ ((((f.getPosition().y) * VisualisationCanvas.iFieldScaleFactor) - 2) + 25), 5, 5);
		}

		List<Bee> beeList = BeeSimulation.getEnvironment().getBees();
		for (Bee f : beeList) {

			g.setColor(new Color(0, 255, 0));
			if (f.isMoving() == true) {
				drawArrow(
						g,
						(VisualisationCanvas.iFieldSizeX * VisualisationCanvas.iFieldScaleFactor)
								+ ((f.getPosition().x * VisualisationCanvas.iFieldScaleFactor) - 1) + 25,
						(VisualisationCanvas.iFieldSizeX * VisualisationCanvas.iFieldScaleFactor)
								+ ((f.getPosition().y * VisualisationCanvas.iFieldScaleFactor) - 1) + 25,
						(VisualisationCanvas.iFieldSizeX * VisualisationCanvas.iFieldScaleFactor)
								+ ((f.getDestination().x * VisualisationCanvas.iFieldScaleFactor) - 1) + 25,
						(VisualisationCanvas.iFieldSizeX * VisualisationCanvas.iFieldScaleFactor)
								+ ((f.getDestination().y * VisualisationCanvas.iFieldScaleFactor) - 1) + 25);
			} else {

				g.fillRect(
						(VisualisationCanvas.iFieldSizeX) * VisualisationCanvas.iFieldScaleFactor
								+ ((((f.getPosition().x) * VisualisationCanvas.iFieldScaleFactor) - 1) + 25),
						(VisualisationCanvas.iFieldSizeY) * VisualisationCanvas.iFieldScaleFactor
								+ ((((f.getPosition().y) * VisualisationCanvas.iFieldScaleFactor) - 1) + 25), 4, 4);

			}
		}

		// repaint();

		/*
		 * if (className.contains("Bee") == true) { g.setColor(new Color(255, 0,
		 * 0)); g.drawRect(250 + (pAktuell.x * 20), 250 + (pAktuell.y * 20), 3,
		 * 3);
		 * 
		 * // Hilfsmethode um Zahl zu extrahieren... Pattern p =
		 * Pattern.compile("[0-9]+"); Matcher m = p.matcher(className); while
		 * (m.find()) { beeValue =
		 * Integer.parseInt(className.substring(m.start(), m.end())); } } if
		 * (className.contains("Flower") == true) { g.setColor(new Color(0, 0,
		 * 255)); g.drawRect(250 + (pAktuell.x * 20), 250 + (pAktuell.y * 20),
		 * 5, 5); }
		 * 
		 * if ((pAktuell.x != pAltzumUeberschreiben.x || pAktuell.y !=
		 * pAltzumUeberschreiben.y) && beeValueOld == beeValue) { g.setColor(new
		 * Color(255, 255, 255)); g.drawRect(250 + (pAltzumUeberschreiben.x *
		 * 20), 250 + (pAltzumUeberschreiben.y * 20), 3, 3); g.setColor(new
		 * Color(0, 0, 255)); g.drawRect(250 + (pAltzumUeberschreiben.x * 20),
		 * 250 + (pAltzumUeberschreiben.y * 20), 5, 5);
		 * 
		 * }
		 * 
		 * pAltzumUeberschreiben.x = pAktuell.x; pAltzumUeberschreiben.y =
		 * pAktuell.y; beeValueOld = beeValue;
		 */
	}
}
