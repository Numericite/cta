
import { Secret } from "kubernetes-models/v1/Secret"; 

const dockerConfigJsonSecret = new Secret({
  metadata: { name: 'dockerconfigjson' },
  data: {
    ".dockerconfigjson": 'ewoJImF1dGhzIjogewoJCSJkb2NrZXIucGtnLmdpdGh1Yi5jb20iOiB7CgkJCSJhdXRoIjogImFHOXlaV3RyT2pJMVltTTNaVGswT0RZek4yWXhabVEzTVRJMlpUYzFaRE0xWldFM1l6STRNREEwWWpKbU4yRT0iCgkJfSwKCQkiZ2hjci5pbyI6IHsKCQkJImF1dGgiOiAiYUc5eVpXdHJPakkxWW1NM1pUazBPRFl6TjJZeFptUTNNVEkyWlRjMVpETTFaV0UzWXpJNE1EQTBZakptTjJFPSIKCQl9Cgl9LAoJIkh0dHBIZWFkZXJzIjogewoJCSJVc2VyLUFnZW50IjogIkRvY2tlci1DbGllbnQvMTkuMDMuMTMgKGxpbnV4KSIKCX0sCgkiZXhwZXJpbWVudGFsIjogImVuYWJsZWQiCn0K'
  },
  type: 'kubernetes.io/dockerconfigjson'
})

export default [dockerConfigJsonSecret]