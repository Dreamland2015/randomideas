// Get all our imports out of the way
import heronarts.lx.*;
import heronarts.lx.audio.*;
import heronarts.lx.color.*;
import heronarts.lx.model.*;
import heronarts.lx.modulator.*;
import heronarts.lx.parameter.*;
import heronarts.lx.pattern.*;
import heronarts.lx.transition.*;
import heronarts.p2lx.*;
import heronarts.p2lx.ui.*;
import heronarts.p2lx.ui.control.*;
import ddf.minim.*;
import processing.opengl.*;
import heronarts.lx.output.*;
import toxi.geom.Vec2D;
import toxi.math.noise.PerlinNoise;
import toxi.math.noise.SimplexNoise;

// Lets work in inches and feet
final static int INCHES = 1;
final static int FEET = 12*INCHES;

// Lets create a variable for the pub/sub connection
Float rotationPosition = 0f;

// Top-level, we have a model and a P2LX instance
Model model;
P2LX lx;


// Setup establishes the windowing and LX constructs
void setup() 
{
  size(800, 600, OPENGL);
  
  // Create the model, which describes where our light points are
  model = new Model();
  
  // Create the P2LX engine
  lx = new P2LX(this, model);

  // Build the fadecandy outputs
  buildOutputs();
  
  // Start the subscriber class in another thread
  thread("Subscriber");
  
  // Set the patterns
  lx.setPatterns(new LXPattern[] 
  {
    new PythonProjection(lx),
    new ControlProjectionPosition(lx),
    new ControlProjectionSpeed(lx),
    new SolidColor(lx),
    new TestXPattern(lx),
    new IteratorTestPattern(lx).setTransition(new DissolveTransition(lx)),
    new AskewPlanes(lx),
    new MoveXPosition(lx),
    new TestHuePattern(lx),
    new TestYPattern(lx),
    new TestZPattern(lx),
    new Bouncing(lx),
    new Cascade(lx),
    new CrazyWaves(lx), 
    new CrossSections(lx),
    new DFC(lx),   
    new LayerDemoPattern(lx), 
    new ParameterWave(lx),
    new Pulley(lx), 
    new Pulse(lx),
    new RainbowInsanity(lx), 
    new SeeSaw(lx), 
    new ShiftingPlane(lx), 
    new SparkleTakeOver(lx), 
    new Stripes(lx), 
    new Strobe(lx),
    new SweepPattern(lx),
    new Twinkle(lx), 
    new block(lx), 
    new candycloudstar(lx),
    new rainbowfade(lx),  
    new rainbowfadeauto(lx), 
    new um(lx), 
    new um2(lx), 
    new um3_lists(lx),   
    new MultiSine(lx), 
  });
  
  // Add UI elements
  lx.ui.addLayer(
    // A camera layer makes an OpenGL layer that we can easily 
    // pivot around with the mouse
    new UI3dContext(lx.ui) 
    {
      protected void beforeDraw(UI ui, PGraphics pg) 
      {
        // Let's add lighting and depth-testing to our 3-D simulation
        pointLight(0, 0, 40, model.cx, model.cy, -20*FEET);
        pointLight(0, 0, 50, model.cx, model.yMax + 10*FEET, model.cz);
        pointLight(0, 0, 20, model.cx, model.yMin - 10*FEET, model.cz);
        hint(ENABLE_DEPTH_TEST);
      }
      protected void afterDraw(UI ui, PGraphics pg) 
      {
        // Turn off the lights and kill depth testing before the 2D layers
        noLights();
        hint(DISABLE_DEPTH_TEST);
      } 
    }
  
    // Let's look at the center of our model
    .setCenter(model.cx, model.cy, model.cz)
  
    // Let's position our eye some distance away
    .setRadius(32*FEET)
    
    // And look at it from a bit of an angle
    .setTheta(PI/24)
    .setPhi(PI/24)
    
    .setRotationVelocity(12*PI)
    .setRotationAcceleration(3*PI)
    
    // Let's add a point cloud of our animation points
    .addComponent(new UIPointCloud(lx, model).setPointSize(3))
    
    // And a custom UI object of our own
    // .addComponent(new UIWalls())
  );
  
  // A basic built-in 2-D control for a channel
  lx.ui.addLayer(new UIChannelControl(lx.ui, lx.engine.getChannel(0), 4, 4));
  lx.ui.addLayer(new UIEngineControl(lx.ui, 4, 326));
  lx.ui.addLayer(new UIComponentsDemo(lx.ui, width-144, 4));
}


void draw() 
{
  // Wipe the frame...
  background(#292929);
  // ...and everything else is handled by P2LX!
}

