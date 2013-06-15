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

	private static class Sheet extends JPanel {

		private static final long serialVersionUID = 1L;
		static Environment _temporaryEnvironment;
		static Map<String, Color> _hiveMap = Maps.newHashMap();
		static Color[] _colorArray = new Color[4];
		static int _currentColor = 0;

		public Sheet(Environment drawingEnvironment) {
			_temporaryEnvironment = drawingEnvironment;

			_colorArray[0] = new Color(255, 159, 0);
			_colorArray[1] = new Color(255, 255, 0);
			_colorArray[2] = new Color(255, 255, 255);
			_colorArray[3] = new Color(255, 118, 71);

			_currentColor = 0;
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
			int r = (int) Math.ceil(0.5 * _FieldScaleFactor);
			double linie1x = x2 + (r * Math.cos(theta1)) * directionCorrection;
			double linie1y = y2 + (r * Math.sin(theta1)) * directionCorrection;
			double linie2x = x2 + (r * Math.cos(theta2)) * directionCorrection;
			double linie2y = y2 + (r * Math.sin(theta2)) * directionCorrection;
			g.drawLine(x2, y2, (int) linie1x, (int) linie1y);
			g.drawLine(x2, y2, (int) linie2x, (int) linie2y);
		}

		@Override
		public void paintComponent(Graphics g) {

			List<BeeHive> beeHiveList = _temporaryEnvironment.getBeeHives();
			for (BeeHive f : beeHiveList) {
				if (_currentColor == 4)
					_currentColor = 0;

				_hiveMap.put(f.getName(), _colorArray[_currentColor]);
				_currentColor++;

				g.setColor(new Color(255, 0, 0));
				g.fillOval((_FieldSizeX) * _FieldScaleFactor + (((f.getPosition().x * _FieldScaleFactor) - 5) + 15),
						(_FieldSizeY) * _FieldScaleFactor + (((f.getPosition().y * _FieldScaleFactor) - 5) + 40), 10,
						10);

			}

			List<Flower> flowerList = _temporaryEnvironment.getFlowers();
			for (Flower f : flowerList) {
				g.setColor(new Color(0, 255, 0));
				g.drawRect((_FieldSizeX) * _FieldScaleFactor + ((((f.getPosition().x) * _FieldScaleFactor) - 2) + 15),
						(_FieldSizeY) * _FieldScaleFactor + ((((f.getPosition().y) * _FieldScaleFactor) - 2) + 40), 5,
						5);
			}

			List<Bee> beeList = _temporaryEnvironment.getBees();
			for (Bee f : beeList) {

				g.setColor(_hiveMap.get(f.getHomeName()));

				if (f.isMoving() == true) {
					drawArrow(g,
							(_FieldSizeX * _FieldScaleFactor) + ((f.getPosition().x * _FieldScaleFactor) - 1) + 15,
							(_FieldSizeX * _FieldScaleFactor) + ((f.getPosition().y * _FieldScaleFactor) - 1) + 40,
							(_FieldSizeX * _FieldScaleFactor) + ((f.getDestination().x * _FieldScaleFactor) - 1) + 15,
							(_FieldSizeX * _FieldScaleFactor) + ((f.getDestination().y * _FieldScaleFactor) - 1) + 40);
				} else {

					g.fillRect((_FieldSizeX) * _FieldScaleFactor
							+ ((((f.getPosition().x) * _FieldScaleFactor) - 1) + 15), (_FieldSizeY) * _FieldScaleFactor
							+ ((((f.getPosition().y) * _FieldScaleFactor) - 1) + 40), 4, 4);

				}
			}
		}
	}

	static int _FieldSizeX = BeeSimulation.getEnvironment().getMaxX();
	static int _FieldSizeY = BeeSimulation.getEnvironment().getMaxY();
	static final int _FieldScaleFactor = 10;
	private JButton _startButton;
	private JButton _pauseButton;
	private JButton _stopButton;
	private ButtonActionListener _actionListener = new ButtonActionListener();
	private Sheet _drawitSheet;
	private Sheet _drawButtonSheet;

	private static final long serialVersionUID = 1L;

	public VisualisationCanvas(Environment env) {
		this.setBackground(new Color(0, 0, 0));
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setSize((_FieldSizeX * _FieldScaleFactor * 2) + 40, (_FieldSizeX * _FieldScaleFactor * 2) + 85);
		setLocationRelativeTo(null);
		setTitle("BeeHive Simulation");
		_drawButtonSheet = new Sheet(env);
		drawButtons(_drawButtonSheet);
		add(_drawButtonSheet);
		setVisible(true);
	}

	public void drawit(Environment env) {

		_drawitSheet = new Sheet(env);
		_drawitSheet.removeAll();
		_drawitSheet.updateUI();

		add(_drawitSheet);
		setVisible(true);
		// _drawitSheet.paintComponent(getGraphics());
		repaint();
	}

	public void drawButtons(Sheet sheet) {
		_startButton = new JButton();
		_pauseButton = new JButton();
		_stopButton = new JButton();
		_startButton.setText("Start");
		_pauseButton.setText("Pause");
		_stopButton.setText("Stop");

		_startButton.setPreferredSize(new Dimension(100, 20));
		_pauseButton.setPreferredSize(new Dimension(100, 20));
		_stopButton.setPreferredSize(new Dimension(100, 20));

		_startButton.addActionListener(_actionListener);
		_pauseButton.addActionListener(_actionListener);
		_stopButton.addActionListener(_actionListener);

		sheet.add(_startButton);
		sheet.add(_pauseButton);
		sheet.add(_stopButton);
	}

	class ButtonActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == _startButton) {
				System.err.println("Start");
			}

			if (e.getSource() == _pauseButton) {
				System.err.println("Pause");
			}

			if (e.getSource() == _stopButton) {
				System.err.println("Stop");
			}
		}
	}
}
