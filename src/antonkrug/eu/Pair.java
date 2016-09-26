package antonkrug.eu;

/**
 * Simple container for tuples, to be able to return back from methods a return
 * code and return string with message at the same time.
 * 
 * @author  Anton Krug
 * @date    26.9.2016
 * @version 1
 */
public class Pair<F, S> {

  private final F first;
  private final S second;

  public Pair(F left, S right) {
    this.first = left;
    this.second = right;
  }

  public F getLeft() {
    return first;
  }

  public S getRight() {
    return second;
  }

  @Override
  public int hashCode() {
    return first.hashCode() ^ second.hashCode();
  }

  @SuppressWarnings("rawtypes")
  @Override
  public boolean equals(Object o) {
    if (!(o instanceof Pair)) return false;
    Pair pairo = (Pair) o;
    return this.first.equals(pairo.getLeft()) && this.second.equals(pairo.getRight());
  }

}
