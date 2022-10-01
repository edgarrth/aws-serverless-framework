![](img/private-endpoint.png)

Para configurar el lambda y el api gateway dentro de la vcp
hay que configurarlo en el `serverless.yaml`

# Configurar Lambda

Se puede configurar a nivel general colocando la configuracion de la vpc a nivel de provider:

```yaml

provider:
  name: aws
  region: us-east-1
  runtime: nodejs12.x
  stage: ${opt:stage, 'dev'}

  #VPC LAMBDA =====================>
  vpc:
    securityGroupIds:
      - sg-0e5e1f6692ad6558a
    subnetIds:
      - subnet-05b1b53c39b4edb41
      - subnet-01c2a3f6d8297abda
  #VPC LAMBDA =====================<
```

# Configurar el API Gateway

Canfigurar el gateway dentro de la VCP

```yaml
#...
provider:
  name: aws
  region: us-east-1
  runtime: nodejs12.x
  stage: ${opt:stage, 'dev'}
  #VPC LAMBDA =====================>
  vpc:
    securityGroupIds:
      - sg-0e5e1f6692ad6558a
    subnetIds:
      - subnet-05b1b53c39b4edb41
      - subnet-01c2a3f6d8297abda
  #VPC LAMBDA =====================>

  #VPC API GATEWAY ================>
  endpointType: private
  resourcePolicy:
    - Effect: Allow
      Principal: '*'
      Action: execute-api:Invoke
      Resource:
        - execute-api:/*/*/*
  #VPC API GATEWAY ================<
#...
```

# Configurar el API Gateway con VCP Endpoint

Canfigurar el gateway dentro de la VCP, agregando un vcp endpoint

```yaml
#...
provider:
  name: aws
  region: us-east-1
  runtime: nodejs12.x
  stage: ${opt:stage, 'dev'}
  #VPC LAMBDA =====================>
  vpc:
    securityGroupIds:
      - sg-0e5e1f6692ad6558a
    subnetIds:
      - subnet-05b1b53c39b4edb41
      - subnet-01c2a3f6d8297abda
  #VPC LAMBDA =====================>

  #VPC API GATEWAY ================>
  endpointType: private
  resourcePolicy:
    - Effect: Allow
      Principal: '*'
      Action: execute-api:Invoke
      Resource:
        - execute-api:/*/*/*
  vpcEndpointIds:
    - vpce-02895382bd0405679
  #VPC API GATEWAY ================<
#...
```