package networkclasses;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

public class Data {
    public Data() {}

    public static Matrix image_to_matrix(int[][] image) {
        Matrix image_matrix = new Matrix(784, 1, false);
        int master_index = 0;
        for(int i = 0; i < image.length; i += 1) {
            for(int j = 0; j < image[i].length; j += 1) {
                image_matrix.matrix[master_index][0] = (double)image[i][j] / 255.0;
                master_index += 1;
            }
        }
        return image_matrix;
    }

    public static Matrix[] get_images(String file_path) throws IOException {
        MnistImageFile images = new MnistImageFile(file_path, "r");
        int data_length = images.getCount();
        Matrix[] matrix_images = new Matrix[data_length];
        int count = 0;
        while(count < data_length) {
            matrix_images[count] = image_to_matrix(images.readImage());
            count += 1;
        }
        return matrix_images;
    }

    public static int[] get_labels(String file_path) throws IOException {
        MnistLabelFile labels = new MnistLabelFile(file_path, "r");
        int data_length = labels.getCount();
        int[] label_ints = new int[data_length];
        int count = 0;
        while(count < data_length) {
            label_ints[count] = labels.readLabel();
            count += 1;
        }
        return label_ints;
    }

    public static void print_image(Matrix image) {
        int index = 0;
        for(int i = 0; i < 28; i += 1) {
            String output = "";
            for(int j = 0; j < 28; j += 1) {
                if (image.matrix[index][0] > 0.5) {
                    output = output + "*";
                } else {
                    output = output + " ";
                }
                index += 1;
            }
            System.out.println(output);
        }
    }

    public static Matrix[] labels_to_matrices(int[] x) {
        Matrix[] out = new Matrix[x.length];
        for(int i = 0; i < out.length; i += 1) {
            out[i] = Operations.vectorize(x[i]);
        }
        return out;
    }
}


class MnistImageFile extends MnistDbFile {
    private int rows;
    private int cols;

    /**
     * Creates new MNIST database image file ready for reading.
     *
     * @param name
     *            the system-dependent filename
     * @param mode
     *            the access mode
     * @throws java.io.IOException
     * @throws java.io.FileNotFoundException
     */
    public MnistImageFile(String name, String mode) throws IOException {
        super(name, mode);

        // read header information
        rows = readInt();
        cols = readInt();
    }

    /**
     * Reads the image at the current position.
     *
     * @return matrix representing the image
     * @throws java.io.IOException
     */
    public int[][] readImage() throws IOException {
        int[][] dat = new int[getRows()][getCols()];
        for (int i = 0; i < getCols(); i++) {
            for (int j = 0; j < getRows(); j++) {
                dat[i][j] = readUnsignedByte();
            }
        }
        return dat;
    }

    /**
     * Move the cursor to the next image.
     *
     * @throws java.io.IOException
     */
    public void nextImage() throws IOException {
        super.next();
    }

    /**
     * Move the cursor to the previous image.
     *
     * @throws java.io.IOException
     */
    public void prevImage() throws IOException {
        super.prev();
    }

    @Override
    protected int getMagicNumber() {
        return 2051;
    }

    /**
     * Number of rows per image.
     *
     * @return int
     */
    public int getRows() {
        return rows;
    }

    /**
     * Number of columns per image.
     *
     * @return int
     */
    public int getCols() {
        return cols;
    }

    @Override
    public int getEntryLength() {
        return cols * rows;
    }

    @Override
    public int getHeaderSize() {
        return super.getHeaderSize() + 8; // to more integers - rows and columns
    }
}

abstract class MnistDbFile extends RandomAccessFile {
    private int count;


    /**
     * Creates new instance and reads the header information.
     *
     * @param name
     *            the system-dependent filename
     * @param mode
     *            the access mode
     * @throws java.io.IOException
     * @throws java.io.FileNotFoundException
     * @see java.io.RandomAccessFile
     */
    public MnistDbFile(String name, String mode) throws IOException {
        super(name, mode);

        if (getMagicNumber() != readInt()) {
            throw new RuntimeException("This MNIST DB file " + name + " should start with the number " + getMagicNumber() + ".");
        }

        count = readInt();
    }

    /**
     * MNIST DB files start with unique integer number.
     *
     * @return integer number that should be found in the beginning of the file.
     */
    protected abstract int getMagicNumber();

    /**
     * The current entry index.
     *
     * @return long
     * @throws java.io.IOException
     */
    public long getCurrentIndex() throws IOException {
        return (getFilePointer() - getHeaderSize()) / getEntryLength() + 1;
    }

    /**
     * Set the required current entry index.
     *
     * @param curr
     *            the entry index
     */
    public void setCurrentIndex(long curr) {
        try {
            if (curr < 0 || curr > count) {
                throw new RuntimeException(curr + " is not in the range 0 to " + count);
            }
            seek(getHeaderSize() + (curr - 1) * getEntryLength());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public int getHeaderSize() {
        return 8; // two integers
    }

    /**
     * Number of bytes for each entry.
     * Defaults to 1.
     *
     * @return int
     */
    public int getEntryLength() {
        return 1;
    }

    /**
     * Move to the next entry.
     *
     * @throws java.io.IOException
     */
    public void next() throws IOException {
        if (getCurrentIndex() < count) {
            skipBytes(getEntryLength());
        }
    }

    /**
     * Move to the previous entry.
     *
     * @throws java.io.IOException
     */
    public void prev() throws IOException {
        if (getCurrentIndex() > 0) {
            seek(getFilePointer() - getEntryLength());
        }
    }

    public int getCount() {
        return count;
    }
}

class MnistLabelFile extends MnistDbFile {

    /**
     * Creates new MNIST database label file ready for reading.
     *
     * @param name
     *            the system-dependent filename
     * @param mode
     *            the access mode
     * @throws java.io.IOException
     * @throws java.io.FileNotFoundException
     */
    public MnistLabelFile(String name, String mode) throws FileNotFoundException, IOException {
        super(name, mode);
    }

    /**
     * Reads the integer at the current position.
     *
     * @return integer representing the label
     * @throws java.io.IOException
     */
    public int readLabel() throws IOException {
        return readUnsignedByte();
    }

    @Override
    protected int getMagicNumber() {
        return 2049;
    }
}
