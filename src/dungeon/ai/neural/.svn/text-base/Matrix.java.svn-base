package dungeon.ai.neural;

/**
 * defines a matrix
 *
 * @author john alexander
 */
public class Matrix implements Cloneable {

    //the matrix
    private double[][] _matrix;
    //row and col counts
    private int _rows;
    private int _cols;

    /*
     * creates a new empty matrix
     */
    public Matrix(int row, int col) {
        _matrix = new double[row][col];
        _rows = row;
        _cols = col;
    }

    /*
     * create a new matrix from existing data
     */
    public Matrix(double matrix[][]) {
        _matrix = matrix;
        _rows = matrix.length;
        _cols = matrix[0].length;
    }

    /*
     * creates a matrix with a single column
     */
    public static Matrix createColumnMatrix(final double input[]) {
        Matrix temp = new Matrix(input.length, 1);
        for (int i = 0; i < input.length; i++) {
            temp.set(i, 0, input[i]);
        }
        return temp;
    }

    /*
     * creates a matrix with a single row
     */
    public static Matrix createRowMatrix(final double input[]) {
        Matrix temp = new Matrix(1, input.length);
        for (int i = 0; i < input.length; i++) {
            temp.set(0, i, input[i]);
        }
        return temp;
    }

    /*
     * adds a value to a cell in the matrix
     */
    public void add(final int row, final int col, final double value) {
        _matrix[row][col] += value;
    }

    /*
     * sets every cell in the matrix to zero
     */
    public void clear() {
        for (int i = 0; i < _rows; i++) {
            for (int j = 0; j < _cols; j++) {
                _matrix[i][j] = 0;
            }
        }
    }

    /*
     * clones the matrix
     */
    @Override
    public Matrix clone() {
        return new Matrix(getMatrix());
    }

    /*
     * determines if two matrices are equal
     */
    public boolean equals(final Matrix matrix) {
        return equals(matrix, 10);
    }

    /*
     * determines if two matrices are equal
     */
    public boolean equals(final Matrix matrix, int precision) {

        double p = Math.pow(10, precision);
        double[][] data = matrix.getMatrix();

        for (int i = 0; i < _rows; i++) {
            for (int j = 0; j < _cols; j++) {
                if ((long) (_matrix[i][j] * p) != ((long) data[i][j] * p)) {
                    return false;
                }
            }
        }
        return true;

    }

    /*
     * get value for a cell
     */
    public double get(final int row, final int col) {
        return _matrix[row][col];
    }

    /*
     * get the matrix data
     */
    public double[][] getMatrix() {
        return _matrix;
    }

    /*
     * get one column of the matrix
     */
    public Matrix getCol(final int col) {
        double[] data = new double[_rows];
        for (int i = 0; i < _rows; i++) {
            data[i] = _matrix[i][col];
        }
        return createColumnMatrix(data);
    }

    /*
     * determins the number of columns in the matrix
     */
    public int getCols() {
        return _cols;
    }

    /*
     * get one row of the matrix
     */
    public Matrix getRow(final int row) {
        double[] data = new double[_cols];
        for (int i = 0; i < _cols; i++) {
            data[i] = _matrix[row][i];
        }
        return createRowMatrix(data);
    }

    /*
     * determins the number of rows in the matrix
     */
    public int getRows() {
        return _rows;
    }

    /*
     * determines if the matrix is a vector
     */
    public boolean isVector() {
        return ((_rows == 1) || (_cols == 1));
    }

    /*
     * determines if every cell in the matrix is zero
     */
    public boolean isZero() {
        return (sum() == 0);
    }

    /*
     * set the value of a cell
     */
    public void set(final int row, final int col, final double value) {
        _matrix[row][col] = value;
    }

    /*
     * return the sum of every cell in the matrix
     */
    public double sum() {
        double temp = 0;
        for (int i = 0; i < _rows; i++) {
            for (int j = 0; j < _cols; j++) {
                temp += _matrix[i][j];
            }
        }
        return temp;
    }

    /*
     * convert two-d to one-d array
     */
    public double[] toPackedArray() {
        double[] temp = new double[_rows * _cols];
        int index = 0;
        for (int i = 0; i < _rows; i++) {
            for (int j = 0; j < _cols; j++) {
                temp[index++] = _matrix[i][j];
            }
        }
        return temp;
    }

    /*
     * convert to string
     */
    @Override
    public String toString() {
        String temp = "";

        for (int i = 0; i < _rows; i++) {
            for (int j = 0; j < _cols; j++) {
                temp += _matrix[i][j] + " ";
            }
            temp += "\n";
        }

        return temp;

    }

    /*
     * randomize the matrix values
     */
    public void randomize(double min, double max) {
        for (int i = 0; i < _rows; i++) {
            for (int j = 0; j < _cols; j++) {
                _matrix[i][j] = (Math.random() * (max - min)) + min;
            }
        }
    }
    
    /*
     * set every row and column to a specifed value
     */
    public void set(double value) {
        for (int i = 0; i < _rows; i++)
            for (int j = 0; j < _cols; j++)
                _matrix[i][j] = value;
    }
    
    /*
     * return the data contained inside this matrix
     */
    public double[][] getData() {
        return _matrix;
    }
}
