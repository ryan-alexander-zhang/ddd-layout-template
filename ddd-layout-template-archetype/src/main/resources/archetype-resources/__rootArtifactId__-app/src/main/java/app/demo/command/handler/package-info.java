#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
/**
 * <p>Package purpose:</p>
 * <p>Application command handlers orchestrating domain operations.
 * Coordinate validation, transaction boundaries, and repository access.</p>
 *
 * <p><strong>Used by modules:</strong></p>
 * <ul>
 *   <li>adapter: to trigger use-cases from external interfaces</li>
 * </ul>
 *
 * <p><strong>Constraints:</strong></p>
 * <ul>
 *   <li>Handlers should be thin and delegate business rules to the domain.</li>
 *   <li>Respect transactional boundaries and idempotency where applicable.</li>
 *   <li>No UI or persistence details embedded; rely on abstractions.</li>
 * </ul>
 */
package ${package}.app.demo.command.handler;
