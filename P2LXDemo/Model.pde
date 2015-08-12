/**
 * This is a very basic model class that is a 3-D matrix
 * of points. The model contains just one fixture.
 */
static class Model extends LXModel 
{
  public Model() 
  {
    super(new Fixture());
  }
  
  private static class Fixture extends LXAbstractFixture 
  {
    
    private static final int MATRIX_SIZE = 12;
    
    private Fixture() 
    {
      // Here's the core loop where we generate the positions
      // of the points in our model
      for (int x = 0; x < MATRIX_SIZE; ++x) 
      {
        for (int y = 0; y < MATRIX_SIZE; ++y) 
        {
          // Add point to the fixture
          addPoint(new LXPoint(x*FEET, y*FEET));
        }
      }
    }
  }
}

private static class Bench extends LXModel
{
  private static final int NLEDS = 10;
  private static final int LEDS_SPACING = 3 * INCHES;

  public final List<Bar> wings;

  Bench()
  {
    super(new Fixture());
    Fixture f = (Fixture) this.fixtures.get(0);
    this.wings = Collections.unmodifiableList(f.wings);
  }

  private static class Fixture extends LXAbstractFixture
  {
    private List<Bar> wings = new ArrayList<Bar>();
    Fixture()
    {
      LXTransform transform = new LXTransform();
      Bar bar = new Bar(transform);
      addPoints(bar);
      this.wings.add(bar);
    }
  }

  private static class Bar extends LXModel
  {
    public Bar(LXTransform transform)
    {
      super(new Fixture(transform));
    }

    private static class Fixture extends LXAbstractFixture
    {
      Fixture(LXTransform transform)
      {
        transform.push();
        for (int i = 0; i < NLEDS; i ++)
        {
          addPoint(new LXPoint(transform));
          transform.translate(LEDS_SPACING, 0, 0);
        }
        transform.pop();
      }
    }
  }
}

