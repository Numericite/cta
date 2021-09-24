import { Deployment } from "kubernetes-models/apps/v1/Deployment"
import { Service } from "kubernetes-models/v1/Service"
import env from "@kosko/env"

const params = await env.component("backend")

const metadata = { name: "backend" };
const labels = { app: "backend" };

const deployment_backend = new Deployment({
  metadata,
  spec: {
    replicas: 1,
    selector: {
      matchLabels: labels
    },
    template: {
      metadata: { labels },
      spec: {
        containers: [
          {
            image: "ghcr.io/numericite/cta/backend:latest",
            name: "backend",
            ports: [{ containerPort: 7005 }]
          }
        ],
        imagePullSecrets: [{ name: 'dockerconfigjson' }]
      }
    }
  }
});

const service_backend = new Service({
  metadata,
  spec: {
    selector: labels,
    ports: [{ port: 7005, targetPort: 7005 }]
  }
});

export default [deployment_backend, service_backend];
