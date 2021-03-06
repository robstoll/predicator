package org.predicode.predicator

import org.predicode.predicator.predicates.Predicate
import reactor.core.publisher.Mono
import reactor.core.publisher.toFlux
import java.util.*

operator fun Rule.Selector.invoke(call: Predicate.Call, knowns: Knowns) = matchingRules(call, knowns)

operator fun Rule.Match.component1(): Rule = rule

operator fun Rule.Match.component2(): Knowns = knowns

/**
 * Creates a selector among the given predicate resolution rules.
 *
 * @param rules rules to select matching ones from.
 */
fun selectOneOf(vararg rules: Rule): Rule.Selector = MatchingRuleSelector(rules)

private class MatchingRuleSelector(private val rules: Array<out Rule>) : Rule.Selector {

    override fun matchingRules(call: Predicate.Call, knowns: Knowns) =
            rules.toFlux().flatMap { rule ->
                Mono.justOrEmpty(rule.match(call, knowns))
            }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as MatchingRuleSelector

        return Arrays.equals(rules, other.rules)
    }

    override fun hashCode() = Arrays.hashCode(rules)

    override fun toString(): String {
        return "{\n  ${rules.joinToString("\n  ")}\n}"
    }

}
