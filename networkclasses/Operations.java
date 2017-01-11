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
}
