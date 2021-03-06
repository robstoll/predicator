package org.predicode.predicator.grammar;

import javax.annotation.Nonnull;
import java.util.BitSet;

import static org.predicode.predicator.grammar.CodePoints.*;


abstract class NameCharPrinter {

    static final BitSet OPERATORS = new BitSet(128);
    static final BitSet BUILTINS = new BitSet(128);
    static final BitSet QUOTES = new BitSet(128);

    static {
        OPERATORS.set('%');
        OPERATORS.set('&');
        OPERATORS.set('|');
        OPERATORS.set('+');
        OPERATORS.set('-');
        OPERATORS.set('*');
        OPERATORS.set('/');
        OPERATORS.set('>');
        OPERATORS.set('<');
        OPERATORS.set('=');
        OPERATORS.set('~');
        OPERATORS.set('^');

        BUILTINS.set(':');
        BUILTINS.set('#');
        BUILTINS.set('$');
        BUILTINS.set('@');

        QUOTES.set(BACKTICK);
        QUOTES.set(SINGLE_QUOTE);
        QUOTES.set(DOUBLE_QUOTE);
        QUOTES.set(UNDERSCORE);
    }

    @Nonnull
    abstract CharClass append(@Nonnull CharClass current, @Nonnull NamePrinter out, @Nonnull CharClass next);

    abstract void print(@Nonnull CharClass current, @Nonnull NamePrinter out, @Nonnull CharClass next, int codePoint);

    static final NameCharPrinter NAME_START_PRINTER = new NameCharPrinter() {

        @Nonnull
        @Override
        CharClass append(
                @Nonnull CharClass current,
                @Nonnull NamePrinter out,
                @Nonnull CharClass next) {
            if (next.separating()) {
                return current; // Skip leading separators
            }
            if (out.getQuoting().openQuote() || !next.nameStart(out.getQuoted())) {
                // Open quote if the name does not start with allowed start symbol
                out.print(out.getQuoted().getQuote());
            }
            return next;
        }

        @Override
        public void print(
                @Nonnull CharClass current,
                @Nonnull NamePrinter out,
                @Nonnull CharClass next,
                int codePoint) {
            // Never prints anything
        }

    };

    static final NameCharPrinter NAME_SEPARATOR_PRINTER = new NameCharPrinter() {

        @Nonnull
        @Override
        CharClass append(
                @Nonnull CharClass current,
                @Nonnull NamePrinter out,
                @Nonnull CharClass next) {
            if (next.separating()) {
                return current; // Skip subsequent separators
            }
            if (!out.getLastNonSeparating().nameGluesWith(next)) {
                // Separate next with space, unless it glues with previous one
                out.print(SPACE);
            }
            return next;
        }

        @Override
        void print(
                @Nonnull CharClass current,
                @Nonnull NamePrinter out,
                @Nonnull CharClass next,
                int codePoint) {
            // Never prints anything
        }

    };

    static final NameCharPrinter NAME_BODY_PRINTER = new NameCharPrinter() {

        @Nonnull
        @Override
        CharClass append(
                @Nonnull CharClass current,
                @Nonnull NamePrinter out,
                @Nonnull CharClass next) {
            return next;
        }

        @Override
        void print(
                @Nonnull CharClass current,
                @Nonnull NamePrinter out,
                @Nonnull CharClass next,
                int codePoint) {
            out.print(codePoint);
        }

    };

    static final NameCharPrinter NAME_ESCAPE_PRINTER = new NameCharPrinter() {

        @Nonnull
        @Override
        CharClass append(
                @Nonnull CharClass current,
                @Nonnull NamePrinter out,
                @Nonnull CharClass next) {
            return next;
        }

        @Override
        void print(
                @Nonnull CharClass current,
                @Nonnull NamePrinter out,
                @Nonnull CharClass next,
                int codePoint) {
            out.print(BACKSLASH);
            out.print(codePoint);
        }

    };

    static final NameCharPrinter NAME_CODE_PRINTER = new NameCharPrinter() {

        @Nonnull
        @Override
        CharClass append(
                @Nonnull CharClass current,
                @Nonnull NamePrinter out,
                @Nonnull CharClass next) {
            return next;
        }

        @Override
        void print(
                @Nonnull CharClass current,
                @Nonnull NamePrinter out,
                @Nonnull CharClass next,
                int codePoint) {
            out.print(BACKSLASH);
            out.print(Integer.toHexString(codePoint));
            out.print(BACKSLASH);
        }

    };

}
