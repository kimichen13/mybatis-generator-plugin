Mybatis Generator Plugin
======================================


[![Travis](https://img.shields.io/travis/kimichen13/mybatis-generator-plugin.svg)](https://travis-ci.org/kimichen13/mybatis-generator-plugin)
[![GitHub stars](https://img.shields.io/github/stars/kimichen13/mybatis-generator-plugin.svg)](https://github.com/kimichen13/mybatis-generator-plugin/stargazers)
[![GitHub forks](https://img.shields.io/github/forks/kimichen13/mybatis-generator-plugin.svg)](https://github.com/kimichen13/mybatis-generator-plugin/network)
[![GitHub license](https://img.shields.io/github/license/kimichen13/mybatis-generator-plugin.svg)](https://github.com/kimichen13/mybatis-generator-plugin/blob/master/LICENSE)


## Description

This is only the wrapper for [MyBatis Generator](http://www.mybatis.org/generator/) on Gradle.

Every details about the generate defined in the file which you declare the path in the `mybatisGenerator/configFile`.

You can override the dependencies to the newest version in the configuration, or other database dependencies.

## Usage 

In your ```build.gradle``` file, add following plugin in two ways:

### Using the [plugins DSL](https://docs.gradle.org/current/userguide/plugins.html#sec:plugins_block):

``` groovy
plugins {
  id "com.qqviaja.gradle.MybatisGenerator" version "2.5"
}
```

### Using [legacy plugin application](https://docs.gradle.org/current/userguide/plugins.html#sec:old_plugin_application):
``` groovy
buildscript {
  repositories {
    maven {
      url "https://plugins.gradle.org/m2/"
    }
  }
  dependencies {
    classpath "com.qqviaja.gradle:mybatis-generator-plugin:2.5"
  }
}

apply plugin: "com.qqviaja.gradle.MybatisGenerator"
```

### Add configuration:

``` groovy
configurations {
    mybatisGenerator
}

mybatisGenerator {
    verbose = true
    configFile = 'src/main/resources/autogen/generatorConfig.xml'
    mybatisProperties = ['key1' : "value1",'key2' : "value2"]
    
    // optional, here is the override dependencies for the plugin or you can add other database dependencies.
    dependencies {
        mybatisGenerator 'org.mybatis.generator:mybatis-generator-core:1.4.2'
        mybatisGenerator 'mysql:mysql-connector-java:5.1.47'
        mybatisGenerator 'org.postgresql:postgresql:42.2.6'
        mybatisGenerator  // Here add your mariadb dependencies or else
    }
}
```

## Properties support in `generatorConfig.xml`

Properties set under `mybatisProperties` can be referenced with placeholder syntax in the `configFile`.

```groovy
        mybatisProperties = ['jdbcUrl'        : 'jdbc:postgresql:.....',
                             'jdbcDriverClass': 'org.postgresql.Driver',
                             'jdbcUsername'   : '...',
                             'jdbcPassword'   : '...',
        ]

```

```xml

<!-- generatorConfig.xml -->

<!-- reference the parameters by using ${...} -->
<jdbcConnection
        driverClass="${jdbcDriverClass}"
        connectionURL="${jdbcUrl}"
        userId="${jdbcUsername}"
        password="${jdbcPassword}">
</jdbcConnection>
```


## Test

Use [Spock Framework](http://spockframework.org/spock/docs/1.3/all_in_one.html#_spock_example_project "Spock Framework") to test, just run ```./gradlew clean check```.
