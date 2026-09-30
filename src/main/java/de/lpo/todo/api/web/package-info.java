/// ### Web-UI
///
/// #### Requirements
///
/// - No dedicated server to host frontend
/// - Dynamic UI with all the web features
/// - Typesafe
///
/// #### Options
///
/// | Option | Evaluation |
/// |---|---|
/// | `Angular` | Entirely new ecosystem |
/// | `Thymeleaf` | Templates are not typesafe |
/// | `Vaadin` | Convenient and suitable, but I use many web features |
/// | `JTE` | Typesafe model generation for templates |
/// | `HTMX` | Dynamic UIs without JavaScript |
///
/// #### Decision: `JTE` + `HTMX`
///
/// It is typesafe + all in one server + no JavaScript!
@NullMarked
package de.lpo.todo.api.web;

import org.jspecify.annotations.NullMarked;
