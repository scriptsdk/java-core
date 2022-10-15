## Summary

ScriptSDK.Core offers a rudimentary implementation of external api communication between a java and stealth client.

## Features

- Implementation of SocketClient for socket communication
- Implementation of PacketClient for external api communication

## Installation

### 🛠 Use as Dependency

Add Settings.xml for Github authentication to repository

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

Add maven package registry to pom.xml.

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

Add dependency to pom.xml file.

```xml
<dependency>
  <groupId>de.scriptsdk</groupId>
  <artifactId>core</artifactId>
  <version>1.0.0</version>
</dependency>
```
