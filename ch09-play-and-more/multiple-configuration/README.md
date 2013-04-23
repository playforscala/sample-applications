# 9.x Using multiple configuration files

Sample application from chapter 9 of [Play for Scala](http://bit.ly/playscala).

This sample shows how to separate configuration files for development, test, acceptance and production (DTAP) environments. The key techniques are to use:

* a default `conf/application.conf` application configuration that is safe for testing
* an optional additional configuration for developer-specific overrides
* a separate configuration file for production that includes the default configuration.
