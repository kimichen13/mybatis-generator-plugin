package com.qqviaja.gradle


import org.gradle.testfixtures.ProjectBuilder
import spock.lang.Specification

/**
 * @author Kimi Chen
 * @since 2020/3/19, Thu
 * */
class MybatisGeneratorPluginTest extends Specification {
    
    def "plugin registers task"() {
        given:
        def project = ProjectBuilder.builder().build()

        when:
        project.pluginManager.apply("com.qqviaja.gradle.MybatisGenerator")

        then:
        project.tasks.find { ("mbGenerator" == it.name) } instanceof MybatisGeneratorTask

        expect:
        true
    }
}