FROM node:18.20.0 AS builder
    ARG ENV
    WORKDIR /app    
    COPY package*.json ./
    RUN npm install
    COPY ./ ./
    RUN npm run build-${ENV}-shift

FROM nginx:latest AS deployer
    ARG ENV
    #COPY --from=builder /app/dist/${ENV} /usr/share/nginx/html/Cube3D
    #RUN chmod -R 755 /usr/share/nginx/html/Cube3D
	COPY --from=builder /app/dist/${ENV} /usr/share/nginx/html
	RUN chmod -R 755 /usr/share/nginx/html
    RUN rm /etc/nginx/conf.d/default.conf
    COPY nginx.conf /etc/nginx/nginx.conf
    #ADD certs/angular-server.crt /etc/nginx/certs/angular-server.crt
	#ADD certs/angular-server.key /etc/nginx/certs/angular-server.key
	EXPOSE 4200
	EXPOSE 443
	EXPOSE 80
	CMD ["nginx", "-g", "daemon off;"]
