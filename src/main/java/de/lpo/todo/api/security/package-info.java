/// ### Security
///
/// #### Requirements
///
/// - Support OAuth 2.0 flows
/// - Support identity providers like Keycloak
/// - Handle permissions and access restrictions
///
/// #### Options
///
/// | Option | Evaluation |
/// |---|---|
/// | `Pure Java` | Don't write this yourself! |
/// | `Apache Shiro` | Covers authentication, roles, sessions, etc. but lacks OIDC support |
/// | `pac4j` | Covers a lot of clients, authenticators, authorizers, and matchers |
///
/// #### Decision: `pac4j`
///
/// There are not many options, so `pac4j` it is!
@NullMarked
package de.lpo.todo.api.security;

import org.jspecify.annotations.NullMarked;
