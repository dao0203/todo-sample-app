package com.example.testing.rules

import dagger.hilt.android.testing.HiltAndroidRule
import org.junit.rules.RuleChain
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement

/**
 * TestRuleの説明:
 * TestRuleは、テストメソッドやテストメソッド群の実行方法や報告方法を変更するものです。
 * TestRuleは、そうしなければ不合格になるテストを合格させるためのチェックを追加したり、
 * テストに必要なセットアップやクリーンアップを行ったり、
 * あるいはテストの実行を監視して それを別の場所で報告したりします。
 */
class HiltAndroidAutoInjectRule(
    private val testInstance: Any,
) : TestRule {
    override fun apply(base: Statement?, description: Description?): Statement {
        // Hilt用のテストルール。これがないとDaggerComponentが生成されない。
        val hiltAndroidRule = HiltAndroidRule(testInstance)
        return RuleChain
            .outerRule(hiltAndroidRule)
            .around(HiltInjectRule(hiltAndroidRule))
            .apply(base, description)
    }
}
