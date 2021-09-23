import { Deployment } from "kubernetes-models/apps/v1/Deployment"
import { Service } from "kubernetes-models/v1/Service"
import env from "@kosko/env"

const params = await env.component("frontend")

const metadata = { name: "frontend" };
const labels = { app: "frontend" };

const deployment_frontend = new Deployment({
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
            image: "ghcr.io/numericite/cta/frontend:latest",
            name: "frontend",
            ports: [{ containerPort: 3000 }],
          }
        ],
        imagePullSecrets: [{ name: 'dockerconfigjson' }]
      }
    }
  }
});

const service_frontend = new Service({
  metadata,
  spec: {
    selector: labels,
    type: "LoadBalancer",
    ports: [{ port: 3000, targetPort: 3000, nodePort: 30000 }]
  }
});

export default [deployment_frontend, service_frontend];
