### Tooling

#### Requirements

- Building the application (Compilation, Packaging, Extension)
- Package management
- Developer convenience

#### Options

| Option      | Evaluation                                                                        |
|-------------|-----------------------------------------------------------------------------------|
| `Pure Java` | Still very cumbersome, no package management                                      |
| `JBang`     | Nice for small to medium projects, can resolve dependencies, little extendibility |
| `Maven`     | Very common, extensible, a lot of tooling available                               |
| `Gradle`    | Very extensible, but more complicated                                             |

#### Decision: `Maven`

`Maven` tends to stay tidy for smaller projects, as well as scale nicely with growing projects.



