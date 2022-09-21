package com.thinkimi.gradle

import groovy.transform.ToString
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.MapProperty

import javax.inject.Inject

/**
 * Created by Maomao Chen on 2/4/16.
 */
@ToString(includeNames = true)
class MybatisGeneratorExtension {

    def overwrite = true
    def configFile = "generatorConfig.xml"
    def verbose = false
    def targetDir = "."
    MapProperty<String, String> mybatisProperties

    @Inject
    MybatisGeneratorExtension(ObjectFactory factory) {
        this.overwrite = overwrite
        this.configFile = configFile
        this.verbose = verbose
        this.targetDir = targetDir
        this.mybatisProperties = factory.mapProperty(String.class, String.class)
    }
}
