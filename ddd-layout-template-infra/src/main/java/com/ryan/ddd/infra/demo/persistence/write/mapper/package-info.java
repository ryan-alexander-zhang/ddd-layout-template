/**
 * <p>Package purpose:</p>
 * <p>Mappers for write-side persistence, translating between domain
 * aggregates and database entities.</p>
 *
 * <p><strong>Used by modules:</strong></p>
 * <ul>
 *   <li>app: consumes repository abstractions (interfaces) while infra provides concrete mapper/repository implementations</li>
 *   <li>start: wires infrastructure beans and configuration at application bootstrap</li>
 * </ul>
 *
 * <p><strong>Constraints:</strong></p>
 * <ul>
 *   <li>Mapper types are infra-only.</li>
 *   <li>Do not leak persistence details outside infra.</li>
 * </ul>
 */
package com.ryan.ddd.infra.demo.persistence.write.mapper;
