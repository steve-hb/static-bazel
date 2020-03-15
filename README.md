# Static Bazel

Static Bazel is a multi-language Bazel migration tool based on static analysis of the already available build files (e.g. pom.xml).

## Supported languages

* Java using Maven

## Features

* Migrating from one build tool to Bazel without manually writing WORKSPACE or BUILD files

## There are already migration tools - why developing a new one?

In my company we tried to migrate to Bazel in the past, but nearly every migration tool was either outdated, missing functionality or even not working at all.
Some of them listed all source files in a very huge list for every library, which isn't really maintainable in my opinion.
And the outdated ones used old Bazel versions or rules - transitive dependency fetching for example:
With the new java rules we don't have to explicit define all transitive dependencies for every library in our BUILD files.

The project was a big mono-repository, so there was/is no way around using a migration tool for us.

And that's basically the reason <i>why</i> I started this project.
