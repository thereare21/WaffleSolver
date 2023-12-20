import java.util.Objects;

//used to store letter information (the character it represents, and the state it's in)
public class Letter {

  private final char letter;
  private final LetterState state;

  public Letter(char letter, LetterState state) {
    this.letter = letter;
    this.state = state;
  }

  public Letter(Letter other) {
    this.letter = other.getLetter();
    this.state = other.getState();
  }

  public char getLetter() {
    return this.letter;
  }

  public LetterState getState() {
    return this.state;
  }

  @Override
  public String toString() {
    return letter + " " + state.name();
  }

  @Override
  public boolean equals(Object other) {
    if (other instanceof Letter) {
      Letter otherPosn = (Letter) other;
      return otherPosn.letter == this.letter && otherPosn.state == this.state;
    } else {
      return false;
    }
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.letter, this.state);
  }
}
