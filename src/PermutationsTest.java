import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PermutationsTest {

  public static void generatePermutations(char[] elements, int left, int right) {
    if (left == right) {
      // Process or print the current permutation
      System.out.println(Arrays.toString(elements));
    } else {
      for (int i = left; i <= right; i++) {
        if (isValidSwap(elements, left, i)) {
          swap(elements, left, i);
          generatePermutations(elements, left + 1, right);
          swap(elements, left, i);  // Backtrack
        }
      }
    }
  }

  public static void generateAllPermutations(char[] elements) {
    generatePermutations(elements, 0, elements.length - 1);
  }

  public static boolean isValidSwap(char[] elements, int left, int right) {
    return true;
  }

  private static  void swap(char[] elements, int i, int j) {
    char temp = elements[i];
    elements[i] = elements[j];
    elements[j] = temp;
  }
}
