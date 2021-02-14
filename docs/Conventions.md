# Principles

- As close to Skia API as possible. Exceptions:
  - Methods that don’t make sense (C++ specific, etc)
  - Fix minor inconsistencies in naming
  - Methods that would clash with the Java ones (e.g. `SkPath#close` would clash with `Managed#close`, so it became `Path#closePath` in Skija)
- Skia modules -> org.jetbrains.skija.* subpackages

# General naming conventions

- camelCase in Java & C++.
- UPPER_CASE for enum values.
- Field/method prefixes:
  - `_n...` for native methods.
  - `_...`  for “private” fields.
  - `get...`/`is...` for getters.
  - `set...`/`with...` for setters.
  - `make...` for static named constructors.
  - `_FLAG_<TYPE>_<NAME>` for bit flags.
  - `...Mask` for bit masks.

Some common dictionary:

- ptr (paintPtr, canvasPtr, ...)
- finalizer
- lerp
- count (instead of size/length/...)
- ...Style instead of ...Config/...Options/...Spec for appearance of something
- ...Mode instead of ...Type/...Style/...Op/...Kind
- serializeToData/..Bytes/makeFromData/..Bytes

# Code convention

- Java/CC method order must match Skia’s *.h method order
- If no reference order is present, sort alphabetically (e.g. java imports)
- No inner classes

# Things we fix in Skia APIs

- Remove prefixes/suffixes. SkCanvas.h -> Canvas.java, kLine_SkPathSegmentMask -> LINE.
- Getter methods ALWAYS start with `get...` / `is...` (`lineNumber` -> `getLineNumber()`, `accessible` -> `isAccessible()`).
- Constructors/static builders ALWAYS start with `make...`.
- Setters/updaters that normally return `void` in Skia must return `this` in Skija.

# Visibility

- All fields/methods `public`.
- Fields/methods/inner classes not for public consumption:
  - also `public`, but prefixed with `_` (`startIndex` -> `_startIndex`).
  - annotated with @ApiStatus.Internal

Why public?

- We cannot anticipate what specific needs our clients might have. Sometimes the difference between totally possible and completely impossible might be as small as a single field made public.
- It’s strictly better to have implementation details accessible and not use them than just have them inaccessible.
- Following Skia, we want to split Skija into multiple packages (core, shaper, paragraph), impossible even with protected visibility.
- We are all grown ups. If clients use fields starting with `_`, we assume they know what they are doing, and that the alternatives were worse.
- As with private APIs, we give no guarantees with regards to `_` fields/methods. They are considered effectively private, but if you really, really need them, they are here for you.

# Data classes

- public final fields prefixed with `_`.
- public getters/setters following javaBeans convention (get/is/set/with).
- setters return this.
- flags/bit masks are not exposed as getters, instead, individual check for each flag value is done.

Why getters/setters?

- If fields goes away if future version, still can be emulated with getter (or getter might throw).
- Computable fields need getters (e.g. height = bottom - top), would be strange to have them as methods but others as fields.
- Some fields need to call native instance, again, getter can hide that.

```cpp
class LineMetrics {
public:
    size_t fStartIndex = 0;
    size_t fEndIndex = 0;
    size_t fEndExcludingWhitespaces = 0;
    size_t fEndIncludingNewline = 0;
    bool   fHardBreak = false;
    int    fFlags = 0;
    enum Flags {
      kIsBold_Flag   = 1,
      kIsItalic_Flag = 2,
    };
}
```

↓

```java
@Data
public class LineMetrics {
    public final long    _startIndex;
    public final long    _endIndex;
    public final long    _endExcludingWhitespaces;
    public final long    _endIncludingNewline;
    public final boolean _hardBreak;
    @Getter(AccessLevel.NONE)
    public final int     _flags;

    public static final int _FLAG_IS_BOLD   = 0b0001;
    public static final int _FLAG_IS_ITALIC = 0b0010;
    public boolean isBold() { return (_flags | _FLAG_IS_BOLD) != 0; }
    public boolean isItalic() { return (_flags | _FLAG_IS_ITALIC) != 0; }
}
```

# Interop

- Native methods are `public static`, name starts with `_n...` + java method name.
  - Exceptions are overloaded Skia methods. Can’t overload in JNI.
- Enum values are passed as ints (for this to work, enums must have same elements in the same order).
- Avoid arrays and objects when possible.
  - Unroll objects and small fixed-size arrays when possible (SkRect -> jfloat, jfloat, jfloat, jfloat, int[2] -> jint, jint).
  - But return data objects, e.g. IRect instead of int[].
- Pointers are passed as longs.
- Two-integers are returned as single long.
- Strings are passed as Strings (converted to UTF-8/UTF-16 on C++ side).
- When a managed object is returned, its ref count should be bumped (normally Skia follows that as well, no extra action needed).
- Prefer `Native.ptr(obj)` to `obj._ptr` because some Skia APIs are fine with accepting `nullptr` as some arguments, so obj might be null.
- Assert known constraint on Java side (e.g. `assert matrix.length == 9`).

Types correspondence:

- string indices -> jint
- size_t -> jlong
- SkColor -> jint
- SkScalar -> jfloat

# Example

```cpp
class SK_API SkCanvas {
public:
    enum PointMode {
        kPoints_PointMode, 
        kLines_PointMode,  
        kPolygon_PointMode,
    };

    void drawRRect(const SkRRect& rrect, const SkPaint& paint);
}
```

↓

```java
public class Canvas extends Managed {
    public enum PointMode { POINTS, LINES, POLYGON }

    public Canvas drawRRect(RRect rr, Paint paint) {
        Native.onNativeCall();
        _nDrawRRect(ptr, rr.left, rr.top, rr.right, rr.bottom, rr.radii, Native.ptr(paint));
        return this;
    }

    public static native void _nDrawRRect(long ptr, float left, float top, float right, float bottom, float[] radii, long paintPtr);
}
```