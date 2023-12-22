import java.sql.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class WaffleImpl implements WaffleInterface {

  private Map<Posn, Letter> letterMap;

  /**
   * Constructs a Waffle board from a string of letters and a string of the states of each letter.
   * The index of the character in the state string corresponds to the index of the character in
   * the letter string.
   * For example, if the first character in letterString was 'A', then the first character in
   * stateString will represent its state.
   *
   * Initializes in this order:
   *  1  2  3  4  5
   *  6     7     8
   *  9  10 11 12 13
   *  14    15    16
   *  17 18 19 20 21
   *
   * @param letterString
   * @param stateString
   */
  public WaffleImpl(String letterString, String stateString) {
    if (letterString.length() != 21 || stateString.length() != 21) {
      throw new IllegalArgumentException("Waffle size must be 21");
    }
    letterMap = new HashMap<>();

    int curX = 0;
    int curY = 0;

    for (int i = 0; i < 21; i++) {
      char curLetter = letterString.charAt(i);
      char curStateLetter = stateString.charAt(i);
      LetterState curState;
      switch(curStateLetter) {
        case 'g':
          curState = LetterState.GREEN;
          break;
        case 'y':
          curState = LetterState.YELLOW;
          break;
        case 'e':
          curState = LetterState.GREY;
          break;
        default:
          throw new IllegalArgumentException("Cannot recognize the state in given state string");
      }
      letterMap.put(new Posn(curX, curY), new Letter(curLetter, curState, new Posn(curX, curY)));

      //tick up curX and curY appropriately
      if (curX == 4) {
        curX = 0;
        curY++;
      } else {
        curX++;
        //skips over the "odd" spaces (1, 1), (1, 3), etc. since those are empty spaces.
        if (curX % 2 == 1 && curY % 2 == 1) {
          curX++;
        }
      }
    }

    for (Map.Entry<Posn, Letter> entry : letterMap.entrySet()) {
      Posn key = entry.getKey();
      Letter value = entry.getValue();
      System.out.println("Key: " + key + ", Value: " + value);
    }
  }

  public WaffleImpl(WaffleInterface other) {
    if (other == null) {
      throw new IllegalArgumentException("Object to copy is null");
    }
    this.letterMap = new HashMap<>();
    int curX = 0;
    int curY = 0;
    for (int i = 0; i < 21; i++) {
      letterMap.put(new Posn(curX, curY), other.getLetterAt(new Posn(curX, curY)));

      //tick up curX and curY appropriately
      if (curX == 4) {
        curX = 0;
        curY++;
      } else {
        curX++;
        //skips over the "odd" spaces (1, 1), (1, 3), etc. since those are empty spaces.
        if (curX % 2 == 1 && curY % 2 == 1) {
          curX++;
        }
      }
    }

  }

  @Override
  public Letter getLetterAt(Posn pos) {
    Letter toReturn = letterMap.get(pos);
    if (toReturn == null) {
      throw new IllegalArgumentException("Position doesn't exist in the waffle");
    }
    return toReturn;
  }

  @Override
  public List<Posn> getPositionsToShift() {
    List<Posn> toReturn = new ArrayList<>();
    for (Map.Entry<Posn, Letter> entry : letterMap.entrySet()) {
      Posn key = entry.getKey();
      Letter value = entry.getValue();
      if (value.getState() != LetterState.GREEN) {
        toReturn.add(key);
      }
    }
    return toReturn;
  }

  @Override
  public void replaceLetter(Posn pos, Letter newLetter) {
    //this guarantees we don't add a new letter at a new position
    if (letterMap.get(pos) == null) {
      throw new IllegalArgumentException("No letter at the given position");
    }
    letterMap.put(pos, newLetter);
  }

  @Override
  public List<Posn> getValidPlacementsOfLetter(Letter letter) {
    //TODO: POTENTIAL ERROR: what if there are two of the same yellow letters? How does it know
    //  to differentiate the position between the two?
    //  Consider having Letters contain a Posn of where it's located
    //    Yeah there's more to keep track of but it might be for the better.
    //    Sucks that we have to call this method basically knowing the position of the letter
    //      In that case can we make this static, and not reliant on any specific letter then?


    Letter letterAt = letterMap.get(letter.getPosition());
    if (!letterAt.equals(letter)) {
      throw new IllegalArgumentException("Cannot find the given letter");
    }
    Posn posnOfLetter = letter.getPosition();

    if (letter.getState() == LetterState.YELLOW) {
      List<Posn> toReturn = new ArrayList<>();
      //letter is located on a row
      if (posnOfLetter.getX() % 2 == 0) {
        for (int y = 0; y < 5; y++) {
          toReturn.add(new Posn(posnOfLetter.getX(), y));
        }
      }
      //letter is located on a column
      if (posnOfLetter.getY() % 2 == 0) {
        for (int x = 0; x < 5; x++) {
          toReturn.add(new Posn(x, posnOfLetter.getY()));
        }
      }
      //removes repeats (if any)
      toReturn =  toReturn.stream().distinct().collect(Collectors.toList());
      return toReturn;
    } else if (letter.getState() == LetterState.GREY) {
      //not sure if I need this, but can easily get rid of this case
      return this.getPositionsToShift();
    } else {
      throw new IllegalArgumentException("Cannot check placements of a green letter");
    }
  }

  @Override
  public String toString() {
    StringBuilder s = new StringBuilder();
    int curX = 0;
    int curY = 0;
    for (int i = 0; i < letterMap.size(); i++) {
      Letter letterAt = letterMap.get(new Posn(curX, curY));
      s.append(letterAt.getLetter()).append(" ");
      if (curX == 4) {
        curX = 0;
        curY++;
        s.append("\n");
      } else {
        curX++;
        //skips over the "odd" spaces (1, 1), (1, 3), etc. since those are empty spaces.
        if (curX % 2 == 1 && curY % 2 == 1) {
          s.append("  ");
          curX++;
        }
      }
    }


    return s.toString();
  }
}
