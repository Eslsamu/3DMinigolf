package controller;

import java.util.Arrays;

public class Test {

	public static Float[][] refactorArray(Float[][] array, int rowPosition, int colPosition) {
    	Float[][] copy = new Float[array.length + (rowPosition == 0 ? 0 : 1)][array[0].length + (colPosition == 0 ? 0 : 1)];
    	System.out.println("Refactoring Array: " + Arrays.deepToString(array) + " : rowPosition: " + rowPosition + " : colPosition: " + colPosition);
    	int realI = rowPosition > -1 ? 0 : 1;
    	int realJ = colPosition > -1 ? 0 : 1;
    	for (int i = 0; i < array.length; i++) {
    		for (int j = 0; j < array[0].length; j++) {
    			copy[realI][realJ] = array[i][j];
    			realJ++;
    		}
    		realI++;
    		realJ = colPosition > -1 ? 0 : 1;
    	}
    	if (rowPosition != 0) {
    		for (int i = 0; i < array[0].length; i++) {
    			copy[rowPosition == -1 ? 0 : array.length][i] = 0f;
    		}
    	}
    	if (colPosition != 0) {
    		for (int i = 0; i < array.length; i++) {
    			copy[i][colPosition == -1 ? 0 : array[0].length] = 0f;
    		}
    	}
    	System.out.println("Returning: " + Arrays.deepToString(copy));
    	return copy;
    }
	
	public static void main(String[] args) {
		Float[][] test = new Float[][] {{1f, 2f}, {0f, 1f}};
		for (int i = 0; i < 10; i++) {
			System.out.println(Arrays.deepToString(test = refactorArray(test, 1, 0)));
		}
	}
	
}
