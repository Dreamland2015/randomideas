/**
 * This is a very basic model class that is a 3-D matrix
 * of points. The model contains just one fixture.
 */
static class Model extends LXModel 
{
  public final Bench bench; 

  public Model() 
  {
    super(new Fixture());
    Fixture f = (Fixture) this.fixtures.get(0);
    this.bench = f.bench;
  }
  
  private static class Fixture extends LXAbstractFixture 
  {
    private final Bench bench;

    private Fixture()
    {
      addPoints(this.bench = new Bench());
    }
  }
}

private static class Bench extends LXModel {
  
  private static final int NROWS = 2;
  private static final int NLEDS = 4;
  private static final int LED_SPACING = 3 * INCHES;
  private static final int ANGLE = 120;
  
  public final List<Wing> wings;
  
  Bench() {
    super(new Fixture());
    Fixture f = (Fixture) this.fixtures.get(0);
    this.wings = Collections.unmodifiableList(f.wings);
  } 
  
  private static class Fixture extends LXAbstractFixture {
    
    private List<Wing> wings = new ArrayList<Wing>();
    
    Fixture() 
    {
      LXTransform transform = new LXTransform();
      for (int i = 0; i < NROWS; i ++)
      {
        transform.translate(i * FEET ,0 , -i * FEET);
        Wing wing = new Wing(transform);
        this.wings.add(wing);
        addPoints(wing); 
      }
    }
  }

  private static class Wing extends LXModel
  {

    public Wing(LXTransform transform)
    {
      super(new Fixture(transform));
    }

    private static class Fixture extends LXAbstractFixture
    {
      private List<Bar> bars = new ArrayList<Bar>();  
      Fixture(LXTransform transform)
      {
        transform.push();
        transform.translate(- NLEDS * LED_SPACING + LED_SPACING ,0,0);
        Bar bar = new Bar(NLEDS - 1, transform);
        this.bars.add(bar);
        addPoints(bar);
        transform.pop();

        transform.push();
        transform.translate(0 ,0, 0);
        transform.rotateY(radians(180 - ANGLE));
        Bar bar2 = new Bar(NLEDS, transform);
        this.bars.add(bar2);
        addPoints(bar2);
        transform.pop();

      }
    }
  }
  
  private static class Bar extends LXModel {
  
  
    public Bar(int numLeds, LXTransform transform) {
      super(new Fixture(numLeds, transform));
    }
    
    private static class Fixture extends LXAbstractFixture {
      Fixture(int numLeds, LXTransform transform) {
        transform.push();
        for (int i = 0; i < numLeds; i++) {
          addPoint(new LXPoint(transform));
          transform.translate(LED_SPACING, 0, 0);
        }
        transform.pop();
      }
    }
  }
}