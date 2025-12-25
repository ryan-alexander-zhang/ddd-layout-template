#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
/**
 * <p>Package purpose:</p>
 * <p>Write-side persistence implementations for the Demo aggregate.
 * Provide repositories and mappers to store domain state.</p>
 *
 * <p><strong>Used by modules:</strong></p>
 * <ul>
 *   <li>app: consumes repository abstractions (interfaces) while infra provides their concrete implementations</li>
 *   <li>start: wires infrastructure beans and configuration at application bootstrap</li>
 * </ul>
 *
 * <p><strong>Constraints:</strong></p>
 * <ul>
 *   <li>Preserve domain invariants; map carefully between domain and persistence.</li>
 *   <li>Technology-specific types remain internal to infra.</li>
 * </ul>
 */
package ${package}.infra.demo.persistence.write;
