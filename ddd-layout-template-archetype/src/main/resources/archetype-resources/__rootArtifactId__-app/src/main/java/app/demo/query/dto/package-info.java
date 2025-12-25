#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
/**
 * <p>Package purpose:</p>
 * <p>Query DTOs and result types for application read models.</p>
 *
 * <p><strong>Used by modules:</strong></p>
 * <ul>
 *   <li>adapter: to present data via APIs</li>
 *   <li>app: to carry data from query handlers to clients</li>
 * </ul>
 *
 * <p><strong>Constraints:</strong></p>
 * <ul>
 *   <li>DTOs are serializable and framework-agnostic.</li>
 *   <li>No business logic; keep them as simple carriers.</li>
 * </ul>
 */
package ${package}.app.demo.query.dto;
