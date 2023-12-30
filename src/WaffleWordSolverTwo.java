import java.sql.Array;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class WaffleWordSolverTwo implements WaffleWordSolverInterface {

  private WaffleInterface waffle;
  private List<Letter> lettersToMove; //might not need this actually.

  //contains data of the positions of every letter in
  //every word, in order of how they are read.
  private List<List<Posn>> positionsOfWords;

  private boolean solutionIsFound;

  private WaffleInterface solution;

  public WaffleWordSolverTwo(WaffleInterface waffle) {
    this.waffle = waffle;
    this.solutionIsFound = false;
    this.lettersToMove = new ArrayList<>();
    for (Posn p : waffle.getPositionsToShift()) {
      lettersToMove.add(waffle.getLetterAt(p));
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
    fillWord(0, lettersToMove, new WaffleImpl(waffle));
    return solution;
  }


  /**
   * Goes through all possible permutations of the given word and fills the letters to change with
   * all possible permutations of the unused letters.
   * @param wordNumber - the current word number. corresponds to the index of the positionsOfWords
   *                   instance variable, which represents the current word to iterate.
   * @param lettersUnused - list of Letter objects that are currently unused.
   * @param waffleCopy - a copy of the waffle object.
   */
  private void fillWord(int wordNumber, List<Letter> lettersUnused, WaffleImpl waffleCopy) {
    //debug
    System.out.println("WORD NUMBER: " + wordNumber);
    for (Letter letter : lettersUnused) {
      System.out.print(letter + " ");
    }
    System.out.println();
    System.out.println(waffleCopy);

    //ALL 6 WORDS WERE FOUND, A SOLUTION IS FOUND
    if (wordNumber == 6) {
      this.solution = waffleCopy;
      System.out.println("SOLUTION");
      System.out.println(solution);

      this.solutionIsFound = true; //terminate all other branches of recursion once solution is found

    } else {

      //prepare for STEP 1
      //list of all positions in the current word that are grey or yellow, and need to be changed to
      //form the correct word.
      List<Posn> allMovablePositions = positionsOfWords.get(wordNumber)
              .stream().map(waffleCopy::getLetterAt)
              .filter(letter -> letter.getState() != LetterState.GREEN)
              .map(Letter::getPosition).collect(Collectors.toList());

      //sort all letters into 3 categories: sniper letters, locked letters, grey letters, and
      //iffy letters
      List<Letter> sniperLetters = new ArrayList<>();
      List<Posn> sniperSpots = new ArrayList<>();
      List<Optional<Letter>> lockedLetters = new ArrayList<>();
      List<Letter> greyAndIffyLetters = new ArrayList<>();

      //iterate through all the unused letters and sort them.
      for (Letter letter : lettersUnused) {

        //if the letter is yellow
        if (letter.getState() == LetterState.YELLOW) {

          //if the letter is contained within the movable positions
          if (allMovablePositions.contains(letter.getPosition())) {
            //if the yellow letter can be placed into more than one word, it is an iffy letter,
            //add it to the grey and iffy yellow letters list. if not, add it to locked yellow letters
            // list.
            if (this.yellowLetterCanBePlacedInMoreThanOneWord(letter, wordNumber)) {
              greyAndIffyLetters.add(letter);
            } else {
              lockedLetters.add(Optional.of(letter));
            }
          }
          //else if the yellow letter is a sniper letter, add it to the sniper list along with its
          //corresponding sniper spot.
          else if (this.yellowLetterIsSniperForWord(positionsOfWords.get(wordNumber), letter, allMovablePositions)) {
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
          //else if it is a yellow letter, and it appears in one of the green spots, it is the niche
          //scenario where the iffy letter becomes a locked letter.
          else if (positionsOfWords.get(wordNumber).contains(letter.getPosition())) {
            lockedLetters.add(Optional.of(letter));
          }
        }
        //else if it is gray, add it to the grey and iffy letters list.
        else if (letter.getState() == LetterState.GREY) {
          greyAndIffyLetters.add(letter);
        }
      }

      //TODO: deal with the edge case where a yellow letter can appear in the space of a green letter
      //  as a result of an iffy yellow letter being solved along one word, but that letter wasn't used.
      //  This means the iffy letter becomes a locked letter, and it must appear along the horizontal.

      //this occurs when two or more iffy letters went unused in their respective words, and are
      // forced to be used in the same word. If this happens, do not generate permutations, as it
      // is invalid.
      if (lockedLetters.size() > allMovablePositions.size()) {
        return;
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

      //DEBUG STUFF
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

      //call step 1
      generateYellowLockedPermutations(lettersUnused, wordNumber, waffleCopy, lockedLetters, lockedUsed, 0,
              sniperLetters, sniperSpots, greyAndIffyLetters, new ArrayList<>(), allMovablePositions);

    }

  }


  /**
   * STEP 1 in generated all permutations of a word. Iterates through all possible placements of
   * "locked letters", or yellow letters that MUST appear in the word.
   * @param allUnusedLetters - NOT USED IN THIS STEP
   * @param wordNumber - NOT USED IN THIS STEP
   * @param waffleCopy - NOT USED IN THIS STEP
   * @param lockedLetters - a list of all locked letter, or an optional empty.
   * @param lockedUsed - a list of boolean denoting which "letters" have been used already.
   * @param index - the current index to place an item.
   * @param sniperLetters - NOT USED IN THIS STEP (see step 2)
   * @param sniperSpots - NOT USED IN THIS STEP (see step 2)
   * @param greyAndIffyLetters - NOT USED IN THIS STEP (see step 3)
   * @param permutationToBuild - a list of the permutation that, by the end of the recursion, will
   *                           build the permutation needed to move to the next step.
   * @param allMovablePositions - NOT USED IN THIS STEP
   */
  private void generateYellowLockedPermutations(
          List<Letter> allUnusedLetters,
          int wordNumber, WaffleImpl waffleCopy,
          List<Optional<Letter>> lockedLetters, List<Boolean> lockedUsed, int index,
          List<Letter> sniperLetters,
          List<Posn> sniperSpots,
          List<Letter> greyAndIffyLetters,
          List<Optional<Letter>> permutationToBuild,
          List<Posn> allMovablePositions) {
    if (this.solutionIsFound) {
      return;
    }
    if (index == lockedLetters.size()) {
      //END OF STEP 1, MOVE TO STEP 2

      //prepare arguments for step 2
      List<Boolean> sniperUsed = new ArrayList<>();
      for (int i = 0; i < sniperLetters.size(); i++) {
        sniperUsed.add(false);
      }

      //for debugging
      System.out.println("WORD NUMBER: " + wordNumber);
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
      generateYellowSniperPermutations(allUnusedLetters, wordNumber, waffleCopy, lockedLetters, sniperLetters, sniperSpots, sniperUsed, new ArrayList<>(),
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
            generateYellowLockedPermutations(allUnusedLetters, wordNumber, waffleCopy, lockedLetters, lockedUsed, index + 1, sniperLetters, sniperSpots,
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
            generateYellowLockedPermutations(allUnusedLetters, wordNumber, waffleCopy, lockedLetters, lockedUsed, index + 1,
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
   * STEP 2 in calculating permutations, calculates all permutations of "sniper letters", or yellow
   * letters that can possibly appear at one specific spot in the current word, or the "sniper spot"
   * Takes all sniper letters and iterates through all permutations where the sniper letter does
   * appear in its respective sniper spot, and all permutations where it does not appear.
   * @param allUnusedLetters - NOT USED IN THIS STEP
   * @param wordNumber - NOT USED IN THIS STEP
   * @param waffleCopy - NOT USED IN THIS STEP
   * @param lockedLetters - NOT USED IN THIS STEP
   * @param sniperLetters - all "sniper letters" for the given word, or all yellow letters that are
   *                      not located within the word, but may appear at one spot in the word.
   * @param sniperSpots - a list of positions of all "sniper spots", index of sniper spot corresponds
   *                    to the index of the sniper letter in sniperLetters
   * @param used - list of Letter of all used sniper letters.
   * @param usedSniperSpots - list of Posn of all used sniper spots.
   * @param greyAndIffyLetters - NOT USED IN THIS STEP
   * @param permutationToBuild - a list of the permutation that, by the end of the recursion, will
   *                            build the permutation needed to move to the next step.
   * @param index - the current index of the item in the permutation to change.
   * @param allMovablePositions - list of all positions to move the letters.
   */
  private void generateYellowSniperPermutations(
          List<Letter> allUnusedLetters,
          int wordNumber, WaffleImpl waffleCopy,
          List<Optional<Letter>> lockedLetters,
          List<Letter> sniperLetters, List<Posn> sniperSpots,
          List<Boolean> used,
          List<Posn> usedSniperSpots,
          List<Letter> greyAndIffyLetters,
          List<Optional<Letter>> permutationToBuild, int index,
          List<Posn> allMovablePositions) {
    if (this.solutionIsFound) {
      return;
    }
    if (index == sniperLetters.size()) {
      //move on to next step

      //debugging
      /*
      System.out.println("WORD NUMBER: " + wordNumber);
      System.out.print("Sniper permutation: ");
      for (Optional<Letter> letterOrEmpty : permutationToBuild) {
        if (letterOrEmpty.isPresent()) {
          System.out.print(letterOrEmpty.get() + " ");
        } else {
          System.out.print("Empty ");
        }
      }
      System.out.println();*/

      //prepare for next step
      List<Boolean> usedGreyIffy = new ArrayList<>();
      for (int i = 0; i < greyAndIffyLetters.size(); i++) {
        usedGreyIffy.add(false);
      }

      //call next step
      generateGreyAndIffyPermutations(allUnusedLetters, wordNumber, waffleCopy, lockedLetters, sniperLetters, greyAndIffyLetters,
              usedGreyIffy, permutationToBuild, 0, allMovablePositions);

    } else {

      //iterate through all sniper letters
      for (int i = index; i < sniperLetters.size(); i++) {
        if (!used.get(i)) {
          used.set(i, true);

          //get the letter at the corresponding sniper spot
          Posn chosenSniperSpot = sniperSpots.get(i);
          int indexOfSniperSpot = allMovablePositions.indexOf(chosenSniperSpot);
          Optional<Letter> letterAtSniperSpot = permutationToBuild.get(indexOfSniperSpot);

          //checks if sniper spot isn't already used by another sniper letter and
          //checks if the sniper spot isn't already filled by a yellow letter from the previous step
          if (!usedSniperSpots.contains(chosenSniperSpot) && letterAtSniperSpot.isEmpty()) {

            //sets the chosen sniper letter's sniper spot to the given sniper letter.
            permutationToBuild.set(indexOfSniperSpot,
                    Optional.of(sniperLetters.get(i)));
            usedSniperSpots.add(chosenSniperSpot);


            //recursively call sniper permutation function
            generateYellowSniperPermutations(allUnusedLetters, wordNumber, waffleCopy, lockedLetters, sniperLetters, sniperSpots,
                    used, usedSniperSpots, greyAndIffyLetters, permutationToBuild, index + 1,
                    allMovablePositions);

            //backtrack
            permutationToBuild.set(indexOfSniperSpot, letterAtSniperSpot);
            usedSniperSpots.remove(chosenSniperSpot);

            //recursively call sniper permutation function without the sniper spot being used
            generateYellowSniperPermutations(allUnusedLetters, wordNumber, waffleCopy, lockedLetters, sniperLetters, sniperSpots,
                    used, usedSniperSpots, greyAndIffyLetters, permutationToBuild, index + 1,
                    allMovablePositions);
            used.set(i, false);
          } else {
            // if the sniper spot is filled by another sniper letter, or
            // if the sniper spot is filled by a yellow letter from a previous step, recursively
            //call without any changes in the permutation.

            generateYellowSniperPermutations(allUnusedLetters, wordNumber, waffleCopy, lockedLetters, sniperLetters, sniperSpots,
                    used, usedSniperSpots, greyAndIffyLetters, permutationToBuild, index + 1,
                    allMovablePositions);

            //backtrack
            used.set(i, false);
          }
        }
      }
    }
  }

  /**
   * STEP 3 in calculating permutations, fills in the rest of the blanks with grey letters, or "iffy"
   * letters (yellow letters that could appear in the current word or another word). Iterates through
   * all permutations of grey and iffy letters in all blank positions in the current permutation.
   * @param allUnusedLetters - NOT USED IN THIS STEP
   * @param wordNumber - NOT USED IN THIS STEP
   * @param waffleCopy - NOT USED IN THIS STEP
   * @param lockedLetters - NOT USED IN THIS STEP
   * @param sniperLetters - NOT USED IN THIS STEP
   * @param greyAndIffyLetters - all grey and iffy letters of the given word. this step will
   *                           iterate through all items in this list.
   * @param usedGreyIffy - list of boolean corresponding to whether the grey/iffy letter at that
   *                     index was used already or not.
   * @param permutationToBuild - the current permutation to build. by the end of this step, the
   *                           permutation should not contain any empties.
   * @param index - the current index of the item in the permutation to change.
   * @param allMovablePosns - NOT USED IN THIS STEP
   */
  private void generateGreyAndIffyPermutations(
          List<Letter> allUnusedLetters,
          int wordNumber, WaffleImpl waffleCopy,
          List<Optional<Letter>> lockedLetters,
          List<Letter> sniperLetters,
          List<Letter> greyAndIffyLetters,
          List<Boolean> usedGreyIffy,
          List<Optional<Letter>> permutationToBuild,
          int index,
          List<Posn> allMovablePosns) {
    if (this.solutionIsFound) {
      return;
    }
    if (index == permutationToBuild.size()) {
      //move to next step


      /*
      System.out.print("Grey/Iffy Permutation: ");
      for (Optional<Letter> letterOrEmpty : permutationToBuild) {
        if (letterOrEmpty.isPresent()) {
          System.out.print(letterOrEmpty.get() + " ");
        } else {
          System.out.print("Empty ");
        }
      }
      System.out.println();
      */

      //modifies data in the waffle copy and builds the word / verifies it is a valid word
      buildWordFromPermutation(
              allUnusedLetters,
              //filters out all the empty objects of locked letters
              lockedLetters.stream().filter(Optional::isPresent).map(Optional::get).collect(Collectors.toList()),
              sniperLetters, greyAndIffyLetters,
              permutationToBuild, allMovablePosns, wordNumber, waffleCopy);


    } else {
      if (permutationToBuild.get(index).isEmpty()) {
        for (int i = 0; i < greyAndIffyLetters.size(); i++) {
          Letter chosenGreyIffy = greyAndIffyLetters.get(i);
          if (!usedGreyIffy.get(i)) {
            usedGreyIffy.set(i, true);
            Optional<Letter> oldLetterOrEmpty = permutationToBuild.get(index);
            permutationToBuild.set(index, Optional.of(chosenGreyIffy));

            generateGreyAndIffyPermutations(allUnusedLetters, wordNumber, waffleCopy, lockedLetters, sniperLetters,
                    greyAndIffyLetters, usedGreyIffy, permutationToBuild, index + 1, allMovablePosns);

            //backtrack
            usedGreyIffy.set(i, false);
            permutationToBuild.set(index, oldLetterOrEmpty);
          }
        }
      } else {
        generateGreyAndIffyPermutations(allUnusedLetters, wordNumber, waffleCopy, lockedLetters, sniperLetters,
                greyAndIffyLetters, usedGreyIffy, permutationToBuild, index + 1, allMovablePosns);
      }

    }
  }

  /**
   * STEP 4 in calculating permutations. Replaces all letters in the waffle copy with the letters
   * from the permutation. Checks if the word formed is a valid word. If yes, move on to the next
   * step, and if not, drop the permutation (do nothing).
   * @param allUnusedLetters - NOT USED IN THIS STEP
   * @param lockedLetters - NOT USED IN THIS STEP
   * @param sniperLetters - NOT USED IN THIS STEP
   * @param greyAndIffyLetters - NOT USED IN THIS STEP
   * @param permutationToBuild - the current permutation to build the word from.
   * @param allMovablePositions - list of positions, where each item is the position in the waffle
   *                            to change the letter. each position's index corresponds to the index
   *                            of the letter in the permutation to build.
   * @param wordNumber - the current word number
   * @param waffleCopy - a copy of the waffle
   */
  private void buildWordFromPermutation(
          List<Letter> allUnusedLetters,
          List<Letter> lockedLetters, List<Letter> sniperLetters, List<Letter> greyAndIffyLetters,
          List<Optional<Letter>> permutationToBuild,
          List<Posn> allMovablePositions,
          int wordNumber, WaffleImpl waffleCopy) {
    if (this.solutionIsFound) {
      return;
    }

    //go through every letter in the permutation and replace letters in the waffle copy with the
    //new chosen letters.
    for (int i = 0; i < permutationToBuild.size(); i++) {
      if (permutationToBuild.get(i).isEmpty()) {
        throw new IllegalStateException("Permutation cannot have any empty slots at this stage");
      } else {
        Letter letterAt = permutationToBuild.get(i).get();
        waffleCopy.replaceLetter(allMovablePositions.get(i), letterAt);
      }
    }

    //create the word from the select positions
    List<Posn> wordPosns = positionsOfWords.get(wordNumber);
    List<Letter> wordLetters = new ArrayList<>();
    for (Posn p : wordPosns) {
      wordLetters.add(waffleCopy.getLetterAt(p));
    }

    //validate the word, and recursively call fillWord
    if (WordValidator.isValidWord(wordLetters)) {

      //for debugging
      System.out.println("Valid word: ");
      for (Letter l : wordLetters) {
        System.out.print(l + " ");
      }
      System.out.println();

      //make a shallow copy of all unused letters to feed into the next step (the next step will
      //make changes to the list, so this should prevent any aliasing problems between
      //different permutations)
      List<Letter> unusedCopy = new ArrayList<>();
      for (Letter l : allUnusedLetters) {
        unusedCopy.add(l);
      }

      //MOVE TO NEXT STEP
      prepareToFillNextWord(
              new ArrayList<>(unusedCopy),
              lockedLetters, sniperLetters, greyAndIffyLetters,
              wordNumber, new WaffleImpl(waffleCopy));
    }
  }

  /**
   * STEP 5 in calculating permutation. Changes all the letters in the waffle to green, and removes
   * the letters from the unused list, then calls fillWord on the next word.
   * @param allUnusedLetters - list of all unused letters up to this point. this step will modify
   *                         this list to remove all the used letters.
   * @param lockedLetters - NOT USED IN THIS STEP
   * @param sniperLetters - NOT USED IN THIS STEP
   * @param greyAndIffyLetters - NOT USED IN THIS STEP
   * @param wordNumber - the current word number
   * @param waffleCopy - a copy of the waffle object.
   */
  private void prepareToFillNextWord(
          List<Letter> allUnusedLetters,
          List<Letter> lockedLetters, List<Letter> sniperLetters, List<Letter> greyAndIffyLetters,
          int wordNumber, WaffleImpl waffleCopy) {
    if (this.solutionIsFound) {
      return;
    }
    //remove all used letters from the list
    //turn all the letters green in the word
    for (Posn p : positionsOfWords.get(wordNumber)) {
      Letter oldLetterAt = waffleCopy.getLetterAt(p);
      if (allUnusedLetters.remove(oldLetterAt)) {
        System.out.println("Removed: " + oldLetterAt + " ");
      }
      Letter newLetter = new Letter(oldLetterAt.getLetter(), LetterState.GREEN, p);
      waffleCopy.replaceLetter(p, newLetter);
    }

    System.out.println("Letters to move: ");
    for (Letter letter : allUnusedLetters) {
      System.out.print(letter + " ");
    }
    System.out.println();

    fillWord(wordNumber + 1, allUnusedLetters, waffleCopy);
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
  private boolean yellowLetterCanBePlacedInMoreThanOneWord(Letter yellowLetter, int wordNumber) {
    if (yellowLetter.getState() != LetterState.YELLOW) {
      throw new IllegalArgumentException("Checking non-yellow letter");
    }
    Posn posOfLetter = yellowLetter.getPosition();

    //checks through all positions of letters that have already been solved.
    //if the yellow letter's position occurs in one of these spots, even though the word has solved already,
    //that means that it was an iffy yellow letter that did not get placed into the previous word.
    //the program will return false, since now that means the iffy letter is now locked to the
    //second possible word it could be placed in.
    for (int i = 0; i < wordNumber; i++) {

      for (Posn p : positionsOfWords.get(i)) {
        System.out.print(p + " ");
        if (yellowLetter.getPosition().equals(p)) {
          return false;
        }
      }
    }

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
  private boolean yellowLetterIsSniperForWord(List<Posn> wordPosns, Letter yellowLetter, List<Posn> allMovablePositions) {
    //if the word is vertical
    if (wordPosns.get(1).getY() - wordPosns.get(0).getY() == 1) {
      //if it is located in a word that is horizontal
      if (yellowLetter.getPosition().getY() % 2 == 0) {
        //check if letter is not in the word itself
        if (yellowLetter.getPosition().getX() != wordPosns.get(0).getX()) {
          //checks if the position in the word in which the sniper letter could appear isn't already
          //taken by a green space. if it is grey or yellow, that means the sniper letter can appear
          //there, and thus returns true.
          Posn sniperSpot = new Posn(wordPosns.get(0).getX(), yellowLetter.getPosition().getY());
          return allMovablePositions.contains(sniperSpot)
                  && waffle.getLetterAt(sniperSpot).getState() != LetterState.GREEN;
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
          Posn sniperSpot = new Posn(yellowLetter.getPosition().getX(), wordPosns.get(0).getY());
          return allMovablePositions.contains(sniperSpot)
                  && waffle.getLetterAt(sniperSpot).getState() != LetterState.GREEN;
        }
        return false;
      }
      return false;
    } else {
      throw new IllegalArgumentException("Word is not horizontal or vertical");
    }
  }


}
