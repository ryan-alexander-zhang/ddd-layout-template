#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
/**
 * <p>Package purpose:</p>
 * <p>Query objects representing read requests. Provide a clear contract
 * for retrieving projections or views.</p>
 *
 * <p><strong>Used by modules:</strong></p>
 * <ul>
 *   <li>adapter: to translate external query parameters</li>
 *   <li>app: to pass query criteria to handlers</li>
 * </ul>
 *
 * <p><strong>Constraints:</strong></p>
 * <ul>
 *   <li>Queries are immutable and validate input early.</li>
 *   <li>No framework annotations or persistence types.</li>
 * </ul>
 */
package ${package}.app.demo.query.query;
