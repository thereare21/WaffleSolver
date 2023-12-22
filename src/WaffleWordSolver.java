import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WaffleWordSolver implements WaffleWordSolverInterface {

  private final WaffleInterface waffle;
  private WaffleInterface solution;

  public WaffleWordSolver(WaffleInterface waffle) {
    this.waffle = waffle;
    this.solution = null;
  }

  @Override
  public WaffleInterface solveWaffle() {
    //go through all permutations somehow
    //chooseFromAllPermutations()
    return null;
  }

  /**
   * Goes through all possible permutations and chooses the one with all 6 valid words.
   * @return - the solved permutation
   */
  private WaffleInterface chooseFromAllPermutations() {
    // get an array of all movable letters going, and feed that into a method

    List<Letter> lettersToMove = new ArrayList<>();
    for (Posn p : waffle.getPositionsToShift()) {
      lettersToMove.add(waffle.getLetterAt(p));
    }

    iterateThroughAllPermutations(lettersToMove);
    return solution;
  }

  private void iterateThroughAllPermutations(List<Letter> lettersToMove) {
    generatePermutation(lettersToMove, 0, lettersToMove.size() - 1);
  }

  private void generatePermutation(List<Letter> lettersToMove, int left, int right) {
    /*
    if (left == right) {
      //check if the current permutation has 6 valid words, and the swapped letters are valid.
      System.out.println(Arrays.toString(elements));
    } else {
      for (int i = left; i <= right; i++) {
        if (isValidSwap(elements, left, i)) {
          swap(elements, left, i);
          generatePermutations(elements, left + 1, right);
          swap(elements, left, i);  // Backtrack
        }
      }
    }*/
  }

  private boolean isValidSwap(List<Letter> lettersToMove, int left, int right) {
    //check if the letter swapped was a yellow letter.
    //if so, check the valid spots of that specific letter.
    return true;
  }
}
