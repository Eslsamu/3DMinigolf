package model;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import javax.imageio.ImageIO;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class Surface {

	private static final Color[] colorArray = new Color[] {Color.GREEN,Color.GREENYELLOW,Color.GREEN,Color.DARKGREEN, Color.SADDLEBROWN};
	public static final int INTERVAL_FRACTION = 50;
	public final Float[][] A = new Float[][]
					{{1f, 0f, 0f, 0f},
					{0f, 0f, 1f, 0f},
					{-3f, 3f, -2f, -1f},
					{2f, -2f, 1f, 1f}};
	public final Float[][] B = new Float[][]
					{{1f, 0f, -3f, 2f},
					{0f, 0f, 3f, -2f},
					{0f, 1f, -2f, 1f},
					{0f, 0f, -1f, 1f}};
	private Float[][][] a, aFriction;
	private int sideLengthX, sideLengthY;
	private float width, length;
	private float squareSize;
	private Float[][] points;
	private Float[][] friction;
	private Image texture;

	public Surface(Float[][] points, Float[][] friction, float width, float length) {
		this.points = checkNulls(points);
		this.friction = checkNulls(friction);
		this.width = width;
		this.length = length;
		System.out.println("Points after removing null rows and columns: " + Arrays.deepToString(this.points));
		a = interpolate(this.points);
		aFriction = interpolate(this.friction);
		createTexture();
	}
	
	private Float[][] checkNulls(Float[][] array) {
		for (int i = 0; i < array.length; i++) {
			int numValues = 0;
			for (int j = 0; j < array[0].length; j++) {
				if (((i == 0 || array[i - 1][j] == null) && (i + 1 == array.length || array[i + 1][j] == null))
					|| ((j == 0 || array[i][j - 1] == null) && (j + 1 == array[0].length || array[i][j + 1] == null)))
					array[i][j] = null;
				if (array[i][j] != null) {
					numValues++;
				}
			}
			if (numValues < 2) {
				array = refactorArray(array, i, -1);
				return checkNulls(array);
			}
		}
		
		for (int i = 0; i < array[0].length; i++) {
			int numValues = 0;
			for (int j = 0; j < array.length; j++) {
				if (array[j][i] != null) {
					numValues++;
				}
			}
			if (numValues < 2) {
				array = refactorArray(array, -1, i);
				return checkNulls(array);
			}
		}
		return array;
	}
	
	private Float[][] refactorArray(Float[][] array, int removeRow, int removeCol) {
		Float[][] copy = new Float[array.length - (removeRow == -1 ? 0 : 1)][array[0].length - (removeCol == -1 ? 0 : 1)];
		int realI = 0, realJ = 0;
		for (int i = 0; i < array.length; i++) {
			if (i != removeRow) {
				for (int j = 0; j < array[0].length; j++) {
					if (j != removeCol) {
						copy[realI][realJ] = array[i][j];
						realJ++;
					}
				}
				realI++;
				realJ = 0;
			}
		}
		return copy;
	}
	
	private void createTexture() {
		BufferedImage bImage = new BufferedImage((int)length, (int)width, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = bImage.createGraphics();
		for (int x = 0; x <= length; x++) {
			for (int y = 0; y <= width; y++) {
				Float friction = getFriction(x, y);
				if (friction != null) {
					Color c = colorArray[(int)(friction*5)];
					g2d.setColor(new java.awt.Color((int)(c.getRed() * 255), (int)(c.getGreen() * 255), (int)(c.getBlue() * 255)));
					g2d.fillRect(x, y, 1, 1);					
				}
			}
		}
		try {
			ImageIO.write(bImage, "jpg", new File("src/files/pictures/level.jpg"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		texture = SwingFXUtils.toFXImage(bImage, null);
		
	}
	
	private Float[][][] interpolate(Float[][] points) {
		sideLengthX = points[0].length;
		sideLengthY = points.length;
		squareSize = 1f / (sideLengthX - 1);
		int squareTotal = (sideLengthX - 1)*(sideLengthY - 1);
		Float[][][] f = new Float[squareTotal][2][2];
		Float[][][] fx = new Float[squareTotal][2][2];
		Float[][][] fy = new Float[squareTotal][2][2];
		Float[][][] fxy = new Float[squareTotal][2][2];

		Float[][] tempfX = new Float[sideLengthY][sideLengthX];
		Float[][] tempfY = new Float[sideLengthY][sideLengthX];
		for (int i = 0; i < points.length; i++) {
			for (int j = 0; j < points[0].length; j++) {
				if (points[i][j] == null) {
					tempfX[i][j] = null;
					tempfY[i][j] = null;
					continue;
				}
				if (i == 0 || points[i - 1][j] == null) {
					tempfY[i][j] = points[i + 1][j] - points[i][j];
				} else if (i + 1 == points.length || points[i + 1][j] == null) {
					tempfY[i][j] = points[i][j] - points[i - 1][j];
				} else {
					tempfY[i][j] = (points[i + 1][j] - points[i - 1][j])/2;
				}
				if (j == 0 || points[i][j - 1] == null) {
					tempfX[i][j] = points[i][j + 1] - points[i][j];
				} else if (j + 1 == points[0].length || points[i][j + 1] == null) {
					tempfX[i][j] = points[i][j] - points[i][j - 1];
				} else {
					tempfX[i][j] = (points[i][j + 1] - points[i][j - 1])/2;
				}
			}
		}

		Float[][] tempfXY = new Float[sideLengthY][sideLengthX];
		for (int i = 0; i < tempfY.length; i++) {
			for (int j = 0; j < tempfY[0].length; j++) {
				if (tempfY[i][j] == null) {
					tempfXY[i][j] = null;
					continue;
				}
				if (j == 0 || tempfXY[i][j - 1] == null) {
					tempfXY[i][j] = tempfY[i][j + 1] - tempfY[i][j];
				} else if (j + 1 == tempfY[0].length || tempfY[i][j + 1] == null) {
					tempfXY[i][j] = tempfY[i][j] - tempfY[i][j - 1];
				} else {
					tempfXY[i][j] = (tempfY[i][j + 1] - tempfY[i][j - 1])/2;
				}
			}
		}


		for (int i = 0; i < f.length; i++) {
			for (int j = 0; j < f[0].length; j++) {
				for (int k = 0; k < f[0][0].length; k++) {
					f[i][j][k] = points[j + i / (sideLengthX - 1)][k + i % (sideLengthX - 1)];
					fx[i][j][k] = tempfX[j + i / (sideLengthX - 1)][k + i % (sideLengthX - 1)];
					fy[i][j][k] = tempfY[j + i / (sideLengthX - 1)][k + i % (sideLengthX - 1)];
					fxy[i][j][k] = tempfXY[j + i / (sideLengthX - 1)][k + i % (sideLengthX - 1)];
				}
			}
		}

		Float[][][] a = new Float[squareTotal][4][4];
		for (int i = 0; i < a.length; i++) {
			Float[][] fs = new Float[][] {{f[i][0][0], f[i][0][1], fy[i][0][0], fy[i][0][1]},
					{f[i][1][0], f[i][1][1], fy[i][1][0], fy[i][1][1]},
					{fx[i][0][0], fx[i][0][1], fxy[i][0][0], fxy[i][0][1]},
					{fx[i][1][0], fx[i][1][1], fxy[i][1][0], fxy[i][1][1]}};
			a[i] = transpose(matrixMultiplication(A, matrixMultiplication(fs, B)));
		}
		return a;
	}
	
	public Float getFriction(float x, float y) {
		x/=length;
		y/=width;
		float sum = 0;
		int currentSquare = 0;
		y *= (sideLengthY - 1f)/(sideLengthX - 1f);
		for (float dx = squareSize; dx <= squareSize * points[0].length; dx+= squareSize) {
			if (x <= dx) {
				for (float dy = squareSize; dy <= (sideLengthY - 1f)/(sideLengthX - 1f); dy+= squareSize) {
					if (y <= dy) {
						if (a[currentSquare] == null)
							return null;
						for (int i = 0; i < aFriction[currentSquare].length; i++) {
							for (int j = 0; j < aFriction[currentSquare][0].length; j++) {
								sum+= aFriction[currentSquare][i][j]*Math.pow((x - (dx - squareSize)) / squareSize, i)*Math.pow((y - (dy - squareSize)) / squareSize, j);
							}
						}
						return sum;
					}
					currentSquare+= sideLengthX - 1;
				}
			}
			currentSquare++;
		}
		return null;
	}

	public Float interpolate(float x, float y) {
		x/=length;
		y/=width;
		float sum = 0;
		int currentSquare = 0;
		y *= (sideLengthY - 1f)/(sideLengthX - 1f);
		for (float dx = squareSize; dx <= squareSize * points[0].length; dx+= squareSize) {
			if (x <= dx) {
				for (float dy = squareSize; dy <= (sideLengthY - 1f)/(sideLengthX - 1f); dy+= squareSize) {
					if (y <= dy) {
						if (a[currentSquare] == null)
							return null;
						for (int i = 0; i < a[currentSquare].length; i++) {
							for (int j = 0; j < a[currentSquare][0].length; j++) {
								sum+= a[currentSquare][i][j]*Math.pow((x - (dx - squareSize)) / squareSize, i)*Math.pow((y - (dy - squareSize)) / squareSize, j);
							}
						}
						return sum;
					}
					currentSquare+= sideLengthX - 1;
				}
			}
			currentSquare++;
		}
		return null;
	}

	public Float interpolateDX(float x, float y) {
		x/=length;
		y/=width;
		float sum = 0;
		int currentSquare = 0;
		y *= (sideLengthY - 1.0)/(sideLengthX - 1.0);
		for (float dx = squareSize; dx <= 1; dx+= squareSize) {
			if (x <= dx) {
				for (float dy = squareSize; dy <= (sideLengthY - 1.0)/(sideLengthX - 1.0); dy+= squareSize) {
					if (y <= dy) {
						if (a[currentSquare] == null)
							return null;
						for (int i = 1; i < a[currentSquare].length; i++) {
							for (int j = 0; j < a[currentSquare][0].length; j++) {
								sum+= a[currentSquare][i][j]*Math.pow((x - (dx - squareSize)) / squareSize, i - 1)*Math.pow((y - (dy - squareSize)) / squareSize, j);
							}
						}
						return sum;
					}
					currentSquare+= sideLengthX - 1;
				}
			}
			currentSquare++;
		}
		return null;
	}

	public Float interpolateDY(float x, float y) {
		x/=length;
		y/=width;
		float sum = 0;
		int currentSquare = 0;
		y *= (sideLengthY - 1.0)/(sideLengthX - 1.0);
		for (float dx = squareSize; dx <= 1; dx+= squareSize) {
			if (x <= dx) {
				for (float dy = squareSize; dy <= (sideLengthY - 1.0)/(sideLengthX - 1.0); dy+= squareSize) {
					if (y <= dy) {
						if (a[currentSquare] == null)
							return null;
						for (int i = 0; i < a[currentSquare].length; i++) {
							for (int j = 1; j < a[currentSquare][0].length; j++) {
								sum+= a[currentSquare][i][j]*Math.pow((x - (dx - squareSize)) / squareSize, i)*Math.pow((y - (dy - squareSize)) / squareSize, j - 1);
							}
						}
						return sum;
					}
					currentSquare+= sideLengthX - 1;
				}
			}
			currentSquare++;
		}
		return null;
	}

	private static Float[][] matrixMultiplication(Float[][] A, Float[][] B) {
		if (A == null || B == null)
			return null;
		int aRows = A.length;
		int aColumns = A[0].length;
		int bRows = B.length;
		int bColumns = B[0].length;

		if (aColumns != bRows) {
			throw new IllegalArgumentException("A:Rows: " + aColumns + " did not match B:Columns " + bRows + ".");
		}

		Float[][] C = new Float[aRows][bColumns];
		for (int i = 0; i < aRows; i++) {
			for (int j = 0; j < bColumns; j++) {
				C[i][j] = 0f;
			}
		}

		for (int i = 0; i < aRows; i++) { // aRow
			for (int j = 0; j < bColumns; j++) { // bColumn
				for (int k = 0; k < aColumns; k++) { // aColumn
					if (A[i][k] == null || B[k][j] == null)
						return null;
					else
						C[i][j] += A[i][k] * B[k][j];
				}
			}
		}
		return C;
	}

	public static Float[][] transpose(Float[][] m1) {
		if (m1 == null)
			return null;
		if (m1.length == 0)
			return null;
		Float[][] m2 = new Float[m1[0].length][m1.length];
		for (int i = 0; i < m1.length; i++) {
			for (int j = 0; j < m2.length; j++) {
				m2[j][i] = m1[i][j];
			}
		}
		return m2;
	}
	
	public Float[][] getPoints() {
		return points;
	}
	
	public Float[][] getFrictions() {
		return friction;
	}
	
	public float getWidth() {
		return width;
	}
	
	public float getLength() {
		return length;
	}

	public Image getTexture() {
		return texture;
	}
}
