import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

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
import java.util.*; 
import java.net.*; 
import java.util.Arrays; 
import org.zeromq.ZMQ; 

import heronarts.p2lx.ui.control.*; 
import ddf.minim.*; 
import heronarts.p2lx.font.*; 
import heronarts.p2lx.*; 
import toxi.math.waves.*; 
import toxi.geom.mesh2d.*; 
import heronarts.lx.midi.device.*; 
import heronarts.lx.modulator.*; 
import com.google.gson.annotations.*; 
import heronarts.lx.output.*; 
import toxi.util.datatypes.*; 
import ddf.minim.signals.*; 
import toxi.math.conversion.*; 
import heronarts.lx.audio.*; 
import heronarts.lx.*; 
import toxi.math.*; 
import heronarts.lx.color.*; 
import toxi.math.noise.*; 
import com.google.gson.internal.*; 
import ddf.minim.ugens.*; 
import ddf.minim.effects.*; 
import com.google.gson.stream.*; 
import heronarts.lx.pattern.*; 
import toxi.geom.mesh.subdiv.*; 
import ddf.minim.analysis.*; 
import com.google.gson.internal.bind.*; 
import toxi.geom.mesh.*; 
import heronarts.lx.effect.*; 
import heronarts.lx.transform.*; 
import heronarts.p2lx.ui.*; 
import heronarts.lx.parameter.*; 
import com.google.gson.*; 
import heronarts.p2lx.video.*; 
import heronarts.p2lx.ui.component.*; 
import heronarts.lx.transition.*; 
import toxi.util.events.*; 
import heronarts.lx.midi.*; 
import com.google.gson.reflect.*; 
import heronarts.lx.model.*; 
import ddf.minim.spi.*; 
import toxi.util.*; 
import toxi.geom.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class FixtureDemo extends PApplet {

// Get all our imports out of the way


















// Let's work in inches
final static int INCHES = 1;
final static int FEET = 12*INCHES;
Float rotationPosition = 0f;

// Top-level, we have a model and a P2LX instance
Model model;
P2LX lx;


// Setup establishes the windowing and LX constructs
public void setup() 
{
  size(800, 600, OPENGL);
  
  // Create the model, which describes where our light points are
  model = new Model();
  
  // Create the P2LX engine
  lx = new P2LX(this, model);
  
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


  // buildOutputs();
  thread("psenvsub");
}


public void draw() 
{
  // Wipe the frame...
  background(0xff292929);
  // ...and everything else is handled by P2LX!
}



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
/*
 * Simple Open Pixel Control client for Processing,
 * designed to sample each LED's color from some point on the canvas.
 *
 * Micah Elizabeth Scott, 2013
 * This file is released into the public domain.
 */




public class OPC
{
  Socket socket;
  OutputStream output;
  String host;
  int port;

  int[] pixelLocations;
  byte[] packetData;
  byte firmwareConfig;
  String colorCorrection;
  boolean enableShowLocations;

  OPC(PApplet parent, String host, int port)
  {
    this.host = host;
    this.port = port;
    this.enableShowLocations = true;
    parent.registerDraw(this);
  }

  // Set the location of a single LED
  public void led(int index, int x, int y)  
  {
    // For convenience, automatically grow the pixelLocations array. We do want this to be an array,
    // instead of a HashMap, to keep draw() as fast as it can be.
    if (pixelLocations == null) {
      pixelLocations = new int[index + 1];
    } else if (index >= pixelLocations.length) {
      pixelLocations = Arrays.copyOf(pixelLocations, index + 1);
    }

    pixelLocations[index] = x + width * y;
  }
  
  // Set the location of several LEDs arranged in a strip.
  // Angle is in radians, measured clockwise from +X.
  // (x,y) is the center of the strip.
  public void ledStrip(int index, int count, float x, float y, float spacing, float angle, boolean reversed)
  {
    float s = sin(angle);
    float c = cos(angle);
    for (int i = 0; i < count; i++) {
      led(reversed ? (index + count - 1 - i) : (index + i),
        (int)(x + (i - (count-1)/2.0f) * spacing * c + 0.5f),
        (int)(y + (i - (count-1)/2.0f) * spacing * s + 0.5f));
    }
  }

  // Set the location of several LEDs arranged in a grid. The first strip is
  // at 'angle', measured in radians clockwise from +X.
  // (x,y) is the center of the grid.
  public void ledGrid(int index, int stripLength, int numStrips, float x, float y,
               float ledSpacing, float stripSpacing, float angle, boolean zigzag)
  {
    float s = sin(angle + HALF_PI);
    float c = cos(angle + HALF_PI);
    for (int i = 0; i < numStrips; i++) {
      ledStrip(index + stripLength * i, stripLength,
        x + (i - (numStrips-1)/2.0f) * stripSpacing * c,
        y + (i - (numStrips-1)/2.0f) * stripSpacing * s, ledSpacing,
        angle, zigzag && (i % 2) == 1);
    }
  }

  // Set the location of 64 LEDs arranged in a uniform 8x8 grid.
  // (x,y) is the center of the grid.
  public void ledGrid8x8(int index, float x, float y, float spacing, float angle, boolean zigzag)
  {
    ledGrid(index, 8, 8, x, y, spacing, spacing, angle, zigzag);
  }

  // Should the pixel sampling locations be visible? This helps with debugging.
  // Showing locations is enabled by default. You might need to disable it if our drawing
  // is interfering with your processing sketch, or if you'd simply like the screen to be
  // less cluttered.
  public void showLocations(boolean enabled)
  {
    enableShowLocations = enabled;
  }
  
  // Enable or disable dithering. Dithering avoids the "stair-stepping" artifact and increases color
  // resolution by quickly jittering between adjacent 8-bit brightness levels about 400 times a second.
  // Dithering is on by default.
  public void setDithering(boolean enabled)
  {
    if (enabled)
      firmwareConfig &= ~0x01;
    else
      firmwareConfig |= 0x01;
    sendFirmwareConfigPacket();
  }

  // Enable or disable frame interpolation. Interpolation automatically blends between consecutive frames
  // in hardware, and it does so with 16-bit per channel resolution. Combined with dithering, this helps make
  // fades very smooth. Interpolation is on by default.
  public void setInterpolation(boolean enabled)
  {
    if (enabled)
      firmwareConfig &= ~0x02;
    else
      firmwareConfig |= 0x02;
    sendFirmwareConfigPacket();
  }

  // Put the Fadecandy onboard LED under automatic control. It blinks any time the firmware processes a packet.
  // This is the default configuration for the LED.
  public void statusLedAuto()
  {
    firmwareConfig &= 0x0C;
    sendFirmwareConfigPacket();
  }    

  // Manually turn the Fadecandy onboard LED on or off. This disables automatic LED control.
  public void setStatusLed(boolean on)
  {
    firmwareConfig |= 0x04;   // Manual LED control
    if (on)
      firmwareConfig |= 0x08;
    else
      firmwareConfig &= ~0x08;
    sendFirmwareConfigPacket();
  } 

  // Set the color correction parameters
  public void setColorCorrection(float gamma, float red, float green, float blue)
  {
    colorCorrection = "{ \"gamma\": " + gamma + ", \"whitepoint\": [" + red + "," + green + "," + blue + "]}";
    sendColorCorrectionPacket();
  }
  
  // Set custom color correction parameters from a string
  public void setColorCorrection(String s)
  {
    colorCorrection = s;
    sendColorCorrectionPacket();
  }

  // Send a packet with the current firmware configuration settings
  public void sendFirmwareConfigPacket()
  {
    if (output == null) {
      // We'll do this when we reconnect
      return;
    }
 
    byte[] packet = new byte[9];
    packet[0] = 0;          // Channel (reserved)
    packet[1] = (byte)0xFF; // Command (System Exclusive)
    packet[2] = 0;          // Length high byte
    packet[3] = 5;          // Length low byte
    packet[4] = 0x00;       // System ID high byte
    packet[5] = 0x01;       // System ID low byte
    packet[6] = 0x00;       // Command ID high byte
    packet[7] = 0x02;       // Command ID low byte
    packet[8] = firmwareConfig;

    try {
      output.write(packet);
    } catch (Exception e) {
      dispose();
    }
  }

  // Send a packet with the current color correction settings
  public void sendColorCorrectionPacket()
  {
    if (colorCorrection == null) {
      // No color correction defined
      return;
    }
    if (output == null) {
      // We'll do this when we reconnect
      return;
    }

    byte[] content = colorCorrection.getBytes();
    int packetLen = content.length + 4;
    byte[] header = new byte[8];
    header[0] = 0;          // Channel (reserved)
    header[1] = (byte)0xFF; // Command (System Exclusive)
    header[2] = (byte)(packetLen >> 8);
    header[3] = (byte)(packetLen & 0xFF);
    header[4] = 0x00;       // System ID high byte
    header[5] = 0x01;       // System ID low byte
    header[6] = 0x00;       // Command ID high byte
    header[7] = 0x01;       // Command ID low byte

    try {
      output.write(header);
      output.write(content);
    } catch (Exception e) {
      dispose();
    }
  }

  // Automatically called at the end of each draw().
  // This handles the automatic Pixel to LED mapping.
  // If you aren't using that mapping, this function has no effect.
  // In that case, you can call setPixelCount(), setPixel(), and writePixels()
  // separately.
  public void draw()
  {
    if (pixelLocations == null) {
      // No pixels defined yet
      return;
    }
 
    if (output == null) {
      // Try to (re)connect
      connect();
    }
    if (output == null) {
      return;
    }

    int numPixels = pixelLocations.length;
    int ledAddress = 4;

    setPixelCount(numPixels);
    loadPixels();

    for (int i = 0; i < numPixels; i++) {
      int pixelLocation = pixelLocations[i];
      int pixel = pixels[pixelLocation];

      packetData[ledAddress] = (byte)(pixel >> 16);
      packetData[ledAddress + 1] = (byte)(pixel >> 8);
      packetData[ledAddress + 2] = (byte)pixel;
      ledAddress += 3;

      if (enableShowLocations) {
        pixels[pixelLocation] = 0xFFFFFF ^ pixel;
      }
    }

    writePixels();

    if (enableShowLocations) {
      updatePixels();
    }
  }
  
  // Change the number of pixels in our output packet.
  // This is normally not needed; the output packet is automatically sized
  // by draw() and by setPixel().
  public void setPixelCount(int numPixels)
  {
    int numBytes = 3 * numPixels;
    int packetLen = 4 + numBytes;
    if (packetData == null || packetData.length != packetLen) {
      // Set up our packet buffer
      packetData = new byte[packetLen];
      packetData[0] = 0;  // Channel
      packetData[1] = 0;  // Command (Set pixel colors)
      packetData[2] = (byte)(numBytes >> 8);
      packetData[3] = (byte)(numBytes & 0xFF);
    }
  }
  
  // Directly manipulate a pixel in the output buffer. This isn't needed
  // for pixels that are mapped to the screen.
  public void setPixel(int number, int c)
  {
    int offset = 4 + number * 3;
    if (packetData == null || packetData.length < offset + 3) {
      setPixelCount(number + 1);
    }

    packetData[offset] = (byte) (c >> 16);
    packetData[offset + 1] = (byte) (c >> 8);
    packetData[offset + 2] = (byte) c;
  }
  
  // Read a pixel from the output buffer. If the pixel was mapped to the display,
  // this returns the value we captured on the previous frame.
  public int getPixel(int number)
  {
    int offset = 4 + number * 3;
    if (packetData == null || packetData.length < offset + 3) {
      return 0;
    }
    return (packetData[offset] << 16) | (packetData[offset + 1] << 8) | packetData[offset + 2];
  }

  // Transmit our current buffer of pixel values to the OPC server. This is handled
  // automatically in draw() if any pixels are mapped to the screen, but if you haven't
  // mapped any pixels to the screen you'll want to call this directly.
  public void writePixels()
  {
    if (packetData == null || packetData.length == 0) {
      // No pixel buffer
      return;
    }
    if (output == null) {
      // Try to (re)connect
      connect();
    }
    if (output == null) {
      return;
    }

    try {
      output.write(packetData);
    } catch (Exception e) {
      dispose();
    }
  }

  public void dispose()
  {
    // Destroy the socket. Called internally when we've disconnected.
    if (output != null) {
      println("Disconnected from OPC server");
    }
    socket = null;
    output = null;
  }

  public void connect()
  {
    // Try to connect to the OPC server. This normally happens automatically in draw()
    try {
      socket = new Socket(host, port);
      socket.setTcpNoDelay(true);
      output = socket.getOutputStream();
      println("Connected to OPC server");
    } catch (ConnectException e) {
      dispose();
    } catch (IOException e) {
      dispose();
    }
    
    sendColorCorrectionPacket();
    sendFirmwareConfigPacket();
  }
}

/**
 * This file has a bunch of example patterns, each illustrating the key
 * concepts and tools of the LX framework.
 */

class PythonProjection extends LXPattern 
{
  private final LXProjection rotation;

  public PythonProjection(LX lx) {
    super(lx);
    rotation = new LXProjection(model);
  }

  public void run(double deltaMs) 
  {
    rotation.reset();
    rotation.center(); // assuming you want to rotate about the center of your model?
    rotation.rotateZ(rotationPosition); // or whatever is appropriate
    float hv = lx.getBaseHuef();
    for (LXVector c : rotation) {
      float d = max(0, abs(c.y) - 10 + .1f*abs(c.z) + .02f*abs(c.x)); // plane / spear thing
      colors[c.index] = lx.hsb(
        100,
        100,
        constrain(140 - 40*d, 0, 100)
      );
    }
  }
} 

class LayerDemoPattern extends LXPattern {
  
  private final BasicParameter colorSpread = new BasicParameter("Clr", 0.5f, 0, 3);
  private final BasicParameter stars = new BasicParameter("Stars", 100, 0, 100);
  
  public LayerDemoPattern(LX lx) {
    super(lx);
    addParameter(colorSpread);
    addParameter(stars);
    addLayer(new CircleLayer(lx));
    addLayer(new RodLayer(lx));
    for (int i = 0; i < 200; ++i) {
      addLayer(new StarLayer(lx));
    }
  }
  
  public void run(double deltaMs) {
    // The layers run automatically
  }
  
  private class CircleLayer extends LXLayer {
    
    private final SinLFO xPeriod = new SinLFO(3400, 7900, 11000); 
    private final SinLFO brightnessX = new SinLFO(model.xMin, model.xMax, xPeriod);
  
    private CircleLayer(LX lx) {
      super(lx);
      addModulator(xPeriod).start();
      addModulator(brightnessX).start();
    }
    
    public void run(double deltaMs) {
      // The layers run automatically
      float falloff = 100 / (4*FEET);
      for (LXPoint p : model.points) {
        float yWave = model.yRange/2 * sin(p.x / model.xRange * PI); 
        float distanceFromCenter = dist(p.x, p.y, model.cx, model.cy);
        float distanceFromBrightness = dist(p.x, abs(p.y - model.cy), brightnessX.getValuef(), yWave);
        colors[p.index] = LXColor.hsb(
          lx.getBaseHuef() + colorSpread.getValuef() * distanceFromCenter,
          100,
          max(0, 100 - falloff*distanceFromBrightness)
        );
      }
    }
  }
  
  private class RodLayer extends LXLayer {
    
    private final SinLFO zPeriod = new SinLFO(2000, 5000, 9000);
    private final SinLFO zPos = new SinLFO(model.zMin, model.zMax, zPeriod);
    
    private RodLayer(LX lx) {
      super(lx);
      addModulator(zPeriod).start();
      addModulator(zPos).start();
    }
    
    public void run(double deltaMs) {
      for (LXPoint p : model.points) {
        float b = 100 - dist(p.x, p.y, model.cx, model.cy) - abs(p.z - zPos.getValuef());
        if (b > 0) {
          addColor(p.index, LXColor.hsb(
            lx.getBaseHuef() + p.z,
            100,
            b
          ));
        }
      }
    }
  }
  
  private class StarLayer extends LXLayer {
    
    private final TriangleLFO maxBright = new TriangleLFO(0, stars, random(2000, 8000));
    private final SinLFO brightness = new SinLFO(-1, maxBright, random(3000, 9000)); 
    
    private int index = 0;
    
    private StarLayer(LX lx) { 
      super(lx);
      addModulator(maxBright).start();
      addModulator(brightness).start();
      pickStar();
    }
    
    private void pickStar() {
      index = (int) random(0, model.size-1);
    }
    
    public void run(double deltaMs) {
      if (brightness.getValuef() <= 0) {
        pickStar();
      } else {
        addColor(index, LXColor.hsb(lx.getBaseHuef(), 50, brightness.getValuef()));
      }
    }
  }
}

class ControlProjectionPosition extends LXPattern 
{
  private final LXProjection rotation;
  // private final SawLFO angle = new SawLFO(0, TWO_PI, 1000);
  private final BasicParameter angle = new BasicParameter("Angle", 0, 0, TWO_PI);


  public ControlProjectionPosition(LX lx) {
    super(lx);
    rotation = new LXProjection(model);
    // addModulator(angle).trigger()
    addParameter(angle);
  }

  public void run(double deltaMs) 
  {
    rotation.reset();
    rotation.center(); // assuming you want to rotate about the center of your model?
    rotation.rotateZ(angle.getValuef()); // or whatever is appropriate
    float hv = lx.getBaseHuef();
    for (LXVector c : rotation) {
      float d = max(0, abs(c.y) - 10 + .1f*abs(c.z) + .02f*abs(c.x)); // plane / spear thing
      colors[c.index] = lx.hsb(
        100,
        100,
        constrain(140 - 40*d, 0, 100)
      );
    }
  }
}

class ControlProjectionSpeed extends LXPattern 
{
  private final LXProjection rotation;
  private final BasicParameter speed = new BasicParameter("speed", 2500, 5000, 0);
  private final SawLFO angle = new SawLFO(0, TWO_PI, speed);


  public ControlProjectionSpeed(LX lx) {
    super(lx);
    rotation = new LXProjection(model);
    // addModulator(speed).trigger();
    addParameter(speed);
    addModulator(angle).trigger();
  }

  public void run(double deltaMs) 
  {
    rotation.reset();
    rotation.center(); // assuming you want to rotate about the center of your model?
    rotation.rotateZ(angle.getValuef()); // or whatever is appropriate
    float hv = lx.getBaseHuef();
    for (LXVector c : rotation) {
      float d = max(0, abs(c.y) - 10 + .1f*abs(c.z) + .02f*abs(c.x)); // plane / spear thing
      colors[c.index] = lx.hsb(
        100,
        100,
        constrain(140 - 40*d, 0, 100)
      );
    }
  }
}

// class ControlProjectionextends LXPattern {
  
//   private final BasicParameter colorSpread = new BasicParameter("Clr", 0.5, 0, 3);
//   private final BasicParameter stars = new BasicParameter("Stars", 100, 0, 100);
  
//   public LayerDemoPattern(LX lx) {
//     super(lx);
//     addParameter(colorSpread);
//     addParameter(stars);
//     addLayer(new CircleLayer(lx));
//     addLayer(new RodLayer(lx));
//     for (int i = 0; i < 200; ++i) {
//       addLayer(new StarLayer(lx));
//     }
//   }
  
//   public void run(double deltaMs) {
//     // The layers run automatically
//   }
  
//   private class CircleLayer extends LXLayer {
    
//     private final SinLFO xPeriod = new SinLFO(3400, 7900, 11000); 
//     private final SinLFO brightnessX = new SinLFO(model.xMin, model.xMax, xPeriod);
  
//     private CircleLayer(LX lx) {
//       super(lx);
//       addModulator(xPeriod).start();
//       addModulator(brightnessX).start();
//     }
    
//     public void run(double deltaMs) {
//       // The layers run automatically
//       float falloff = 100 / (4*FEET);
//       for (LXPoint p : model.points) {
//         float yWave = model.yRange/2 * sin(p.x / model.xRange * PI); 
//         float distanceFromCenter = dist(p.x, p.y, model.cx, model.cy);
//         float distanceFromBrightness = dist(p.x, abs(p.y - model.cy), brightnessX.getValuef(), yWave);
//         colors[p.index] = LXColor.hsb(
//           lx.getBaseHuef() + colorSpread.getValuef() * distanceFromCenter,
//           100,
//           max(0, 100 - falloff*distanceFromBrightness)
//         );
//       }
//     }
//   }

class MoveXPosition extends LXPattern
{
  private final float modelMin = model.xMin - 50;
  private final float modelMax = model.xMax + 50;
  private final BasicParameter xPos = new BasicParameter("XPos", 100, modelMin, modelMax);

  public MoveXPosition(LX lx)
  {
    super(lx);
      addParameter(xPos);
  }

  public void run(double deltaMs) 
  {
    float hueValue = lx.getBaseHuef();
    for (LXPoint p : model.points)
    {
      float brightnessValue = max(0, 100 - abs(p.x - xPos.getValuef()));
      colors[p.index] = lx.hsb(hueValue, 100, brightnessValue);
    } 
  }
}

class SolidColor extends LXPattern
{
  private final float modelMin = model.xMin - 50;
  private final float modelMax = model.xMax + 50;
  private final BasicParameter hueValue = new BasicParameter("Hue", 100, 0, 255);
  private final BasicParameter satValue = new BasicParameter("Sat", 100, 0, 100);
  private final BasicParameter briValue = new BasicParameter("Bright", 100, 0, 100);


  public SolidColor(LX lx)
  {
    super(lx);
      addParameter(hueValue);
      addParameter(satValue);
      addParameter(briValue);
  }

  public void run(double deltaMs) 
  {
    for (LXPoint p : model.points)
    {
      colors[p.index] = lx.hsb(hueValue.getValuef(), satValue.getValuef(), briValue.getValuef());
    } 
  }
}




class TestXPattern extends LXPattern 
{
  private final SinLFO xPos = new SinLFO(model.xMin, model.xMax, 4000);
  public TestXPattern(LX lx) 
  {
    super(lx);
    addModulator(xPos).trigger();
  }
  public void run(double deltaMs) 
  {
    float hv = lx.getBaseHuef();
    for (LXPoint p : model.points) 
    {
      // This is a common technique for modulating brightness.
      // You can use abs() to determine the distance between two
      // values. The further away this point is from an exact
      // point, the more we decrease its brightness
      float bv = max(0, 100 - abs(p.x - xPos.getValuef()));
      colors[p.index] = lx.hsb(hv, 100, bv);
    }
  }
}

class TestHuePattern extends LXPattern 
{
  public TestHuePattern(LX lx) 
  {
    super(lx);
  }
  
  public void run(double deltaMs) 
  {
    // Access the core master hue via this method call
    float hv = lx.getBaseHuef();
    for (int i = 0; i < colors.length; ++i) 
    {
      colors[i] = lx.hsb(hv, 100, 100);
    }
  } 
}

class TestYPattern extends LXPattern 
{
  private final SinLFO yPos = new SinLFO(model.yMin, model.yMax, 4000);
  public TestYPattern(LX lx) 
  {
    super(lx);
    addModulator(yPos).trigger();
  }
  public void run(double deltaMs) 
  {
    float hv = lx.getBaseHuef();
    for (LXPoint p : model.points) 
    {
      float bv = max(0, 100 - abs(p.y - yPos.getValuef()));
      colors[p.index] = lx.hsb(hv, 100, bv);
    }
  }
}


class TestZPattern extends LXPattern 
{
  private final SinLFO zPos = new SinLFO(model.zMin, model.zMax, 4000);
  public TestZPattern(LX lx) 
  {
    super(lx);
    addModulator(zPos).trigger();
  }
  public void run(double deltaMs) 
  {
    float hv = lx.getBaseHuef();
    for (LXPoint p : model.points) 
    {
      float bv = max(0, 100 - abs(p.z - zPos.getValuef()));
      colors[p.index] = lx.hsb(hv, 100, bv);
    }
  }
}
/**
 * This file has a bunch of example patterns, each illustrating the key
 * concepts and tools of the LX framework.
 */
//***************************ASkewPlanes*********************************************
//***************************ASkewPlanes*********************************************
//***************************ASkewPlanes*********************************************

class AskewPlanes extends LXPattern {
  private final BasicParameter sat = new BasicParameter("sat", 1, 0.1f, 10);
  private final BasicParameter size = new BasicParameter("size", 18, 3, 20);
  private final BasicParameter clr = new BasicParameter("clr", 256, 90, 340);
  private final BasicParameter stars = new BasicParameter("Stars", 85, 35, 100);

  AskewPlanes(LX lx) {
    super(lx);
    addParameter(sat);
    addParameter(size);
    addParameter(clr);
    addParameter(stars);
    addLayer(new PlanesLayer(lx));
    for (int i = 0; i < 600; ++i) {
      addLayer(new StarLayer(lx));
    }
  }

  public void run(double deltaMs) {
  }

  private class PlanesLayer extends LXLayer {

    class Plane {
      private final SinLFO a;
      private final SinLFO b;
      private final SinLFO c;
      float av = 1;
      float bv = 1;
      float cv = 1;
      float denom = 0.1f;

      Plane(int i) {
        addModulator(a = new SinLFO(-1, 1, 9324 + 1029*i)).trigger();
        addModulator(b = new SinLFO(-1, 1, 11566 - 1104*i)).trigger();
        addModulator(c = new SinLFO(-50, 50, 7421 + 1000*i * ((i % 2 == 0) ? 1 : -1))).trigger();
      }  
      public void run(double deltaMs) {
        av = a.getValuef();
        bv = b.getValuef();
        cv = c.getValuef();
        denom = sqrt(av*av + bv*bv);
      }
    }

    final Plane[] planes;
    int NUM_PLANES = 3;

    private PlanesLayer(LX lx) {
      super(lx);
      planes = new Plane[NUM_PLANES];
      for (int i = 0; i < planes.length; ++i) {
        planes[i] = new Plane(i);
      }
    }
    public void run(double deltaMs) { 
      float huev = clr.getValuef();
      //NUM_PLANES = numplanes.getValuei();

      // This is super fucking bizarre. But if this is a for loop, the framerate
      // tanks to like 30FPS, instead of 60. Call them manually and it works fine.
      // Doesn't make ANY sense... there must be some weird side effect going on
      // with the Processing internals perhaps?
      //    for (Plane plane : planes) {
      //      plane.run(deltaMs);
      //    }
      planes[0].run(deltaMs);
      planes[1].run(deltaMs);
      planes[2].run(deltaMs);
      //planes[3].run(deltaMs);
      //planes[4].run(deltaMs);
      //planes[5].run(deltaMs);      

      for (LXPoint p : model.points) {
        float d = MAX_FLOAT;
        for (Plane plane : planes) {
          if (plane.denom != 0) {
            d = min(d, abs(plane.av*(p.x-model.cx) + plane.bv*(p.y-model.cy) + plane.cv) / plane.denom);
          }
        }
        colors[p.index] = lx.hsb(
        (huev + abs(p.x-model.cx) + p.y) % 360, 
        max(0, 100 - sat.getValuef()*abs(p.x - model.cx)), 
        //saturation.getValuef(), 
        constrain(140 - size.getValuef()*d, 0, 100)
          );
      }
    }
  }
 private class StarLayer extends LXLayer {

    private final TriangleLFO maxBright = new TriangleLFO(0, stars, random(2000, 8000));
    private final SinLFO brightness = new SinLFO(-1, maxBright, random(3000, 9000)); 

    private int index = 0;

    private StarLayer(LX lx) { 
      super(lx);
      addModulator(maxBright).start();
      addModulator(brightness).start();
      pickStar();
    }

    private void pickStar() {
      index = (int) random(0, model.size-1);
    }

    public void run(double deltaMs) {
      if (brightness.getValuef() <= 0) {
        pickStar();
      } else {
        addColor(index, LXColor.hsb(lx.getBaseHuef(), 50, brightness.getValuef()));
      }
    }
  }
}

//***************************Rainbowfadeauto*********************************************
//***************************Rainbowfadeauto*********************************************
//***************************Rainbowfadeauto*********************************************

class rainbowfadeauto extends LXPattern {
  //float period = 1000;
  //private final BasicParameter periodT = new BasicParameter("T", 1, .001, 1000);
  private final BasicParameter speed = new BasicParameter("speed", 5, .1f, 25);
  private final BasicParameter saturation = new BasicParameter("sat", 100, 30, 100);
  private final BasicParameter bright = new BasicParameter("brite", 100, 40, 100);
  private final SinLFO ysign = new SinLFO(1, -1, 10548);
  private final SinLFO xsign = new SinLFO(-1, 1, 7893);
  private final SinLFO zsign = new SinLFO(1, -1, 6211);
  private final BasicParameter size = new BasicParameter("size", 2, 1.4f, 8);
  //private final BasicParameter ysign = new BasicParameter("ys", -1, -1, 1);
  //private final BasicParameter xsign = new BasicParameter("xs", -1, -1, 1);
  //private final BasicParameter zsign = new BasicParameter("zs", -1, -1, 1);

  public rainbowfadeauto(LX lx) {
    super(lx);
    addParameter(speed);
    addParameter(saturation);
    addParameter(size);
    addParameter(bright);
    //addParameter(periodT);
    addModulator(ysign).trigger();
    addModulator(xsign).trigger();
    addModulator(zsign).trigger();
    //addParameter(ysign);
    //addParameter(xsign);
    //addParameter(zsign);
  }
  public void run(double deltaMs) {


    for (LXPoint p : model.points) {
      colors[p.index] = lx.hsb(
      lx.getBaseHuef() * speed.getValuef() - ((ysign.getValuef())*p.y + (xsign.getValuef())*p.x + (zsign.getValuef())*p.z) * size.getValuef(), 
      saturation.getValuef(), 
      bright.getValuef());
    }
  }
}

//***************************Rods*********************************************
//***************************Rods*********************************************
//***************************Rods*********************************************
class Rods extends LXPattern {
  //private final BasicParameter huerate = new BasicParameter("hue", 88, 1, 360);
  //private final BasicParameter hueup = new BasicParameter("hue", 88, 1, 360);
  private final BasicParameter hue = new BasicParameter("hue", 300, 1, 320);
  private final BasicParameter numberofrodsup = new BasicParameter("up", 15, 4, 100);
  private final BasicParameter numberofrodsdown = new BasicParameter("down", 15, 4, 100);
  private final BasicParameter speed = new BasicParameter("spd", 0.7f, 0.3f, 1.2f);
  //private final BasicParameter saturation = new BasicParameter("sat", 100, 0, 100);
  //final SinLFO saturationup = new SinLFO(100, 0, 26432);
  //final SinLFO saturationdown = new SinLFO(0, 100, 14421);
 
 // private final BasicParameter falloff = new BasicParameter("fall", 2.8, 1, 8);
 // private final BasicParameter rodthick = new BasicParameter("thick", 3, 1, 30);

public Rods(LX lx) {
    super(lx);
    addLayer(new RodsDown(lx));
    addLayer(new RodsUp(lx));
    //addParameter(huerate);
    addParameter(numberofrodsup);
    addParameter(numberofrodsdown);
    addParameter(hue);
    //addParameter(saturation);
    addParameter(speed);
    //addModulator(saturationup).start();
    //addModulator(saturationdown).start();
    //addParameter(hueup);
    //addParameter(falloff);
    //addParameter(rodthick);
} 
public void run(double deltaMs) {
    //The layers run automatically
      for (LXPoint p : model.points) {
        colors[p.index] = 0;
      }  

}
 private class RodsDown extends LXLayer { 
 
    int numRods = 100;
    private final FloatList rodx = new FloatList();
    private final FloatList rody = new FloatList();
    private final FloatList rodz = new FloatList();
    private final FloatList rodhue = new FloatList();
    private final FloatList rodspeed = new FloatList();
    private final FloatList distancefromrod = new FloatList();
    float rodheight = model.yMax;

    private RodsDown(LX lx) {
      super(lx);
    
      for (int i = 0; i < numRods; i++) {
        rodx.set(i, random(model.xMin, model.xMax));  
        rody.set(i, 30);  
        rodz.set(i, random(model.zMin, model.zMax));
        rodspeed.set(i, random(0.8f, 2.6f));
      //  rodhue.set(i, random(280, 310));
      }
    }
  
    public void run(double deltaMs) {
  
      int numRods = round(numberofrodsdown.getValuef());
      float rodsize = 3;
  
//      for (LXPoint p : model.points) {
//        colors[p.index] = 0;
//      }
  
      for (int i = 0; i < numRods; i++) {
        rody.set(i, rody.get(i) - (rodspeed.get(i)*speed.getValuef()));
        if (rody.get(i) < model.yMin - rody.get(i)/2) {
          rodx.set(i, random(model.xMin, model.xMax));  
          rody.set(i, model.yMax + rodheight/2);  
          rodz.set(i, random(model.zMin, model.zMax));
          //rodhue.set(i, rodhue.get(i) + 1);  
          //if (rodhue.get(i) > 360) {
          //  rodhue.set(i, 280);
          //}
        }
      }
  
      for (LXPoint p : model.points) {
  
        for (int i = 0; i < numRods; i++) {
  
          float hv = hue.getValuef(); 
          float rodius = abs(p.y - rody.get(i));
          distancefromrod.set(i, rodius);
          float bv = max(0, 100 - distancefromrod.get(i)*4);
          if (p.x > rodx.get(i) - rodsize && p.x < rodx.get(i) + rodsize &&
            p.z > rodz.get(i) - rodsize && p.z < rodz.get(i) + rodsize &&
            p.y > rody.get(i) - rodheight/2 && p.y < rody.get(i) + rodheight/2)
          {
            colors[p.index] = lx.hsb(
            hv, 
            min(100, 100 - (abs(p.y - model.cy)*2.3f)), 
            bv);
          }
        }
      }
    }
  }
 private class RodsUp extends LXLayer { 
 
    int numRods = 100;
    private final FloatList rodx = new FloatList();
    private final FloatList rody = new FloatList();
    private final FloatList rodz = new FloatList();
    private final FloatList rodhue = new FloatList();
    private final FloatList rodspeed = new FloatList();
    private final FloatList distancefromrod = new FloatList();
    float rodheight = model.yMax;

    private RodsUp(LX lx) {
      super(lx);
    
      for (int i = 0; i < numRods; i++) {
        rodx.set(i, random(model.xMin, model.xMax));  
        rody.set(i, 1);  
        rodz.set(i, random(model.zMin, model.zMax));
        rodspeed.set(i, random(0.8f, 2.6f));
      //  rodhue.set(i, random(280, 310));
      }
    }
  
    public void run(double deltaMs) {
  
      int numRods = round(numberofrodsup.getValuef());
      float rodsize = 3;
  
//      for (LXPoint p : model.points) {
//        colors[p.index] = 0;
//      }
  
      for (int i = 0; i < numRods; i++) {
        rody.set(i, rody.get(i) + (rodspeed.get(i)*speed.getValuef()));
        if (rody.get(i) > model.yMax + rody.get(i)/2) {
          rodx.set(i, random(model.xMin, model.xMax));  
          rody.set(i, model.yMin - rodheight/2);  
          rodz.set(i, random(model.zMin, model.zMax));
          //rodhue.set(i, rodhue.get(i) + 1);  
          //if (rodhue.get(i) > 360) {
          //  rodhue.set(i, 280);
          //}
        }
      }
  
      for (LXPoint p : model.points) {
  
        for (int i = 0; i < numRods; i++) {
  
          float hv = hue.getValuef(); 
          float rodius = abs(p.y - rody.get(i));
          distancefromrod.set(i, rodius);
          float bv = max(0, 100 - distancefromrod.get(i)*4);
          if (p.x > rodx.get(i) - rodsize && p.x < rodx.get(i) + rodsize &&
            p.z > rodz.get(i) - rodsize && p.z < rodz.get(i) + rodsize &&
            p.y > rody.get(i) - rodheight/2 && p.y < rody.get(i) + rodheight/2)
          {
            colors[p.index] = lx.hsb(
            (hv * 1.4f) % 360, 
            min(100, 100 - (abs(p.y - model.cy)*2.3f)), 
            bv);
          }
        }
      }
    }
  }
}
//***************************Multisin*********************************************
//***************************Multisin*********************************************
//***************************Multisin*********************************************
class MultiSine extends LXPattern {
  final int numLayers = 3;
  int[][] distLayerDivisors = {
    {
      10, 50, 10
    }
    , {
      10, 50, 10
    }
  }; 
  final BasicParameter brightEffect = new BasicParameter("Bright", 6, 4, 10);
  final BasicParameter colorspread = new BasicParameter("clr", 40, 10, 60);
  final BasicParameter saturation = new BasicParameter("sat", 90, 15, 90);
  final BasicParameter hue = new BasicParameter("hue", 0, 0, 360);
  final BasicParameter[] timingSettings = {
    new BasicParameter("T1", 6300, 5000, 300000), 
    new BasicParameter("T2", 4300, 2000, 100000), 
    new BasicParameter("T3", 11000, 10000, 200000)
    };
  SinLFO[] frequencies = {
    new SinLFO(0, 1, timingSettings[0]), 
    new SinLFO(0, 1, timingSettings[1]), 
    new SinLFO(0, 1, timingSettings[2])
    };      
    MultiSine(LX lx) {
      super(lx);
      for (int i = 0; i < numLayers; i++) {
       // addParameter(timingSettings[i]);
        addModulator(frequencies[i]).start();
        
      }
      addParameter(brightEffect);
      addParameter(saturation);
      addParameter(colorspread);
      addParameter(hue);
    }

  public void run(double deltaMs) {
    if (getChannel().getFader().getNormalized() == 0) return;

    for (LXPoint p : model.points) {
      float[] combinedDistanceSines = {
        0, 0
      };
      for (int i = 0; i < numLayers; i++) {
        combinedDistanceSines[0] += sin(TWO_PI * frequencies[i].getValuef() + p.y / distLayerDivisors[0][i]) / numLayers;
        combinedDistanceSines[1] += sin(TWO_PI * frequencies[i].getValuef() + TWO_PI*(p.z / distLayerDivisors[1][i])) / numLayers;
      }
      float hueVal = (colorspread.getValuef() * sin(TWO_PI * 1.2f*(combinedDistanceSines[0] + combinedDistanceSines[1])) + hue.getValuef()) % 360;
      float brightVal = (100 - 100) + 100 * (2 + combinedDistanceSines[0] + combinedDistanceSines[1]) / brightEffect.getValuef();
      float satVal = saturation.getValuef() + 10 * sin(TWO_PI * (combinedDistanceSines[0] + combinedDistanceSines[1]));
      colors[p.index] = lx.hsb(hueVal, satVal, brightVal);
    }
  }
}
//***************************candycloudstar*********************************************
//***************************candycloudstar*********************************************
//***************************candycloudstar*********************************************
class candycloudstar extends LXPattern {
  
  final BasicParameter darkness = new BasicParameter("DARK", 8, 7.5f, 8.5f);
  final BasicParameter scale = new BasicParameter("SCAL", 2400, 300, 3500);
  final BasicParameter speed = new BasicParameter("SPD", 1, 1, 2);
  final BasicParameter strclr = new BasicParameter("strclr", 132, 1, 360);
  private final BasicParameter stars = new BasicParameter("Stars", 90, 30, 100);
 
  public candycloudstar(LX lx) {
    super(lx);
    addLayer(new CandyCloud(lx)); 
    for (int i = 0; i < 600; ++i) {
      addLayer(new StarLayer(lx));
    }
    addParameter(stars);
    addParameter(scale);
    addParameter(darkness);
   addParameter(strclr);  
  }
  public void run(double deltaMs) {
    // The layers run automatically
  }

private class CandyCloud extends LXLayer {
  



  double time = 0;

  CandyCloud(LX lx) {
    super(lx);

  }

  public void run(double deltaMs) {
    if (getChannel().getFader().getNormalized() == 0) return;

    time += deltaMs;
    for (LXPoint p : model.points) {
      double adjustedX = p.x / scale.getValue();
      double adjustedY = p.y / scale.getValue();
      double adjustedZ = p.z / scale.getValue();
      double adjustedTime = time * speed.getValue() / 5000;

      float hue = ((float)SimplexNoise.noise(adjustedX, adjustedY, adjustedZ, adjustedTime) + 1) / 2 * 1080 % 360;

      float brightness = min(max((float)SimplexNoise.noise(p.x / 100, p.y / 100, p.z / 100 + 10000, time / 5000) * 2 + 8 - darkness.getValuef(), 0), 1) * 100;

      colors[p.index] = lx.hsb(hue, 100, brightness);
    }
  }
}

   private class StarLayer extends LXLayer {

    private final TriangleLFO maxBright = new TriangleLFO(0, stars, random(2000, 8000));
    private final SinLFO brightness = new SinLFO(-1, maxBright, random(3000, 9000)); 

    private int index = 0;

    private StarLayer(LX lx) { 
      super(lx);
      addModulator(maxBright).start();
      addModulator(brightness).start();
      pickStar();
    }

    private void pickStar() {
      index = (int) random(0, model.size-1);
    }

    public void run(double deltaMs) {
      if (brightness.getValuef() <= 0) {
        pickStar();
      } else {
        addColor(index, LXColor.hsb(strclr.getValuef(), 65, brightness.getValuef()));
      }
    }
  }
}





//********************************ShiftingPlanes******************************************************************
//********************************ShiftingPlanes******************************************************************

class ShiftingPlane extends LXPattern {

  final SinLFO a = new SinLFO(-.2f, .2f, 5300);
  final SinLFO b = new SinLFO(1, -1, 13300);
  final SinLFO c = new SinLFO(-1.4f, 1.4f, 5700);
  final SinLFO d = new SinLFO(-10, 10, 9500);

  ShiftingPlane(LX lx) {
    super(lx);
    addModulator(a).trigger();
    addModulator(b).trigger();
    addModulator(c).trigger();
    addModulator(d).trigger();
  }

  public void run(double deltaMs) {
    float hv = lx.getBaseHuef();
    float av = a.getValuef();
    float bv = b.getValuef();
    float cv = c.getValuef();
    float dv = d.getValuef();    
    float denom = sqrt(av*av + bv*bv + cv*cv);
    for (LXPoint p : model.points) {
      float d = abs(av*(p.x-model.cx) + bv*(p.y-model.cy) + cv*(p.z-model.cz) + dv) / denom;
      colors[p.index] = lx.hsb(
      (hv + abs(p.x-model.cx)*.6f + abs(p.y-model.cy)*.9f + abs(p.z - model.cz)) % 360, 
      constrain(110 - d*6, 0, 100), 
      constrain(130 - 7*d, 0, 100)
        );
    }
  }
}

//***********************************Pulley*******************************************************************
//***********************************Puley*******************************************************************
//***********************************Pulley*******************************************************************

class Pulley extends LXPattern {

  final int NUM_DIVISIONS = 16;
  private final Accelerator[] gravity = new Accelerator[NUM_DIVISIONS];
  private final Click[] delays = new Click[NUM_DIVISIONS];

  private final Click reset = new Click(2500);
  private boolean isRising = false;

  private BasicParameter sz = new BasicParameter("SIZE", 0.01f);
  private BasicParameter beatAmount = new BasicParameter("BEAT", 0.25f);

  Pulley(LX lx) {
    super(lx);
    for (int i = 0; i < NUM_DIVISIONS; ++i) {
      addModulator(gravity[i] = new Accelerator(0, 0, 0));
      addModulator(delays[i] = new Click(0));
    }
    addModulator(reset).start();
    addParameter(sz);
    addParameter(beatAmount);
    trigger();
  }

  private void trigger() {
    isRising = !isRising;
    int i = 0;
    for (Accelerator g : gravity) {
      if (isRising) {
        g.setSpeed(random(10, 20), 0).start();
      } else {
        g.setVelocity(0).setAcceleration(-200);
        delays[i].setDuration(random(0, 100)).trigger();
      }
      ++i;
    }
  }

  public void run(double deltaMs) {
    if (reset.click()) {
      trigger();
    }

    if (isRising) {
      // Fucking A, had to comment this all out because of that bizarre
      // Processing bug where some simple loop takes an absurd amount of
      // time, must be some pre-processor bug
      //      for (Accelerator g : gravity) {
      //        if (g.getValuef() > model.yMax) {
      //          g.stop();
      //        } else if (g.getValuef() > model.yMax*.55) {
      //          if (g.getVelocityf() > 10) {
      //            g.setAcceleration(-16);
      //          } else {
      //            g.setAcceleration(0);
      //          }
      //        }
      //      }
    } else {
      int j = 0;
      for (Click d : delays) {
        if (d.click()) {
          gravity[j].start();
          d.stop();
        }
        ++j;
      }
      for (Accelerator g : gravity) {
        if (g.getValuef() < 0) {
          g.setValue(-g.getValuef());
          g.setVelocity(-g.getVelocityf() * random(0.64f, 0.84f));
        }
      }
    }


    float fPos = 1 - lx.tempo.rampf();
    if (fPos < .01f) {
      fPos = .02f + 4 * (.2f - fPos);
    }
    float falloff = 100.f / (3 + sz.getValuef() * 36 + fPos * beatAmount.getValuef()*48);
    for (LXPoint p : model.points) {
      int gi = (int) constrain((p.x - model.xMin) * NUM_DIVISIONS / (model.xMax - model.xMin), 0, NUM_DIVISIONS-1);
      colors[p.index] = lx.hsb(
      (lx.getBaseHuef() + abs(p.x - model.cx)*.8f + p.y*.4f) % 360, 
      constrain(130 - p.y*.8f, 0, 100), 
      max(0, 100 - abs(p.y - gravity[gi].getValuef())*falloff)
        );
    }
  }
}

////***********************************Bouncyball*******************************************************************
////******************************************************************************************************
////***********************************Bouncyball*******************************************************************
//class BouncyBalls extends LXPattern {
//  
//  static final int NUM_BALLS = 1;
//  
//  class BouncyBall {
//       
//    Accelerator yPos;
//    TriangleLFO xPos = new TriangleLFO(0, model.xMax, random(8000, 19000));
//    float zPos;
//    
//    BouncyBall(int i) {
//      addModulator(xPos.setBasis(random(0, TWO_PI))).start();
//      addModulator(yPos = new Accelerator(0, 0, 0));
//      zPos = lerp(model.zMin, model.zMax, (i+2.) / (NUM_BALLS + 4.));
//    }
//    
//    void bounce(float midiVel) {
//      float v = 100 + 80*midiVel;
//      yPos.setSpeed(v, getAccel(v, 60 / lx.tempo.bpmf())).start();
//    }
//    
//    float getAccel(float v, float oneBeat) {
//      return -2*v / oneBeat;
//    }
//    
//    void run(double deltaMs) {
//      float flrLevel = flr.getValuef() * model.xMax/2.;
//      if (yPos.getValuef() < flrLevel) {
//        if (yPos.getVelocity() < -50) {
//          yPos.setValue(2*flrLevel-yPos.getValuef());
//          float v = -yPos.getVelocityf() * bounce.getValuef();
//          yPos.setSpeed(v, getAccel(v, 60 / lx.tempo.bpmf()));
//        } else {
//          yPos.setValue(flrLevel).stop();
//        }
//      }
//      float falloff = 130.f / (12 + blobSize.getValuef() * 36);
//      float xv = xPos.getValuef();
//      float yv = yPos.getValuef();
//      
//      for (LXPoint p : model.points) {
//        float d = sqrt((p.x-xv)*(p.x-xv) + (p.y-yv)*(p.y-yv) + .1*(p.z-zPos)*(p.z-zPos));
//        float b = constrain(130 - falloff*d, 0, 100);
//        if (b > 0) {
//          blendColor(p.index, lx.hsb(
//            (lx.getBaseHuef() + p.y*.5 + abs(model.cx - p.x) * .5) % 360,
//            max(0, 100 - .45*(p.y - flrLevel)),
//            b
//          ), LXColor.Blend.ADD);
//        }
//      }
//    }
//  }
//  
//  final BouncyBall[] balls = new BouncyBall[NUM_BALLS];
//  
//  final BasicParameter bounce = new BasicParameter("BNC", .8);
//  final BasicParameter flr = new BasicParameter("FLR", 0);
//  final BasicParameter blobSize = new BasicParameter("SIZE", 0.5);
//  
//  BouncyBalls(LX lx) {
//    super(lx);
//    for (int i = 0; i < balls.length; ++i) {
//      balls[i] = new BouncyBall(i);
//    }
//    addParameter(bounce);
//    addParameter(flr);
//    addParameter(blobSize);
//  }
//  
//  public void run(double deltaMs) {
//    setColors(#000000);
//    for (BouncyBall b : balls) {
//      b.run(deltaMs);
//    }
//  }
//  }
//***********************************XC*******************************************************************
//******************************************************************************************************
//***********************************XC*******************************************************************
class CrossSections extends LXPattern {

  final SinLFO x = new SinLFO(0, model.xMax, 5000);
  final SinLFO y = new SinLFO(0, model.yMax, 6000);
  final SinLFO z = new SinLFO(0, model.zMax, 7000);

  final BasicParameter saturation = new BasicParameter("sat", 1, 0.05f, 10);
  final BasicParameter xw = new BasicParameter("XWID", 0.3f);
  final BasicParameter yw = new BasicParameter("YWID", 0.3f);
  final BasicParameter zw = new BasicParameter("ZWID", 0.3f);  
  final BasicParameter xr = new BasicParameter("XRAT", 0.7f);
  final BasicParameter yr = new BasicParameter("YRAT", 0.6f);
  final BasicParameter zr = new BasicParameter("ZRAT", 0.5f);
  final BasicParameter xl = new BasicParameter("XLEV", 1);
  final BasicParameter yl = new BasicParameter("YLEV", 1);
  final BasicParameter zl = new BasicParameter("ZLEV", 0.5f);


  CrossSections(LX lx) {
    super(lx);
    addModulator(x).trigger();
    addModulator(y).trigger();
    addModulator(z).trigger();
    addParams();
  }

  protected void addParams() {
    addParameter(saturation);
    addParameter(xr);
    //addParameter(yr);
    addParameter(zr);    
    //addParameter(xw);
    //addParameter(xl);
    addParameter(yl);
    //addParameter(zl);
    //addParameter(yw);    
    //addParameter(zw);
  }

  public void onParameterChanged(LXParameter p) {
    if (p == xr) {
      x.setDuration(10000 - 8800*p.getValuef());
    } else if (p == yr) {
      y.setDuration(10000 - 9000*p.getValuef());
    } else if (p == zr) {
      z.setDuration(10000 - 9000*p.getValuef());
    }
  }

  float xv, yv, zv;

  protected void updateXYZVals() {
    xv = x.getValuef();
    yv = y.getValuef();
    zv = z.getValuef();
  }

  public void run(double deltaMs) {
    updateXYZVals();

    float xlv = 100*xl.getValuef();
    float ylv = 100*yl.getValuef();
    float zlv = 100*zl.getValuef();

    float xwv = 100.f / (10 + 40*xw.getValuef());
    float ywv = 100.f / (10 + 40*yw.getValuef());
    float zwv = 100.f / (10 + 40*zw.getValuef());

    for (LXPoint p : model.points) {
      int c = 0;
      c = PImage.blendColor(c, lx.hsb(
      (lx.getBaseHuef() + p.x/10 + p.y/3) % 360, 
      constrain(140 - 1.1f*abs(p.x - model.xMax/2.f)*saturation.getValuef(), 0, 100)*0.6f, 
      max(0, xlv - xwv*abs(p.x - xv))
        ), ADD);
      c = PImage.blendColor(c, lx.hsb(
      (lx.getBaseHuef() + 80 + p.y/10) % 360, 
      constrain(140 - 2.2f*abs(p.y - model.yMax/2.f)*saturation.getValuef(), 0, 100)*0.6f, 
      max(0, ylv - ywv*abs(p.y - yv))
        ), ADD); 
      c = PImage.blendColor(c, lx.hsb(
      (lx.getBaseHuef() + 160 + p.z / 10 + p.y/2) % 360, 
      constrain(140 - 2.2f*abs(p.z - model.zMax/2.f)*saturation.getValuef(), 0, 100)*0.6f, 
      max(0, zlv - zwv*abs(p.z - zv))
        ), ADD); 
      colors[p.index] = c;
    }
  }
}

////***********************************CubeBoune*******************************************************************
////******************************************************************************************************
////***********************************CubeBounce*******************************************************************
//
//class CubeBounce extends LXPattern {
//
//  private final BasicParameter cvel = new BasicParameter("cvel", 1, 1/2, 10);
//  
//  
//  class BouncingCube {
//    float bcx;
//    float bcy;
//    float bcz;
//    float edgelengthxz;
//    float edgelengthy;
//    float hue;
//    float bcvelx;
//    float bcvely;
//    float bcvelz;
//    float rhue;
//  
//    
//  BouncingCube() {
//    edgelengthxz = FEET*2;
//    edgelengthy = FEET*1.3;
//    bcx = 3;
//    bcy = random(1, 15);
//    bcz = 3;
//    bcvelx = .25;
//    bcvely = .25;
//    bcvelz = .25;
//    hue = random(100);
//    rhue = second();
//    
//  }
//  } 
//  
//  private BouncingCube bouncingcube;
//  public CubeBounce(LX lx)
//  {
//    super(lx);
//    bouncingcube = new BouncingCube();
//    addParameter(cvel);
//  }
//  public void run(double deltaMs) {
//    
//  
//    for(LXPoint p : model.points) {
//      colors[p.index] = 0;
//    }
//   
//   if (bouncingcube.bcx > model.xMax || bouncingcube.bcx < model.xMin) {
//    bouncingcube.bcvelx = -bouncingcube.bcvelx;
//    bouncingcube.hue = random(255);
//   } 
//   if (bouncingcube.bcy > model.yMax || bouncingcube.bcx < model.yMin) {
//    bouncingcube.bcvely = -bouncingcube.bcvely;
//    bouncingcube.hue = random(255);
//   }
//    if (bouncingcube.bcz > model.zMax || bouncingcube.bcx < model.zMin) {
//    bouncingcube.bcvelz = -bouncingcube.bcvelz;
//    bouncingcube.hue = random(255);
//   }
//   
//   
//   bouncingcube.bcx = bouncingcube.bcx + bouncingcube.bcvelx * cvel.getValuef();
//   bouncingcube.bcy = bouncingcube.bcy + bouncingcube.bcvely * cvel.getValuef();
//   bouncingcube.bcz = bouncingcube.bcz + bouncingcube.bcvelz * cvel.getValuef();
// 
//   for (LXPoint p : model.points) {
//     if (p.x > bouncingcube.bcx - bouncingcube.edgelengthxz && p.x < bouncingcube.bcx + bouncingcube.edgelengthxz &&
//        p.y > bouncingcube.bcy - bouncingcube.edgelengthy && p.y < bouncingcube.bcy + bouncingcube.edgelengthy &&
//       p.z > bouncingcube.bcz - bouncingcube.edgelengthxz && p.z < bouncingcube.bcz + bouncingcube.edgelengthxz) 
//   {
//     colors[p.index] = lx.hsb(bouncingcube.hue, 60, 100);
//     //colors[p.index] = lx.hsb(
//        //(bouncingcube.hue + abs(p.x - model.cx)*1 + p.y*.4) % 360,
//        //constrain(130 - p.y*.8, 0, 100),
//        //max(0, 100));
//        //colors[p.index] = lx.hsb(86, 55, max(0, 1000 - abs(p.y - bouncingcube.bcy*4)));
//   }
//   }
//}
//}

////***********************************RF*******************************************************************
////******************************************************************************************************
////***********************************RF*******************************************************************
//
//class RainbowRods extends LXPattern {
//
//  class Rod {
//    float rodx;
//    float rodz;
//    float rody;
//    float rahue;
//    float rbhue;
//    float rchue;
//    float rdhue;
//    float rehue;
//    float ravel;
//    float rbvel;
//    float rcvel;
//    float rdvel;
//    float revel;
//    float rodheight;
//    float rodsize;
//
//    Rod() {
//      rodx = random (model.xMin, model.xMax);
//      rody = 1;
//      rodz = random (model.xMin, model.xMax);
//      rahue = random(20, 84);
//      rbhue = random(20, 84);
//      rchue = random(20, 84);
//      rdhue = random(20, 84);
//      rehue = random(20, 84);
//      ravel = random (.3, 1.1);
//      rbvel = random (.3, 1.1);
//      rcvel = random (.3, 1.1);
//      rdvel = random (.3, 1.1);
//      revel = random (.3, 1.1);
//      rodheight = model.yMax;
//      rodsize = 10;
//    }
//  }
//
//  private Rod roda;
//  private Rod rodb;
//  private Rod rodc;
//  private Rod rodd;
//  private Rod rode;
//  public RainbowRods(LX lx)
//  {
//    super(lx);
//    roda = new Rod();
//    rodb = new Rod();
//    rodc = new Rod();
//    rodd = new Rod();
//    rode = new Rod();
//  }
//  public void run(double deltaMx) {
//
//    for (LXPoint p : model.points) {
//      colors[p.index] = 0;
//    }
//
//    if (roda.rody > model.yMax + roda.rodheight/2) {
//      roda.rody = -roda.rodheight/2;
//      roda.rodx = random (model.xMin, model.xMax);
//      roda.rodz = random (model.xMin, model.xMax);
//    }
//
//    if (rodb.rody > model.yMax + rodb.rodheight/2) {
//      rodb.rody = -rodb.rodheight/2;
//      rodb.rodx = random (model.xMin, model.xMax);
//      rodb.rodz = random (model.xMin, model.xMax);
//    }
//
//
//    if (rodc.rody > model.yMax + rodc.rodheight/2) {
//      rodc.rody = -rodc.rodheight/2;
//      rodc.rodx = random (model.xMin, model.xMax);
//      rodc.rodz = random (model.xMin, model.xMax);
//    }
//
//    if (rodd.rody > model.yMax + rodd.rodheight/2) {
//      rodd.rody = -rodd.rodheight/2;
//      rodd.rodx = random (model.xMin, model.xMax);
//      rodd.rodz = random (model.xMin, model.xMax);
//    }
//
//    if (rode.rody > model.yMax + rode.rodheight/2) {
//      rode.rody = -rode.rodheight/2;
//      rode.rodx = random (model.xMin, model.xMax);
//      rode.rodz = random (model.xMin, model.xMax);
//    }
//
//    roda.rody = roda.rody + roda.ravel;
//    rodb.rody = rodb.rody + rodb.rbvel;
//    rodc.rody = rodc.rody + rodc.rcvel;
//    rodd.rody = rodd.rody + rodd.rdvel;
//    rode.rody = rode.rody + rode.revel;
//
//    for (LXPoint p : model.points) {
//      if (p.x > roda.rodx - roda.rodsize && p.x < roda.rodx + roda.rodsize &&
//        p.z > roda.rodz - roda.rodsize && p.z < roda.rodz + roda.rodsize &&
//        p.y > roda.rody - roda.rodheight/2 && p.y < roda.rody + roda.rodheight/2)
//      {
//        //colors[p.index] = lx.hsb((millis() * 0.05), 50, 89);
//        colors[p.index] = lx.hsb(roda.rahue, 70, 95);
//      }
//    }
//
//
//    for (LXPoint p : model.points) {
//      if (p.x > rodb.rodx - rodb.rodsize && p.x < rodb.rodx + rodb.rodsize &&
//        p.z > rodb.rodz - rodb.rodsize && p.z < rodb.rodz + rodb.rodsize &&
//        p.y > rodb.rody - rodb.rodheight/2 && p.y < rodb.rody + rodb.rodheight/2)
//      {
//        //colors[p.index] = lx.hsb((millis() * 0.1 + p.y * 2), 50, 89);
//        colors[p.index] = lx.hsb(rodb.rbhue, 70, 95);
//      }
//    }
//    for (LXPoint p : model.points) {
//      if (p.x > rodc.rodx - rodc.rodsize && p.x < rodc.rodx + rodc.rodsize &&
//        p.z > rodc.rodz - rodc.rodsize && p.z < rodc.rodz + rodc.rodsize &&
//        p.y > rodc.rody - rodc.rodheight/2 && p.y < rodc.rody + rodc.rodheight/2)
//      {
//        //colors[p.index] = lx.hsb((millis() * 0.2 + p.y * 2), 50, 89);
//        colors[p.index] = lx.hsb(rodc.rchue, 70, 89);
//      }
//    }
//
//    for (LXPoint p : model.points) {
//      if (p.x > rodd.rodx - rodd.rodsize && p.x < rodd.rodx + rodd.rodsize &&
//        p.z > rodd.rodz - rodd.rodsize && p.z < rodd.rodz + rodd.rodsize &&
//        p.y > rodd.rody - rodd.rodheight/2 && p.y < rodd.rody + rodd.rodheight/2)
//      {
//        //colors[p.index] = lx.hsb((millis() * 0.4 + p.y * 2), 50, 89);
//        colors[p.index] = lx.hsb(rodd.rdhue, 70, 89);
//      }
//    }
//
//    for (LXPoint p : model.points) {
//      if (p.x > rode.rodx - rode.rodsize && p.x < rode.rodx + rode.rodsize &&
//        p.z > rode.rodz - rode.rodsize && p.z < rode.rodz + rode.rodsize &&
//        p.y > rode.rody - rode.rodheight/2 && p.y < rode.rody + rode.rodheight/2)
//      {
//        //colors[p.index] = lx.hsb((millis() * 0.03 + p.y * 2), 50, 89);
//        colors[p.index] = lx.hsb(rode.rehue, 70, 89);
//      }
//    }
//  }
//}

//--------------------------------xwave------------------------------------------------------------

class ywave extends LXPattern {

  private final SinLFO yPos = new SinLFO(0, model.yMax, 2000);

  public ywave(LX lx) {
    super(lx);
    addModulator(yPos).trigger();
  }
  public void run(double deltaMs) {
    float hv = lx.getBaseHuef();
    for (LXPoint p : model.points) {
      // This is a common technique for modulating brightness.
      // You can use abs() to determine the distance between two
      // values. The further away this point is from an exact
      // point, the more we decrease its brightness
      float bv = max(0, 100 - abs(p.y - yPos.getValuef()) * 3);
      colors[p.index] = lx.hsb(hv, 100, bv);
    }
  }
}
//--------------------------------xwave------------------------------------------------------------

class xwave extends LXPattern {

  private final SinLFO xPos = new SinLFO(0, model.xMax, 2000);

  public xwave(LX lx) {
    super(lx);
    addModulator(xPos).trigger();
  }
  public void run(double deltaMs) {
    float hv = lx.getBaseHuef();
    for (LXPoint p : model.points) {
      // This is a common technique for modulating brightness.
      // You can use abs() to determine the distance between two
      // values. The further away this point is from an exact
      // point, the more we decrease its brightness
      float bv = max(0, 100 - abs(p.x - xPos.getValuef()) * 6);
      colors[p.index] = lx.hsb(hv, 100, bv);
    }
  }
}
//--------------------------------zwave------------------------------------------------------------


class zwave extends LXPattern {

  private final SinLFO zPos = new SinLFO(0, model.zMax, 2000);

  public zwave(LX lx) {
    super(lx);
    addModulator(zPos).trigger();
  }
  public void run(double deltaMs) {
    float hv = lx.getBaseHuef();
    for (LXPoint p : model.points) {
      // This is a common technique for modulating brightness.
      // You can use abs() to determine the distance between two
      // values. The further away this point is from an exact
      // point, the more we decrease its brightness
      float bv = max(0, 100 - abs(p.z - zPos.getValuef()) * 3);
      colors[p.index] = lx.hsb(hv, 100, bv);
    }
  }
}

//--------------------------------RainbowInsanity------------------------------------------------------------

class RainbowInsanity extends LXPattern {

  private final SinLFO yPos = new SinLFO(0, model.yMax, 1000);
  private final SinLFO brightnessY = new SinLFO(model.yMin, model.yMax, yPos);
  private final BasicParameter saturation = new BasicParameter("sat", 60, 0, 100);

  public RainbowInsanity(LX lx) {
    super(lx);
    addModulator(yPos).trigger();
    addParameter(saturation);
  }
  public void run(double deltaMs) {
    float falloff = 10 / (FEET);

    for (LXPoint p : model.points) {
      float yWave = model.yRange*0.9f * sin(p.y / model.yRange * PI); 
      float distanceFromCenter = dist(p.x, p.y, model.cx, model.cy);
      float distanceFromBrightness = dist(p.y, abs(p.y - model.cy), brightnessY.getValuef(), yWave);
      colors[p.index] = LXColor.hsb(
      lx.getBaseHuef()/2 * distanceFromCenter*0.2f, 
      saturation.getValuef(), 
      max(0, 100 - falloff*distanceFromBrightness)
        );
    }
  }
}

//--------------------------------crazywaves------------------------------------------------------------

class CrazyWaves extends LXPattern {

  private final SinLFO yPos = new SinLFO(0, model.yMax, 8000);
  private final BasicParameter thickness = new BasicParameter("thick", 1, 1, 5);
  private final BasicParameter saturation = new BasicParameter("sat", 20, 0, 100);

  public CrazyWaves(LX lx) {
    super(lx);
    addModulator(yPos).trigger();
    addParameter(thickness);
    addParameter(saturation);
  }
  public void run(double deltaMs) {
    float hv = lx.getBaseHuef();
    for (LXPoint p : model.points) {
      float bv = max(0, 1000 - abs(p.y - yPos.getValuef()) * thickness.getValuef());
      colors[p.index] = lx.hsb(hv, saturation.getValuef(), bv);
    }
  }
}
//--------------------------------Rainbowfade------------------------------------------------------------

class rainbowfade extends LXPattern {
  private final BasicParameter speed = new BasicParameter("speed", .1f, 0.02f, .5f);
  private final BasicParameter saturation = new BasicParameter("sat", 30, 0, 100);
  private final BasicParameter ysign = new BasicParameter("ys", -1, -1, 1);
  private final BasicParameter xsign = new BasicParameter("xs", -1, -1, 1);
  private final BasicParameter zsign = new BasicParameter("zs", -1, -1, 1);

  public rainbowfade(LX lx) {
    super(lx);
    addParameter(speed);
    addParameter(saturation);
    addParameter(ysign);
    addParameter(xsign);
    addParameter(zsign);
  }
  public void run(double deltaMs) {
    for (LXPoint p : model.points) {
      colors[p.index] = lx.hsb(
      millis() * speed.getValuef() - ((ysign.getValuef())*p.y + (xsign.getValuef())*p.x + (zsign.getValuef())*p.z) * 2, 
      saturation.getValuef(), 
      80);
    }
  }
}
//--------------------------------DFC------------------------------------------------------------

class DFC extends LXPattern {
  private final BasicParameter thickness = new BasicParameter("thick", 6, 1, 20);
  private final BasicParameter speed = new BasicParameter("speed", 0.05f, 0.05f, .5f);
  private final BasicParameter saturation = new BasicParameter("sat", 30, 0, 100);

  public DFC(LX lx) {
    super(lx);
    addParameter(thickness);
    addParameter(speed);
    addParameter(saturation);
  }
  public void run(double deltaMs) {
    for (LXPoint p : model.points) {
      float distancefromcenter = dist(p.x, p.y, p.z, model.cx, model.cy, model.cz);
      colors[p.index] = lx.hsb(millis() * speed.getValuef() - distancefromcenter * thickness.getValuef(), 
      saturation.getValuef(), 
      100 - distancefromcenter*2);
    }
  }
}



//--------------------------------MultiSine------------------------------------------------------------


//--------------------------------sparkletakeover------------------------------------------------------------
class SparkleTakeOver extends LXPattern {
  int[] sparkleTimeOuts;
  int lastComplimentaryToggle = 0;
  int complimentaryToggle = 0;
  boolean resetDone = false;
  final SinLFO timing = new SinLFO(6000, 10000, 20000);
  final SawLFO coverage = new SawLFO(0, 100, timing);
  final BasicParameter hueVariation = new BasicParameter("HueVar", 0.1f, 0.1f, 0.4f);
  float hueSeparation = 180;
  float newHueVal;
  float oldHueVal;
  float newBrightVal = 100;
  float oldBrightVal = 100;
  SparkleTakeOver(LX lx) {
    super(lx);
    sparkleTimeOuts = new int[model.points.size()];
    addModulator(timing).start();    
    addModulator(coverage).start();
    addParameter(hueVariation);
  }  
  public void run(double deltaMs) {
    if (getChannel().getFader().getNormalized() == 0) return;

    if (coverage.getValuef() < 5) {
      if (!resetDone) {
        lastComplimentaryToggle = complimentaryToggle;
        oldBrightVal = newBrightVal;
        if (random(5) < 2) {          
          complimentaryToggle = 1 - complimentaryToggle;
          newBrightVal = 100;
        } else {
          newBrightVal = (newBrightVal == 100) ? 70 : 100;
        }
        for (int i = 0; i < model.points.size (); i++) {
          sparkleTimeOuts[i] = 0;
        }        
        resetDone = true;
      }
    } else {
      resetDone = false;
    }
    for (LXPoint p : model.points) {  
      float newHueVal = (lx.getBaseHuef() + complimentaryToggle * hueSeparation + hueVariation.getValuef() * p.y) % 360;
      float oldHueVal = (lx.getBaseHuef() + lastComplimentaryToggle * hueSeparation + hueVariation.getValuef() * p.y) % 360;
      if (sparkleTimeOuts[p.index] > millis()) {        
        colors[p.index] = lx.hsb(newHueVal, (30 + coverage.getValuef()) / 1.3f, newBrightVal);
      } else {
        colors[p.index] = lx.hsb(oldHueVal, (140 - coverage.getValuef()) / 1.4f, oldBrightVal);
        float chance = random(abs(sin((TWO_PI / 360) * p.x * 4) * 50) + abs(sin(TWO_PI * (p.y / 9000))) * 50);
        if (chance > (100 - 100*(pow(coverage.getValuef()/100, 2)))) {
          sparkleTimeOuts[p.index] = millis() + 50000;
        } else if (chance > 1.1f * (100 - coverage.getValuef())) {
          sparkleTimeOuts[p.index] = millis() + 100;
        }
      }
    }
  }
}


//--------------------------------------------------sparklehelix--------------------------------------------------------------------------

class SparkleHelix extends LXPattern {
  final BasicParameter minCoil = new BasicParameter("MinCOIL", .02f, .005f, .05f);
  final BasicParameter maxCoil = new BasicParameter("MaxCOIL", .03f, .005f, .05f);
  final BasicParameter Coil = new BasicParameter("COIL", .03f, .005f, .15f);
  final BasicParameter sparkle = new BasicParameter("Spark", 80, 140, 30);
  final BasicParameter sparkleSaturation = new BasicParameter("Sat", 40, 0, 80);
  final BasicParameter counterSpiralStrength = new BasicParameter("Double", 0, 0, 1);
  final BasicParameter hues = new BasicParameter("hue", 310, 0, 360);

  final SinLFO coil = new SinLFO(Coil, minCoil, 8000);
  final SinLFO rate = new SinLFO(6000, 1000, 19000);
  final SawLFO spin = new SawLFO(0, TWO_PI, rate);
  final SinLFO width = new SinLFO(10, 20, 11000);
  int[] sparkleTimeOuts;
  SparkleHelix(LX lx) {
    super(lx);
    addParameter(Coil);
    addParameter(hues);
    addParameter(sparkle);
    addParameter(sparkleSaturation);
    addParameter(minCoil);
    addParameter(maxCoil);
    addParameter(counterSpiralStrength);
    addModulator(rate).start();
    addModulator(coil).start();    
    addModulator(spin).start();
    addModulator(width).start();
    sparkleTimeOuts = new int[model.points.size()];
  }

  public void run(double deltaMs) {
    if (getChannel().getFader().getNormalized() == 0) return;

    for (LXPoint p : model.points) {
      float compensatedWidth = (0.7f + .02f / coil.getValuef()) * width.getValuef();
      float spiralVal = max(0, 100 - (100*TWO_PI / (compensatedWidth))*LXUtils.wrapdistf((TWO_PI / 360) * p.x, 8*TWO_PI + spin.getValuef() + coil.getValuef()*(p.y-model.cy), TWO_PI));
      float counterSpiralVal = counterSpiralStrength.getValuef() * max(0, 100 - (100*TWO_PI / (compensatedWidth))*LXUtils.wrapdistf((TWO_PI / 360) * p.x, 8*TWO_PI - spin.getValuef() - coil.getValuef()*(p.y-model.cy), TWO_PI));
      float hueVal = (hues.getValuef() + .4f*p.y) % 360;
      if (sparkleTimeOuts[p.index] > millis()) {        
        colors[p.index] = lx.hsb(hueVal, sparkleSaturation.getValuef(), 100);
      } else {
        colors[p.index] = lx.hsb(hueVal, 100, max(spiralVal, counterSpiralVal));        
        if (random(max(spiralVal, counterSpiralVal)) > sparkle.getValuef()) {
          sparkleTimeOuts[p.index] = millis() + 100;
        }
      }
    }
  }
}

//----------------------------------------------------------------------------------------------------------------------------

class um extends LXPattern {
  private final SinLFO fadetime = new SinLFO(1, 0.1f, 6000);

  private final BasicParameter thickness = new BasicParameter("thick", 6, 1, 20);
  private final BasicParameter speed = new BasicParameter("speed", 0.05f, 0.05f, .5f);
  private final BasicParameter saturation = new BasicParameter("sat", 30, 0, 100);


  float pointx = random (model.xMin, model.xMax);
  float pointy = random (model.yMin, model.yMax);
  float pointz = random (model.zMin, model.zMax);


  public um(LX lx) {
    super(lx);
    addParameter(thickness);
    addParameter(speed);
    addParameter(saturation);
    addModulator(fadetime).trigger();
  }
  public void run(double deltaMs) {

    for (LXPoint p : model.points) {
      float distancefrompoint = dist(p.x, p.y, p.z, pointx, pointy, pointz);
      colors[p.index] = lx.hsb(millis() * speed.getValuef() - distancefrompoint * thickness.getValuef(), 
      saturation.getValuef(), 
      max(0, 100 - distancefrompoint * 2) * fadetime.getValuef());

      if (fadetime.getValuef() < 1) {
        pointx = random (model.xMin, model.xMax);
        pointy = random (model.yMin, model.yMax);
        pointz = random (model.zMin, model.zMax);
      }
    }
  }
}

//----------------------------------------------------------------------------------------------------------------------------

class um2 extends LXPattern {
  private final SinLFO fadetime = new SinLFO(1, 0, 6000);

  private final BasicParameter thickness = new BasicParameter("thick", 6, 1, 20);
  private final BasicParameter speed = new BasicParameter("speed", 0.05f, 0.05f, .5f);
  private final BasicParameter saturation = new BasicParameter("sat", 30, 0, 100);


  float pointx = random (model.xMin, model.xMax);
  float pointy = random (model.yMin, model.yMax);
  float pointz = random (model.zMin, model.zMax);


  public um2(LX lx) {
    super(lx);
    addParameter(thickness);
    addParameter(speed);
    addParameter(saturation);
    addModulator(fadetime).trigger();
  }
  public void run(double deltaMs) {

    if (fadetime.getValuef() < 0.01f) {
      pointx = random (model.xMin, model.xMax);
      pointy = random (model.yMin, model.yMax);
      pointz = random (model.zMin, model.zMax);
    }


    for (LXPoint p : model.points) {
      float distancefrompoint = dist(p.x, p.y, p.z, pointx, pointy, pointz);
      colors[p.index] = lx.hsb(millis() * speed.getValuef() - distancefrompoint * thickness.getValuef(), 
      saturation.getValuef(), 
      max(0, 100 - distancefrompoint * 1.5f) * fadetime.getValuef());
    }
  }
}


/*
//----------------------------------------------------------------------------------------------------------------------------
 
 class um3 extends LXPattern {
 private final SawLFO fadetimeA = new SawLFO(1, 0.01, 3439);
 private final SawLFO fadetimeB = new SawLFO(1, 0.01, 2213);
 private final SawLFO fadetimeC = new SawLFO(1, 0.01, 1284);
 private final SawLFO fadetimeD = new SawLFO(1, 0.01, 5227);
 private final SawLFO fadetimeE = new SawLFO(1, 0.01, 4022);
 private final SawLFO fadetimeF = new SawLFO(1, 0.01, 9012);
 
 
 private final BasicParameter saturation = new BasicParameter("sat", 45, 0, 100);
 
 
 float pointxA = random (model.xMin, model.xMax);
 float pointyA = random (model.yMin, model.yMax);
 float pointzA = random (model.zMin, model.zMax);
 float pointxB = random (model.xMin, model.xMax);
 float pointyB = random (model.yMin, model.yMax);
 float pointzB = random (model.zMin, model.zMax);
 float pointxC = random (model.xMin, model.xMax);
 float pointyC = random (model.yMin, model.yMax);
 float pointzC = random (model.zMin, model.zMax);
 float pointxD = random (model.xMin, model.xMax);
 float pointyD = random (model.yMin, model.yMax);
 float pointzD = random (model.zMin, model.zMax);
 float pointxE = random (model.xMin, model.xMax);
 float pointyE = random (model.yMin, model.yMax);
 float pointzE = random (model.zMin, model.zMax);
 float pointxF = random (model.xMin, model.xMax);
 float pointyF = random (model.yMin, model.yMax);
 float pointzF = random (model.zMin, model.zMax);
 //  float hueA = random(1, 360);
 //  float hueB = random(1, 360);
 //  float hueC = random(1, 360);
 //  float hueD = random(1, 360);
 
 
 
 public um3(LX lx) {
 super(lx);
 addParameter(saturation);
 addModulator(fadetimeA).trigger();
 addModulator(fadetimeB).trigger();
 addModulator(fadetimeC).trigger();
 addModulator(fadetimeD).trigger();
 addModulator(fadetimeE).trigger();
 addModulator(fadetimeF).trigger();
 }
 
 public void run(double deltaMs) {
 
 if (fadetimeA.getValuef() < 0.02) {
 pointxA = random (model.xMin, model.xMax);
 pointyA = random (model.yMin, model.yMax);
 pointzA = random (model.zMin, model.zMax);
 //hueA = random(1, 360);
 } 
 
 //   if (fadetimeA.getValuef() < 0.1) {
 //      pointxA = random (model.xMin, model.xMax);
 //      pointyA = random (model.yMin, model.yMax);
 //      pointzA = random (model.zMin, model.zMax);
 //      //hueA = random(1, 360);
 //    }   
 
 if (fadetimeB.getValuef() < 0.02) {
 pointxB = random (model.xMin, model.xMax);
 pointyB = random (model.yMin, model.yMax);
 pointzB = random (model.zMin, model.zMax);
 //hueB = random(1, 360);  
 }
 
 //    if (fadetimeB.getValuef() < 0.1) {
 //      pointxB = random (model.xMin, model.xMax);
 //      pointyB = random (model.yMin, model.yMax);
 //      pointzB = random (model.zMin, model.zMax);
 //      //hueA = random(1, 360);
 //    }   
 
 if (fadetimeC.getValuef() < 0.02) {
 pointxC = random (model.xMin, model.xMax);
 pointyC = random (model.yMin, model.yMax);
 pointzC = random (model.zMin, model.zMax);
 //hueB = random(1, 360);
 }
 
 //    if (fadetimeC.getValuef() < 0.1) {
 //      pointxC = random (model.xMin, model.xMax);
 //      pointyC = random (model.yMin, model.yMax);
 //      pointzC = random (model.zMin, model.zMax);
 //    }
 
 if (fadetimeD.getValuef() < 0.02) {
 pointxD = random (model.xMin, model.xMax);
 pointyD = random (model.yMin, model.yMax);
 pointzD = random (model.zMin, model.zMax);
 //hueB = random(1, 360);  
 }
 
 //    if (fadetimeD.getValuef() < 0.1) {
 //      pointxD = random (model.xMin, model.xMax);
 //      pointyD = random (model.yMin, model.yMax);
 //      pointzD = random (model.zMin, model.zMax);
 //    }
 
 if (fadetimeE.getValuef() < 0.02) {
 pointxE = random (model.xMin, model.xMax);
 pointyE = random (model.yMin, model.yMax);
 pointzE = random (model.zMin, model.zMax);
 //hueB = random(1, 360);  
 }
 
 //    if (fadetimeE.getValuef() < 0.1) {
 //      pointxE = random (model.xMin, model.xMax);
 //      pointyE = random (model.yMin, model.yMax);
 //      pointzE = random (model.zMin, model.zMax);
 //    }
 
 if (fadetimeF.getValuef() < 0.02) {
 pointxF = random (model.xMin, model.xMax);
 pointyF = random (model.yMin, model.yMax);
 pointzF = random (model.zMin, model.zMax);
 //hueB = random(1, 360);  
 }
 
 //    if (fadetimeF.getValuef() < 0.1) {
 //      pointxF = random (model.xMin, model.xMax);
 //      pointyF = random (model.yMin, model.yMax);
 //      pointzF = random (model.zMin, model.zMax);
 //    }
 
 for (LXPoint p: model.points) {
 float distancefrompointA = dist(p.x, p.y, p.z, pointxA, pointyA, pointzA);
 float distancefrompointB = dist(p.x, p.y, p.z, pointxB, pointyB, pointzB);
 float distancefrompointC = dist(p.x, p.y, p.z, pointxC, pointyC, pointzC);
 float distancefrompointD = dist(p.x, p.y, p.z, pointxD, pointyD, pointzD);
 float distancefrompointE = dist(p.x, p.y, p.z, pointxE, pointyE, pointzE);
 float distancefrompointF = dist(p.x, p.y, p.z, pointxF, pointyF, pointzF);
 
 
 //float hueA = millis() * speed.getValuef() - distancefrompointA * thickness.getValuef();
 //float hueB = millis() * speed.getValuef() - distancefrompointB * thickness.getValuef();
 float brightnessA = max(0, 100 - distancefrompointA * 2.85) * fadetimeA.getValuef();
 float brightnessB = max(0, 100 - distancefrompointB * 2.85) * fadetimeB.getValuef();
 float brightnessC = max(0, 100 - distancefrompointC * 2.85) * fadetimeC.getValuef();
 float brightnessD = max(0, 100 - distancefrompointD * 2.85) * fadetimeD.getValuef();
 float brightnessE = max(0, 100 - distancefrompointD * 2.85) * fadetimeE.getValuef();
 float brightnessF = max(0, 100 - distancefrompointD * 2.85) * fadetimeF.getValuef();
 float hueA = max(0, 360 - distancefrompointA);
 float hueB = max(0, 360 - distancefrompointB);
 float hueC = max(0, 360 - distancefrompointC);
 float hueD = max(0, 360 - distancefrompointD);
 float hueE = max(0, 360 - distancefrompointE);
 float hueF = max(0, 360 - distancefrompointF);
 colors[p.index] = lx.hsb(
 lx.getBaseHuef()/3 + (360 - hueA + hueB + hueC + hueD + hueE + hueF),
 saturation.getValuef(),
 min(100, (brightnessA + brightnessB + brightnessC + brightnessD + brightnessE +brightnessF)));
 }
 }
 }
 
 */

//----------------------------------------------------------------------------------------------------------------------------


class Stripes extends LXPattern {
  final BasicParameter minSpacing = new BasicParameter("MinSpacing", 0.5f, .3f, 2.5f);
  final BasicParameter maxSpacing = new BasicParameter("MaxSpacing", 2, .3f, 2.5f);
  final SinLFO spacing = new SinLFO(minSpacing, maxSpacing, 8000);
  final SinLFO slopeFactor = new SinLFO(0.05f, 0.2f, 19000);

  Stripes(LX lx) {
    super(lx);
    addParameter(minSpacing);
    addParameter(maxSpacing);
    addModulator(slopeFactor).start();
    addModulator(spacing).start();
  }

  public void run(double deltaMs) {
    if (getChannel().getFader().getNormalized() == 0) return;

    for (LXPoint p : model.points) {  
      float hueVal = (millis()/1000 + .1f*p.y) % 360;
      float brightVal = 50 + 50 * sin(spacing.getValuef() * (sin((TWO_PI / 360) * 4 * p.x) + slopeFactor.getValuef() * p.y)); 
      colors[p.index] = lx.hsb(hueVal, 100, brightVal);
    }
  }
}
//---------------------------------seesaw------------------------------------------------------------------------------


class SeeSaw extends LXPattern {

  final LXProjection projection = new LXProjection(model);

  final SinLFO rate = new SinLFO(2000, 11000, 19000);
  final SinLFO rz = new SinLFO(-15, 15, rate);
  final SinLFO rx = new SinLFO(-70, 70, 11000);
  final SinLFO width = new SinLFO(1*FEET, 8*FEET, 13000);

  SeeSaw(LX lx) {
    super(lx);
    addModulator(rate).start();
    addModulator(rx).start();
    addModulator(rz).start();
    addModulator(width).start();
  }

  public void run(double deltaMs) {
    if (getChannel().getFader().getNormalized() == 0) return;

    projection
      .reset()
      .center()
        .rotate(rx.getValuef() * PI / 180, 1, 0, 0)
          .rotate(rz.getValuef() * PI / 180, 0, 0, 1);
    for (LXVector v : projection) {
      colors[v.index] = lx.hsb(
      (lx.getBaseHuef() + min(120, abs(v.y))) % 360, 
      100, 
      max(0, 100 - (100/(1*FEET))*max(0, abs(v.y) - 0.5f*width.getValuef()))
        );
    }
  }
}

//---------------------------------------------------------------------------------------------------------------
class SweepPattern extends LXPattern {

  final SinLFO speedMod = new SinLFO(3000, 9000, 5400);
  final SinLFO yPos = new SinLFO(model.yMin, model.yMax, speedMod);
  final SinLFO width = new SinLFO("WIDTH", 2*FEET, 20*FEET, 19000);

  final SawLFO offset = new SawLFO(0, TWO_PI, 9000);

  final BasicParameter amplitude = new BasicParameter("AMP", 10*FEET, 0, 20*FEET);
  final BasicParameter speed = new BasicParameter("SPEED", 1, 0, 3);
  final BasicParameter height = new BasicParameter("HEIGHT", 0, -300, 300);
  final SinLFO amp = new SinLFO(0, amplitude, 5000);

  SweepPattern(LX lx) {
    super(lx);
    addModulator(speedMod).start();
    addModulator(yPos).start();
    addModulator(width).start();
    addParameter(amplitude);
    addParameter(speed);
    addParameter(height);
    addModulator(amp).start();
    addModulator(offset).start();
  }

  public void onParameterChanged(LXParameter parameter) {
    super.onParameterChanged(parameter);
    if (parameter == speed) {
      float speedVar = 1/speed.getValuef();
      speedMod.setRange(9000 * speedVar, 5400 * speedVar);
    }
  }


  public void run(double deltaMs) {
    if (getChannel().getFader().getNormalized() == 0) return;

    for (LXPoint p : model.points) {
      float yp = yPos.getValuef() + amp.getValuef() * sin((p.x - model.cx) * .01f + offset.getValuef());
      colors[p.index] = lx.hsb(
      (lx.getBaseHuef() + abs(p.x - model.cx) * .2f +  p.z*.1f + p.y*.1f) % 360, 
      constrain(abs(p.y - model.cy), 0, 100), 
      max(0, 100 - (100/width.getValuef())*abs(p.y - yp - height.getValuef()))
        );
    }
  }
}



//----------------------------------------------------------------------------------------------------------------------------

class um3_lists extends LXPattern {


  private final BasicParameter saturation = new BasicParameter("sat", 45, 0, 70);
  private final BasicParameter dots = new BasicParameter("dots", 6, 1, 8);
  private final BasicParameter bright = new BasicParameter("bright", .4f, 0.3f, .6f);
  private final BasicParameter huefactor = new BasicParameter("hue", 1, 0.25f, 1.25f);




  int NUM_OF_DOTS = 15;


  private final ArrayList<SawLFO> fadetimes = new ArrayList<SawLFO>();


  public FloatList pointx = new FloatList();
  public FloatList pointy = new FloatList();
  public FloatList pointz = new FloatList();

  public FloatList distancefrompoint = new FloatList();
  public FloatList brightnessval = new FloatList();
  public FloatList hueval = new FloatList();



  public um3_lists(LX lx) {
    super(lx);
    addParameter(saturation);
    addParameter(dots);
    addParameter(bright);
    addParameter(huefactor);

    //int NUM_OF_DOTS = round(dots.getValuef());

    for (int i = 0; i < NUM_OF_DOTS; i++) {
      fadetimes.add(new SawLFO(1, 0.01f, PApplet.parseInt(random(5000, 15000))));
      pointx.append( random(model.xMin, model.xMax) );
      pointy.append( random(model.xMin, model.xMax) );
      pointz.append( random(model.xMin, model.xMax) );
    }
    for (int i=0; i < NUM_OF_DOTS; i=i+1) {
      addModulator(fadetimes.get(i)).trigger();
    }
  }

  public void run(double deltaMs) {

    int NUM_OF_DOTS = round(dots.getValuef());

    println(NUM_OF_DOTS);
    println(bright.getValuef());


    for (int i=0; i < NUM_OF_DOTS; i=i+1) {
      if (fadetimes.get(i).getValue() < 0.02f) {
        pointx.set(i, random(model.xMin, model.xMax));
        pointy.set(i, random(model.xMin, model.xMax));
        pointz.set(i, random(model.xMin, model.xMax));
      }
    }

    float dfp;

    for (LXPoint p : model.points) {
      for (int i=0; i < NUM_OF_DOTS; i=i+1) {

        dfp = dist(p.x, p.y, p.z, pointx.get(i), pointy.get(i), pointz.get(i));
        distancefrompoint.set(i, dfp);
        float bness=max(0, 100 - distancefrompoint.get(i) * 2.85f) * fadetimes.get(i).getValuef();

        brightnessval.set(i, bness);

        float hoo = max(0, 360 - distancefrompoint.get(i));
        hueval.set(i, hoo);

        float brightnessumm = 0;
        float hoosum = 0;

        for (float br : brightnessval) {
          brightnessumm+=br;
        }

        for (float h : hueval) {
          hoosum+=h;
        }

        colors[p.index] = lx.hsb(
        lx.getBaseHuef()/3 + (360 - hoosum * huefactor.getValuef()), 
        saturation.getValuef(), 
        min(100, brightnessumm * bright.getValuef()));
      }
    }
  }
}

//----------------------------------------------------------------------------------------------------------------------------
//make this animation relative to deltaMs

//class Rods extends LXPattern {
//  //private final BasicParameter huerate = new BasicParameter("hue", 88, 1, 360);
//  //private final BasicParameter hueup = new BasicParameter("hue", 88, 1, 360);
//  private final BasicParameter hue = new BasicParameter("hue", 300, 1, 320);
//  private final BasicParameter numberofrodsup = new BasicParameter("up", 15, 4, 100);
//  private final BasicParameter numberofrodsdown = new BasicParameter("down", 15, 4, 100);
//  private final BasicParameter speed = new BasicParameter("spd", 0.7, 0.3, 1.2);
//  //private final BasicParameter saturation = new BasicParameter("sat", 100, 0, 100);
//  //final SinLFO saturationup = new SinLFO(100, 0, 26432);
//  //final SinLFO saturationdown = new SinLFO(0, 100, 14421);
// 
// // private final BasicParameter falloff = new BasicParameter("fall", 2.8, 1, 8);
// // private final BasicParameter rodthick = new BasicParameter("thick", 3, 1, 30);
//
//public Rods(LX lx) {
//    super(lx);
//    addLayer(new RodsDown(lx));
//    addLayer(new RodsUp(lx));
//    //addParameter(huerate);
//    addParameter(numberofrodsup);
//    addParameter(numberofrodsdown);
//    addParameter(hue);
//    //addParameter(saturation);
//    addParameter(speed);
//    //addModulator(saturationup).start();
//    //addModulator(saturationdown).start();
//    //addParameter(hueup);
//    //addParameter(falloff);
//    //addParameter(rodthick);
//} 
//public void run(double deltaMs) {
//    //The layers run automatically
//      for (LXPoint p : model.points) {
//        colors[p.index] = 0;
//      }  
//
//}
// private class RodsDown extends LXLayer { 
// 
//    int numRods = 100;
//    private final FloatList rodx = new FloatList();
//    private final FloatList rody = new FloatList();
//    private final FloatList rodz = new FloatList();
//    private final FloatList rodhue = new FloatList();
//    private final FloatList rodspeed = new FloatList();
//    private final FloatList distancefromrod = new FloatList();
//    float rodheight = model.yMax;
//
//    private RodsDown(LX lx) {
//      super(lx);
//    
//      for (int i = 0; i < numRods; i++) {
//        rodx.set(i, random(model.xMin, model.xMax));  
//        rody.set(i, 30);  
//        rodz.set(i, random(model.zMin, model.zMax));
//        rodspeed.set(i, random(0.8, 2.6));
//      //  rodhue.set(i, random(280, 310));
//      }
//    }
//  
//    public void run(double deltaMs) {
//  
//      int numRods = round(numberofrodsdown.getValuef());
//      float rodsize = 3;
//  
////      for (LXPoint p : model.points) {
////        colors[p.index] = 0;
////      }
//  
//      for (int i = 0; i < numRods; i++) {
//        rody.set(i, rody.get(i) - (rodspeed.get(i)*speed.getValuef()));
//        if (rody.get(i) < model.yMin - rody.get(i)/2) {
//          rodx.set(i, random(model.xMin, model.xMax));  
//          rody.set(i, model.yMax + rodheight/2);  
//          rodz.set(i, random(model.zMin, model.zMax));
//          //rodhue.set(i, rodhue.get(i) + 1);  
//          //if (rodhue.get(i) > 360) {
//          //  rodhue.set(i, 280);
//          //}
//        }
//      }
//  
//      for (LXPoint p : model.points) {
//  
//        for (int i = 0; i < numRods; i++) {
//  
//          float hv = hue.getValuef(); 
//          float rodius = abs(p.y - rody.get(i));
//          distancefromrod.set(i, rodius);
//          float bv = max(0, 100 - distancefromrod.get(i)*4);
//          if (p.x > rodx.get(i) - rodsize && p.x < rodx.get(i) + rodsize &&
//            p.z > rodz.get(i) - rodsize && p.z < rodz.get(i) + rodsize &&
//            p.y > rody.get(i) - rodheight/2 && p.y < rody.get(i) + rodheight/2)
//          {
//            colors[p.index] = lx.hsb(
//            hv, 
//            min(100, 100 - (abs(p.y - model.cy)*2.3)), 
//            bv);
//          }
//        }
//      }
//    }
//  }
// private class RodsUp extends LXLayer { 
// 
//    int numRods = 100;
//    private final FloatList rodx = new FloatList();
//    private final FloatList rody = new FloatList();
//    private final FloatList rodz = new FloatList();
//    private final FloatList rodhue = new FloatList();
//    private final FloatList rodspeed = new FloatList();
//    private final FloatList distancefromrod = new FloatList();
//    float rodheight = model.yMax;
//
//    private RodsUp(LX lx) {
//      super(lx);
//    
//      for (int i = 0; i < numRods; i++) {
//        rodx.set(i, random(model.xMin, model.xMax));  
//        rody.set(i, 1);  
//        rodz.set(i, random(model.zMin, model.zMax));
//        rodspeed.set(i, random(0.8, 2.6));
//      //  rodhue.set(i, random(280, 310));
//      }
//    }
//  
//    public void run(double deltaMs) {
//  
//      int numRods = round(numberofrodsup.getValuef());
//      float rodsize = 3;
//  
////      for (LXPoint p : model.points) {
////        colors[p.index] = 0;
////      }
//  
//      for (int i = 0; i < numRods; i++) {
//        rody.set(i, rody.get(i) + (rodspeed.get(i)*speed.getValuef()));
//        if (rody.get(i) > model.yMax + rody.get(i)/2) {
//          rodx.set(i, random(model.xMin, model.xMax));  
//          rody.set(i, model.yMin - rodheight/2);  
//          rodz.set(i, random(model.zMin, model.zMax));
//          //rodhue.set(i, rodhue.get(i) + 1);  
//          //if (rodhue.get(i) > 360) {
//          //  rodhue.set(i, 280);
//          //}
//        }
//      }
//  
//      for (LXPoint p : model.points) {
//  
//        for (int i = 0; i < numRods; i++) {
//  
//          float hv = hue.getValuef(); 
//          float rodius = abs(p.y - rody.get(i));
//          distancefromrod.set(i, rodius);
//          float bv = max(0, 100 - distancefromrod.get(i)*4);
//          if (p.x > rodx.get(i) - rodsize && p.x < rodx.get(i) + rodsize &&
//            p.z > rodz.get(i) - rodsize && p.z < rodz.get(i) + rodsize &&
//            p.y > rody.get(i) - rodheight/2 && p.y < rody.get(i) + rodheight/2)
//          {
//            colors[p.index] = lx.hsb(
//            (hv * 1.4) % 360, 
//            min(100, 100 - (abs(p.y - model.cy)*2.3)), 
//            bv);
//          }
//        }
//      }
//    }
//  }
//}

//-------------

class block extends LXPattern {

  final SinLFO growth = new SinLFO(10, 80, 3000);

  float cubecenterx = model.cx;
  float cubecentery = model.cy;
  float cubecenterz = model.cz; 


  public block(LX lx) {
    super(lx);
    addModulator(growth).trigger();
  }

  public boolean withinbounds(float center, float len, float position) 

  {
    return center - len/2 < position && position < center + len/2;
  }

  public void run(double deltaMs) {
    float cubesize = growth.getValuef();

    if (growth.getValuef() < 11) {
      cubecenterx = random(model.xMin, model.xMax);
      cubecentery = random(model.yMin, model.yMax);
      cubecenterz = random(model.zMin, model.zMax);
    }
    for (LXPoint p : model.points) {
      float distancefromcube = dist(p.x, p.y, p.z, cubecenterx, cubecentery, cubecenterz);
      if (withinbounds(cubecenterx, cubesize, p.x) && 
        withinbounds(cubecentery, cubesize, p.y) && 
        withinbounds(cubecenterz, cubesize, p.z)) {
        colors[p.index] = lx.hsb(
        lx.getBaseHuef(), 
        63, 
        100);
      } else {
        colors[p.index] = 0;
      }
    }
  }
}















//====================================================================
class Twinkle extends LXPattern {

  private SinLFO[] bright;
  final BasicParameter brightnessParam = new BasicParameter("Brightness", 0.8f, 0.5f, 1);
  final int numBrights = 18;
  final int density = 20;
  int[] sparkleTimeOuts;
  int[] cubeToModulatorMapping;

  Twinkle(LX lx) {
    super(lx);
    addParameter(brightnessParam);

    sparkleTimeOuts = new int[model.size];
    cubeToModulatorMapping = new int[model.size];

    for (int i = 0; i < cubeToModulatorMapping.length; i++ ) {
      cubeToModulatorMapping[i] = (int)random(numBrights);
    }    

    bright = new SinLFO[numBrights];
    int numLight = density / 100 * bright.length; // number of brights array that are most bright
    int numDarkReverse = (bright.length - numLight) / 2; // number of brights array that go from light to dark

    for (int i = 0; i < bright.length; i++ ) {
      if (i <= numLight) {
        if (random(1) < 0.5f) {
          bright[i] = new SinLFO((int)random(80, 100), 0, (int)random(2300, 7700));
        } else {
          bright[i] = new SinLFO(0, (int)random(70, 90), (int)random(5300, 9200));
        }
      } else if ( i < numDarkReverse ) {
        bright[i] = new SinLFO((int)random(50, 70), 0, (int)random(3300, 11300));
      } else {
        bright[i] = new SinLFO(0, (int)random(30, 80), (int)random(3100, 9300));
      }
      addModulator(bright[i]).start();
    }
  }

  public void run(double deltaMs) {
    if (getChannel().getFader().getNormalized() == 0) return;

    for (LXPoint p : model.points) {
      if (sparkleTimeOuts[p.index] < millis()) {
        // randomly change modulators        
        if (random(10) <= 3) {
          cubeToModulatorMapping[p.index] = (int)random(numBrights);
        }
        sparkleTimeOuts[p.index] = millis() + (int)random(11100, 23300);
      }
      colors[p.index] = lx.hsb(
      0, 
      0, 
      bright[cubeToModulatorMapping[p.index]].getValuef() * brightnessParam.getValuef()
        );
    }
  }
}

class BoomEffect extends LXEffect {

  final BasicParameter falloff = new BasicParameter("WIDTH", 0.5f);
  final BasicParameter speed = new BasicParameter("SPD", 0.5f);
  final BasicParameter bright = new BasicParameter("BRT", 1.0f);
  final BasicParameter sat = new BasicParameter("SAT", 0.2f);
  List<Layer> layers = new ArrayList<Layer>();
  final float maxr = sqrt(model.xMax*model.xMax + model.yMax*model.yMax + model.zMax*model.zMax) + 10;

  class Layer {
    LinearEnvelope boom = new LinearEnvelope(-40, 500, 1300);

    Layer() {
      addModulator(boom);
      trigger();
    }

    public void trigger() {
      float falloffv = falloffv();
      boom.setRange(-100 / falloffv, maxr + 100/falloffv, 4000 - speed.getValuef() * 3300);
      boom.trigger();
    }

    public void run(double deltaMs) {
      float brightv = 100 * bright.getValuef();
      float falloffv = falloffv();
      float satv = sat.getValuef() * 100;
      float huev = lx.getBaseHuef();
      for (LXPoint p : model.points) {
        addColor(p.index, lx.hsb(
        huev, 
        satv, 
        constrain(brightv - falloffv*abs(boom.getValuef() - dist(p.x, 2*p.y, 3*p.z, model.xMax/2, model.yMax, model.zMax*1.5f)), 0, 100)) 
          );
      }
    }
  }

  BoomEffect(LX lx) {
    super(lx, true);
    addParameter(falloff);
    addParameter(speed);
    addParameter(bright);
    addParameter(sat);
  }

  public void onEnable() {
    for (Layer l : layers) {
      if (!l.boom.isRunning()) {
        l.trigger();
        return;
      }
    }
    layers.add(new Layer());
  }

  private float falloffv() {
    return 20 - 19 * falloff.getValuef();
  }

  public void onTrigger() {
    onEnable();
  }

  public void run(double deltaMs) {
    for (Layer l : layers) {
      if (l.boom.isRunning()) {
        l.run(deltaMs);
      }
    }
  }
}































class Cascade extends LXPattern {

  final BasicParameter size = new BasicParameter("SIZE", 10, 10, 50);
  final BasicParameter rate = new BasicParameter("RATE", 3000, 1500, 6000);
  final BasicParameter max = new BasicParameter("MAX", 1.9f*model.cy, 1.9f*model.cy, 1.9f*model.yMax);
  final BasicParameter min = new BasicParameter("MIN", 0, 0, model.cy);
  final BasicParameter xColor = new BasicParameter("X-COLOR", 0);
  final BasicParameter yColor = new BasicParameter("Y-COLOR", 0);

  final SinLFO py = new SinLFO(min, max, rate);

  Cascade(LX lx) {
    super(lx);
    addParameter(size);
    addParameter(rate);
    addParameter(min);
    addParameter(max);
    addParameter(xColor);
    addParameter(yColor);

    addModulator(py).start();
  }

  public void run(double deltaMs) {
    for (LXPoint p : model.points) {
      float hShift =
        abs(p.x - model.cx) / model.xRange * 360 * xColor.getValuef() +
        abs(p.y - model.cy) / model.yRange * 360 * yColor.getValuef();
      colors[p.index] = lx.hsb(
      max(30, (lx.getBaseHuef() + hShift + (p.x - model.cx)) % 360), 
      100, 
      max(150, (lx.getBaseHuef() + hShift + (p.y - model.cy)) % 360)
        );
    }
  }
}
























class Strobe extends LXPattern {
  final BasicParameter speed = new BasicParameter("SPEED", 100, 1, 100);
  float time = 0.f;

  Strobe(LX lx) {
    super(lx);
    addParameter(speed);
  }

  public void run(double deltaMs) {
    time += deltaMs * speed.getValuef();
    float timeS = time / 1000.f;

    for (LXPoint p : model.points) {
      colors[p.index] = lx.hsb(
      50, 
      100, 
      75 * ((Math.round(timeS) % 2))
        );
    }
  }
}



class Pulse extends LXPattern {
  final BasicParameter speed = new BasicParameter("SPEED", 1, 0.1f, 10);
  final BasicParameter hue = new BasicParameter("hue", 45, 0, 360);
  final BasicParameter saturation = new BasicParameter("sat", 100, 0, 100);
  float time = 0.f;

  Pulse(LX lx) {
    super(lx);
    addParameter(speed);
    addParameter(hue);
    addParameter(saturation);
  }

  public void run(double deltaMs) {
    time += deltaMs * speed.getValuef();
    float timeS = time / 1000.f;

    for (LXPoint p : model.points) {
      colors[p.index] = lx.hsb(
      hue.getValuef(), 
      saturation.getValuef(), 
      100 * ((Math.round(timeS) % 2))
        );
    }
  }
}
















class Bouncing extends LXPattern {

  final BasicParameter size = new BasicParameter("SIZE", 1, 1, 5);
  final BasicParameter rate = new BasicParameter("RATE", 2000, 1000, 4000);
  final BasicParameter max = new BasicParameter("MAX", model.cy, model.cy, model.yMax);
  final BasicParameter min = new BasicParameter("MIN", 0, 0, model.cy);

  final SinLFO py = new SinLFO(min, max, rate);

  Bouncing(LX lx) {
    super(lx);
    addParameter(size);
    addParameter(rate);
    addParameter(min);
    addParameter(max);
    addModulator(py).start();
  }

  public void run(double deltaMs) {
    for (LXPoint p : model.points) {
      colors[p.index] = lx.hsb(
      0, 
      100, 
      max(0, 100 - (100/size.getValuef()) * abs(p.y - py.getValuef()))
        );
    }
  }
}






























class ParameterWave extends LXPattern {

  final BasicParameter amp = new BasicParameter("AMP", 1);
  final BasicParameter speed = new BasicParameter("SPD", 0.5f, -1, 1); 
  final BasicParameter period = new BasicParameter("PERIOD", 0.5f, 0.5f, 5);
  final BasicParameter thick = new BasicParameter("THICK", 2, 1, 5);
  final BasicParameter xColor = new BasicParameter("X-COLOR", 0);
  final BasicParameter yColor = new BasicParameter("Y-COLOR", 0);
  final BasicParameter colorQ = new BasicParameter("clrs", 1, 1, 360);
  final BasicParameter saturation = new BasicParameter("sat", 2, 1, 6);
  final BasicParameter colorspread = new BasicParameter("clrspd", 2.8f, 1, 4);
  final SinLFO thickness = new SinLFO(0.5f, 3, 30324);
  final SinLFO amplitude = new SinLFO(0, 0.65f, 15422);

  private float base = 0;
  private float altBase = 0;

  ParameterWave(LX lx) {
    super(lx);
   // addParameter(amp);
    addParameter(speed);
    addParameter(saturation);
   // addParameter(period);
   // addParameter(thick);
  //  addParameter(xColor);
   // addParameter(yColor);
    addParameter(colorspread);
    addParameter(colorQ);
    addModulator(thickness).start();
    addModulator(amplitude).start();
  }

  public void run(double deltaMs) {
    base += deltaMs / 1000.f * TWO_PI * speed.getValuef();

    altBase += deltaMs / 1000.f * TWO_PI * (speed.getValuef() * 1.23f);

    for (LXPoint p : model.points) {
      float svy = model.cy + amplitude.getValuef() * model.yRange/2.f*sin(base + (p.x - model.cx) / model.xRange * TWO_PI * period.getValuef());
      float hShift =
        abs(p.x - model.cx) / model.xRange * 360 * xColor.getValuef() +
        abs(p.y - model.cy) / model.yRange * 360 * yColor.getValuef();
      int clr = lx.hsb(
      (lx.getBaseHuef() + hShift) % 360, 
     //(lx.getBaseHuef() + p.y) % 360,
      100, 
      max(0, 100 - (100 / (thickness.getValuef()*FEET)) * abs(p.y - svy))
        );

      float svy2 = model.cy + amplitude.getValuef() * model.yRange/2.f*sin(altBase + (p.x - model.cx) / model.xRange * TWO_PI * period.getValuef());
      float hShift2 =
        abs(p.x) / model.xRange * 360 * xColor.getValuef() +
        abs(p.y) / model.yRange * 360 * yColor.getValuef();

      float bri = constrain(max(0, 100 - (100 / (thickness.getValuef()*FEET)) * abs(p.y - svy2)), 0, 100);
      float sat;
      if (bri > brightness(clr)) {
        sat = 100;
      } else {
        bri = brightness(clr);
        sat = saturation(clr);
      }
      
      
      
      
      colors[p.index] = lx.hsb(
      //(hue(clr) + (lx.getBaseHuef() + hShift2)) % 360, 
      //(p.y *6 + colorQ.getValuef()) % 360,
      ((abs(p.y - model.cy)*colorspread.getValuef()) + colorQ.getValuef()) % 360,
      max(0, (100 - abs(p.y - model.cy)*saturation.getValuef())), 
      bri
        );
    }
  }
}



























/*
class Fire extends LXPattern {
 
 private class Flame {
 public float fheight;
 public float x;
 public float z;
 public int flametimercounter;
 
 public Flame (float fheight, float x, float y){
 this.fheight = fheight;
 this.x=x;
 this.z=z;
 this.flametimercounter=0;
 }
 }
 
 //parameters go here
 ArrayList<Flame> flames = new ArrayList<Flame>();
 float addthis;
 int distancefromflame;
 boolean addorsubtract=true;
 
 PImage fireColors = loadImage("Fire.png");
 
 Fire(LX lx) {
 super(lx);
 
 Flame a_flame = new Flame((model.yMax-model.yMin) * 0.7, (model.xMax-model.xMin) * 0.5, (model.zMax-model.zMin) * 0.5);
 flames.add(a_flame);
 }
 
 public void run(double deltaMS){
 for (Flame a_flame: flames) {
 a_flame.flametimercounter+=1;
 if (a_flame.flametimercounter > 10000){
 a_flame.flametimercounter=0;
 }
 addthis = (model.yMax-model.yMin) * random(10)/100;
 if (random(10,10000)== a_flame.flametimercounter){
 addorsubtract=!addorsubtract;
 }
 if (addorsubtract){
 a_flame.fheight+=addthis;
 }
 else {
 a_flame.fheight-=addthis;
 }
 }
 for (LXPoint p : model.points){
 float distancefromflame = 2;
 float heightofsaidflame = 1;
 for (Flame a_flame: flames) {
 float the_distance = sqrt(pow(p.x-a_flame.x,2)+pow(p.z-a_flame.z,2));
 the_distance = the_distance / (model.xMax-model.xMin);
 if (the_distance < distancefromflame){
 distancefromflame=the_distance;
 heightofsaidflame = a_flame.fheight;
 }
 }
 float position_on_spectrum = abs(heightofsaidflame - p.y) - distancefromflame;
 float hoo = fireColors.get(1, int((position_on_spectrum+1)/(2*fireColors.height)));
 // println(hoo);
 colors[p.index] = lx.hsb(hoo,100,100);
 //Make a spectrum that goes black-red-yellow-red-black and from -1 to 1 with 0 being the yellow in the middle
 //set the color of the LED based on the variable position_on_spectrum on that spectrum
 }
 }
 }
 
/*
 
/*
 //--------------------------------------------fire----------------------------------------------------------------------------------------
 class Fire extends LXPattern {
 final BasicParameter maxHeight = new BasicParameter("HEIGHT", 0.8, 0.3, 1);
 final BasicParameter flameSize = new BasicParameter("SIZE", 30, 10, 75);  
 final BasicParameter flameCount = new BasicParameter ("FLAMES", 75, 0, 75);
 final BasicParameter hue = new BasicParameter("HUE", 0, 0, 360);
 private LinearEnvelope fireHeight = new LinearEnvelope(0,0,500);
 
 private float height = 0;
 private int numFlames = 75;
 private Flame[] flames;
 
 private class Flame {
 public float flameHeight = 0;
 public float theta = random(0, 360);
 public LinearEnvelope decay = new LinearEnvelope(0,0,0);
 
 public Flame(float maxHeight, boolean groundStart){
 float flameHeight = random(0, maxHeight);
 decay.setRange(75, model.yMax * flameHeight, 1200 * flameHeight);
 if (!groundStart) {
 decay.setBasis(random(0,1));
 }
 addModulator(decay).start();
 }
 }
 
 Fire(LX lx) {
 super(lx);
 
 //patternMode = PATTERN_MODE_CUSTOM;
 
 addParameter(maxHeight);
 addParameter(flameSize);
 addParameter(flameCount);
 addParameter(hue);
 addModulator(fireHeight);
 
 flames = new Flame[numFlames];
 for (int i = 0; i < numFlames; ++i) {
 flames[i] = new Flame(height, false);
 }
 }
 
 public void updateNumFlames(int numFlames) {
 Flame[] newFlames = Arrays.copyOf(flames, numFlames);
 if (flames.length < numFlames) {
 for (int i = flames.length; i < numFlames; ++i) {
 newFlames[i] = new Flame(height, false);
 }
 }
 flames = newFlames;
 }
 
 public void run(double deltaMs) {
 if (true) {
 if (getChannel().getFader().getNormalized() == 0) return;
 
 height = maxHeight.getValuef();
 numFlames = (int) flameCount.getValuef();
 } else {
 height = fireHeight.getValuef();
 }
 
 if (flames.length != numFlames) {
 updateNumFlames(numFlames);
 }
 for (int i = 0; i < flames.length; ++i) {
 if (flames[i].decay.finished()) {
 removeModulator(flames[i].decay);
 flames[i] = new Flame(height, true);
 }
 }
 
 for (LXPoint p: model.points ) {
 float yn = p.y / model.yMax;
 float cBrt = 0;
 float cHue = 0;
 float flameWidth = flameSize.getValuef();
 for (int i = 0; i < flames.length; ++i) {
 if (abs(flames[i].theta - p.x) < (flameWidth * (1- yn))) {
 cBrt = min(100, max(0, 100 + cBrt- 2 * abs(p.y - flames[i].decay.getValuef()) - flames[i].decay.getBasisf() * 25)) ;
 cHue = max(0,  (cHue + cBrt * 0.7) * 0.5);
 }
 }
 colors[p.index] = lx.hsb(
 (cHue + hue.getValuef()) % 360,
 100,
 min(100, cBrt + (float) Math.pow(height, 0.25) * (1 - yn)  * (1 - yn) * 75)
 );
 }
 }
 }
 
 */

/**
 * Here's a simple extension of a camera component. This will be
 * rendered inside the camera view context. We just override the
 * onDraw method and invoke Processing drawing methods directly.
 */
class UIWalls extends UI3dComponent {
  
  private final float WALL_MARGIN = 2*FEET;
  private final float WALL_SIZE = model.xRange + 2*WALL_MARGIN;
  private final float WALL_THICKNESS = 1*INCHES;
  
  protected void onDraw(UI ui, PGraphics pg) {
    fill(0xff666666);
    noStroke();
    pushMatrix();
    translate(model.cx, model.cy, model.zMax + WALL_MARGIN);
    box(WALL_SIZE, WALL_SIZE, WALL_THICKNESS);
    translate(-model.xRange/2 - WALL_MARGIN, 0, -model.zRange/2 - WALL_MARGIN);
    box(WALL_THICKNESS, WALL_SIZE, WALL_SIZE);
    translate(model.xRange + 2*WALL_MARGIN, 0, 0);
    box(WALL_THICKNESS, WALL_SIZE, WALL_SIZE);
    translate(-model.xRange/2 - WALL_MARGIN, model.yRange/2 + WALL_MARGIN, 0);
    box(WALL_SIZE, WALL_THICKNESS, WALL_SIZE);
    translate(0, -model.yRange - 2*WALL_MARGIN, 0);
    box(WALL_SIZE, WALL_THICKNESS, WALL_SIZE);
    popMatrix();
  }
}

class UIEngineControl extends UIWindow {
  
  final UIKnob fpsKnob;
  
  UIEngineControl(UI ui, float x, float y) {
    super(ui, "ENGINE", x, y, UIChannelControl.WIDTH, 96);
        
    y = UIWindow.TITLE_LABEL_HEIGHT;
    new UIButton(4, y, width-8, 20) {
      protected void onToggle(boolean enabled) {
        lx.engine.setThreaded(enabled);
        fpsKnob.setEnabled(enabled);
      }
    }
    .setActiveLabel("Multi-Threaded")
    .setInactiveLabel("Single-Threaded")
    .addToContainer(this);
    
    y += 24;
    fpsKnob = new UIKnob(4, y);    
    fpsKnob
    .setParameter(lx.engine.framesPerSecond)
    .setEnabled(lx.engine.isThreaded())
    .addToContainer(this);
  }
}

class UIComponentsDemo extends UIWindow {
  
  static final int NUM_KNOBS = 4; 
  final BasicParameter[] knobParameters = new BasicParameter[NUM_KNOBS];  
  
  UIComponentsDemo(UI ui, float x, float y) {
    super(ui, "UI COMPONENTS", x, y, 140, 10);
    
    for (int i = 0; i < knobParameters.length; ++i) {
      knobParameters[i] = new BasicParameter("Knb" + (i+1), i+1, 0, 4);
      knobParameters[i].addListener(new LXParameterListener() {
        public void onParameterChanged(LXParameter p) {
          println(p.getLabel() + " value:" + p.getValue());
        }
      });
    }
    
    y = UIWindow.TITLE_LABEL_HEIGHT;
    
    new UIButton(4, y, width-8, 20)
    .setLabel("Toggle Button")
    .addToContainer(this);
    y += 24;
    
    new UIButton(4, y, width-8, 20)
    .setActiveLabel("Boop!")
    .setInactiveLabel("Momentary Button")
    .setMomentary(true)
    .addToContainer(this);
    y += 24;
    
    for (int i = 0; i < 4; ++i) {
      new UIKnob(4 + i*34, y)
      .setParameter(knobParameters[i])
      .setEnabled(i % 2 == 0)
      .addToContainer(this);
    }
    y += 48;
    
    for (int i = 0; i < 4; ++i) {
      new UISlider(UISlider.Direction.VERTICAL, 4 + i*34, y, 30, 60)
      .setParameter(new BasicParameter("VSl" + i, (i+1)*.25f))
      .setEnabled(i % 2 == 1)
      .addToContainer(this);
    }
    y += 64;
    
    for (int i = 0; i < 2; ++i) {
      new UISlider(4, y, width-8, 24)
      .setParameter(new BasicParameter("HSl" + i, (i + 1) * .25f))
      .setEnabled(i % 2 == 0)
      .addToContainer(this);
      y += 28;
    }
    
    new UIToggleSet(4, y, width-8, 24)
    .setParameter(new DiscreteParameter("Ltrs", new String[] { "A", "B", "C", "D" }))
    .addToContainer(this);
    y += 28;
    
    for (int i = 0; i < 4; ++i) {
      new UIIntegerBox(4 + i*34, y, 30, 22)
      .setParameter(new DiscreteParameter("Dcrt", 10))
      .addToContainer(this);
    }
    y += 26;
    
    new UILabel(4, y, width-8, 24)
    .setLabel("This is just a label.")
    .setAlignment(CENTER, CENTER)
    .setBorderColor(ui.theme.getControlDisabledColor())
    .addToContainer(this);
    y += 28;
    
    setSize(width, y);
  }
} 
/* Build FadeCandy output */

public void buildOutputs()
{
	lx.addOutput(new FadecandyOutput(lx, "localhost", 7890));
}


public void psenvsub () {
    // Prepare our context and subscriber
    ZMQ.Context context = ZMQ.context(1);
    ZMQ.Socket subscriber = context.socket(ZMQ.SUB);
    println("did a thing");

    subscriber.connect("tcp://localhost:6000");
    subscriber.subscribe("".getBytes());
    while (!Thread.currentThread ().isInterrupted ()) {
        // Read envelope with address
        String address = subscriber.recvStr ();
        // Read message contents
        String contents = subscriber.recvStr ();
        float number = Float.parseFloat(contents);
        println(contents);

        rotationPosition = number;

    }
    subscriber.close ();
    context.term ();
}

  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "FixtureDemo" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
