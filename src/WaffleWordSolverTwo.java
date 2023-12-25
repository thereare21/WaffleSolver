import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
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

    for (List<Posn> wordPosns : positionsOfWords) {
      for (Posn p : wordPosns) {
        System.out.print(p + " ");
      }
      System.out.println();
    }
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
      List<Posn> allMovablePositions = positionsOfWords.get(wordNumber)
              .stream().map(waffle::getLetterAt).filter(letter -> letter.getState() != LetterState.GREEN)
              .map(Letter::getPosition).collect(Collectors.toList());

      //sorting all letters into 3 categories: sniper letters, locked letters, grey letters, and
      //iffy letters
      List<Letter> sniperLetters = new ArrayList<>();
      List<Optional<Letter>> lockedLetters = new ArrayList<>();
      List<Letter> greyAndIffyLetters = new ArrayList<>();
      for (Letter letter : lettersUnused) {
        if (letter.getState() == LetterState.YELLOW) {
          if (allMovablePositions.contains(letter.getPosition())) {
            if (this.yellowLetterCanBePlacedInMoreThanOneWord(letter)) {
              greyAndIffyLetters.add(letter);
            } else {
              lockedLetters.add(Optional.of(letter));
            }
          } else if (this.yellowLetterIsSniperForWord(positionsOfWords.get(wordNumber), letter)) {
            sniperLetters.add(letter);
          }
        } else if (letter.getState() == LetterState.GREY) {
          greyAndIffyLetters.add(letter);
        }
      }

      //fill out locked letters with optional empties until it is the same size as allMovablePositions
      while (lockedLetters.size() < allMovablePositions.size()) {
        lockedLetters.add(Optional.empty());
      }
      List<Boolean> lockedUsed = new ArrayList<>();
      for (int i = 0; i < lockedLetters.size(); i++) {
        lockedUsed.add(false);
      }

      System.out.println("All movable positions");
      for (Posn p : allMovablePositions) {
        System.out.print(p + " ");
      }
      System.out.println();

      System.out.println("Locked letters: ");
      for (Optional<Letter> letterOrEmpty : lockedLetters) {
        if (letterOrEmpty.isPresent()) {
          System.out.print(letterOrEmpty.get() + " ");
        } else {
          System.out.print("Empty ");
        }
      }
      System.out.println();

      generateYellowLockedPermutations(lockedLetters, lockedUsed, 0,
              sniperLetters, greyAndIffyLetters, new ArrayList<>(), allMovablePositions);

    }

  }


  private void generateYellowLockedPermutations(
          List<Optional<Letter>> lockedLetters, List<Boolean> lockedUsed, int index, List<Letter> sniperLetters, List<Letter> greyAndIffyLetters,
          List<Optional<Letter>> permutationToBuild, List<Posn> allMovablePositions) {
    if (index == lockedLetters.size()) {
      //move on to the next step
      generateYellowSniperPermutations(lockedLetters, sniperLetters, greyAndIffyLetters, new ArrayList<>(), 0, allMovablePositions);
      System.out.println("Permutation");
      for (Optional<Letter> letterOrEmpty : permutationToBuild) {
        if (letterOrEmpty.isPresent()) {
          System.out.print(letterOrEmpty.get() + " ");
        } else {
          System.out.print("Empty ");
        }
      }
      System.out.println();
    } else {
      for (int i = 0; i < lockedLetters.size(); i++) {
        Optional<Letter> letterOrEmpty = lockedLetters.get(i);
        //checks if the letter is not used yet
        if (!lockedUsed.get(i)) {
          //checks if the letter is not in its original position
          if ((letterOrEmpty.isPresent()
                  && !letterOrEmpty.get().equals(waffle.getLetterAt(allMovablePositions.get(index))))
              || letterOrEmpty.isEmpty()) {
            lockedUsed.set(i, true);
            permutationToBuild.add(letterOrEmpty);
            generateYellowLockedPermutations(lockedLetters, lockedUsed, index + 1, sniperLetters, greyAndIffyLetters,
                    permutationToBuild, allMovablePositions);
            //backtrack
            permutationToBuild.remove(letterOrEmpty);
            lockedUsed.set(i, false);
          }
        }
      }
    }
  }

  private void generateYellowSniperPermutations(List<Optional<Letter>> lockedLetters,
                                                List<Letter> sniperLetters, List<Letter> greyAndIffyLetters,
                                                List<Optional<Letter>> permutationToBuild, int index,
                                                List<Posn> allMovablePositions) {
    if (index == sniperLetters.size()) {
      //move on to next step
    } else {
      Letter curSniper = sniperLetters.get(index);
      //if (wordPosns.get(1).getY() - wordPosns.get(0).getY() == 1)
      //PROBLEM: multiple snipers can share the same spot that it can appear in.
      //PROBLEM: how do you know the "sniping spot"? consider storing the data of the sniping spot
      //of each sniper letter before passing it as an argument.
    }
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
    return posOfLetter.getX() % 2 == 0 && posOfLetter.getY() % 2 == 0;
  }

  /**
   * Checks if the given yellow letter can appear in the given word, even if it isn't located
   * within the word. it will appear in exactly one location in the given word (hence, a sniper
   * letter)
   * @param wordPosns - the positions of each letter in the word to check.
   * @param yellowLetter - the yellow letter in question
   * @return - true if it is a "sniper" letter.
   */
  private boolean yellowLetterIsSniperForWord(List<Posn> wordPosns, Letter yellowLetter) {
    //if the word is vertical
    if (wordPosns.get(1).getY() - wordPosns.get(0).getY() == 1) {
      //if it is located in a word that is horizontal
      if (yellowLetter.getPosition().getY() % 2 == 0) {
        //check if letter is not in the word itself
        if (yellowLetter.getPosition().getX() != wordPosns.get(0).getX()) {
          //checks if the position in the word in which the sniper letter could appear isn't already
          //taken by a green space. if it is grey or yellow, that means the sniper letter can appear
          //there, and thus returns true.
          return waffle.getLetterAt(
                  new Posn(wordPosns.get(0).getX(), yellowLetter.getPosition().getY())).getState()
                  != LetterState.GREEN;
        }
        return false;
      }
      return false;
    } //else if word is horizontal
    else if (wordPosns.get(1).getX() - wordPosns.get(0).getX() == 1) {
      //if it is located in a word that is vertical
      if (yellowLetter.getPosition().getX() % 2 == 0) {
        if (yellowLetter.getPosition().getY() % 2 == 0) {
          //check if letter is not in the word itself
          if (yellowLetter.getPosition().getY() != wordPosns.get(0).getY()) {
            return waffle.getLetterAt(
                    new Posn(yellowLetter.getPosition().getX(), wordPosns.get(0).getY())).getState()
                    != LetterState.GREEN;
          }
          return false;
        }
      }
      return false;
    } else {
      throw new IllegalArgumentException("Word is not horizontal or vertical");
    }
  }



}
