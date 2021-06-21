package org.jetbrains.skija;

import java.lang.ref.*;
import java.util.*;
import org.jetbrains.annotations.*;
import org.jetbrains.skija.impl.*;

@ApiStatus.Internal
public class PathSegmentIterator extends Managed implements Iterator<PathSegment> {
    static { Library.staticLoad(); }
    
    public final Path _path;
    public PathSegment _nextSegment;

    @Override
    public PathSegment next() {
        try {
            if (_nextSegment._verb == PathVerb.DONE)
                throw new NoSuchElementException();
            PathSegment res = _nextSegment;
            _nextSegment = _nNext(_ptr);
            return res;
        } finally {
            Reference.reachabilityFence(this);
        }
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
        try {
            long ptr = _nMake(Native.getPtr(path), forceClose);
            PathSegmentIterator i = new PathSegmentIterator(path, ptr);
            i._nextSegment = _nNext(ptr);
            return i;
        } finally {
            Reference.reachabilityFence(path);
        }
    }

    @ApiStatus.Internal
    public static class _FinalizerHolder {
        public static final long PTR = _nGetFinalizer();
    }

    public static native long _nMake(long pathPtr, boolean forceClose);
    public static native long _nGetFinalizer();
    public static native PathSegment _nNext(long ptr);
}
