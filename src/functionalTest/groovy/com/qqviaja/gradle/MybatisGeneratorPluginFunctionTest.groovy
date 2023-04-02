package com.qqviaja.gradle

import org.gradle.testkit.runner.GradleRunner
import org.testcontainers.containers.DockerComposeContainer
import org.testcontainers.containers.GenericContainer
import org.testcontainers.spock.Testcontainers
import org.testcontainers.utility.MountableFile
import spock.lang.Shared
import spock.lang.Specification

/**
 *
 * @author Kimi Chen
 * @since 2020/3/19, Thu  *
 *
 * */
@Testcontainers
class MybatisGeneratorPluginFunctionTest extends Specification {

    @Shared
    private DockerComposeContainer container = new DockerComposeContainer<>(new File("src/functionalTest/docker-compose.yaml"))

    def setupSpec() {
        container.start()
    }

    def cleanupSpec() {
        container.stop()
    }

    def "mybatis generator task"() {

        def projectDir = new File("build/functionalTest")
        projectDir.deleteDir()
        projectDir.mkdirs()
        def autogen = new File(projectDir, "src/main/resources/autogen")
        autogen.mkdirs()
        new File(autogen, "generatorConfig.xml") << new File("src/functionalTest/resources/autogen/generatorConfig.xml").text
        new File(projectDir, ".gradle/daemon/${gradleVersion}").mkdirs()
        new File(projectDir, "src/main/java/com/qqviaja/gradle").mkdirs()
        new File(projectDir, "settings.gradle") << "rootProject.name = 'mybatis-generator-plugin-test'"
        new File(projectDir, "build.gradle") <<
                """plugins {
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
        mybatisGenerator 'org.mybatis.generator:mybatis-generator-core:1.4.2'
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

        def gradleContainer = new GenericContainer<>("gradle:${gradleVersion}-jdk${javaVersion}")
                .withFileSystemBind(projectDir.absolutePath, '/app')
                .withFileSystemBind('~/Github/mybatis-generator-plugin', '/home/gradle/plugin')
                .withWorkingDirectory('/app')

        setup:
        gradleContainer.withCopyFileToContainer(MountableFile.forHostPath("build/functionalTest/build.gradle"), '/app/build.gradle')
        gradleContainer.withCopyFileToContainer(MountableFile.forHostPath("build/functionalTest/settings.gradle"), '/app/settings.gradle')
        gradleContainer.withCopyFileToContainer(MountableFile.forHostPath("build/functionalTest/src/main/resources/autogen/generatorConfig.xml"), '/app/src/main/resources/autogen/generatorConfig.xml')

        def gradleRunner = GradleRunner.create()
                .withProjectDir(projectDir)
                .withGradleVersion(gradleVersion)
                .withArguments("mbGenerator")
                .withPluginClasspath()

        when:
        gradleContainer.start()
        gradleRunner.build()

        then:
        new File(projectDir, "src/main/java/com/qqviaja/gradle/dao/CustomerMapper.java").exists()
        new File(projectDir, "src/main/java/com/qqviaja/gradle/model/Customer.java").exists()
        new File(projectDir, "src/main/java/com/qqviaja/gradle/model/CustomerExample.java").exists()
        new File(projectDir, "src/main/resources/mapper/CustomerMapper.xml").exists()

        cleanup:
        gradleContainer.stop()

        where:
        gradleVersion << ["7.2", "7.3.3", "8.0.2"]
        javaVersion << ["8", "11", "11"]
    }

}
