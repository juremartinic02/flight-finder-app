# Step 1: Build React app
FROM node:18-alpine AS build-stage

WORKDIR /app

# Copy package files and install dependencies (cached)
COPY package.json package-lock.json ./
RUN npm install

# Copy app source and build
COPY . .
RUN npm run build

# Step 2: Serve with Nginx
FROM nginx:stable-alpine

# Remove default Nginx content
RUN rm -rf /usr/share/nginx/html/*

# Copy built app from build-stage
# For Vite (default 'dist' folder):
COPY --from=build-stage /app/dist /usr/share/nginx/html


EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]