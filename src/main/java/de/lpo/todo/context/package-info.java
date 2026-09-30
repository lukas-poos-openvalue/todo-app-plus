/// ### Dependency management
///
/// #### Requirements
///
/// - Manage beans and their dependencies
/// - Support lifecycle methods (start up, tear down)
/// - Compile-time
///
/// #### Options
///
/// | Option | Evaluation |
/// |---|---|
/// | `Pure Java` | Centralize instance creation / use constructor injection |
/// | `Dagger 2` | Fast and well known in Android, but very complicated for the features offered |
/// | `Avaje Inject` | Simple standard annotation processor |
///
/// #### Decision: `Pure Java`
///
/// Since I only need singletons with initialization and closing, a `Pure Java` solution seems
/// perfectly fine!
///
/// @see ApplicationContext
@NullMarked
package de.lpo.todo.context;

import org.jspecify.annotations.NullMarked;
