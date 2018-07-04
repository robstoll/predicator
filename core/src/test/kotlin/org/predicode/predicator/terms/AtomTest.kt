package org.predicode.predicator.terms

import ch.tutteli.atrium.api.cc.en_GB.toBe
import ch.tutteli.atrium.verbs.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.predicode.predicator.Knowns
import org.predicode.predicator.predicates.Predicate
import org.predicode.predicator.predicates.TestPredicateResolver
import org.predicode.predicator.testutils.isEmpty
import org.predicode.predicator.testutils.notToBeEmpty
import org.predicode.predicator.testutils.toContain


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
                .toContain(knowns)
    }

    @Test
    fun `matches placeholder`() {
        assertThat(namedAtom("name1").match(Placeholder.placeholder(), knowns))
                .toContain(knowns)
    }

    @Test
    fun `does not match atom with another name`() {
        assertThat(namedAtom("name1").match(namedAtom("name2"), knowns)).isEmpty()
    }

    @Test
    fun `resolves variable`() {

        val atom = namedAtom("name")
        val variable = namedVariable("var")

        knowns = Knowns(variable)

        assertThat(atom.match(variable, knowns)).notToBeEmpty {
            assertThat(subject.resolution(variable).value())
                    .toContain(atom)
        }
    }

    @Test
    fun `does not match other terms`() {

        val atom = namedAtom("name")

        assertThat(atom.match(namedKeyword("name"), knowns)).isEmpty()
        assertThat(atom.match(rawValue(123), knowns)).isEmpty()

    }

    @Test
    fun `expands to itself`() {

        val atom = namedAtom("name")

        assertThat(atom.expand(resolver))
                .toContain(Term.Expansion(atom, knowns))
    }

}
