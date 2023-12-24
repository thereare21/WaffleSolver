import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class WaffleWordSolverTwo implements WaffleWordSolverInterface {

  private WaffleInterface waffle;
  private List<Letter> lettersToPlace; //might not need this actually.
  private List<List<Posn>> positionsOfWords; //contains data of the positions of every letter in
  //every word, in order of how they are read.

  private WaffleInterface solution;

  public WaffleWordSolverTwo(WaffleInterface waffle) {
    this.waffle = waffle;
    this.lettersToPlace = new ArrayList<>();
    for (Posn p : waffle.getPositionsToShift()) {
      lettersToPlace.add(waffle.getLetterAt(p));
    }

    this.positionsOfWords = new ArrayList<>();
    //add the positions of letters in the horizontal words to the positionsOfWords
    for (int row = 0; row < 5; row += 2) {
      List<Posn> letterRow = new ArrayList<>();
      for (int column = 0; column < 5; column++) {
        letterRow.add(new Posn(column, row));
      }
      positionsOfWords.add(letterRow);
    }
    //add the positions of letters in the vertical words to the positionsOfWords
    for (int column = 0; column < 5; column += 2) {
      List<Posn> letterColumn = new ArrayList<>();
      for (int row = 0; row < 5; row++) {
        letterColumn.add(new Posn(column, row));
      }
      positionsOfWords.add(letterColumn);
    }

    //sorts the words by which ones are easiest to solve first
    //that way, it is more optimized.
    positionsOfWords.sort(Comparator.comparingInt(this::getWordWeight));

    //for debug purposes, check if positionsOfWords stores the correct data.
    /*
    for (List<Posn> wordPosns : positionsOfWords) {
      for (Posn p : wordPosns) {
        System.out.print(p + " ");
      }
      System.out.println();
    }*/
  }

  /**
   * Returns a weight value of the word depending on how easy it is to fill in the blank letters.
   * For example, if there is a word that has 2 open slots that are both yellow, it will take less
   * permutations than a word with all 5 grey letters. The less the weight, the easier it is to
   * iterate over its permutations.
   * @return - a weight estimation based on how many permutations it will take.
   */
  private int getWordWeight(List<Posn> letterPosnsOfWord) {
    int weight = 0;
    for (Posn p : letterPosnsOfWord) {
      Letter curLetter = waffle.getLetterAt(p);
      if (curLetter.getState() == LetterState.YELLOW) {
        weight += 2;
      } else if (curLetter.getState() == LetterState.GREY) {
        weight++;
      }
    }
    return weight;
  }

  @Override
  public WaffleInterface solveWaffle() {
    List<Letter> lettersToMove = new ArrayList<>();
    for (Posn p : waffle.getPositionsToShift()) {
      lettersToMove.add(waffle.getLetterAt(p));
    }
    fillWord(0, lettersToMove, new WaffleImpl(waffle));
    return null;
  }



  //fills the given five letter word sequence with valid letters, and returns whether or not the
  //word was filled.
  private void fillWord(int wordNumber, List<Letter> lettersUnused, WaffleImpl waffleCopy) {

    if (wordNumber == 6) {
      this.solution = waffleCopy;
    } else {
      //fill the word
      //identify yellow/grey letters
      //loop that goes thru every valid permutation
        // if the word is a valid word in the English dictionary
          // remove used letters from lettersUnused
          // call fillWord(wordNumber + 1, lettersUnused, new WaffleImpl(waffleCopy)
          // add used letters back to lettersUnused
    }

  }

  private void iterateThruAllPermutations(int wordNumber, List<Letter> lettersUnused, WaffleImpl waffleCopy) {
    //get all positions of variable letters
    //get all yellow letters that cannot be in other words - iterate through all permutations
    //get all yellow letters that can be in other words - iterate through all permutations
    //for all remaining positions, permute through all remaining grey letters

    //TODO consider moving these to fillWord, and set them as parameters for this method
    List<Posn> allMovablePositions = positionsOfWords.get(wordNumber)
            .stream().map(waffle::getLetterAt).filter(letter -> letter.getState() != LetterState.GREEN)
            .map(Letter::getPosition).collect(Collectors.toList());

    //get all yellow letters (regardless of whether it can be placed in multiple words or not)
    List<Letter> allYellowLettersInWord = new ArrayList<>();
    for (Posn p : allMovablePositions) {
      Letter letterAt = waffle.getLetterAt(p);
      if (letterAt.getState() == LetterState.YELLOW) {
        allYellowLettersInWord.add(letterAt);
      }
    }

    //TODO figure out a way to iterate through all permutations of yellow letters, then grey letters
    for (Letter yellowLetter : allYellowLettersInWord) {
      if (this.yellowLetterCanBePlacedInMoreThanOneWord(yellowLetter)) {

      } else {

      }
    }
  }

  /**
   * TODO possibly move to separate class...
   * @param word
   * @return
   */
  private boolean wordContainedInEnglishDictionary(String word) {
    return true;
  }

  /**
   * Checks if a yellow letter can be placed in more than one word (if it appears in a junction
   * between 2 words and it is unclear which word it will end up in)
   * @param yellowLetter - the yellow letter in question
   * @return - true if it can be placed in more than one word, false if not.
   */
  private boolean yellowLetterCanBePlacedInMoreThanOneWord(Letter yellowLetter) {
    if (yellowLetter.getState() != LetterState.YELLOW) {
      throw new IllegalArgumentException("Checking non-yellow letter");
    }
    Posn posOfLetter = yellowLetter.getPosition();
    return posOfLetter.getX() % 2 == 1 || posOfLetter.getY() % 2 == 1;
  }



}
