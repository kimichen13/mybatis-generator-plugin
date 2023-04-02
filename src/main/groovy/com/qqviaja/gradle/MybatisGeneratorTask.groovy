package com.qqviaja.gradle

import org.gradle.api.DefaultTask
import org.gradle.api.file.FileCollection
import org.gradle.api.internal.project.IsolatedAntBuilder
import org.gradle.api.provider.MapProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.TaskAction

/**
 * Created by Kimi Chen on 2/4/16.
 */
abstract class MybatisGeneratorTask extends DefaultTask {

    MybatisGeneratorTask() {
        description = 'Mybatis Generator Task'
        group = 'Util'
    }

    @Internal
    abstract Property<Boolean> getOverwrite()

    @Internal
    abstract Property<String> getConfigFile()

    @Internal
    abstract Property<Boolean> getVerbose()

    @Internal
    abstract Property<String> getTargetDir()

    @Internal
    abstract MapProperty<String, String> getMybatisProperties()

    @Internal
    abstract Property<FileCollection> getMybatisGeneratorClasspath()

    @TaskAction
    void executeCargoAction() {
        services.get(IsolatedAntBuilder).withClasspath(getMybatisGeneratorClasspath().get()).execute {
            ant.taskdef(name: 'mbgenerator', classname: 'org.mybatis.generator.ant.GeneratorAntTask')

            ant.properties['generated.source.dir'] = getTargetDir().get()
            getMybatisProperties().get().each { key, val ->
                ant.project.setProperty(key, val)
            }
            ant.mbgenerator(overwrite: getOverwrite().get(), configfile: getConfigFile().get(), verbose: getVerbose().get()) {
                propertyset {
                    getMybatisProperties().get().each { key, val ->
                        propertyref(name: key)
                    }
                }
            }
        }
    }

}
