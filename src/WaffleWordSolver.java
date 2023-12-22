import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Word Solver version 1. Tried to implement a brute force approach and iterate through all
 * permutations, looking for the one that contains the 6 words. This does not work because 13! is
 * 6 billion permutations, it will take way too long.
 */
public class WaffleWordSolver implements WaffleWordSolverInterface {

  private final WaffleInterface waffle;
  private WaffleInterface solution;
  private List<Posn> posnsToShift;

  private static int numPermutationsCounted;

  public WaffleWordSolver(WaffleInterface waffle) {
    this.waffle = waffle;
    this.solution = null;
    this.posnsToShift = waffle.getPositionsToShift();
    numPermutationsCounted = 0;
  }

  @Override
  public WaffleInterface solveWaffle() {
    //go through all permutations somehow
    return chooseFromAllPermutations();
  }

  /**
   * Goes through all possible permutations and chooses the one with all 6 valid words.
   * @return - the solved permutation
   */
  private WaffleInterface chooseFromAllPermutations() {
    // get an array of all movable letters going, and feed that into a method

    List<Letter> lettersToMove = new ArrayList<>();
    for (Posn p : posnsToShift) {
      lettersToMove.add(waffle.getLetterAt(p));
    }

    iterateThroughAllPermutations(lettersToMove);
    return solution;
  }

  private void iterateThroughAllPermutations(List<Letter> lettersToMove) {
    generatePermutation(lettersToMove, 0, lettersToMove.size() - 1);
  }

  private void generatePermutation(List<Letter> lettersToMove, int left, int right) {

    //generate a waffle with the letters in their respective swapped positions.

    if (left == right) {
      //check if the current permutation has 6 valid words, and the swapped letters are valid.
      //System.out.println(Arrays.toString(lettersToMove));

      WaffleImpl waffleCopy = new WaffleImpl(waffle);
      for (int i = 0; i < posnsToShift.size(); i++) {
        waffleCopy.replaceLetter(posnsToShift.get(i), lettersToMove.get(i));
      }

      numPermutationsCounted++;
      System.out.println(numPermutationsCounted);

      //System.out.println(waffleCopy);
      //System.out.println();


    } else {
      for (int i = left; i <= right; i++) {
        if (isValidSwap(lettersToMove, left, i)) {
          swap(lettersToMove, left, i);
          generatePermutation(lettersToMove, left + 1, right);
          swap(lettersToMove, left, i);  // Backtrack
        }
      }
    }
  }

  private boolean isValidSwap(List<Letter> lettersToMove, int left, int right) {
    //check if the letter swapped was a yellow letter.
    //if so, check the valid spots of that specific letter.
    Letter letterAt = lettersToMove.get(left);
    if (letterAt.getState() == LetterState.YELLOW) {
      return waffle.getValidPlacementsOfLetter(letterAt).contains(lettersToMove.get(right).getPosition());
    }
    return true;
  }

  private void swap(List<Letter> elements, int i, int j) {
    Letter temp = elements.get(i);
    elements.set(i, elements.get(j));
    elements.set(j, temp);
  }

  private boolean hasSixWords(WaffleInterface wafflePermutation) {
    //check all rows

    //check all columns

    return true;
  }
}
