/// ### Configuration
///
/// #### Requirements
///
/// - Support *.properties, *.yml files for configuration
/// - Support overrides via environment variables
/// - Support nested properties
///
/// #### Options
///
/// | Option | Evaluation |
/// |---|---|
/// | `Pure Java` | Initially simple, but grows with requirements |
/// | `Avaje Config` | Simple and small library that can do very much |
/// | `SmallRye Config` | Default for Quarkus, very feature-rich |
///
/// #### Decision: `Avaje Config`
///
/// Since it is not much I need (but slightly too much to implement in pure Java) `Avaje Config` is
/// perfect!
@NullMarked
package de.lpo.todo.config;

import org.jspecify.annotations.NullMarked;
