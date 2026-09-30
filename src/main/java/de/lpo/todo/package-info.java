/// ### Application structure
///
/// #### Requirements
///
/// - Main entry point
/// - Application lifecycle of a server
/// - Small but production ready
///
/// #### Options
///
/// | Option | Evaluation |
/// |---|---|
/// | `Pure Java` | Very cumbersome, not production ready, much can be overlooked |
/// | `Servlet Container` | Well defined platforms, but tied to Jakarta EE framework |
/// | `Webserver` | Many small libraries available (`Javalin`, `Avaje Jex`, `Jetty`) |
///
/// #### Decision: `Javalin`
///
/// Using a simple webserver framework handles the complexity that is unreasonable
/// to tackle yourself.
///
/// @see TodoAppPlus
@NullMarked
package de.lpo.todo;

import org.jspecify.annotations.NullMarked;
