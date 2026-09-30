---
author: Lukas Poos, OpenValue Düsseldorf
date: dd.MM.YYYY
paging: "%d / %d"
---

# Can the modern Java dev survive without Spring-Boot & Co.?

### lpo@dell-xps-17 $ whoami
- `Lukas Poos`
- IT consultant @ `OpenValue Düsseldorf` 
- Passionate about `Java`, `Rust`, `Web`, `NixOS`, `Self-hosting`, `Vim & Helix`, ...

### lpo@dell-xps-17 $ tldr presentation.md
- Slides & Sources on GitHub: https://github.com/lukas-poos-openvalue/todo-app-plus
- `QR code` at the end

---

## Overview

- Introduction
- Exploration time
    - Tooling
    - Application structure
    - Dependency management
    - Configuration
    - Database
    - API
    - API security
    - Web-UI
    - Testing
- Summary

---

## Introduction

### Relevant terms

- `Application framework` = framework to build applications (duh)
    - Batteries included
    - feature-rich
    - production ready
    - tooling
    - integrations
    - documentation
    - ...
- `Language` = Programming language and the _entire_ ecosystem 
  - Standard library
  - tooling
  - packages
  - libraries
  - frameworks

---

## Introduction

### Some questions for you

#### Do you use `Spring Boot` regularly?

---

## Introduction

### Some questions for you

#### Do you use `Quarkus` regularly?

---

## Introduction

### Some questions for you

#### Do you use other Java application frameworks? (`Micronaut`, `Helidon`, ...)

---

## Introduction

### Some questions for you

#### Have you ever written code in `Rust`?

---

## Introduction

### Some questions for you

#### Have you ever written code in `Go`?

---

## Introduction

### Observations about application frameworks

- `Rust` --> no big application frameworks
- `Go` --> no big application frameworks
- `Java` --> It's the de facto standard
    - Back in the days --> No other way
    - Now its way to common to avoid it
- Newer languages --> Better tooling and libraries
- *BUT*: Java has grown amazingly

---

## Introduction

### Sooo... do you need the application frameworks in Java anymore?

- Can you substitute application framework features...
  - ...with the Java standard library and tooling?
  - ...with other libraries and tooling?
- How far can you push it?
- Can you avoid application frameworks entirely?

---

## Exploration time

### Introducing: Demo application

- Example app we will look at: `Todo App Plus`
- We'll walk though feature-by-feature (Tooling, Application structure, ...)
  - `Disclaimer`: Not enough time for full evaluations
- Time to show the app and explore the code!

---

## Exploration time

~~~bat -p tooling.md
### Tooling

See  [tooling.md](./tooling.md)
~~~

---

## Exploration time

~~~sed -n s#^[[:space:]]*///[[:space:]]\{0,1\}##p src/main/java/de/lpo/todo/package-info.java
### Application structure

See  [root package](./src/main/java/de/lpo/todo/package-info.java)
~~~

---

## Exploration time

~~~sed -n s#^[[:space:]]*///[[:space:]]\{0,1\}##p src/main/java/de/lpo/todo/context/package-info.java
### Dependency management

See  [context package](./src/main/java/de/lpo/todo/context/package-info.java)
~~~

---

## Exploration time

~~~sed -n s#^[[:space:]]*///[[:space:]]\{0,1\}##p src/main/java/de/lpo/todo/config/package-info.java
### Configuration

See  [config package](./src/main/java/de/lpo/todo/config/package-info.java)
~~~

---

## Exploration time

~~~sed -n s#^[[:space:]]*///[[:space:]]\{0,1\}##p src/main/java/de/lpo/todo/database/package-info.java
## 5. Database

See  [database package](./src/main/java/de/lpo/todo/database/package-info.java)
See  [repository package](./src/main/java/de/lpo/todo/repository/package-info.java)
~~~

---

## Exploration time

~~~sed -n s#^[[:space:]]*///[[:space:]]\{0,1\}##p src/main/java/de/lpo/todo/api/package-info.java
### API

See  [API package](./src/main/java/de/lpo/todo/api/package-info.java)
~~~

---

## Exploration time

~~~sed -n s#^[[:space:]]*///[[:space:]]\{0,1\}##p src/main/java/de/lpo/todo/api/security/package-info.java
### API security 

See  [API security package](./src/main/java/de/lpo/todo/api/security/package-info.java)
~~~

---

## Exploration time

~~~sed -n s#^[[:space:]]*///[[:space:]]\{0,1\}##p src/main/java/de/lpo/todo/api/web/package-info.java
### Web UI 

See  [API web package](./src/main/java/de/lpo/todo/web/api/package-info.java)
~~~

---

## Exploration time

~~~sed -n s#^[[:space:]]*///[[:space:]]\{0,1\}##p src/test/java/de/lpo/todo/package-info.java
### Testing 

See  [Tests](./src/test/java/de/lpo/todo/package-info.java)
~~~

---

## Summary

### Some answers for you

_Can_ you survive without Spring-Boot & Co.?

*YES*

---

## Summary

### Some answers for you

Do you have to consider more things?

*More options, so yes*

---

## Summary

### Some answers for you

Is the developer experience as smooth?

*No*

---

## Summary

### Some answers for you

Is the resulting artifact smaller?

*Most likely not*

---

## Summary

### Some answers for you

Is the resulting artifact better?

*Less complex, more explicit, not necessarily faster*

---

## Summary

### Some answers for you

_Should_ you survive without Spring-Boot & Co.?

*No*

---

## Summary

### Lessons learned

- There is no silver bullet solution
  - Application frameworks aren't
  - Alternatives aren't either
- This exploration was about...
  - ...finding other ways to solve problems
  - ...learning about other tools
  - ...taking the time to explore
  - ...being curious, not judgemental

---

# The End

## Thanks for listening

Lukas Poos, IT-consultant, OpenValue Düsseldorf GmbH


## Slides & Sources
```
█████████████████████████████████████
██ ▄▄▄▄▄ █ ▄▄ █▄▄ █▀ ██▀ █▀█ ▄▄▄▄▄ ██
██ █   █ ██▄█▀▀▄█▀▀ █▄█ ▀▀██ █   █ ██
██ █▄▄▄█ █ ▀▀▄ ▀▀▄ █ █▀ ████ █▄▄▄█ ██
██▄▄▄▄▄▄▄█ ▀▄█▄▀▄█▄█ ▀ █ █▄█▄▄▄▄▄▄▄██
██ ▄ ██▄▄▀ ▄█▄ ▄█▄ ██▀ ▀▄ ▀▄  ▄██  ██
██▄ ▄██▄▄▀ ▄ ▀ ▄ ▀ ▄ ▄ ▄▀▄▀   ▀███▄██
██ ▄█▄▄▄▄▀▀  ██  ███▀ ▀▀█▀▄█ ▄██▀  ██
██▄ ██▀▄▄▀▀▀▄▄█▀█▄█▄▄ ▀█▀ █▀▀▄█▄██▄██
██▄█▀▀█ ▄█▄▀▀▄ ▄█▄ █▀▀▄█▀ ▀█  █ ▀▀ ██
██  ▀███▄ ▄▀▄▀ ▄ ▀ ▄▄▄▀▄▄▄█▀█▀▄███▄██
███ ▄   ▄█▀▀▄██  █▄▀▄  ██  ▄ ▀█ ▀▀ ██
██▄▄▄ ▄▀▄▀▄▀▀▄█▀█ ▄▄▀▄▀▄▀ █▀▀▄ ▀██▄██
██▄▄▄▄██▄█   ▄ ▄██▄█▀ ▀▀██ ▄▄▄  █▄▀██
██ ▄▄▄▄▄ █▀▀▄▀ ▄  █▄▄▄ ██▀ █▄█ ▀██ ██
██ █   █ ██ ▀██   ▀▀█▀ █▀ ▄ ▄▄ ▄▀▀ ██
██ █▄▄▄█ █  ▀▄█▀█▄▀▄█▄███▀  ▀█▄▀▄▄▄██
██▄▄▄▄▄▄▄█▄█▄▄▄▄█▄███▄▄███▄▄▄█▄███▄██
█████████████████████████████████████
```
[Slides & Sources](https://github.com/lukas-poos-openvalue/todo-app-plus)

---

# The End

## Q&A

Any questions?

## Feedback
```
█████████████████████████████
██ ▄▄▄▄▄ █▀▄▄▀▄   ▄█ ▄▄▄▄▄ ██
██ █   █ █▄  █▄█▀▄▄█ █   █ ██
██ █▄▄▄█ █ ▀██▄▄   █ █▄▄▄█ ██
██▄▄▄▄▄▄▄█ ▀▄▀ █ █ █▄▄▄▄▄▄▄██
██  ▀▄█▀▄▄██ ▄ ▄▄█ ▀ ▄▄█▄▄▀██
██ █▀ █▀▄▄▀▄▄█▄▀█▀▄▄ ▀▀██▄▄██
████  ▀▄▄▀▄█▀▄  ██▄   █ ▀▄ ██
██▄▀▀▄▄█▄ ▄▀▀▄ ▀ █▄█▄▄ █▄▀▄██
██▄▄█▄▄▄▄▄ ▄ ▀██ ▀ ▄▄▄  ▄█▀██
██ ▄▄▄▄▄ █▄▀█▀▀▄▀▄ █▄█  ▀▄▄██
██ █   █ █▀█▀▄██ ▀▄▄▄   ▀████
██ █▄▄▄█ █▀█▄▀  ▄ ▀▀█▄▄▀▄█▄██
██▄▄▄▄▄▄▄█▄▄▄█▄▄█▄▄▄▄▄███▄▄██
█████████████████████████████
```
[Feedback](https://sfeedback.com/Yf56hd)
