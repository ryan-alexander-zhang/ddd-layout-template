#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
/**
 * <p>Command API package for DDD + CQRS.</p>
 *
 * <p><strong>Purpose:</strong></p>
 * <ul>
 *   <li>Hosts HTTP controllers that handle Command-side operations (write model).</li>
 *   <li>
 *     Controllers translate REST payloads into application commands
 *     and return command responses via assemblers.
 *   </li>
 * </ul>
 *
 * <p><strong>Used by modules (DDD layering):</strong></p>
 * <ul>
 *   <li>adapter uses app: controllers call application services/handlers</li>
 *   <li>adapter does not depend on infra/domain directly</li>
 * </ul>
 *
 * <p><strong>Subpackages:</strong></p>
 * <ul>
 *   <li>{@code request}: DTOs representing REST request payloads for Command endpoints.</li>
 *   <li>{@code response}: DTOs representing REST responses for Command endpoints (acknowledgements, identifiers, and metadata; not query or read views).</li>
 * </ul>
 *
 * <p><strong>Notes:</strong></p>
 * <ul>
 *   <li>This package contains only Command-side concerns; Query-side code lives elsewhere.</li>
 *   <li>Keep controllers thin and free of business logic; delegate to application services.</li>
 *   <li>Perform input validation at the boundary and map exceptions to HTTP status codes.</li>
 * </ul>
 */
package ${package}.demo.adapter.api.command;
