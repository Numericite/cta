FROM node:12-alpine

ARG buildenv

ENV ENVFILE="environments/env.${buildenv}"

WORKDIR /

COPY *.json /

RUN npm install

COPY . ./
COPY ${ENVFILE} .env

RUN npm run build

ENV NUXT_HOST 0.0.0.0

ENTRYPOINT ["npm", "run", "start"]