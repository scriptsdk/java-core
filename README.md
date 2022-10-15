## Summary

Stealth Client is an alternative Ultima Online client written in Delphi and distributed on their [website](https://stealth.od.ua/ "website"). Stealth exposes a socket based external api which is subject of this project.

ScriptSDK.Core is a rudimentary implementation of external api communication between a java and stealth client. Since its a core package it only offers very basical models for developers to communicate to Stealth Client.

## Features

- Implementation of SocketClient for socket communication
- Implementation of PacketClient for external api communication

## Installation

### 🛠 Use source code

Developers can fork this repository and use it as it is for their own forks or implementations. Even thought its possible to directly use the sourcecode as base, its recommned to implement this project as maven package.

### 📦 Use as maven package

In order to use Github maven repositories, developers must be registered at github and being able to create a [personal access token](https://docs.github.com/en/authentication/keeping-your-account-and-data-secure/creating-a-personal-access-token "personal access token").

As next step the personal access token must be usable through local maven repository by adding a **Settings.xml** to local .m2 folder. This is regulary located at **C:\Users\USER\.m2\**

```xml
<settings xmlns="http://maven.apache.org/SETTINGS/1.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.0.0 https://maven.apache.org/xsd/settings-1.0.0.xsd">
<servers>
    <server>
        <id>github</id>
        <username>USERNAME</username>
        <password>AUTH_TOKEN</password>
    </server>
</servers>
</settings>
```

The last step is adding references to package repository to pom.xml file.

```xml
    <repositories>
        <repository>
            <id>github</id>
            <name>GitHub ScriptSDK Java Core</name>
            <url>https://maven.pkg.github.com/stealth-scriptsdk/java-core</url>
            <releases><enabled>true</enabled></releases>
            <snapshots><enabled>false</enabled></snapshots>
        </repository>
    </repositories>
```



```xml
<dependency>
  <groupId>de.scriptsdk</groupId>
  <artifactId>core</artifactId>
  <version>1.0.2</version>
</dependency>
```
