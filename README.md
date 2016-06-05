# PackageManager

This is a toy package manganger built as a standard dropwizard application utilizing gradle for dependency management.

## Requirements

You need Gradle 1.1 or higher, otherwise you'll run into a [dependency resolution bug](http://issues.gradle.org/browse/GRADLE-2285).

## Run

Use the [Gradle Application Plugin](http://www.gradle.org/docs/current/userguide/application_plugin.html).

#### Create Distributable Archive

To create a distributable ZIP archive including all dependencies for your application just run `./gradlew distZip`. The
resulting archive will be saved as `./build/distributions/PackageManager.zip`.

You can now unzip PackageManager.zip and go inside the PackageManager folder

#### Configuring Application

You can find the configuration file in `config/package-manager.yml`

1. `localRepo` This is the path to the folder that contains the list of files with all the dependencies
2. `gitRepo` This is the URL of the remote git repository that contains the list of files with all the dependencies
3. `http` is standard dropwizard application configuration block for application connectors
4. `logging` is standard dropwizard application configuration block to setup the logging levels and location.

Refer [dropwizard configuration](http://www.dropwizard.io/0.6.2/manual/core.html#configuration)

#### Running application

1. Run `./bin/PackageManager update config/package-manager.yml` to install/update the package list
2. Run `./bin/PackageManager install config/package-manager.yml <package-name>`. This will print out a map of the required dependencies and where you can find them in the order in which they should be installed

## Create a Jar

[Gradle OneJar Plugin](https://github.com/rholder/gradle-one-jar) will create
a JAR file of the project including all dependencies

To create a JAR with all dependencies just run `./gradlew oneJar`. The resulting JAR will be saved as `./build/libs/PackageManager-standalone.jar`.
