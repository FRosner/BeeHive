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

	@Override
	public void paintComponent(Graphics g) {

		List<BeeHive> beeHiveList = BeeSimulation.getEnvironment().getBeeHives();
		for (int i = 0; i < beeHiveList.size(); i++) {
			g.setColor(new Color(255, 0, 0));
			g.drawRect(
					(VisualisationCanvas.iFieldSizeX) * VisualisationCanvas.iFieldScaleFactor
							+ (((beeHiveList.get(i).getPosition().x * VisualisationCanvas.iFieldScaleFactor) - 5) + 25),
					(VisualisationCanvas.iFieldSizeY) * VisualisationCanvas.iFieldScaleFactor
							+ (((beeHiveList.get(i).getPosition().y * VisualisationCanvas.iFieldScaleFactor) - 5) + 25),
					10, 10);
		}

		List<Flower> flowerList = BeeSimulation.getEnvironment().getFlowers();
		for (int i = 0; i < flowerList.size(); i++) {
			g.setColor(new Color(0, 0, 255));
			g.drawRect(
					(VisualisationCanvas.iFieldSizeX)
							* VisualisationCanvas.iFieldScaleFactor
							+ ((((flowerList.get(i).getPosition().x) * VisualisationCanvas.iFieldScaleFactor) - 2) + 25),
					(VisualisationCanvas.iFieldSizeY)
							* VisualisationCanvas.iFieldScaleFactor
							+ ((((flowerList.get(i).getPosition().y) * VisualisationCanvas.iFieldScaleFactor) - 2) + 25),
					5, 5);
		}

		List<Bee> beeList = BeeSimulation.getEnvironment().getBees();
		for (int i = 0; i < beeList.size(); i++) {
			g.setColor(new Color(0, 255, 0));
			g.fillRect((VisualisationCanvas.iFieldSizeX) * VisualisationCanvas.iFieldScaleFactor
					+ ((((beeList.get(i).getPosition().x) * VisualisationCanvas.iFieldScaleFactor) - 1) + 25),
					(VisualisationCanvas.iFieldSizeY) * VisualisationCanvas.iFieldScaleFactor
							+ ((((beeList.get(i).getPosition().y) * VisualisationCanvas.iFieldScaleFactor) - 1) + 25),
					4, 4);
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
