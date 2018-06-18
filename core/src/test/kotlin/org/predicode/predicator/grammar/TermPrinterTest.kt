package org.predicode.predicator.grammar

import ch.tutteli.atrium.api.cc.en_UK.toBe
import ch.tutteli.atrium.assertThat
import org.junit.jupiter.api.Test
import org.predicode.predicator.terms.*


internal class TermPrinterTest {

    @Test
    fun keyword() {
        assertThat(printTerms(namedKeyword("keyword name")))
                .toBe("keyword name")
    }

    @Test
    fun atom() {
        assertThat(printTerms(namedAtom("atom name")))
                .toBe("'atom name")
    }

    @Test
    fun variable() {
        assertThat(printTerms(namedVariable("variable name")))
                .toBe("_variable name")
    }

    @Test
    fun phrase() {
        assertThat(
                printTerms(
                        Phrase(
                                namedKeyword("keyword"),
                                namedAtom("atom"))))
                .toBe("(keyword 'atom)")
    }

    @Test
    fun `closes quotes before keywords`() {
        ch.tutteli.atrium.assertThat(printTerms(
                namedAtom("atom"),
                namedKeyword("keyword")))
                .toBe("'atom' keyword")
        ch.tutteli.atrium.assertThat(
                printTerms(
                        namedVariable("variable"),
                        namedKeyword("keyword")))
                .toBe("_variable_ keyword")
    }

    @Test
    fun `separates keywords`() {
        assertThat(printTerms(
                namedKeyword("first"),
                namedKeyword("second")))
                .toBe("first second")
    }

    @Test
    fun `separates keywords from quoted terms`() {
        assertThat(printTerms(
                namedKeyword("keyword"),
                namedAtom("atom")))
                .toBe("keyword 'atom")
        assertThat(printTerms(
                namedAtom("atom"),
                namedKeyword("keyword")))
                .toBe("'atom' keyword")
        assertThat(printTerms(
                namedKeyword("keyword"),
                namedVariable("variable")))
                .toBe("keyword _variable")
        assertThat(printTerms(
                namedVariable("variable"),
                namedKeyword("keyword")))
                .toBe("_variable_ keyword")
    }

    @Test
    fun `does not double-quote terms`() {
        assertThat(printTerms(
                namedAtom("atom+"),
                namedKeyword("keyword")))
                .toBe("'atom+' keyword")
        assertThat(printTerms(
                namedVariable("variable+"),
                namedKeyword("keyword")))
                .toBe("_variable+_ keyword")
    }

    @Test
    fun `separates values from quoted terms`() {
        assertThat(printTerms(
                rawValue("value"),
                namedAtom("atom")))
                .toBe("[value] 'atom")
        assertThat(printTerms(
                namedAtom("atom"),
                rawValue("value")))
                .toBe("'atom [value]")
        assertThat(printTerms(
                rawValue("value"),
                namedVariable("variable")))
                .toBe("[value] _variable")
        assertThat(printTerms(
                namedVariable("variable"),
                rawValue("value")))
                .toBe("_variable [value]")
    }

    @Test
    fun `separates phrases from quoted terms`() {

        val phrase = Phrase(
                namedKeyword("keyword"),
                rawValue("value"))

        assertThat(printTerms(phrase, namedAtom("atom")))
                .toBe("(keyword [value]) 'atom")
        assertThat(printTerms(namedAtom("atom"), phrase))
                .toBe("'atom (keyword [value])")
        assertThat(printTerms(phrase, namedVariable("variable")))
                .toBe("(keyword [value]) _variable")
        assertThat(printTerms(namedVariable("variable"), phrase))
                .toBe("_variable (keyword [value])")
    }

    @Test
    fun `separates values from keywords`() {
        assertThat(printTerms(
                rawValue("value"),
                namedKeyword("keyword")))
                .toBe("[value] keyword")
        assertThat(printTerms(
                namedKeyword("keyword"),
                rawValue("value")))
                .toBe("keyword [value]")
    }

    @Test
    fun `separates phrases from keywords`() {

        val phrase = Phrase(
                namedKeyword("keyword"),
                rawValue("value"))

        assertThat(printTerms(phrase, namedKeyword("keyword")))
                .toBe("(keyword [value]) keyword")
        assertThat(printTerms(namedKeyword("keyword"), phrase))
                .toBe("keyword (keyword [value])")
    }

    @Test
    fun `separates values`() {
        assertThat(printTerms(
                rawValue("first"),
                rawValue("second")))
                .toBe("[first] [second]")
    }

    @Test
    fun `separates phrases`() {
        assertThat(
                printTerms(
                        Phrase(namedKeyword("phrase 1")),
                        Phrase(namedKeyword("phrase 2"))))
                .toBe("(phrase 1) (phrase 2)")
    }

}