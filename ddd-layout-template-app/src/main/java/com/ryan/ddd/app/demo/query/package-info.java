/**
 * <p>Application query layer entry package.</p>
 *
 * <p><strong>Used by modules (DDD layering):</strong></p>
 * <ul>
 *   <li>adapter calls app: serves query endpoints via query handlers</li>
 *   <li>app depends on domain abstractions; infra provides read-model implementations</li>
 *   <li>start wires app and infra components at bootstrap</li>
 * </ul>
 *
 * <p><strong>Constraints:</strong></p>
 * <ul>
 *   <li>Encapsulate read logic; keep handlers side-effect free.</li>
 *   <li>Return view/DTO types suitable for presentation.</li>
 * </ul>
 */
package com.ryan.ddd.app.demo.query;
