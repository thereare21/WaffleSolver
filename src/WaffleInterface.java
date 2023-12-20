import java.util.List;

public interface WaffleInterface {

  /**
   * Returns the letter located at the given position.
   * @param pos - position to retrieve the letter at.
   * @return - the letter at the given position.
   */
  Letter getLetterAt(Posn pos);

  /**
   * Returns a list of all positions that contains letters that need to be moved (yellow and grey
   * letters)
   * @return - list of positions of all yellow and grey tiles.
   */
  List<Posn> getPositionsToShift();

  /**
   * Replaces the letter at the given position with the new given letter.
   * @param pos - the position to replace
   * @param newLetter - the new letter.
   */
  void replaceLetter(Posn pos, Letter newLetter);

  /**
   * Returns a list of all valid positions a grey or yellow letter can move to. Yellow letters can
   * only move in a certain row or column, while grey letters can be placed anywhere.
   * @param letter - a grey or yellow letter
   * @return - a list of valid placement positions the letter can go in.
   */
  List<Posn> getValidPlacementsOfLetter(Letter letter);
}
