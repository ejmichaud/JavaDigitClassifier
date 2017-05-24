import networkclasses.*;
import guis.*;

import java.io.FileNotFoundException;
import  java.io.IOException;

public class Main {
    public static void main (String[] args) throws IOException, FileNotFoundException {
				Network net = new Network("params/parameters9694.txt");
				GUI show = new GUI();
				show.showMouseListener();


		/*** THE NETWORK LAYER SIZES ***/



		/*
		int[] sizes = {784, 300, 10};
		*/

		/*** COLLECT AND PROCESS TRAINING AND TEST DATA ***/
		/*
		Matrix[] training_images = Data.get_images("/Users/Eric/Desktop/MNIST/training_images");
		System.out.println("Done importing training images");
		Matrix[] training_labels = Data.labels_to_matrices(Data.get_labels("/Users/Eric/Desktop/MNIST/training_labels"));
		System.out.println("Done importing training labels");


		Matrix[] test_images = Data.get_images("/Users/Eric/Desktop/MNIST/test_images");
		System.out.println("Done importing test images");
		int[] test_labels = Data.get_labels("/Users/Eric/Desktop/MNIST/test_labels");
		System.out.println("Done importing test labels");


		System.out.println("Tuplifying training data...");
		Matrix[][] training_data = new Matrix[training_images.length][2];
		for(int i = 0; i < training_data.length; i += 1) {
			training_data[i] = new Matrix[]{training_images[i], training_labels[i]};
		}
		System.out.println("Done processing " + training_data.length + " training datas");

		/*** INITIALIZE, TRAIN, AND SAVE NETWORK ***/

		/*** INITIALIZE AND TEST NETWORK ***/
		/*
		Network net = new Network("/Users/Eric/Desktop/parameters9660.txt");
		System.out.println(net.evaluate(test_images, test_labels));

		for(int i = 2; i < 10; i++) {
			Data.print_image(test_images[i]);
			System.out.println(Operations.argmax(net.feedforward(test_images[i])));
		}
		*/
	}
}
