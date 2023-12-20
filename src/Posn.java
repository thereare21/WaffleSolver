import java.util.Objects;

public class Posn {
  private final int x;
  private final int y;

  public Posn(int x, int y) {
    this.x = x;
    this.y = y;
  }

  public Posn(Posn other) {
    this.x = other.x;
    this.y = other.y;
  }

  public int getX() {
    return x;
  }

  public int getY() {
    return y;
  }

  @Override
  public String toString() {
    return "(" + x + ", " + y + ")";
  }

  @Override
  public boolean equals(Object other) {
    if (other instanceof Posn) {
      Posn otherPosn = (Posn) other;
      return otherPosn.x == this.x && otherPosn.y == this.y;
    } else {
      return false;
    }
  }

  @Override
  public int hashCode() {
    return Objects.hash(x, y);
  }
}
