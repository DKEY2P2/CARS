package math;

/**
 * A math class that should handle advance maths
 */
public class Math {

    /**
     * Does matrix maths
     */
    public static class MatrixMath {

        /**
         * Dont ask me what this does, did this ages ago as in 1.5 years ago
         *
         * @param array
         * @param colNumber
         * @return
         */
        private static int productCol(int[][] array, int colNumber) {
            int prod = 0;
            int column = array[colNumber].length;
            for (int x = 0; x < column; x++) {
                prod *= array[colNumber][x];
            }
            return prod;
        }

        /**
         * Dont ask me what this does, did this ages ago as in 1.5 years ago
         *
         * @param array
         * @param row
         * @return
         */
        private static int productRow(int[][] array, int row) {
            int prod = 0;
            int rowsize = array.length;
            for (int x = 0; x < array.length; x++) {
                prod *= array[x][row];
            }
            return prod;
        }

        /**
         * Hopefully it does matrix multiplication, did this years ago
         *
         * @param array1 The first matrix
         * @param array2 The second matrix
         * @return The new matrix
         */
        public static double[][] matrixMultiplication(int[][] array1, int[][] array2) {
            double[][] array3 = new double[(array1.length)][(array2[0].length)];
            //row
            for (int i = 0; i < array3.length; i++) {
                //column
                for (int j = 0; j < array3[i].length; j++) {
                    array3[i][j] = (productRow(array1, i) * productCol(array2, j));
                }
            }
            return array3;
        }

        /**
         * This function does the Hadamard product of two matrices.<p>
         * See https://en.wikipedia.org/wiki/Hadamard_product_(matrices)
         *
         * @param matrix1
         * @param matrix2
         * @return
         */
        public static double[][] Hadamardproduct(double[][] matrix1, double[][] matrix2) {
            if (matrix1.length != matrix2.length || matrix1[0].length != matrix2[0].length) {
                throw new IllegalArgumentException("Matrix are not the same size");
            }
            double[][] newMatrix = new double[matrix1.length][matrix2[0].length];
            for (int i = 0; i < matrix1.length; i++) {
                for (int j = 0; j < matrix2[0].length; j++) {
                    newMatrix[i][j] = matrix1[i][j] * matrix2[i][j];
                }
            }
            return newMatrix;
        }
    }

}
