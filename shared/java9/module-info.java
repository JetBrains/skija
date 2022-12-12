module org.jetbrains.skija.shared {
    exports org.jetbrains.skija;
    exports org.jetbrains.skija.impl;
    exports org.jetbrains.skija.shaper;
    exports org.jetbrains.skija.skottie;
    exports org.jetbrains.skija.sksg;
    exports org.jetbrains.skija.svg;
    exports org.jetbrains.skija.paragraph;

    requires static lombok;
    requires static org.jetbrains.annotations;
}