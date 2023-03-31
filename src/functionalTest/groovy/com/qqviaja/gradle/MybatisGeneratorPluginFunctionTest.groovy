package com.qqviaja.gradle

import org.gradle.testkit.runner.GradleRunner
import org.testcontainers.containers.DockerComposeContainer
import org.testcontainers.spock.Testcontainers
import spock.lang.Specification

/**
 *
 * @author Kimi Chen
 * @since 2020/3/19, Thu  *
 *
 * */
@Testcontainers
class MybatisGeneratorPluginFunctionTest extends Specification {

    private static DockerComposeContainer container

    static {
        container = new DockerComposeContainer<>(new File("src/functionalTest/docker-compose.yaml"))
        container.start()
    }

    def cleanup() {
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

        new File(projectDir, "src/main/java/com/qqviaja/gradle").mkdirs()


        new File(projectDir, "settings.gradle") << ""
        new File(projectDir, "build.gradle") << """
            plugins {
                id('com.qqviaja.gradle.MybatisGenerator')
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
                
                // optional, here is the override dependencies for the plugin or you can add other database dependencies.
                dependencies {
                    mybatisGenerator 'org.mybatis.generator:mybatis-generator-core:1.4.0'
                    mybatisGenerator 'mysql:mysql-connector-java:5.1.47'
                    mybatisGenerator 'org.postgresql:postgresql:42.2.6'
                    mybatisGenerator  // Here add your mariadb dependencies or else
                }
                
                mybatisProperties = ['jdbcUrl'        : 'jdbc:postgresql://localhost:5435/postgres',
                             'jdbcDriverClass': 'org.postgresql.Driver',
                             'jdbcUsername'   : 'qqviaja',
                             'jdbcPassword'   : '123456',
                ]
            }
        """

        when:
        def runner = GradleRunner.create()
        runner.forwardOutput()
        runner.withPluginClasspath()
        runner.withArguments("mbGenerator")
        runner.withProjectDir(projectDir)
        runner.build()

        then:
        new File(projectDir, "src/main/java/com/qqviaja/gradle/dao/CustomerMapper.java").exists()
        new File(projectDir, "src/main/java/com/qqviaja/gradle/model/Customer.java").exists()
        new File(projectDir, "src/main/java/com/qqviaja/gradle/model/CustomerExample.java").exists()
        new File(projectDir, "src/main/resources/mapper/CustomerMapper.xml").exists()
    }

}
