package com.thinkimi.gradle

import org.gradle.testkit.runner.GradleRunner
import org.testcontainers.containers.DockerComposeContainer
import spock.lang.Specification

/**
 *
 * @author Kimi Chen
 * @since 2020/3/19, Thu  *     */
class MybatisGeneratorPluginFunctionTest extends Specification {

    private DockerComposeContainer container

    void setup() {
        container = new DockerComposeContainer<>(new File("src/functionalTest/docker-compose.yaml"))
    }

    void cleanup() {
        container.stop()
    }

    def "mybatis generator task"() {
        given:
        def config = new File("src/functionalTest/resources/autogen/generatorConfig.xml")

        def projectDir = new File("build/functionalTest")
        projectDir.mkdirs()
        def autogen = new File(projectDir, "src/main/resources/autogen")
        autogen.mkdirs()
        new File(autogen, "generatorConfig.xml") << config.text

        new File(projectDir, "src/main/java/com/thinkimi/gradle").mkdirs()


        new File(projectDir, "settings.gradle") << ""
        new File(projectDir, "build.gradle") << """

            plugins {
                id('com.thinkimi.gradle.MybatisGenerator')
                id('java')
            }

            repositories {
                mavenCentral()
            }

            configurations {
                mybatisGenerator
            }

            mybatisGenerator {
                verbose = true
                configFile = 'build/functionalTest/src/main/resources/autogen/generatorConfig.xml'
                parameters username: 'thinkimi', password: '123456'
                // optional, here is the override dependencies for the plugin or you can add other database dependencies.
                dependencies {
                    mybatisGenerator 'org.mybatis.generator:mybatis-generator-core:1.3.7'
                    mybatisGenerator 'mysql:mysql-connector-java:5.1.47'
                    mybatisGenerator 'org.postgresql:postgresql:42.2.6'
                    mybatisGenerator  // Here add your mariadb dependencies or else
                }
            }
        """

        when:
        def runner = GradleRunner.create()
        runner.forwardOutput()
        runner.withPluginClasspath()
        runner.withArguments("mbGenerator")
        runner.withProjectDir(projectDir)
        def result = runner.build()

        then:
        new File(projectDir, "src/main/java/com/thinkimi/gradle/dao/CustomerMapper.java").exists()
        new File(projectDir, "src/main/java/com/thinkimi/gradle/model/Customer.java").exists()
        new File(projectDir, "src/main/java/com/thinkimi/gradle/model/CustomerExample.java").exists()
        new File(projectDir, "src/main/resources/mapper/CustomerMapper.xml").exists()
    }

}
