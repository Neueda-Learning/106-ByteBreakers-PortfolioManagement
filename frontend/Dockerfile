# ============================================
# Stage 1: Builder - Node.js Build Stage
# ============================================
# This stage uses Node.js to install dependencies and build the Vite production bundle
# The result is an optimized static bundle ready for serving
FROM node:lts-alpine AS builder

# Add labels for metadata
LABEL maintainer="Portfolio Manager Team"
LABEL stage="builder"

# Set working directory for build operations
WORKDIR /build

# Copy package files
# These are copied first to leverage Docker layer caching
# If dependencies haven't changed, npm install won't need to re-run
COPY package.json package-lock.json ./

# Install dependencies
# --ci (clean install) ensures reproducible builds by using exact versions from package-lock.json
# --legacy-peer-deps is used if there are peer dependency conflicts (common with npm)
RUN npm ci

# Copy source code
# This is done after npm install so that source changes don't invalidate the node_modules layer
COPY . .

# Build the Vite production bundle
# - tsc: Compile TypeScript to JavaScript
# - vite build: Build optimized production bundle
# The .env.production file is automatically loaded by Vite, injecting VITE_API_BASE_URL
RUN npm run build

# ============================================
# Stage 2: Runtime - Nginx Web Server
# ============================================
# This stage contains only Nginx and the built static assets
# Nginx will serve the React SPA and handle routing for React Router
# Resulting image is very small (~50-60MB including Nginx Alpine)
FROM nginx:alpine

# Add labels
LABEL maintainer="Portfolio Manager Team"
LABEL description="Portfolio Manager React/Vite Frontend"
LABEL version="1.0"

# Copy custom Nginx configuration
# This configuration handles React Router SPA routing with try_files directive
# Enables gzip compression for all text-based assets
COPY nginx.conf /etc/nginx/nginx.conf

# Copy built application from builder stage to Nginx document root
# The dist/ folder contains all static files (HTML, CSS, JS, assets)
# /usr/share/nginx/html/ is the default Nginx serve directory
COPY --from=builder /build/dist /usr/share/nginx/html

# Expose port 80 (container port - mapped to 8082 on host in docker-compose)
# This tells Docker that Nginx listens on port 80
EXPOSE 80

# Health check for Docker and Docker Compose
# Checks every 30 seconds if Nginx is responding
# Container is considered unhealthy after 3 consecutive failures
HEALTHCHECK --interval=30s --timeout=5s --start-period=10s --retries=3 \
    CMD wget -q -O- http://localhost/index.html > /dev/null 2>&1 || exit 1

# Nginx runs in the foreground by default in Alpine
# CMD is inherited from nginx:alpine and starts Nginx
