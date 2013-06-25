package de.unihalle.sim.main;

import java.awt.Color;
import java.awt.Graphics;
import java.util.List;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import com.google.common.collect.Maps;

import de.unihalle.sim.entities.Bee;
import de.unihalle.sim.entities.BeeHive;
import de.unihalle.sim.entities.Flower;

public class VisualisationCanvas extends JFrame {

	private class Sheet extends JPanel {

		private static final long serialVersionUID = 1L;
		private Environment _temporaryEnvironment;
		private Map<String, Color> _hiveMap = Maps.newHashMap();
		private Color[] _colorArray = new Color[4];
		private int _currentColor;

		public Sheet(Environment drawingEnvironment) {
			_temporaryEnvironment = drawingEnvironment;

			_colorArray[0] = new Color(255, 159, 0);
			_colorArray[1] = new Color(255, 255, 0);
			_colorArray[2] = new Color(255, 255, 255);
			_colorArray[3] = new Color(255, 118, 71);

			_currentColor = 0;

			List<BeeHive> beeHiveList = _temporaryEnvironment.getBeeHives();
			for (BeeHive f : beeHiveList) {
				if (_currentColor == 4) {
					_currentColor = 0;
				}

				_hiveMap.put(f.getName(), _colorArray[_currentColor]);
				_currentColor++;
			}
		}

		private void drawArrow(Graphics g, int x1, int y1, int x2, int y2) {
			if (x1 == x2 && y1 == y2) {

			} else {
				g.drawLine(x1, y1, x2, y2);

				double directionCorrection = 1;
				if (x2 > x1) {
					directionCorrection = -1;
				}
				double alpha = Math.atan((double) (y2 - y1) / (double) (x2 - x1));
				double theta1 = alpha + Math.PI / 8d;
				double theta2 = alpha - Math.PI / 8d;
				int r = (int) Math.ceil(0.5 * _fieldScaleFactor);
				double linie1x = x2 + (r * Math.cos(theta1)) * directionCorrection;
				double linie1y = y2 + (r * Math.sin(theta1)) * directionCorrection;
				double linie2x = x2 + (r * Math.cos(theta2)) * directionCorrection;
				double linie2y = y2 + (r * Math.sin(theta2)) * directionCorrection;
				g.drawLine(x2, y2, (int) linie1x, (int) linie1y);
				g.drawLine(x2, y2, (int) linie2x, (int) linie2y);
			}
		}

		@Override
		public void paintComponent(Graphics g) {

			List<BeeHive> beeHiveList = _temporaryEnvironment.getBeeHives();
			for (BeeHive f : beeHiveList) {
				g.setColor(new Color(255, 0, 0));
				g.fillOval(
						(_fieldSizeX / 2) * _fieldScaleFactor + (((f.getPosition().x * _fieldScaleFactor) - 5) + 15),
						(_fieldSizeY / 2) * _fieldScaleFactor + (((f.getPosition().y * _fieldScaleFactor) - 5) + 15),
						10, 10);

			}

			List<Flower> flowerList = _temporaryEnvironment.getFlowers();
			for (Flower f : flowerList) {
				g.setColor(new Color(0, 255, 0));
				g.drawRect(
						(_fieldSizeX / 2) * _fieldScaleFactor + (((f.getPosition().x * _fieldScaleFactor) - 2) + 15),
						(_fieldSizeY / 2) * _fieldScaleFactor + (((f.getPosition().y * _fieldScaleFactor) - 2) + 15),
						5, 5);
			}

			List<Bee> beeList = _temporaryEnvironment.getBees();
			for (Bee f : beeList) {
				g.setColor(_hiveMap.get(f.getHomeName()));

				if (f.isMoving()) {
					drawArrow(g, (_fieldSizeX / 2 * _fieldScaleFactor) + ((f.getPosition().x * _fieldScaleFactor) - 1)
							+ 15, (_fieldSizeY / 2 * _fieldScaleFactor) + ((f.getPosition().y * _fieldScaleFactor) - 1)
							+ 15, (_fieldSizeX / 2 * _fieldScaleFactor)
							+ ((f.getDestination().x * _fieldScaleFactor) - 1) + 15,
							(_fieldSizeY / 2 * _fieldScaleFactor) + ((f.getDestination().y * _fieldScaleFactor) - 1)
									+ 15);
				} else {
					g.fillRect((_fieldSizeX / 2) * _fieldScaleFactor
							+ ((((f.getPosition().x) * _fieldScaleFactor) - 1) + 15), (_fieldSizeY / 2)
							* _fieldScaleFactor + ((((f.getPosition().y) * _fieldScaleFactor) - 1) + 15), 4, 4);
				}
			}

		}
	}

	private int _fieldSizeX;
	private int _fieldSizeY;
	private final int _fieldScaleFactor = 10;
	private Sheet _drawitSheet;

	private static final long serialVersionUID = 1L;

	public VisualisationCanvas(Environment env) {
		_fieldSizeX = Math.abs(env.getMaxX()) + Math.abs(env.getMinX());
		_fieldSizeY = Math.abs(env.getMaxY()) + Math.abs(env.getMinY());
		this.setBackground(new Color(0, 0, 0));
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setSize((_fieldSizeX * _fieldScaleFactor) + 40, (_fieldSizeY * _fieldScaleFactor) + 60);
		setLocationRelativeTo(null);
		setTitle("BeeHive Simulation");
		setAutoRequestFocus(false);

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
}
