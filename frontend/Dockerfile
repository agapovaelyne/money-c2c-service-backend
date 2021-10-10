FROM node:14.0-alpine3.11
RUN mkdir -p /app/front
WORKDIR /app/front
COPY package*.json ./
RUN npm install
COPY frontend/card-transfer-front .
EXPOSE 3000
RUN npm rebuild node-sass
ENTRYPOINT ["npm"]
CMD ["run", "start" ]
