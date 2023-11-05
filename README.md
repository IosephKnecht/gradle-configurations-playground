# Gradle Configurations Playground

A project to demonstrate the sharing of artifacts between projects using 
configurations using the example of a report aggregation task for unit tests.

## Installation
Clone this repository and import into **Android Studio**
```bash
git clone git@github.com:IosephKnecht/gradle-configurations-playground.git
```

## Project structure

```text
gradle-configurations-playground
├───app                             <-- Dummy android application module
├───feature-1-android               <-- Dummy android library module
├───feature-2-kotlin                <-- Dummy kotlin library module
├───feature-3-android               <-- Dummy android library module
├───feature-4-kotlin                <-- Dummy kotlin library module
├───feature-5-android               <-- Dummy android library module
├───feature-6-kotlin                <-- Dummy kotlin library module
├───feature-7-android               <-- Dummy android library module
├───feature-8-kotlin                <-- Dummy kotlin library module
├───feature-9-android               <-- Dummy android library module
└───plugins
    └───test-report-plugin
        ├───test-aggregator         <-- Plugin module for unit test reports consumer (aggregates all reports into one)
        ├───test-interceptor        <-- Plugin module for report producers for unit tests
        └───test-reports-attributes <-- Module for sharing common configuration attributes between plugins
```

## Gradle tasks to demonstrate report aggregation
The root project provides several tasks for running unit tests and subsequent aggregation of reports from submodules into one common HTML report.

These are the tasks:
- testExternalDebugGroup1UnitTest
- testExternalDebugGroup2UnitTest
- testExternalReleaseGroup1UnitTest
- testExternalReleaseGroup2UnitTest
- testInternalDebugGroup1UnitTest
- testInternalDebugGroup2UnitTest
- testInternalReleaseGroup1UnitTest
- testInternalReleaseGroup2UnitTest

To check report aggregation, you can call one of the tasks from the console, for example, like this:
```shell
./gradlew :testExternalDebugGroup1UnitTest
```

You can find the aggregated report at:
`path-to-gradle-configuration-playground-project/build/test-reports`
