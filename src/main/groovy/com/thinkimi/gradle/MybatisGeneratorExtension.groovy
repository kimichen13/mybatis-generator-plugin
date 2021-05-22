package com.thinkimi.gradle

import groovy.transform.ToString

/**
 * Created by Maomao Chen on 2/4/16.
 */
@ToString(includeNames = true)
class MybatisGeneratorExtension {

    def overwrite = true
    def configFile = "generatorConfig.xml"
    def verbose = false
    def targetDir = "."
    def parameters = [:]

}
