package de.unihalle.sim.main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import com.google.common.collect.Maps;

import de.unihalle.sim.entities.Bee;
import de.unihalle.sim.entities.BeeHive;
import de.unihalle.sim.entities.Flower;

public class VisualisationCanvas extends JFrame {

	static int iFieldSizeX = BeeSimulation.getEnvironment().getMaxX();
	static int iFieldSizeY = BeeSimulation.getEnvironment().getMaxY();
	static final int iFieldScaleFactor = 10;
	static Environment temporaryEnv;
	private JButton startButton;
	private JButton pauseButton;
	private JButton stopButton;
	private ButtonActionListener buttonActionListener = new ButtonActionListener();

	private static final long serialVersionUID = 1L;

	public VisualisationCanvas() {
		this.setBackground(new Color(0, 0, 0));
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setSize((iFieldSizeX * iFieldScaleFactor * 2) + 50, (iFieldSizeX
				* iFieldScaleFactor * 2) + 50);
		setLocationRelativeTo(null);
		setTitle("BeeHive Simulation");
	}

	public void drawit(Environment env) {

		temporaryEnv = env;
		Sheet sh = new Sheet(temporaryEnv);
		sh.removeAll();
		sh.updateUI();
		drawButtons(sh);
		add(sh);
		repaint();
		setVisible(true);
	}

	public void drawButtons(Sheet sheet) {
		startButton = new JButton();
		pauseButton = new JButton();
		stopButton = new JButton();
		startButton.setText("Start");
		pauseButton.setText("Pause");
		stopButton.setText("Stop");

		startButton.setPreferredSize(new Dimension(100, 20));
		pauseButton.setPreferredSize(new Dimension(100, 20));
		stopButton.setPreferredSize(new Dimension(100, 20));

		startButton.addActionListener(buttonActionListener);
		pauseButton.addActionListener(buttonActionListener);
		stopButton.addActionListener(buttonActionListener);

		sheet.add(startButton);
		sheet.add(pauseButton);
		sheet.add(stopButton);
	}

	class ButtonActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == startButton) {
				System.out.println("Start");
			}

			if (e.getSource() == pauseButton) {
				System.out.println("Pause");
			}

			if (e.getSource() == stopButton) {
				System.out.println("Stop");
			}
		}
	}
}

class Sheet extends JPanel {

	private static final long serialVersionUID = 1L;
	static Environment temporaryEnv;
	static Map<String, Color> hiveMap = Maps.newHashMap();
	static Color[] colorArray = new Color[4];
	static int currentColor = 0;

	public Sheet(Environment drawingEnvironment) {
		temporaryEnv = drawingEnvironment;

		colorArray[0] = new Color(255, 159, 0);
		colorArray[1] = new Color(255, 255, 0);
		colorArray[2] = new Color(255, 255, 255);
		colorArray[3] = new Color(255, 118, 71);

		currentColor = 0;
	}

	private void drawArrow(Graphics g, int x1, int y1, int x2, int y2) {
		g.drawLine(x1, y1, x2, y2);
		double directionCorrection = 1;
		if (x2 > x1) {
			directionCorrection = -1;
		}
		double alpha = Math.atan((double) (y2 - y1) / (double) (x2 - x1));
		double theta1 = alpha + Math.PI / 8d;
		double theta2 = alpha - Math.PI / 8d;
		int r = (int) Math.ceil(0.5 * VisualisationCanvas.iFieldScaleFactor);
		double linie1x = x2 + (r * Math.cos(theta1)) * directionCorrection;
		double linie1y = y2 + (r * Math.sin(theta1)) * directionCorrection;
		double linie2x = x2 + (r * Math.cos(theta2)) * directionCorrection;
		double linie2y = y2 + (r * Math.sin(theta2)) * directionCorrection;
		g.drawLine(x2, y2, (int) linie1x, (int) linie1y);
		g.drawLine(x2, y2, (int) linie2x, (int) linie2y);
	}

	@Override
	public void paintComponent(Graphics g) {

		List<BeeHive> beeHiveList = temporaryEnv.getBeeHives();
		for (BeeHive f : beeHiveList) {
			if (currentColor == 4)
				currentColor = 0;

			hiveMap.put(f.getName(), colorArray[currentColor]);
			currentColor++;

			g.setColor(new Color(255, 0, 0));
			g.fillOval(
					(VisualisationCanvas.iFieldSizeX)
							* VisualisationCanvas.iFieldScaleFactor
							+ (((f.getPosition().x * VisualisationCanvas.iFieldScaleFactor) - 5) + 25),
					(VisualisationCanvas.iFieldSizeY)
							* VisualisationCanvas.iFieldScaleFactor
							+ (((f.getPosition().y * VisualisationCanvas.iFieldScaleFactor) - 5) + 25),
					10, 10);

		}

		List<Flower> flowerList = temporaryEnv.getFlowers();
		for (Flower f : flowerList) {
			g.setColor(new Color(0, 255, 0));
			g.drawRect(
					(VisualisationCanvas.iFieldSizeX)
							* VisualisationCanvas.iFieldScaleFactor
							+ ((((f.getPosition().x) * VisualisationCanvas.iFieldScaleFactor) - 2) + 25),
					(VisualisationCanvas.iFieldSizeY)
							* VisualisationCanvas.iFieldScaleFactor
							+ ((((f.getPosition().y) * VisualisationCanvas.iFieldScaleFactor) - 2) + 25),
					5, 5);
		}

		List<Bee> beeList = temporaryEnv.getBees();
		for (Bee f : beeList) {

			g.setColor(hiveMap.get(f.getHomeName()));

			if (f.isMoving() == true) {
				drawArrow(
						g,
						(VisualisationCanvas.iFieldSizeX * VisualisationCanvas.iFieldScaleFactor)
								+ ((f.getPosition().x * VisualisationCanvas.iFieldScaleFactor) - 1)
								+ 25,
						(VisualisationCanvas.iFieldSizeX * VisualisationCanvas.iFieldScaleFactor)
								+ ((f.getPosition().y * VisualisationCanvas.iFieldScaleFactor) - 1)
								+ 25,
						(VisualisationCanvas.iFieldSizeX * VisualisationCanvas.iFieldScaleFactor)
								+ ((f.getDestination().x * VisualisationCanvas.iFieldScaleFactor) - 1)
								+ 25,
						(VisualisationCanvas.iFieldSizeX * VisualisationCanvas.iFieldScaleFactor)
								+ ((f.getDestination().y * VisualisationCanvas.iFieldScaleFactor) - 1)
								+ 25);
			} else {

				g.fillRect(
						(VisualisationCanvas.iFieldSizeX)
								* VisualisationCanvas.iFieldScaleFactor
								+ ((((f.getPosition().x) * VisualisationCanvas.iFieldScaleFactor) - 1) + 25),
						(VisualisationCanvas.iFieldSizeY)
								* VisualisationCanvas.iFieldScaleFactor
								+ ((((f.getPosition().y) * VisualisationCanvas.iFieldScaleFactor) - 1) + 25),
						4, 4);

			}
		}
	}
}
