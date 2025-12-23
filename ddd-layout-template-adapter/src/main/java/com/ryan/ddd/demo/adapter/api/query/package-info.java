/**
 * Query API package for DDD + CQRS.
 * <p>
 * Purpose: - Hosts HTTP controllers that handle Query-side operations (read model). - Controllers
 * translate REST query parameters into application/domain queries and return view DTOs assembled
 * from the read model.
 * </p>
 * Subpackages: - response: DTOs representing REST responses for Query endpoints (read views).
 * <p>
 * Notes: - This package contains only Query-side concerns; Command-side code lives elsewhere. -
 * Keep controllers thin and free of business logic; delegate to application query services. -
 * Validate input (filters, pagination, sorting) and map exceptions to HTTP status codes.
 * </p>
 */
package com.ryan.ddd.demo.adapter.api.query;

