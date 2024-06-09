package visitor;

public class OffsetInfo {
  private int depth, offset;

  OffsetInfo(int depth, int offset) {
    this.depth = depth;
    this.offset = offset;
  }

  public void setOffset(int offset) {
    this.offset = offset;
  }

  public int getDepth() {
    return depth;
  }

  public int getOffset() {
    return offset;
  }
}
