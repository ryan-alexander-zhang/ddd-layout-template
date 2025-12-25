#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
/**
 * <p>Package purpose:</p>
 * <p>Mappers for read-side persistence, translating between database
 * rows/documents and POJOs used by the application.</p>
 *
 * <p><strong>Used by modules (DDD layering):</strong></p>
 * <ul>
 *   <li>app consumes repository interfaces; infra provides mapper/repository implementations</li>
 *   <li>start wires infrastructure beans and configuration at application bootstrap</li>
 * </ul>
 *
 * <p><strong>Constraints:</strong></p>
 * <ul>
 *   <li>Mapper interfaces/classes are infrastructure-specific.</li>
 *   <li>Do not leak mapper types to the domain or adapter layers.</li>
 * </ul>
 */
package ${package}.infra.demo.persistence.read.mapper;
