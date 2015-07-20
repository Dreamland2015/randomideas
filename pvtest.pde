PVector v;

void setup() {
  size(200,200);

  v = new PVector(0, -50);
}

void draw() {
  translate(width/2, height/2);
    background(255);
  stroke(0);
  fill(175);
  ellipse(v.x, v.y, 12, 12);
  v.rotate(PI/100);
}
