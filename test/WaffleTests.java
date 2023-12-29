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

    WaffleInterface waffle = new WaffleImpl(
            "fbouegiulsoomgeloemna", "geeggeeegygyyeyegyyeg"
    );
    System.out.println(waffle);
    WaffleWordSolverInterface waffleSolver = new WaffleWordSolverTwo(waffle);
    waffleSolver.solveWaffle();
  }

  @Test
  public void testWaffleArchiveTwo() {
    WaffleInterface waffle = new WaffleImpl(
            "scgolnndindeeriuffare", "geeeggeyyggeyeyegeyyg"
    );

    //ALSO HAS A SPECIAL EDGE CASE

    System.out.println(waffle);
    WaffleWordSolverInterface waffleSolver = new WaffleWordSolverTwo(waffle);
    waffleSolver.solveWaffle();

  }

}
