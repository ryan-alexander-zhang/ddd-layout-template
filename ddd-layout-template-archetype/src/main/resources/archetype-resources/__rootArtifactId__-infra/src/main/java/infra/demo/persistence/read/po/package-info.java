#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
/**
 * <p>Package purpose:</p>
 * <p>Persistence objects (POJOs) for read-side data models.</p>
 *
 * <p><strong>Used by modules (DDD layering):</strong></p>
 * <ul>
 *   <li>app consumes read repository interfaces; infra uses POs internally</li>
 *   <li>start wires infrastructure beans and configuration at application bootstrap</li>
 * </ul>
 *
 * <p><strong>Constraints:</strong></p>
 * <ul>
 *   <li>POs mirror read schemas and are infra-only.</li>
 *   <li>Do not embed business logic.</li>
 * </ul>
 */
package ${package}.infra.demo.persistence.read.po;
