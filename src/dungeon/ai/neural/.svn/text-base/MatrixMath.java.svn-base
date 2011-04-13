package dungeon.ai.neural;

/**
 * contains matrix opps commonly used in ann's
 *
 * @author john alexander
 */
public class MatrixMath {

    /*
     * add two matrixes
     */
    public static Matrix add(final Matrix a, final Matrix b) {

        double[][] temp = new double[a.getRows()][a.getCols()];

        for (int i = 0; i < a.getRows(); i++) {
            for (int j = 0; j < a.getCols(); j++) {
                temp[i][j] = a.get(i, j) + b.get(i, j);
            }
        }

        return new Matrix(temp);
    }

    /*
     * divide matrix by scaler
     */
    public static Matrix divide(final Matrix a, final double b) {

        double[][] temp = new double[a.getRows()][a.getCols()];

        for (int i = 0; i < a.getRows(); i++) {
            for (int j = 0; j < a.getCols(); j++) {
                temp[i][j] = a.get(i, j) / b;
            }
        }

        return new Matrix(temp);
    }

    /*
     * dot product of two matrixes
     */
    public static double dotProduct(final Matrix a, final Matrix b) {

        double[] aTemp = a.toPackedArray();
        double[] bTemp = b.toPackedArray();

        double result = 0;
        for (int i = 0; i < aTemp.length; i++) {
            result += aTemp[i] * bTemp[i];
        }

        return result;

    }

    /*
     * creates an identiy matrix
     */
    public static Matrix identity(final int size) {

        double[][] temp = new double[size][size];

        for (int i = 0; i < size; i++) {
            temp[i][i] = 1;
        }

        return new Matrix(temp);
    }

    /*
     * multiply matrix by scaler
     */
    public static Matrix multiply(final Matrix a, final double b) {

        double[][] temp = new double[a.getRows()][a.getCols()];

        for (int i = 0; i < a.getRows(); i++) {
            for (int j = 0; j < a.getCols(); j++) {
                temp[i][j] = a.get(i, j) * b;
            }
        }

        return new Matrix(temp);
    }

    /*
     * multiply matrix by another matrix
     */
    public static Matrix multiply(final Matrix a, final Matrix b) {

        double[][] temp = new double[a.getRows()][b.getCols()];

        for (int i = 0; i < a.getRows(); i++) {
            for (int j = 0; j < b.getCols(); j++) {
                temp[i][j] = dotProduct(a.getRow(i), b.getCol(j));
            }
        }

        return new Matrix(temp);
    }

    /*
     * subtract matrix from matrix
     */
    public static Matrix subtract(final Matrix a, final Matrix b) {

        double[][] temp = new double[a.getRows()][a.getCols()];

        for (int i = 0; i < a.getRows(); i++) {
            for (int j = 0; j < a.getCols(); j++) {
                temp[i][j] = a.get(i, j) - b.get(i, j);
            }
        }

        return new Matrix(temp);

    }

    /*
     * transpose matrix
     */
    public static Matrix transpose(final Matrix input) {

        double[][] temp = new double[input.getCols()][input.getRows()];

        for (int i = 0; i < input.getRows(); i++)
            for (int j = 0; j < input.getCols(); j++)
                temp[j][i] = input.get(i, j);

        return new Matrix(temp);

    }

    /*
     * squared length of a vector
     */
    public static double vectorLength(final Matrix input) {

        double[] temp = input.toPackedArray();
        double result = 0;

        for (int i = 0; i < temp.length; i++) {
            result += Math.pow(temp[i], 2);
        }

        return Math.sqrt(result);
    }
}
