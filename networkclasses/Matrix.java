package networkclasses;

import java.util.Random;
import java.lang.Math;

public class Matrix {
    /**************PROPERTIES**************/
    public double[][] matrix;
    public int rows;
    public int columns;

    /**************INITIALIZER**************/
    public Matrix(int rws, int clms, boolean rand) {
        double[][] m = new double[rws][clms];
        Random gen = new Random();
        for(int i = 0; i < m.length; i += 1) {
            for(int j = 0; j < m[i].length; j += 1) {
                if (!rand) {
                    m[i][j] = 0.0;
                } else {
                    m[i][j] = gen.nextGaussian();
                }
            }
        }
        matrix = m;
        rows = rws;
        columns = clms;
    }

    /**************ACCESSORS**************/
    public int[] shape() {
        int[] a = new int[2];
        a[0] = rows;
        a[1] = columns;
        return a;
    }

    public void printString() {
        for (int i = 0; i < matrix.length; i += 1) {
            String r = "";
            for (int j = 0; j < matrix[i].length; j += 1) {
                r += "" + matrix[i][j] + ", ";
            }
            System.out.println(r);
        }
    }

    /**************OPERATIONS**************/
    public Matrix add(Matrix y) {
        Matrix result = new Matrix(rows, columns, false);
        for(int i = 0; i < result.matrix.length; i += 1) {
            for(int j = 0; j < result.matrix[i].length; j += 1) {
                result.matrix[i][j] = matrix[i][j] + y.matrix[i][j];
            }
        }
        return result;
    }

    public Matrix subtract(Matrix y) {
        Matrix result = new Matrix(rows, columns, false);
        for(int i = 0; i < result.matrix.length; i += 1) {
            for(int j = 0; j < result.matrix[i].length; j += 1) {
                result.matrix[i][j] = matrix[i][j] - y.matrix[i][j];
            }
        }
        return result;
    }

    public Matrix multiply(Matrix y) {
        Matrix result = new Matrix(rows, columns, false);
        for(int i = 0; i < result.matrix.length; i += 1) {
            for(int j = 0; j < result.matrix[i].length; j += 1) {
                result.matrix[i][j] = matrix[i][j] * y.matrix[i][j];
            }
        }
        return result;
    }

    public Matrix multiply_constant(double c) {
        Matrix result = new Matrix(rows, columns, false);
        for(int i = 0; i < result.matrix.length; i += 1) {
            for(int j = 0; j < result.matrix[i].length; j += 1) {
                result.matrix[i][j] = matrix[i][j] * c;
            }
        }
        return result;
    }

    public Matrix divide(Matrix y) {
        Matrix result = new Matrix(rows, columns, false);
        for(int i = 0; i < result.matrix.length; i += 1) {
            for(int j = 0; j < result.matrix[i].length; j += 1) {
                result.matrix[i][j] = matrix[i][j] / y.matrix[i][j];
            }
        }
        return result;
    }

    public Matrix transpose() {
        Matrix result = new Matrix(columns, rows, false);
        for(int i = 0; i < matrix.length; i += 1) {
            for(int j = 0; j < matrix[i].length; j += 1) {
                result.matrix[j][i] = matrix[i][j];
            }
        }
        return result;
    }

    public Matrix dot(Matrix y) {
        Matrix result = new Matrix(rows, y.columns, false);
        for(int i = 0; i < result.rows; i += 1) {
            for(int j = 0; j < result.columns; j += 1) {
                double innerSum = 0;
                for(int a = 0; a < columns; a += 1) {
                    innerSum += matrix[i][a] * y.matrix[a][j];
                }
                result.matrix[i][j] = innerSum;
            }
        }
        return result;
    }

    public Matrix log() {
        Matrix result = new Matrix(rows, columns, false);
        for(int i = 0; i < result.matrix.length; i += 1) {
            for(int j = 0; j < result.matrix[i].length; j += 1) {
                result.matrix[i][j] = Math.log(matrix[i][j]);
            }
        }
        return result;
    }

    public Matrix subtract_from_constant(double c) {
        Matrix result = new Matrix(rows, columns, false);
        for(int i = 0; i < result.matrix.length; i += 1) {
            for(int j = 0; j < result.matrix[i].length; j += 1) {
                result.matrix[i][j] = c - matrix[i][j];
            }
        }
        return result;
    }
}
