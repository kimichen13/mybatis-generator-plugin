package com.qqviaja.gradle

import org.gradle.api.Plugin
import org.gradle.api.internal.project.ProjectInternal

/**
 * Created by Kimi Chen on 2/4/16.
 */
class MybatisGeneratorPlugin implements Plugin<ProjectInternal> {

    @Override
    void apply(ProjectInternal project) {

        project.logger.info "Configuring Mybatis Generator for project: $project.name"

        final mybatisGeneratorExtension = project.extensions.create("mybatisGenerator", MybatisGeneratorExtension)

        project.configurations.create('mybatisGenerator').with {
            description = 'The cargo libraries to be used for this project.'
        }

        project.tasks.register("mbGenerator", MybatisGeneratorTask.class) {

            if (project.configurations.named('mybatisGenerator').get().getDependencies().isEmpty()) {
                project.dependencies {
                    mybatisGenerator 'org.mybatis.generator:mybatis-generator-core:1.4.0'
                    mybatisGenerator 'mysql:mysql-connector-java:5.1.47'
                    mybatisGenerator 'org.postgresql:postgresql:42.2.6'
                }
            }

            it.getMybatisGeneratorClasspath().set(project.configurations.named('mybatisGenerator'))


            it.getOverwrite().set(mybatisGeneratorExtension.getOverwrite())
            it.getConfigFile().set(mybatisGeneratorExtension.getConfigFile())
            it.getVerbose().set(mybatisGeneratorExtension.getVerbose())
            it.getTargetDir().set(mybatisGeneratorExtension.getTargetDir())

            it.getMybatisProperties().set(mybatisGeneratorExtension.getMybatisProperties())

        }


    }

}
