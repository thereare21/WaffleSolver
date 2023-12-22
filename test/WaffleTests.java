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

}
