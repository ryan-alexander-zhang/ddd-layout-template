#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
/**
 * <p>Package purpose:</p>
 * <p>Command DTOs for application use-cases. Represent input/output data
 * for invoking domain commands in a technology-agnostic way.</p>
 *
 * <p><strong>Used by modules:</strong></p>
 * <ul>
 *   <li>adapter: to map HTTP/transport payloads to command DTOs</li>
 *   <li>app: to pass data between application layer and domain commands</li>
 * </ul>
 *
 * <p><strong>Constraints:</strong></p>
 * <ul>
 *   <li>DTOs are simple data carriers without business logic.</li>
 *   <li>No dependencies on web or persistence frameworks.</li>
 *   <li>Fields should be validated by handlers before use.</li>
 * </ul>
 */
package ${package}.app.demo.command.dto;
