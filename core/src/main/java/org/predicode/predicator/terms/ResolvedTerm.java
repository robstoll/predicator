package org.predicode.predicator.terms;

import jdk.nashorn.internal.ir.annotations.Immutable;
import org.predicode.predicator.predicates.Predicate;
import reactor.core.publisher.Flux;

import javax.annotation.Nonnull;


/**
 * A plain term a query {@link Variable variable} may resolve to.
 */
@Immutable
public abstract class ResolvedTerm extends MappedTerm {

    ResolvedTerm() {
    }

    @Nonnull
    public abstract <P, R> R accept(@Nonnull Visitor<P, R> visitor, @Nonnull P p);

    @Nonnull
    @Override
    public final <P, R> R accept(@Nonnull MappedTerm.Visitor<P, R> visitor, @Nonnull P p) {
        return accept((Visitor<P, R>) visitor, p);
    }

    @Nonnull
    @Override
    public final Flux<Expansion> expand(@Nonnull Predicate.Resolver resolver) {
        return Flux.just(new Expansion(this, resolver.getKnowns()));
    }

    public interface Visitor<P, R> {

        @Nonnull
        default R visitAtom(@Nonnull Atom atom, @Nonnull P p) {
            return visitResolved(atom, p);
        }

        @Nonnull
        default R visitValue(@Nonnull Value<?> value, @Nonnull P p) {
            return visitResolved(value, p);
        }

        @Nonnull
        R visitResolved(@Nonnull ResolvedTerm term, @Nonnull P p);

    }

}
