package org.predicode.predicator.terms

import ch.tutteli.atrium.api.cc.en_UK.toBe
import ch.tutteli.atrium.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.predicode.predicator.Knowns
import org.predicode.predicator.predicates.Predicate
import org.predicode.predicator.predicates.TestPredicateResolver
import java.util.*


class KeywordTest {

    lateinit var knowns: Knowns
    lateinit var resolver: Predicate.Resolver

    @BeforeEach
    fun `create knowns`() {
        knowns = Knowns()
        resolver = TestPredicateResolver(knowns)
    }

    @Test
    fun `string representation`() {
        assertThat(namedKeyword("keyword").toString())
                .toBe("`keyword`")
    }

    @Test
    fun `matches keyword with the same name`() {
        assertThat(namedKeyword("name1").match(namedKeyword("name1"), knowns).get()) {
            toBe(knowns)
        }
    }

    @Test
    fun `does not match keyword with another name`() {
        assertThat(namedKeyword("name1").match(namedKeyword("name2"), knowns))
                .toBe(Optional.empty())
    }

    @Test
    fun `does not match placeholder`() {
        assertThat(namedKeyword("name").match(Placeholder.placeholder(), knowns))
                .toBe(Optional.empty())
    }

    @Test
    fun `does not match other terms`() {

        val keyword = namedKeyword("name")

        assertThat(keyword.match(namedAtom("name"), knowns))
                .toBe(Optional.empty())
        assertThat(keyword.match(rawValue(123), knowns))
                .toBe(Optional.empty())
        assertThat(keyword.match(namedVariable("name"), knowns))
                .toBe(Optional.empty())
    }

    @Test
    fun `expands to itself`() {

        val keyword = namedKeyword("name")

        assertThat(keyword.expand(resolver).get())
                .toBe(Term.Expansion(keyword, knowns))
    }

}