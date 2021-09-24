import { Deployment } from "kubernetes-models/apps/v1/Deployment"
import { Service } from "kubernetes-models/v1/Service"
import env from "@kosko/env"

const params = await env.component("dashboard")

const metadata = { name: "dashboard" };
const labels = { app: "dashboard" };

const deployment_dashboard = new Deployment({
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
            image: "ghcr.io/numericite/cta/dashboard:latest",
            name: "dashboard",
            ports: [{ containerPort: 3001 }],
          }
        ],
        imagePullSecrets: [{ name: 'dockerconfigjson' }]
      }
    }
  }
});

const service_dashboard = new Service({
  metadata,
  spec: {
    selector: labels,
    ports: [{ port: 3001, targetPort: 3001 }]
  }
});

export default [deployment_dashboard, service_dashboard];
