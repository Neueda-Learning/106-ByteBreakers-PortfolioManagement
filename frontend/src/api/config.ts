/**
 * API Configuration
 *
 * The API base URL is sourced from environment variables:
 * - VITE_API_BASE_URL (for production/Docker builds)
 * - Falls back to localhost:8081 for local development
 */

export const API_BASE_URL =
  import.meta.env.VITE_API_BASE_URL || "http://localhost:8081";
