package com.thinkimi.gradle

import org.gradle.api.Plugin
import org.gradle.api.internal.project.ProjectInternal

/**
 * Created by Maomao Chen on 2/4/16.
 */
class MybatisGeneratorPlugin implements Plugin<ProjectInternal> {

    @Override
    void apply(ProjectInternal project) {
        project.logger.info "Configuring Mybatis Generator for project: $project.name"
        MybatisGeneratorTask task = project.tasks.create("mbGenerator", MybatisGeneratorTask);
        project.configurations.create('mybatisGenerator').with {
            description = 'The cargo libraries to be used for this project.'
        }
        project.extensions.create("mybatisGenerator", MybatisGeneratorExtension)

        task.conventionMapping.with {
            mybatisGeneratorClasspath = {
                def config = project.configurations['mybatisGenerator']
                if (config.dependencies.empty) {
                    project.dependencies {
                        mybatisGenerator 'org.mybatis.generator:mybatis-generator-core:1.3.7'
                        mybatisGenerator 'mysql:mysql-connector-java:5.1.47'
                        mybatisGenerator 'org.postgresql:postgresql:42.2.6'
                    }
                }
                config
            }
            overwrite = { project.mybatisGenerator.overwrite }
            configFile = { project.mybatisGenerator.configFile }
            verbose = { project.mybatisGenerator.verbose }
            targetDir = { project.mybatisGenerator.targetDir }
        }
    }

}
