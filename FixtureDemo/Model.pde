import java.util.*;

/**
 * This is a very basic model class that is a 3-D matrix
 * of points. The model contains just one fixture.
 */
static class Model extends LXModel 
{

  final List<StripModel> strips;
  
  public Model() 
  {
    super(new Fixture());
    Fixture fixture = (Fixture) this.fixtures.get(0);
    this.strips = Collections.unmodifiableList(fixture.strips);
  }
  
  private static class Fixture extends LXAbstractFixture 
  {

    final List<StripModel> strips = new ArrayList<StripModel>();

    private Fixture() 
    {
      for (int i = 0; i < 10; ++i) {
        StripModel strip = new StripModel(50, i);
        addPoints(strip);
        this.strips.add(strip);
      }
    }
  }
}

/**
 * Simple model of a strip of points in one axis.
 */
public static class StripModel extends LXModel {

  public final int length;
  public final int y;

  public StripModel(int length, int y) {
    super(new Fixture(length, y));
    this.length = length;
    this.y = y;
  }

  private static class Fixture extends LXAbstractFixture {
    private Fixture(int length, int y) {
      for (int i = 0; i < length; ++i) {
        addPoint(new LXPoint(i *6, y * 12, 0));
      }
    }
  }
}