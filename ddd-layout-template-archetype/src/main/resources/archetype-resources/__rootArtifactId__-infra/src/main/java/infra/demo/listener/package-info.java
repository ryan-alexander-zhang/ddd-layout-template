#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
/**
 * <p>Application event listeners.</p>
 *
 * <p><strong>Used by modules (DDD layering):</strong></p>
 * <ul>
 *   <li>app/domain publish domain events; application listeners react to them</li>
 *   <li>infra provides event publishing/subscription mechanisms (e.g., outbox)</li>
 *   <li>start registers listener beans during bootstrap</li>
 * </ul>
 *
 * <p><strong>Constraints:</strong></p>
 * <ul>
 *   <li>Listeners should be lightweight; delegate business logic to the domain.</li>
 *   <li>Handle idempotency and error handling explicitly.</li>
 * </ul>
 */
package ${package}.infra.demo.listener;

