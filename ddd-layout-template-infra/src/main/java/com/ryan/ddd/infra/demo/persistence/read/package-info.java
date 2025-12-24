/**
 * <p>Package purpose:</p>
 * <p>Read-side persistence implementations for queries.
 * Provide data access to projections and read models.</p>
 *
 * <p><strong>Used by modules (DDD layering):</strong></p>
 * <ul>
 *   <li>app consumes read repository interfaces; infra provides concrete implementations</li>
 *   <li>start wires infrastructure beans and configuration at application bootstrap</li>
 * </ul>
 *
 * <p><strong>Constraints:</strong></p>
 * <ul>
 *   <li>Read operations must be side-effect free.</li>
 *   <li>Use technology-specific mappers/entities only within infra.</li>
 *   <li>Respect boundaries: do not expose persistence types outside infra.</li>
 * </ul>
 */
package com.ryan.ddd.infra.demo.persistence.read;
