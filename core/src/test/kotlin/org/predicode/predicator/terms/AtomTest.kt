package org.predicode.predicator.terms

import ch.tutteli.atrium.api.cc.en_UK.toBe
import ch.tutteli.atrium.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.predicode.predicator.Knowns
import org.predicode.predicator.predicates.Predicate
import org.predicode.predicator.predicates.TestPredicateResolver
import java.util.*


class AtomTest {

    lateinit var knowns: Knowns
    lateinit var resolver: Predicate.Resolver

    @BeforeEach
    fun `create knowns`() {
        knowns = Knowns()
        resolver = TestPredicateResolver(knowns)
    }

    @Test
    fun `string representation`() {
        assertThat(namedAtom("atom").toString())
                .toBe("'atom'")
    }

    @Test
    fun `matches atom with the same name`() {
        assertThat(namedAtom("name1").match(namedAtom("name1"), knowns))
                .toBe(Optional.of(knowns))
    }

    @Test
    fun `matches placeholder`() {
        assertThat(namedAtom("name1").match(Placeholder.placeholder(), knowns))
                .toBe(Optional.of(knowns))
    }

    @Test
    fun `does not match atom with another name`() {
        assertThat(namedAtom("name1").match(namedAtom("name2"), knowns))
                .toBe(Optional.empty())
    }

    @Test
    fun `resolves variable`() {

        val atom = namedAtom("name")
        val variable = namedVariable("var")

        knowns = Knowns(variable)

        assertThat(atom.match(variable, knowns).get()) {
            assertThat(subject.resolution(variable).value().get())
                    .toBe(atom)
        }
    }

    @Test
    fun `does not match other terms`() {

        val atom = namedAtom("name")

        assertThat(atom.match(namedKeyword("name"), knowns))
                .toBe(Optional.empty())
        assertThat(atom.match(rawValue(123), knowns))
                .toBe(Optional.empty())

    }

    @Test
    fun `expands to itself`() {

        val atom = namedAtom("name")

        assertThat(atom.expand(resolver).get())
                .toBe(Term.Expansion(atom, knowns))
    }

}