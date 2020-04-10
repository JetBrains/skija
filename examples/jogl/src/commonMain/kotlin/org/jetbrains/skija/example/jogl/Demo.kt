package org.jetbrains.skija.example.jogl

expect class Demo() {
    fun run(): Int
}

fun main() {
    Demo().run()
}