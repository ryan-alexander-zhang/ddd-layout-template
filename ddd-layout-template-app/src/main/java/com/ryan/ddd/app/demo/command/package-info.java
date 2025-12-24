/**
 * <p>Application command layer entry package.</p>
 *
 * <p><strong>Used by modules (DDD layering):</strong></p>
 * <ul>
 *   <li>adapter calls app: triggers use-cases via command handlers</li>
 *   <li>app depends on domain abstractions; infra provides implementations of repository/interfaces</li>
 *   <li>start wires app and infra components at bootstrap</li>
 * </ul>
 *
 * <p><strong>Constraints:</strong></p>
 * <ul>
 *   <li>Coordinate domain operations; keep orchestration logic, not business rules.</li>
 *   <li>Remain framework-agnostic in DTOs and handlers; use abstractions.</li>
 * </ul>
 */
package com.ryan.ddd.app.demo.command;
