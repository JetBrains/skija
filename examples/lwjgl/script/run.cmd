java -Djava.awt.headless=true -ea -esa ^
-cp target\classes;^
..\..\shared\target\classes;^
..\..\native\target\classes;^
%HOMEPATH%/.m2/repository/org/jetbrains/skija/skija-shared/0.0.0-SNAPSHOT/skija-shared-0.0.0-SNAPSHOT.jar;^
%HOMEPATH%/.m2/repository/org/lwjgl/lwjgl/3.2.3/lwjgl-3.2.3.jar;^
%HOMEPATH%/.m2/repository/org/lwjgl/lwjgl-glfw/3.2.3/lwjgl-glfw-3.2.3.jar;^
%HOMEPATH%/.m2/repository/org/lwjgl/lwjgl-opengl/3.2.3/lwjgl-opengl-3.2.3.jar;^
%HOMEPATH%/.m2/repository/org/lwjgl/lwjgl/3.2.3/lwjgl-3.2.3-natives-windows.jar;^
%HOMEPATH%/.m2/repository/org/lwjgl/lwjgl-glfw/3.2.3/lwjgl-glfw-3.2.3-natives-windows.jar;^
%HOMEPATH%/.m2/repository/org/lwjgl/lwjgl-opengl/3.2.3/lwjgl-opengl-3.2.3-natives-windows.jar ^
org.jetbrains.skija.examples.lwjgl.Main
