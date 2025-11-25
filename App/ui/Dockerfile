# Stage 1: Build the Angular application
FROM node:20-alpine AS build

# Setup the working directory
WORKDIR /usr/src/app

# Copy package.json and package-lock.json
COPY package.json package-lock.json ./

# Install dependencies
RUN npm install

# Copy other files and folders to the working directory
COPY . .

# Build Angular application in PROD mode
RUN npm run build -- --configuration production

# Stage 2: Serve the application using Nginx
FROM nginx:1.27-alpine

# Copy the browser build output directly into the Nginx web root
COPY --from=build /usr/src/app/dist/ui/browser /usr/share/nginx/html

# Copy custom nginx configuration
COPY nginx.conf /etc/nginx/conf.d/default.conf

# Expose port 80
EXPOSE 80

# Start Nginx server
CMD ["nginx", "-g", "daemon off;"]
