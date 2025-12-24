/**
 * <p>Package purpose:</p>
 * <p>View models for query responses, optimized for presentation.</p>
 *
 * <p><strong>Used by modules:</strong></p>
 * <ul>
 *   <li>adapter: to render API responses</li>
 *   <li>app: to return data from query handlers</li>
 * </ul>
 *
 * <p><strong>Constraints:</strong></p>
 * <ul>
 *   <li>View models should be read-only and serializable.</li>
 *   <li>Avoid leaking domain internals; map from read models.</li>
 * </ul>
 */
package com.ryan.ddd.app.demo.query.view;
