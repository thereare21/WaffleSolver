import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class WaffleTests {

  @Test
  public void testWaffleInitialization() {
    //archive waffle #3
    WaffleInterface waffle = new WaffleImpl(
        "speedatptocirnempeiey", "gggegeeyyegeyegegyyeg");
    Assert.assertTrue(true);
  }

  @Test
  public void getLetterAtException() {
    WaffleInterface waffle = new WaffleImpl(
            "speedatptocirnempeiey", "gggegeeyyegeyegegyyeg");
    Assert.assertThrows(
            IllegalArgumentException.class,
            () -> waffle.getLetterAt(new Posn(1, 1)));
    Assert.assertThrows(
            IllegalArgumentException.class,
            () -> waffle.getLetterAt(new Posn(1, 3)));
    Assert.assertThrows(
            IllegalArgumentException.class,
            () -> waffle.getLetterAt(new Posn(3, 1)));
    Assert.assertThrows(
            IllegalArgumentException.class,
            () -> waffle.getLetterAt(new Posn(3, 3)));
  }

  @Test
  public void testGetLetterAtWorks() {
    WaffleInterface waffle = new WaffleImpl(
            "speedatptocirnempeiey", "gggegeeyyegeyegegyyeg");
    Assert.assertEquals(
        waffle.getLetterAt(new Posn(0, 0)).getLetter(), 's'
    );
  }

  @Test
  public void testGetPositionsToShiftWorks() {
    WaffleInterface waffle = new WaffleImpl(
            "speedatptocirnempeiey", "gggegeeyyegeyegegyyeg");
    Assert.assertTrue(waffle.getPositionsToShift().contains(new Posn(0, 3)));
    Assert.assertFalse(waffle.getPositionsToShift().contains(new Posn(0, 0)));

    for (Posn p : waffle.getPositionsToShift()) {
      System.out.println(p);
    }
  }

  @Test
  public void testValidPlacementsOfLetterWorks() {
    WaffleInterface waffle = new WaffleImpl(
            "speedatptocirnempeiey", "gggegeeyyegeyegegyyeg");
    List<Posn> validPlacements = waffle.getValidPlacementsOfLetter(new Letter('i', LetterState.YELLOW, new Posn(2, 4)));
    for (Posn p: validPlacements) {
      System.out.println(p);
    }
    System.out.println(waffle);
  }

  @Test
  public void testValidPlacementsMultipleSameYellowLetters() {
    //from archive waffle #4
    WaffleInterface waffle = new WaffleImpl(
            "ndeeyeeltraeckaidnsks", "gygygyyeeegeyyeygeyeg");
    System.out.println(waffle);
    List<Posn> validPlacements = waffle.getValidPlacementsOfLetter(new Letter('e', LetterState.YELLOW, new Posn(3, 0)));
    for (Posn p: validPlacements) {
      System.out.println(p);
    }
  }

  @Test
  public void testGeneratePermutations() {
    //tests if all permutations are generated given a list of characters
    PermutationsTest.generateAllPermutations(new char[]{'a', 'b', 'c', 'd', 'e'});
    //seems to work fine i guess
  }

  @Test
  public void testLetterEquality() {
    Letter l1 = new Letter('e', LetterState.YELLOW, new Posn(1, 0));
    Letter l2 = new Letter('e', LetterState.YELLOW, new Posn(1, 0));

    Assert.assertEquals(l1, l2);
  }

  @Test
  public void testWaffleCopyConstructorWorks() {
    WaffleInterface waffle = new WaffleImpl(
            "ndeeyeeltraeckaidnsks", "gygygyyeeegeyyeygeyeg");
    WaffleImpl waffleCopy = new WaffleImpl(waffle);
    Assert.assertEquals(waffleCopy.toString(), waffle.toString());
  }

  @Test
  public void testWafflePermutationsWorks() {
    WaffleInterface waffle = new WaffleImpl(
            "ndeeyeeltraeckaidnsks", "gygygyyeeegeyyeygeyeg");
    WaffleWordSolverInterface waffleSolver = new WaffleWordSolver(waffle);
    waffleSolver.solveWaffle();
  }

  @Test
  public void testWaffleSolverPositionsOfWordsWorks() {
    WaffleInterface waffle = new WaffleImpl(
            "ndeeyeeltraeckaidnsks", "gygygyyeeegeyyeygeyeg");
    WaffleWordSolverInterface waffleSolver = new WaffleWordSolverTwo(waffle);
    waffleSolver.solveWaffle();
  }

  @Test
  public void testWaffleSolverFirstStepWorksTwo() {
    WaffleInterface waffle = new WaffleImpl(
            "speedatptocirnempeiey", "gggegeeyyegeyegegyyeg");
    WaffleWordSolverInterface waffleSolver = new WaffleWordSolverTwo(waffle);
    waffleSolver.solveWaffle();
  }

  @Test
  public void testWaffleSolverFirstStepWorksThree() {
    WaffleInterface waffle = new WaffleImpl(
            "crmvperglaivyenbelouy", "ggeegeeyyegeyeeegeyeg");
    WaffleWordSolverInterface waffleSolver = new WaffleWordSolverTwo(waffle);
    waffleSolver.solveWaffle();
  }

  @Test
  public void testYellowLockedSwapped() {
    WaffleInterface waffle = new WaffleImpl(
            "axaxaxxxxxxxxxxxxxxxx", "gygygeeeeeeeeeeeeeeee");
    System.out.println(waffle);
    WaffleWordSolverInterface waffleSolver = new WaffleWordSolverTwo(waffle);
    waffleSolver.solveWaffle();

  }

  @Test
  public void testYellowLockedOneYellowOneGrey() {
    WaffleInterface waffle = new WaffleImpl(
            "axaxaxxxxxxxxxxxxxxxx", "gygegeeeeeeeeeeeeeeee");
    System.out.println(waffle);
    WaffleWordSolverInterface waffleSolver = new WaffleWordSolverTwo(waffle);
    waffleSolver.solveWaffle();
  }

  @Test
  public void testYellowLockedOneYellowTwoGrey() {
    WaffleInterface waffle = new WaffleImpl(
            "xxbxcxxxxxxxxxxxxxxxx", "eegygeeeeeeeeeeeeeeee");
    System.out.println(waffle);
    WaffleWordSolverInterface waffleSolver = new WaffleWordSolverTwo(waffle);
    waffleSolver.solveWaffle();
  }

  @Test
  public void testYellowLockedOneYellowFourGrey() {
    WaffleInterface waffle = new WaffleImpl(
            "xbxxxxxxxxxxxxxxxxxxx", "eyeeeeeeeeeeeeeeeeeee");
    System.out.println(waffle);
    WaffleWordSolverInterface waffleSolver = new WaffleWordSolverTwo(waffle);
    waffleSolver.solveWaffle();
  }


  @Test
  public void testSniperLettersOneSniperLetter() {
    WaffleInterface waffle = new WaffleImpl(
            "aaxaaxxxxxxxxxxxxxbxx", "ggeggeeeeeeeeeeeeeyee");
    WaffleWordSolverInterface waffleSolver = new WaffleWordSolverTwo(waffle);
    waffleSolver.solveWaffle();
  }

  @Test
  public void testSniperLettersTwoSniperLettersSameSpot() {
    WaffleInterface waffle = new WaffleImpl(
            "aaxaaxxxxxxxxxcxxxbxx", "ggeggeeeeeeeeeyeeeyee");
    System.out.println(waffle);
    WaffleWordSolverInterface waffleSolver = new WaffleWordSolverTwo(waffle);
    waffleSolver.solveWaffle();
  }

  @Test
  public void testSniperLettersTwoSniperLettersDifferentSpots() {
    WaffleInterface waffle = new WaffleImpl(
            "xaxaxcxdxxxxxxxxxxbxx", "egegeyeyeeeeeeeeeeyee");
    System.out.println(waffle);
    WaffleWordSolverInterface waffleSolver = new WaffleWordSolverTwo(waffle);
    waffleSolver.solveWaffle();

  }

  @Test
  public void testSniperLettersAndLockedLetterCombo() {
    WaffleInterface waffle = new WaffleImpl(
            "xxxaxcxdxxxxxxxxxxbxx", "eyegeyeyeeeeeeeeeeyee");
    System.out.println(waffle);
    WaffleWordSolverInterface waffleSolver = new WaffleWordSolverTwo(waffle);
    waffleSolver.solveWaffle();
  }

  @Test
  public void testValidWordCanBeFound() {
    WaffleInterface waffle = new WaffleImpl(
            "crvmperglaivyenbelouy", "ggeegeeyyegeyeeegeyeg"
    );
    System.out.println(waffle);
    WaffleWordSolverInterface waffleSolver = new WaffleWordSolverTwo(waffle);
    waffleSolver.solveWaffle();
  }

  @Test
  public void testWaffleArchiveOne() {

    //THIS HAS A SPECIAL EDGE CASE OF TWO E'S TURNING YELLOW AND VYING FOR THE SAME SPOT
    //There are 2 yellow E's at (1, 4) and (2, 3), and it turns out both are suggesting the
    //correct position at (2, 4). Once one E is placed there, the other yellow E turns grey.

    //It will be tricky to account for this because there is no way to know for sure if
    //that the correct position is indeed (2, 4) and only one of them needs to be in that position.
    //Will have to iterate through both scenarios somehow...

    WaffleInterface waffle = new WaffleImpl(
            "fbouegiulsoomgeloemna", "geeggeeegygyyeyegyyeg"
    );
    System.out.println(waffle);
    WaffleWordSolverInterface waffleSolver = new WaffleWordSolverTwo(waffle);
    waffleSolver.solveWaffle();
  }

  @Test
  public void testWaffleArchiveTwo() {
    //works
    WaffleInterface waffle = new WaffleImpl(
            "scgolnndindeeriuffare", "geeeggeyyggeyeyegeyyg"
    );

    System.out.println(waffle);
    WaffleWordSolverInterface waffleSolver = new WaffleWordSolverTwo(waffle);
    waffleSolver.solveWaffle();

  }

  @Test
  public void testWaffleArchiveSix() {
    WaffleInterface waffle = new WaffleImpl(
            "socoprtceatsentamfeoy", "gyyggyyeyegeeeeygeyeg"
    );
    System.out.println(waffle);
    WaffleWordSolverInterface waffleSolver = new WaffleWordSolverTwo(waffle);
    waffleSolver.solveWaffle();

    //EDGE CASE: there could be 2 iffy letters that may appear as locked letters in the same word.
    //  in this case, there were iffy letters in (2, 0) and (2, 4), and the solver attempted to
    //  solve the word in row 0 and row 4 without using either iffy letter. Thus, the iffy letters
    //  became locked in the column 2 word, and it ended up exceeding the movable positions.

    //Would need to find a way to either detect this scenario and just continue if that is the case,
    //or find a way to account for these before sorting all unused letters into locked letters, etc.
  }

  @Test
  public void testWaffleArchiveSeven() {
    WaffleInterface waffle = new WaffleImpl(
            "onwigronrrtcravrtxpee", "gyyygyyeyegeyeeegeyeg"
    );
    System.out.println(waffle);
    WaffleWordSolverInterface waffleSolver = new WaffleWordSolverTwo(waffle);
    waffleSolver.solveWaffle();

    //FAILED: edge case is same as Waffle #6 basically

  }

  @Test
  public void testWaffleArchiveEight() {
    //works
    WaffleInterface waffle = new WaffleImpl(
            "slropamorodchtrontreh", "geyegeegyygeeeeegygeg"
    );
    System.out.println(waffle);
    WaffleWordSolverInterface waffleSolver = new WaffleWordSolverTwo(waffle);
    waffleSolver.solveWaffle();
  }

  @Test
  public void testWaffleArchiveNine() {
    //works
    WaffleInterface waffle = new WaffleImpl(
            "tjmiloragoailnlkneaia", "geegggeyeggeyeeegeyyg"
    );
    System.out.println(waffle);
    WaffleWordSolverInterface waffleSolver = new WaffleWordSolverTwo(waffle);
    waffleSolver.solveWaffle();
  }

  @Test
  public void testWaffleArchiveTen() {
    //works
    WaffleInterface waffle = new WaffleImpl(
            "bnebkneavlidllnvemalt", "gyyegeeeyggeyyeegeyeg"
    );
    System.out.println(waffle);
    WaffleWordSolverInterface waffleSolver = new WaffleWordSolverTwo(waffle);
    waffleSolver.solveWaffle();
  }

  @Test
  public void testWaffle717() {
    //works
    WaffleInterface waffle = new WaffleImpl(
            "lliknelenseuaigttshay", "geyygeyeeygyyyeggeeyg"
    );
    System.out.println(waffle);
    WaffleWordSolverInterface waffleSolver = new WaffleWordSolverTwo(waffle);
    waffleSolver.solveWaffle();
  }

  @Test
  public void testWaffle697() {
    //works
    WaffleInterface waffle = new WaffleImpl(
            "ardatpentiomhlebtjrre", "geyygeyeeygyeyeggeegg"
    );
    System.out.println(waffle);
    WaffleWordSolverInterface waffleSolver = new WaffleWordSolverTwo(waffle);
    waffleSolver.solveWaffle();
  }

  @Test
  public void testWaffle691() {

  }

}

