/**
 * <p>Query API package for DDD + CQRS.</p>
 *
 * <p><strong>Purpose:</strong></p>
 * <ul>
 *   <li>Hosts HTTP controllers that handle Query-side operations (read model).</li>
 *   <li>Controllers translate REST query parameters into application queries and return view DTOs assembled from the read model.</li>
 * </ul>
 *
 * <p><strong>Used by modules (DDD layering):</strong></p>
 * <ul>
 *   <li>adapter uses app: controllers call application query handlers</li>
 *   <li>adapter does not depend on infra/domain directly</li>
 * </ul>
 *
 * <p><strong>Subpackages:</strong></p>
 * <ul>
 *   <li>response: DTOs representing REST responses for Query endpoints (read views).</li>
 * </ul>
 *
 * <p><strong>Notes:</strong></p>
 * <ul>
 *   <li>This package contains only Query-side concerns; Command-side code lives elsewhere.</li>
 *   <li>Keep controllers thin and free of business logic; delegate to application query services.</li>
 *   <li>Validate input (filters, pagination, sorting) and map exceptions to HTTP status codes.</li>
 * </ul>
 */
package com.ryan.ddd.adapter.demo.adapter.api.query;
