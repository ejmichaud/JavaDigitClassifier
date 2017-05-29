package networkclasses;

import java.io.*;

public class Network {
    public int[] sizes;
    public int num_sizes;
    public Matrix[] biases;
    public Matrix[] weights;

    public Network(int[] szs) {
        sizes = szs;
        num_sizes = sizes.length;
        Matrix[] b = new Matrix[num_sizes-1];
        Matrix[] w = new Matrix[num_sizes-1];
        for(int i = 1; i < num_sizes; i += 1) {
            b[i-1] = new Matrix(sizes[i], 1, true);
            w[i-1] = new Matrix(sizes[i], sizes[i-1], true);
        }
        biases = b;
        weights = w;
    }

    public Network(String path) {
        int[] szs = new int[3];

        try {
            FileReader fileReader = new FileReader(path);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            szs[0] = Integer.parseInt(bufferedReader.readLine());
            szs[1] = Integer.parseInt(bufferedReader.readLine());
            szs[2] = Integer.parseInt(bufferedReader.readLine());
            sizes = szs;
            num_sizes = szs.length;

            Matrix[] b = new Matrix[num_sizes-1];
            Matrix[] w = new Matrix[num_sizes - 1];
            for(int i = 1; i < num_sizes; i += 1) {
                b[i-1] = new Matrix(sizes[i], 1, false);
                w[i-1] = new Matrix(sizes[i], sizes[i-1],false);
            }

            for(int i = 0; i < num_sizes - 1; i += 1) {
                for(int j = 0; j < b[i].rows; j += 1) {
                    b[i].matrix[j][0] = Double.parseDouble(bufferedReader.readLine());
                }
                for(int r = 0; r < w[i].rows; r += 1) {
                    for(int c = 0; c < w[i].columns; c += 1) {
                        w[i].matrix[r][c] = Double.parseDouble(bufferedReader.readLine());
                    }
                }
            }
            biases = b;
            weights = w;
            bufferedReader.close();
        } catch (FileNotFoundException ex) {
            System.out.println(
                    "Unable to open file '" + path + "'");
        } catch (IOException ex) {
            System.out.println(
                    "Error reading file '" + path + "'");
        }
    }

    public Matrix feedforward(Matrix a) {
        for(int i = 0; i < biases.length; i += 1) {
            a = Operations.sigmoid_matrix(weights[i].dot(a).add(biases[i]));
        }
        return a;
    }

    public void SGD(Matrix[][] training_data, int epochs, int mini_batch_size, double eta, Matrix[] test_images, int[] test_labels) {
        int n = training_data.length;
        for(int epoch = 0; epoch < epochs; epoch += 1) {
            Matrix[][] shuffled_data = Operations.shuffle(training_data);
            Matrix[][][] mini_batches = new Matrix[n / mini_batch_size][mini_batch_size][2];
            int batch_index = 0;
            for(int k = 0; k < n; k += mini_batch_size) {
                mini_batches[batch_index] = Operations.subArray(training_data, k, mini_batch_size);
                batch_index += 1;
            }
            for(int i = 0; i < mini_batches.length; i += 1) {
                update_mini_batch(mini_batches[i], eta);
            }
            System.out.println("Epoch " + epoch + " --> " + evaluate(test_images, test_labels));
        }
    }

    private void update_mini_batch(Matrix[][] mini_batch, double eta) {
        /* Initialize empty nabla variables */
        Matrix[] nabla_b = new Matrix[biases.length];
        Matrix[] nabla_w = new Matrix[weights.length];
        for(int i = 0; i < biases.length; i += 1) {
            int[] bshape = biases[i].shape();
            int[] wshape = weights[i].shape();
            nabla_b[i] = new Matrix(bshape[0], bshape[1], false);
            nabla_w[i] = new Matrix(wshape[0], wshape[1], false);
        }

        /* Accumulate gradient sums */
        for(int i = 0; i < mini_batch.length; i += 1) {
            Matrix[][] nablas = backprop(mini_batch[i][0], mini_batch[i][1]);
            for(int j = 0; j < nabla_b.length; j += 1) {
                nabla_b[j] = nabla_b[j].add(nablas[0][j]);
                nabla_w[j] = nabla_w[j].add(nablas[1][j]);
            }
        }

        /* Update network parameters */
        for(int i = 0; i < biases.length; i += 1) {
            biases[i] = biases[i].add(nabla_b[i].multiply_constant(-eta / mini_batch.length));
            weights[i] = weights[i].add(nabla_w[i].multiply_constant(-eta / mini_batch.length));
        }
    }

    private Matrix[][] backprop(Matrix a, Matrix y) {
        /* Initialize nabla variables */
        Matrix[] nabla_b = new Matrix[biases.length];
        Matrix[] nabla_w = new Matrix[weights.length];
        for(int i = 0; i < biases.length; i += 1) {
            int[] bshape = biases[i].shape();
            int[] wshape = weights[i].shape();
            nabla_b[i] = new Matrix(bshape[0], bshape[1], false);
            nabla_w[i] = new Matrix(wshape[0], wshape[1], false);
        }

        /* Forward Propagate */
        Matrix activation = a;
        Matrix[] activations = new Matrix[num_sizes];
        activations[0] = activation;
        Matrix[] zs = new Matrix[num_sizes-1];
        for(int i = 0; i < biases.length; i += 1) {
            Matrix z = weights[i].dot(activation).add(biases[i]);
            zs[i] = z;
            activation = Operations.sigmoid_matrix(z);
            activations[i + 1] = activation;
        }

        /* Back propagate */
        Matrix delta = Operations.cost_derivative_with_sigmoid(zs[zs.length - 1], activations[activations.length - 1], y);
        nabla_b[nabla_b.length - 1] = delta;
        nabla_w[nabla_w.length - 1] = delta.dot(activations[activations.length - 2].transpose());
        for(int l = 2; l < num_sizes; l += 1) {
            Matrix z = zs[zs.length - l];
            Matrix sp = Operations.sigmoid_prime_matrix(z);
            delta = weights[weights.length - l + 1].transpose().dot(delta).multiply(sp);
            nabla_b[nabla_b.length - l] = delta;
            nabla_w[nabla_w.length - l] = delta.dot(activations[activations.length - l - 1].transpose());
        }
        Matrix[][] out = new Matrix[2][];
        out[0] = nabla_b;
        out[1] = nabla_w;
        return out;
    }

    public int evaluate(Matrix[] test_images, int[] test_labels) {
        int counter = 0;
        for(int i = 0; i < test_images.length; i += 1) {
            if (Operations.argmax(feedforward(test_images[i])) == test_labels[i]) {
                counter += 1;
            }
        }
        return counter;
    }

    public void save(String path) throws IOException {
        try {
            FileWriter fileWriter = new FileWriter(path);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

            for (int i = 0; i < num_sizes; i += 1) {
                bufferedWriter.write(Integer.toString(sizes[i]));
                bufferedWriter.newLine();
            }

            for(int i = 0; i < num_sizes - 1; i += 1) {
                for(int j = 0; j < biases[i].rows; j += 1) {
                    bufferedWriter.write(Double.toString(biases[i].matrix[j][0]));
                    bufferedWriter.newLine();
                }
                for(int r = 0; r < weights[i].rows; r += 1) {
                    for(int c = 0; c < weights[i].columns; c += 1) {
                        bufferedWriter.write(Double.toString(weights[i].matrix[r][c]));
                        bufferedWriter.newLine();
                    }
                }
            }
            bufferedWriter.close();
        } catch (IOException ex) {
            System.out.println("Error writing to file '" + path + "'");
        }
    }
}
