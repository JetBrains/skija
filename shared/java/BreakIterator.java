package org.jetbrains.skija;

import java.lang.ref.*;
import java.util.*;
import org.jetbrains.annotations.*;
import org.jetbrains.skija.*;
import org.jetbrains.skija.impl.*;

/**
 * <p>A class that locates boundaries in text.  This class defines a protocol for
 * objects that break up a piece of natural-language text according to a set
 * of criteria.  Instances or subclasses of BreakIterator can be provided, for
 * example, to break a piece of text into words, sentences, or logical characters
 * according to the conventions of some language or group of languages.
 *
 * We provide four built-in types of BreakIterator:
 * <ul>
 * <li>makeSentenceInstance() returns a BreakIterator that locates boundaries
 * between sentences.  This is useful for triple-click selection, for example.
 * <li>makeWordInstance() returns a BreakIterator that locates boundaries between
 * words.  This is useful for double-click selection or "find whole words" searches.
 * This type of BreakIterator makes sure there is a boundary position at the
 * beginning and end of each legal word.  (Numbers count as words, too.)  Whitespace
 * and punctuation are kept separate from real words.
 * <li>makeLineInstance() returns a BreakIterator that locates positions where it is
 * legal for a text editor to wrap lines.  This is similar to word breaking, but
 * not the same: punctuation and whitespace are generally kept with words (you don't
 * want a line to start with whitespace, for example), and some special characters
 * can force a position to be considered a line-break position or prevent a position
 * from being a line-break position.
 * <li>makeCharacterInstance() returns a BreakIterator that locates boundaries between
 * logical characters.  Because of the structure of the Unicode encoding, a logical
 * character may be stored internally as more than one Unicode code point.  (A with an
 * umlaut may be stored as an a followed by a separate combining umlaut character,
 * for example, but the user still thinks of it as one character.)  This iterator allows
 * various processes (especially text editors) to treat as characters the units of text
 * that a user would think of as characters, rather than the units of text that the
 * computer sees as "characters".</ul>
 * The text boundary positions are found according to the rules
 * described in Unicode Standard Annex #29, Text Boundaries, and
 * Unicode Standard Annex #14, Line Breaking Properties.  These
 * are available at http://www.unicode.org/reports/tr14/ and
 * http://www.unicode.org/reports/tr29/.
 * <p>
 * BreakIterator's interface follows an "iterator" model (hence the name), meaning it
 * has a concept of a "current position" and methods like first(), last(), next(),
 * and previous() that update the current position.  All BreakIterators uphold the
 * following invariants:
 * <ul><li>The beginning and end of the text are always treated as boundary positions.
 * <li>The current position of the iterator is always a boundary position (random-
 * access methods move the iterator to the nearest boundary position before or
 * after the specified position, not _to_ the specified position).
 * <li>DONE is used as a flag to indicate when iteration has stopped.  DONE is only
 * returned when the current position is the end of the text and the user calls next(),
 * or when the current position is the beginning of the text and the user calls
 * previous().
 * <li>Break positions are numbered by the positions of the characters that follow
 * them.  Thus, under normal circumstances, the position before the first character
 * is 0, the position after the first character is 1, and the position after the
 * last character is 1 plus the length of the string.
 * <li>The client can change the position of an iterator, or the text it analyzes,
 * at will, but cannot change the behavior.  If the user wants different behavior, he
 * must instantiate a new iterator.</ul>
 *
 * BreakIterator accesses the text it analyzes through a CharacterIterator, which makes
 * it possible to use BreakIterator to analyze text in any text-storage vehicle that
 * provides a CharacterIterator interface.
 *
 * <b>Note:</b>  Some types of BreakIterator can take a long time to create, and
 * instances of BreakIterator are not currently cached by the system.  For
 * optimal performance, keep instances of BreakIterator around as long as makes
 * sense.  For example, when word-wrapping a document, don't create and destroy a
 * new BreakIterator for each line.  Create one break iterator for the whole document
 * (or whatever stretch of text you're wrapping) and use it to do the whole job of
 * wrapping the text.
 *
  * <P>
 * <strong>Examples</strong>:<P>
 * Creating and using text boundaries
 * <blockquote>
 * <pre>
 * public static void main(String args[]) {
 *      if (args.length == 1) {
 *          String stringToExamine = args[0];
 *          //print each word in order
 *          BreakIterator boundary = BreakIterator.makeWordInstance();
 *          boundary.setText(stringToExamine);
 *          printEachForward(boundary, stringToExamine);
 *          //print each sentence in reverse order
 *          boundary = BreakIterator.makeSentenceInstance(Locale.US);
 *          boundary.setText(stringToExamine);
 *          printEachBackward(boundary, stringToExamine);
 *          printFirst(boundary, stringToExamine);
 *          printLast(boundary, stringToExamine);
 *      }
 * }
 * </pre>
 * </blockquote>
 *
 * Print each element in order
 * <blockquote>
 * <pre>
 * public static void printEachForward(BreakIterator boundary, String source) {
 *     int start = boundary.first();
 *     for (int end = boundary.next();
 *          end != BreakIterator.DONE;
 *          start = end, end = boundary.next()) {
 *          System.out.println(source.substring(start,end));
 *     }
 * }
 * </pre>
 * </blockquote>
 *
 * Print each element in reverse order
 * <blockquote>
 * <pre>
 * public static void printEachBackward(BreakIterator boundary, String source) {
 *     int end = boundary.last();
 *     for (int start = boundary.previous();
 *          start != BreakIterator.DONE;
 *          end = start, start = boundary.previous()) {
 *         System.out.println(source.substring(start,end));
 *     }
 * }
 * </pre>
 * </blockquote>
 *
 * Print first element
 * <blockquote>
 * <pre>
 * public static void printFirst(BreakIterator boundary, String source) {
 *     int start = boundary.first();
 *     int end = boundary.next();
 *     System.out.println(source.substring(start,end));
 * }
 * </pre>
 * </blockquote>
 *
 * Print last element
 * <blockquote>
 * <pre>
 * public static void printLast(BreakIterator boundary, String source) {
 *     int end = boundary.last();
 *     int start = boundary.previous();
 *     System.out.println(source.substring(start,end));
 * }
 * </pre>
 * </blockquote>
 *
 * Print the element at a specified position
 * <blockquote>
 * <pre>
 * public static void printAt(BreakIterator boundary, int pos, String source) {
 *     int end = boundary.following(pos);
 *     int start = boundary.previous();
 *     System.out.println(source.substring(start,end));
 * }
 * </pre>
 * </blockquote>
 *
 * Find the next word
 * <blockquote>
 * <pre>
 * public static int nextWordStartAfter(int pos, String text) {
 *     BreakIterator wb = BreakIterator.makeWordInstance();
 *     wb.setText(text);
 *     int wordStart = wb.following(pos);
 *     for (;;) {
 *         int wordLimit = wb.next();
 *         if (wordLimit == BreakIterator.DONE) {
 *             return BreakIterator.DONE;
 *         }
 *         int wordStatus = wb.getRuleStatus();
 *         if (wordStatus != BreakIterator.WORD_NONE) {
 *             return wordStart;
 *         }
 *         wordStart = wordLimit;
 *      }
 * }
 * </pre>
 * The iterator returned by {@link #makeWordInstance()} is unique in that
 * the break positions it returns don't represent both the start and end of the
 * thing being iterated over.  That is, a sentence-break iterator returns breaks
 * that each represent the end of one sentence and the beginning of the next.
 * With the word-break iterator, the characters between two boundaries might be a
 * word, or they might be the punctuation or whitespace between two words.  The
 * above code uses {@link #getRuleStatus()} to identify and ignore boundaries associated
 * with punctuation or other non-word characters.
 * </blockquote>
 */
public class BreakIterator extends Managed implements Cloneable {
    static { Library.staticLoad(); }

    /**
     * DONE is returned by previous() and next() after all valid
     * boundaries have been returned.
     */
    public static final int DONE = -1;

    /**
     * Tag value for "words" that do not fit into any of other categories.
     * Includes spaces and most punctuation.
     */
    public static final int WORD_NONE           = 0;

    /**
     * Upper bound for tags for uncategorized words.
     */
    public static final int WORD_NONE_LIMIT     = 100;

    /**
     * Tag value for words that appear to be numbers, lower limit.
     */
    public static final int WORD_NUMBER         = 100;

    /**
     * Tag value for words that appear to be numbers, upper limit.
     */
    public static final int WORD_NUMBER_LIMIT   = 200;

    /**
     * Tag value for words that contain letters, excluding
     * hiragana, katakana or ideographic characters, lower limit.
     */
    public static final int WORD_LETTER         = 200;

    /**
     * Tag value for words containing letters, upper limit
     */
    public static final int WORD_LETTER_LIMIT   = 300;

    /**
     * Tag value for words containing kana characters, lower limit
     */
    public static final int WORD_KANA           = 300;

    /**
     * Tag value for words containing kana characters, upper limit
     */
    public static final int WORD_KANA_LIMIT     = 400;

    /**
     * Tag value for words containing ideographic characters, lower limit
     */
    public static final int WORD_IDEO           = 400;

    /**
     * Tag value for words containing ideographic characters, upper limit
     */
    public static final int WORD_IDEO_LIMIT     = 500;

    @ApiStatus.Internal public U16String _text;

    @ApiStatus.Internal
    public BreakIterator(long ptr) {
        super(ptr, _FinalizerHolder.PTR);
    }

    @Override
    public void close() {
        super.close();
        if (_text != null)
            _text.close();
    }

    /**
     * Create a copy of this iterator
     */
    @Override
    public BreakIterator clone() {
        Stats.onNativeCall();
        return new BreakIterator(_nClone(_ptr));
    }

    /**
     * Returns a new BreakIterator instance for character breaks for the default locale.
     */
    public static BreakIterator makeCharacterInstance() {
        return makeCharacterInstance(null);
    }

    /**
     * Returns a new BreakIterator instance for character breaks for the given locale.
     */
    public static BreakIterator makeCharacterInstance(String locale) {
        Stats.onNativeCall();
        return new BreakIterator(_nMake(0, locale)); // UBRK_CHARACTER
    }

    /**
     * Returns a new BreakIterator instance for word breaks for the default locale.
     */
    public static BreakIterator makeWordInstance() {
        return makeWordInstance(null);
    }

    /**
     * Returns a new BreakIterator instance for word breaks for the given locale.
     */
    public static BreakIterator makeWordInstance(String locale) {
        Stats.onNativeCall();
        return new BreakIterator(_nMake(1, locale)); // UBRK_WORD
    }

    /**
     * Returns a new BreakIterator instance for line breaks for the default locale.
     */
    public static BreakIterator makeLineInstance() {
        return makeLineInstance(null);
    }

    /**
     * Returns a new BreakIterator instance for line breaks for the given locale.
     */
    public static BreakIterator makeLineInstance(String locale) {
        Stats.onNativeCall();
        return new BreakIterator(_nMake(2, locale)); // UBRK_LINE
    }

    /**
     * Returns a new BreakIterator instance for sentence breaks for the default locale.
     */
    public static BreakIterator makeSentenceInstance() {
        return makeSentenceInstance(null);
    }

    /**
     * Returns a new BreakIterator instance for sentence breaks for the given locale.
     */
    public static BreakIterator makeSentenceInstance(String locale) {
        Stats.onNativeCall();
        return new BreakIterator(_nMake(3, locale)); // UBRK_SENTENCE
    }

    /**
     * Returns character index of the text boundary that was most recently
     * returned by {@link next()}, {@link next(int)}, {@link previous()},
     * {@link first()}, {@link last()}, {@link following(int)} or
     * {@link preceding(int)}. If any of these methods returns
     * {@link BreakIterator#DONE} because either first or last text boundary
     * has been reached, it returns the first or last text boundary depending
     * on which one is reached.
     */
    public int current() {
        try {
            Stats.onNativeCall();
            return _nCurrent(_ptr);
        } finally {
            Reference.reachabilityFence(this);
        }
    }

    /**
     * Returns the boundary following the current boundary. If the current
     * boundary is the last text boundary, it returns {@link BreakIterator#DONE}
     * and the iterator's current position is unchanged. Otherwise, the
     * iterator's current position is set to the boundary following the current
     * boundary.
     */
    public int next() {
        try {
            Stats.onNativeCall();
            return _nNext(_ptr);
        } finally {
            Reference.reachabilityFence(this);
        }
    }

    /**
     * Advances the iterator either forward or backward the specified number of steps.
     * Negative values move backward, and positive values move forward.  This is
     * equivalent to repeatedly calling next() or previous().
     * @param n The number of steps to move.  The sign indicates the direction
     * (negative is backwards, and positive is forwards).
     * @return The character offset of the boundary position n boundaries away from
     * the current one.
     */
    public int next(int n) {
        int result = 0;
        if (n > 0) {
            for (; n > 0 && result != DONE; --n) {
                result = next();
            }
        } else if (n < 0) {
            for (; n < 0 && result != DONE; ++n) {
                result = previous();
            }
        } else {
            result = current();
        }
        return result;
    }

    /**
     * Returns the boundary following the current boundary. If the current
     * boundary is the last text boundary, it returns {@link BreakIterator#DONE}
     * and the iterator's current position is unchanged. Otherwise, the
     * iterator's current position is set to the boundary following the current
     * boundary.
     */
    public int previous() {
        try {
            Stats.onNativeCall();
            return _nPrevious(_ptr);
        } finally {
            Reference.reachabilityFence(this);
        }
    }

    /**
     * Returns the first boundary. The iterator's current position is set to the first text boundary.
     */
    public int first() {
        try {
            Stats.onNativeCall();
            return _nFirst(_ptr);
        } finally {
            Reference.reachabilityFence(this);
        }
    }

    /**
     * Returns the last boundary. The iterator's current position is set to the last text boundary.
     */
    public int last() {
        try {
            Stats.onNativeCall();
            return _nLast(_ptr);
        } finally {
            Reference.reachabilityFence(this);
        }
    }

    /**
     * Returns the last boundary preceding the specified character offset.
     * If the specified offset is equal to the first text boundary, it returns
     * {@link BreakIterator#DONE} and the iterator's current position is
     * unchanged. Otherwise, the iterator's current position is set to the
     * returned boundary. The value returned is always less than the offset or
     * the value {@link BreakIterator#DONE}.
     */
    public int preceding(int offset) {
        try {
            Stats.onNativeCall();
            return _nPreceding(_ptr, offset);
        } finally {
            Reference.reachabilityFence(this);
        }
    }

    /**
     * Returns the first boundary following the specified character offset.
     * If the specified offset is equal to the last text boundary, it returns
     * {@link BreakIterator#DONE} and the iterator's current position is
     * unchanged. Otherwise, the iterator's current position is set to the
     * returned boundary. The value returned is always greater than the offset or
     * the value {@link BreakIterator#DONE}.
     */
    public int following(int offset) {
        try {
            Stats.onNativeCall();
            return _nFollowing(_ptr, offset);
        } finally {
            Reference.reachabilityFence(this);
        }
    }

    /**
     * Returns true if the specified character offset is a text boundary.
     */
    public boolean isBoundary(int offset) {
        try {
            Stats.onNativeCall();
            return _nIsBoundary(_ptr, offset);
        } finally {
            Reference.reachabilityFence(this);
        }
    }

    /**
     * For rule-based BreakIterators, return the status tag from the
     * break rule that determined the boundary at the current iteration position.
     * <p>
     * For break iterator types that do not support a rule status,
     * a default value of 0 is returned.
     * <p>
     * @return The status from the break rule that determined the boundary
     * at the current iteration position.
     */
    public int getRuleStatus() {
        try {
            Stats.onNativeCall();
            return _nGetRuleStatus(_ptr);
        } finally {
            Reference.reachabilityFence(this);
        }
    }

    /**
     * For RuleBasedBreakIterators, get the status (tag) values from the break rule(s)
     * that determined the the boundary at the current iteration position.
     * <p>
     * For break iterator types that do not support rule status,
     * no values are returned.
     *
     * @return  an array with the status values.
     */
    public int[] getRuleStatuses() {
        try {
            Stats.onNativeCall();
            return _nGetRuleStatuses(_ptr);
        } finally {
            Reference.reachabilityFence(this);
        }
    }

    /**
     * Set a new text string to be scanned. The current scan position is reset to {@link first()}.
     */
    public void setText(String text) {
        try {
            Stats.onNativeCall();
            _text = new U16String(text);
            _nSetText(_ptr, Native.getPtr(_text));
        } finally {
            Reference.reachabilityFence(this);
            Reference.reachabilityFence(_text);
        }
    }

    @ApiStatus.Internal
    public static class _FinalizerHolder {
        public static final long PTR = _nGetFinalizer();
    }

    @ApiStatus.Internal public static native long _nGetFinalizer();
    @ApiStatus.Internal public static native long _nMake(int type, String locale);
    @ApiStatus.Internal public static native long _nClone(long ptr);
    @ApiStatus.Internal public static native int  _nCurrent(long ptr);
    @ApiStatus.Internal public static native int  _nNext(long ptr);
    @ApiStatus.Internal public static native int  _nPrevious(long ptr);
    @ApiStatus.Internal public static native int  _nFirst(long ptr);
    @ApiStatus.Internal public static native int  _nLast(long ptr);
    @ApiStatus.Internal public static native int  _nPreceding(long ptr, int offset);
    @ApiStatus.Internal public static native int  _nFollowing(long ptr, int offset);
    @ApiStatus.Internal public static native boolean _nIsBoundary(long ptr, int offset);
    @ApiStatus.Internal public static native int  _nGetRuleStatus(long ptr);
    @ApiStatus.Internal public static native int[] _nGetRuleStatuses(long ptr);
    @ApiStatus.Internal public static native void _nSetText(long ptr, long textPtr);
}
