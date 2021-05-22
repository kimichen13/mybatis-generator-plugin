package com.thinkimi.gradle

import org.gradle.api.file.FileCollection
import org.gradle.api.internal.ConventionTask
import org.gradle.api.internal.project.IsolatedAntBuilder
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.TaskAction

/**
 * Created by Maomao Chen on 2/4/16.
 */
class MybatisGeneratorTask extends ConventionTask {

    MybatisGeneratorTask(){
        description = 'Mybatis Generator Task'
        group = 'Util'
    }

    @Internal
    def overwrite
    @Internal
    def configFile
    @Internal
    def verbose
    @Internal
    Map parameters
    @Internal
    FileCollection mybatisGeneratorClasspath

    @TaskAction
    void executeCargoAction() {
        services.get(IsolatedAntBuilder).withClasspath(getMybatisGeneratorClasspath()).execute {
            ant.taskdef(name: 'mbgenerator', classname: 'org.mybatis.generator.ant.GeneratorAntTask')
            parameters.each { ant.property(name: it.key, value: it.value) }
            ant.mbgenerator(overwrite: getOverwrite(), configfile: getConfigFile(), verbose: getVerbose()) {
                propertyset {
                    parameters.each { propertyref(name: it.key) }
                }
            }
        }
    }

}
