package org.jetbrains.skija;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.skija.impl.Managed;
import org.jetbrains.skija.impl.Native;
import org.jetbrains.skija.impl.Stats;

import java.util.Iterator;
import java.util.NoSuchElementException;

@ApiStatus.Internal
public class PathSegmentIterator extends Managed implements Iterator<PathSegment> {
    public final Path _path;
    public PathSegment _nextSegment;

    @Override
    public PathSegment next() {
        if (_nextSegment._verb == PathVerb.DONE)
            throw new NoSuchElementException();
        PathSegment res = _nextSegment;
        _nextSegment = _nNext(_ptr);
        return res;
    }

    @Override
    public boolean hasNext() {
        return _nextSegment._verb != PathVerb.DONE;
    }

    @ApiStatus.Internal
    public PathSegmentIterator(Path path, long ptr) {
        super(ptr, _nGetFinalizer());
        this._path = path;
        Stats.onNativeCall();
    }

    public static PathSegmentIterator make(Path path, boolean forceClose) {
        long ptr = _nMake(Native.getPtr(path), forceClose);
        PathSegmentIterator i = new PathSegmentIterator(path, ptr);
        i._nextSegment = _nNext(ptr);
        return i;
    }

    public static final  long _finalizerPtr = _nGetFinalizer();
    public static native long _nMake(long pathPtr, boolean forceClose);
    public static native long _nGetFinalizer();
    public static native PathSegment _nNext(long ptr);
}
