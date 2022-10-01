![](DistributedTracing.png)

Para habilitar la trazabilidad distribuida se tiene que habilitar tanto la configuracion en el
archivo descritor (serverless.yaml) y por codigo.

# Configuracion en serverless.yaml

```yaml
provider:
  name: aws
  region: us-east-1
  runtime: java11
  stage: ${opt:stage, 'dev'}

  #Distributed Tracing =============>
  tracing:
    lambda: true
    apiGateway: true
  #Distributed Tracing =============<
```

# Codigo

## Dependencias

Agregar la siguiente dependencia:

```xml
<dependency>
    <groupId>com.amazonaws</groupId>
    <artifactId>aws-xray-recorder-sdk-core</artifactId>
    <version>2.9.1</version>
</dependency>
```

## Trazabilidad de Dynamodb y SNS
