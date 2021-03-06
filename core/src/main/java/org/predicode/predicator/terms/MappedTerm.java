package org.predicode.predicator.terms;

import jdk.nashorn.internal.ir.annotations.Immutable;

import javax.annotation.Nonnull;

import static org.predicode.predicator.terms.Placeholder.placeholder;


/**
 * A plain term a local {@link Variable variable} may be mapped to.
 */
@Immutable
public abstract class MappedTerm extends PlainTerm {

    MappedTerm() {
    }

    @Nonnull
    @Override
    public final SignatureTerm getSignature() {
        return placeholder();
    }

    @Nonnull
    public abstract <P, R> R accept(@Nonnull MappedTerm.Visitor<P, R> visitor, @Nonnull P p);

    @Nonnull
    @Override
    public final <P, R> R accept(@Nonnull PlainTerm.Visitor<P, R> visitor, @Nonnull P p) {
        return accept((Visitor<P, R>) visitor, p);
    }

    public interface Visitor<P, R> extends ResolvedTerm.Visitor<P, R> {

        @Nonnull
        default R visitVariable(@Nonnull Variable variable, @Nonnull P p) {
            return visitMapped(variable, p);
        }

        @Override
        @Nonnull
        default R visitResolved(@Nonnull ResolvedTerm term, @Nonnull P p) {
            return visitMapped(term, p);
        }

        @Nonnull
        R visitMapped(@Nonnull MappedTerm term, @Nonnull P p);

    }

}
