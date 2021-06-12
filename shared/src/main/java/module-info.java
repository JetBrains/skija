module skija.shared {

  exports org.jetbrains.skija;
  exports org.jetbrains.skija.shaper;
  exports org.jetbrains.skija.skottie;
  exports org.jetbrains.skija.sksg;
  exports org.jetbrains.skija.paragraph;
  exports org.jetbrains.skija.svg;
  /* TODO remove impl package */
  exports org.jetbrains.skija.impl;

  requires static lombok;
  requires static org.jetbrains.annotations;

}