import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
        weight += 1;
      } else if (curLetter.getState() == LetterState.GREY) {
        weight += 2;
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
      //Map<Letter, Posn> sniperLetters = new HashMap<>();
      List<Letter> sniperLetters = new ArrayList<>();
      List<Posn> sniperSpots = new ArrayList<>();
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
            Posn sniperSpot;
            if (this.wordIsHorizontal(positionsOfWords.get(wordNumber))) {
              sniperSpot = new Posn(letter.getPosition().getX(),
                      positionsOfWords.get(wordNumber).get(0).getY());
            } else {
              sniperSpot = new Posn(positionsOfWords.get(wordNumber).get(0).getX(),
                      letter.getPosition().getY());
            }
            sniperLetters.add(letter);
            sniperSpots.add(sniperSpot);
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
      List<Boolean> emptyUsed = new ArrayList<>();
      for (int i = 0; i < lockedLetters.size(); i++) {
        emptyUsed.add(false);
      }

      System.out.println("All movable positions");
      for (Posn p : allMovablePositions) {
        System.out.print(p + " ");
      }
      System.out.println();

      System.out.print("Locked letters: ");
      for (Optional<Letter> letterOrEmpty : lockedLetters) {
        if (letterOrEmpty.isPresent()) {
          System.out.print(letterOrEmpty.get() + " ");
        } else {
          System.out.print("Empty ");
        }
      }
      System.out.println();

      System.out.print("Sniper letters: ");
      for (Letter sniper : sniperLetters) {
        System.out.print(sniper + " ");
      }
      System.out.println();

      generateYellowLockedPermutations(lockedLetters, lockedUsed, 0,
              sniperLetters, sniperSpots, greyAndIffyLetters, new ArrayList<>(), allMovablePositions);

    }

  }


  /**
   * Step 1 in generated all permutations of a word. Iterates through all possible placements of
   * "locked letters", or yellow letters that MUST appear in the word.
   * @param lockedLetters - a list of all locked letter, or an optional empty.
   * @param lockedUsed - a list of boolean denoting which "letters" have been used already.
   * @param index - the current index to place an item.
   * @param sniperLetters - a list of all sniper letters. will not be used in this step, but will
   *                      be passed along to future steps.
   * @param sniperSpots - a list of all sniper spots, corresponding to
   * @param greyAndIffyLetters - all "grey" and "iffy" letters. this won't be used in this method,
   *                           but it will be used as a way to pass onto subsequent steps.
   * @param permutationToBuild - a list of the permutation that, by the end of the recursion, will
   *                           build the permutation needed to move to the next step.
   * @param allMovablePositions - a list of Posn that cooresponds to the positions the items in the
   *                            permutation list will eventually be placed.
   */
  private void generateYellowLockedPermutations(
          List<Optional<Letter>> lockedLetters, List<Boolean> lockedUsed, int index,
          List<Letter> sniperLetters,
          List<Posn> sniperSpots,
          List<Letter> greyAndIffyLetters,
          List<Optional<Letter>> permutationToBuild,
          List<Posn> allMovablePositions) {
    if (index == lockedLetters.size()) {
      //move on to the next step
      List<Boolean> sniperUsed = new ArrayList<>();
      for (int i = 0; i < sniperLetters.size(); i++) {
        sniperUsed.add(false);
      }

      //for debugging
      System.out.print("Permutation: ");
      for (Optional<Letter> letterOrEmpty : permutationToBuild) {
        if (letterOrEmpty.isPresent()) {
          System.out.print(letterOrEmpty.get() + " ");
        } else {
          System.out.print("Empty ");
        }
      }
      System.out.println();

      //generate sniper permutations based on the chosen permutation
      generateYellowSniperPermutations(lockedLetters, sniperLetters, sniperSpots, sniperUsed, new ArrayList<>(),
              greyAndIffyLetters, permutationToBuild, 0, allMovablePositions);


    } else {
      boolean emptyIsUsed = false;
      for (int i = 0; i < lockedLetters.size(); i++) {
        Optional<Letter> letterOrEmpty = lockedLetters.get(i);
        //checks if the letter is not used yet
        if (!lockedUsed.get(i)) {
          //checks if the letter is not in its original position
          if ((letterOrEmpty.isPresent()
                  && !letterOrEmpty.get().equals(waffle.getLetterAt(allMovablePositions.get(index))))) {
            //recursively call this method with updated data
            lockedUsed.set(i, true); //set the letter to used
            permutationToBuild.add(letterOrEmpty); //add the letter to the permutation
            generateYellowLockedPermutations(lockedLetters, lockedUsed, index + 1, sniperLetters, sniperSpots,
                    greyAndIffyLetters, permutationToBuild, allMovablePositions);

            //backtrack
            permutationToBuild.remove(letterOrEmpty);
            lockedUsed.set(i, false);
          } else if (letterOrEmpty.isEmpty() && !emptyIsUsed) {
            //if the spot is empty, except set all the empties used to true
            lockedUsed.set(i, true);
            emptyIsUsed = true; //an empty should only be iterated over once, since it will
            //end up being the same permutations anyways.

            permutationToBuild.add(letterOrEmpty);
            generateYellowLockedPermutations(lockedLetters, lockedUsed, index + 1,
                    sniperLetters, sniperSpots,
                    greyAndIffyLetters, permutationToBuild, allMovablePositions);

            //backtrack
            permutationToBuild.remove(letterOrEmpty);
            lockedUsed.set(i, false);


          }
        }
      }
    }
  }

  /**
   * TODO
   * @param lockedLetters
   * @param sniperLetters
   * @param sniperSpots
   * @param used
   * @param usedSniperSpots
   * @param greyAndIffyLetters
   * @param permutationToBuild
   * @param index
   * @param allMovablePositions
   */
  private void generateYellowSniperPermutations(List<Optional<Letter>> lockedLetters,
                                                List<Letter> sniperLetters, List<Posn> sniperSpots,
                                                List<Boolean> used,
                                                List<Posn> usedSniperSpots,
                                                List<Letter> greyAndIffyLetters,
                                                List<Optional<Letter>> permutationToBuild, int index,
                                                List<Posn> allMovablePositions) {
    if (index == sniperLetters.size()) {
      //move on to next step
      System.out.print("Sniper permutation: ");
      for (Optional<Letter> letterOrEmpty : permutationToBuild) {
        if (letterOrEmpty.isPresent()) {
          System.out.print(letterOrEmpty.get() + " ");
        } else {
          System.out.print("Empty ");
        }
      }
      System.out.println();
    } else {
      //Letter curSniper = sniperLetters.get(index);
      //if (wordPosns.get(1).getY() - wordPosns.get(0).getY() == 1)
      //PROBLEM: multiple snipers can share the same spot that it can appear in. find a way to
      //create permutations that don't conflict with each other.
      //PROBLEM: how do you know the "sniping spot"? consider storing the data of the sniping spot
      //of each sniper letter before passing it as an argument, possibly as hashmap?
      for (int i = index; i < sniperLetters.size(); i++) {
        if (!used.get(i)) {

          Posn chosenSniperSpot = sniperSpots.get(i);
          if (!usedSniperSpots.contains(chosenSniperSpot)) {
            int indexOfSniperSpot = allMovablePositions.indexOf(chosenSniperSpot);

            Optional<Letter> letterAtSniperSpot = permutationToBuild.get(indexOfSniperSpot);

            //checks if it isn't already filled by a yellow letter from the previous step
            if (letterAtSniperSpot.isEmpty()) {
              //sets the chosen sniper letter's sniper spot to the given sniper letter.
              permutationToBuild.set(indexOfSniperSpot,
                      Optional.of(sniperLetters.get(i)));
              usedSniperSpots.add(chosenSniperSpot);
              used.set(i, true);

              //recursively call sniper permutation function
              generateYellowSniperPermutations(lockedLetters, sniperLetters, sniperSpots,
                      used, usedSniperSpots, greyAndIffyLetters, permutationToBuild, index + 1,
                      allMovablePositions);

              //backtrack
              permutationToBuild.set(indexOfSniperSpot, letterAtSniperSpot);
              usedSniperSpots.remove(chosenSniperSpot);

              //recursively call sniper permutation function without the sniper spot being used
              generateYellowSniperPermutations(lockedLetters, sniperLetters, sniperSpots,
                      used, usedSniperSpots, greyAndIffyLetters, permutationToBuild, index + 1,
                      allMovablePositions);
              used.set(i, false);
            } else {
              //if the sniper spot is filled by a yellow letter from a previous step, recursively
              //call without any changes in the permutation.
              used.set(i, true);
              generateYellowSniperPermutations(lockedLetters, sniperLetters, sniperSpots,
                      used, usedSniperSpots, greyAndIffyLetters, permutationToBuild, index + 1,
                      allMovablePositions);
              used.set(i, false);
            }
          } else {
            // if the sniper spot is already used, recursively call, but without any changes in the
            // permutation, just increment index by 1
            used.set(i, true);
            generateYellowSniperPermutations(lockedLetters, sniperLetters, sniperSpots,
                    used, usedSniperSpots, greyAndIffyLetters, permutationToBuild, index + 1,
                    allMovablePositions);
            used.set(i, false);
          }
        }
      }
    }
  }



  private boolean wordIsHorizontal(List<Posn> wordLetterPosns) {
    if (wordLetterPosns.get(1).getX() - wordLetterPosns.get(0).getX() == 1) {
      return true;
    } else if (wordLetterPosns.get(1).getY() - wordLetterPosns.get(0).getY() == 1) {
      return false;
    }
    throw new IllegalArgumentException("Word is not horizontal or vertical");
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
        //check if letter is not in the word itself
        if (yellowLetter.getPosition().getY() != wordPosns.get(0).getY()) {
          return waffle.getLetterAt(
                  new Posn(yellowLetter.getPosition().getX(), wordPosns.get(0).getY())).getState()
                  != LetterState.GREEN;
        }
        return false;
      }
      return false;
    } else {
      throw new IllegalArgumentException("Word is not horizontal or vertical");
    }
  }



}
