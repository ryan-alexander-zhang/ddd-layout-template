/**
 * <p>Package purpose:</p>
 * <p>Persistence objects (POJOs) for write-side domain storage.</p>
 *
 * <p><strong>Used by modules (DDD layering):</strong></p>
 * <ul>
 *   <li>app consumes repository abstractions; infra uses POs internally for persistence</li>
 *   <li>start wires infrastructure beans and configuration at application bootstrap</li>
 * </ul>
 *
 * <p><strong>Constraints:</strong></p>
 * <ul>
 *   <li>POs represent storage schemas; keep them free of business logic.</li>
 *   <li>Infra-only, not exposed to domain or adapter layers.</li>
 * </ul>
 */
package com.ryan.ddd.infra.demo.persistence.write.po;
