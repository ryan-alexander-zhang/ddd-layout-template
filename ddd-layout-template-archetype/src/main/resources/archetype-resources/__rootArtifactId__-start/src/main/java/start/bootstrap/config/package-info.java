#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
/**
 * <p>Package purpose:</p>
 * <p>Bootstrap and configuration for application startup, including
 * event type registration and infrastructure wiring.</p>
 *
 * <p><strong>Used by modules (DDD layering):</strong></p>
 * <ul>
 *   <li>start wires app and infra components</li>
 *   <li>app consumes configured services</li>
 * </ul>
 *
 * <p><strong>Constraints:</strong></p>
 * <ul>
 *   <li>Keep configuration declarative; avoid business logic.</li>
 *   <li>Do not couple to specific runtime environments beyond necessary settings.</li>
 * </ul>
 */
package ${package}.start.bootstrap.config;
