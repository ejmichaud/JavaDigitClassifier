package networkclasses;

import java.util.Random;

public class Operations {
    public Operations() {}

    public static double sigmoid(double z) {
        return 1.0 / (1.0 + Math.exp(-z));
    }

    public static Matrix sigmoid_matrix(Matrix z) {
        Matrix result = new Matrix(z.rows, z.columns, false);
        for(int i = 0; i < z.matrix.length; i += 1) {
            for(int j = 0; j < z.matrix[i].length; j += 1) {
                result.matrix[i][j] = sigmoid(z.matrix[i][j]);
            }
        }
        return result;
    }

    public static Matrix sigmoid_prime_matrix(Matrix z) {
        Matrix result = new Matrix(z.rows, z.columns, false);
        for(int i = 0; i < z.matrix.length; i += 1) {
            for(int j = 0; j < z.matrix[i].length; j += 1) {
                result.matrix[i][j] = sigmoid(z.matrix[i][j]) * (1.0 - sigmoid(z.matrix[i][j]));
            }
        }
        return result;
    }

    public static Matrix cost_derivative_with_sigmoid(Matrix z, Matrix aL, Matrix y) {
        return aL.subtract(y);
    }

    public static double cost(Matrix aL, Matrix y) {
        double sum = 0;
        for(int i = 0; i < aL.matrix.length; i += 1) {
            sum += (y.matrix[i][0] * Math.log(aL.matrix[i][0])) + ((1 - y.matrix[i][0]) * Math.log(1 - aL.matrix[i][0]));
        }
        return -sum;
    }

    public static Matrix[][] subArray(Matrix[][] array, int start, int len) {
        Matrix[][] out = new Matrix[len][];
        int index = 0;
        for(int i = start; i < start + len; i += 1) {
            out[index] = array[i];
            index += 1;
        }
        return out;
    }

    public static int argmax(Matrix x) {
        int i = 0;
        double high = 0;
        for(int j = 0; j < x.matrix.length; j += 1) {
            if (x.matrix[j][0] > high) {
                i = j;
                high = x.matrix[j][0];
            }
        }
        return i;
    }

    public static Matrix vectorize(int x) {
        Matrix out = new Matrix(10,1,false);
        out.matrix[x][0] = 1.0;
        return out;
    }

    public static Matrix[][] shuffle(Matrix[][] arr) {
        Random gen = new Random();
        Matrix[][] array = arr;
        for(int i = arr.length - 1; i > 0; i -= 1) {
            int index = gen.nextInt(i + 1);
            Matrix[] a = array[index];
            array[index] = array[i];
            array[i] = a;
        }
        return array;
    }

		public static Matrix shiftLeft(Matrix m) {
			Matrix a = new Matrix(28, 28, false);
			for (int i = 0; i < 27; i++) {
				for (int j = 0; j < 27; j++) {
					a.matrix[i][j] = m.matrix[i][j+1];
				}
			}
			return a;
		}

		public static Matrix shiftRight(Matrix m) {
			Matrix a = new Matrix(28, 28, false);
			for (int i = 1; i < 28; i++) {
				for (int j = 1; j < 28; j++) {
					a.matrix[i][j] = m.matrix[i][j-1];
				}
			}
			return a;
		}

		public static Matrix shiftUp(Matrix m) {
			Matrix a = new Matrix(28, 28, false);
			for (int i = 0; i < 27; i++) {
				for (int j = 0; j < 27; j++) {
					a.matrix[i][j] = m.matrix[i+1][j];
				}
			}
			return a;
		}

		public static Matrix shiftDown(Matrix m) {
			Matrix a = new Matrix(28, 28, false);
			for (int i = 1; i < 28; i++) {
				for (int j = 1; j < 28; j++) {
					a.matrix[i][j] = m.matrix[i-1][j];
				}
			}
			return a;
		}

		public static double getCenterX(Matrix m) {
			double x = 0;
			double sum = 0;
			for (int i = 0; i < m.rows; i++) {
				for (int j = 0; j < m.columns; j++) {
					sum += m.matrix[i][j];
					x += j * m.matrix[i][j];
				}
			}
			return x / sum;
		}

		public static double getCenterY(Matrix m) {
			double y = 0;
			double sum = 0;
			for (int i = 0; i < m.rows; i++) {
				for (int j = 0; j < m.columns; j++) {
					sum += m.matrix[i][j];
					y += i * m.matrix[i][j];
				}
			}
			return y / sum;
		}

		public static Matrix normalize(Matrix m) {
			//compute differences
			int xDiff = 14 - (int) Math.round(getCenterX(m));
			int yDiff = 14 - (int) Math.round(getCenterY(m));
			//do the shifting
			if (xDiff < 0) {
				for (int i = 0; i < -xDiff; i++) {
					m = shiftLeft(m);
				}
			} else {
				for (int i = 0; i < xDiff; i++) {
					m = shiftRight(m);
				}
			}

			if (yDiff < 0) {
				for (int i = 0; i < -yDiff; i++) {
					m = shiftUp(m);
				}
			} else {
				for (int i = 0; i < yDiff; i++) {
					m = shiftDown(m);
				}
			}
			//all done shifting!
			return m;
		}

		/*
		public static double getRadius(Matrix m) {
			double radSum = 0;
			double sum = 0;
			for (int r = 0; r < m.rows; r++) {
				for (int c = 0; c < m.columns; c++) {
					double xCoor = c - 14 + 0.5;
					double yCoor = 14 - r - 0.5;
					radSum += Math.sqrt(Math.pow(xCoor, 2) + Math.pow(yCoor,2)) * m.matrix[r][c];
					sum += m.matrix[r][c];
				}
			}
			return radSum / sum;
		}

		public static Matrix stretch(Matrix m) {
			Matrix a = new Matrix(28,28,false);
			double factor = 6.25 / getRadius(m);
			for (int r = 0; r < 28; r++) {
				for (int c = 0; c < 28; c++) {
					if (m.matrix[r][c] != 0) {
						double x = c - 14 + 0.5;
						double y = 14 - r - 0.5;
						double theta = Math.atan2(y, x);
						double rad = Math.sqrt(Math.pow(x,2) + Math.pow(y,2));
						double newRad = factor * rad;
						x = Math.cos(theta) * newRad;
						y = Math.sin(theta) * newRad;
						int row = (int) (14 - 0.5 - y);
						int col = (int) (x + 14 - 0.5);
						if (row >= 28) {
							row = 27;
						} else if (row < 0) {
							row = 0;
						}
						if (col >= 28) {
							col = 27;
						} else if (col < 0) {
							col = 0;
						}
						a.matrix[row][col] = m.matrix[r][c];
					}
				}
			}
			return a;
		}
		*/
}
