import java.util.Objects;

//used to store letter information (the character it represents, and the state it's in)
public class Letter {

  private final char letter;
  private final LetterState state;
  private Posn position;

  public Letter(char letter, LetterState state, Posn position) {
    this.letter = letter;
    this.state = state;
    this.position = position;
  }

  public Letter(Letter other) {
    this.letter = other.getLetter();
    this.state = other.getState();
    this.position = other.position;
  }

  public char getLetter() {
    return this.letter;
  }

  public LetterState getState() {
    return this.state;
  }

  public Posn getPosition() {
    return this.position;
  }

  @Override
  public String toString() {
    return letter + " " + state.name() + " " + this.position;
  }

  @Override
  public boolean equals(Object other) {
    if (other instanceof Letter) {
      Letter otherLetter = (Letter) other;
      return otherLetter.letter == this.letter && otherLetter.state == this.state
              && otherLetter.position.equals(this.position);
    } else {
      return false;
    }
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.letter, this.state, this.position);
  }
}
