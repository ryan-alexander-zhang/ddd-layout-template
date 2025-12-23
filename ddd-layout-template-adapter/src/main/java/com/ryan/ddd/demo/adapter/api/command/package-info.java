/**
 * Command API package for DDD + CQRS.
 * <p>
 * Purpose: - Hosts HTTP controllers that handle Command-side operations (write model). -
 * Controllers translate REST payloads into application/domain commands and return command responses
 * by assembler.
 * </p>
 * Subpackages: - request: DTOs representing REST request payloads for Command endpoints. -
 * response: DTOs representing REST responses for Command endpoints (acknowledgements, identifiers,
 * and metadata; not query/read views).
 * <p>
 * Notes: - This package contains only Command-side concerns; Query-side code lives elsewhere. -
 * Keep controllers thin and free of business logic; delegate to application services. - Perform
 * input validation at the boundary and map exceptions to HTTP status codes.
 * </p>
 */
package com.ryan.ddd.demo.adapter.api.command;