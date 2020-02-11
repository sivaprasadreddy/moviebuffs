# Use below nginx version
FROM nginx:1.15.2-alpine

# Copy the build folder of the react app
COPY ../kart-ui-react/build /var/www

# Copy the ngnix configrations
COPY ../kart-ui-react/deployments/nginx.conf /etc/nginx/nginx.conf

# Expose it on port 80
EXPOSE 80

ENTRYPOINT ["nginx","-g","daemon off;"]
