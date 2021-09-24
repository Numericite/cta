import { Ingress } from "kubernetes-models/networking.k8s.io/v1beta1/Ingress"

const ingress = new Ingress({
  metadata: {
    name: 'cta-ingress',
    annotations: {
      'kubernetes.io/ingress.class': 'alb',
      'alb.ingress.kubernetes.io/scheme': 'internet-facing',
      'alb.ingress.kubernetes.io/target-type': 'ip'
    }
  },
  spec: {
    rules: {
      host: 'cta-recette.numericite.eu',
      http: {
        paths: [
          { path: '/', backend: { serviceName: 'frontend', servicePort: 3000 } },
          { path: '/ctadashboard', backend: { serviceName: 'dashboard', servicePort: 3001 } },
          { path: '/api', backend: { serviceName: 'dashboard', servicePort: 7005 } },
        ]
      }
    }
  }
})

export default [ingress]