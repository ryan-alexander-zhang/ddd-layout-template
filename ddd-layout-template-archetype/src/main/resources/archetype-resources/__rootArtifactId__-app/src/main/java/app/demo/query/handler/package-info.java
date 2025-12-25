#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
/**
 * <p>Package purpose:</p>
 * <p>Application query handlers for reading models and projections.
 * Encapsulate query logic and coordinate access to read repositories.</p>
 *
 * <p><strong>Used by modules:</strong></p>
 * <ul>
 *   <li>adapter: to serve query endpoints</li>
 * </ul>
 *
 * <p><strong>Constraints:</strong></p>
 * <ul>
 *   <li>Handlers must be side-effect free and read-only.</li>
 *   <li>No coupling to transport or persistence implementations.</li>
 *   <li>Return view/DTO types suitable for presentation.</li>
 * </ul>
 */
package ${package}.app.demo.query.handler;

