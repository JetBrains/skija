module skija.windows {
  
  requires transitive skija.shared;
  
  opens org.jetbrains.skija.platform to skija.shared;

}