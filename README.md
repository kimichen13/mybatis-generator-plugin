Mybatis Generator Plugin
======================================


[![Travis](https://img.shields.io/travis/kimichen13/mybatis-generator-plugin.svg)](https://travis-ci.org/kimichen13/mybatis-generator-plugin)
[![GitHub stars](https://img.shields.io/github/stars/kimichen13/mybatis-generator-plugin.svg)](https://github.com/kimichen13/mybatis-generator-plugin/stargazers)
[![GitHub forks](https://img.shields.io/github/forks/kimichen13/mybatis-generator-plugin.svg)](https://github.com/kimichen13/mybatis-generator-plugin/network)
[![GitHub license](https://img.shields.io/github/license/kimichen13/mybatis-generator-plugin.svg)](https://github.com/kimichen13/mybatis-generator-plugin/blob/master/LICENSE)



## Usage 

In your ```build.gradle``` file, add following plugin:

``` groovy
buildscript {
  repositories {
    maven {
      url "https://plugins.gradle.org/m2/"
    }
  }
  dependencies {
    classpath "gradle.plugin.com.thinkimi.gradle:mybatis-generator-plugin:2.0"
  }
}

apply plugin: "com.thinkimi.gradle.MybatisGenerator"

configurations {
    mybatisGenerator
}

mybatisGenerator {
    verbose = true
    configFile = 'src/main/resources/autogen/generatorConfig.xml'
}
```
