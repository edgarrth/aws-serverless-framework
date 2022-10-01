# Exponer servicios

Las lambdas que necesiten exponerse como servicio usaran el api gateway de AWS para ello se configuraran eventos HTTP en las funciones.

Se deberan configurar las siguientes secciones:

-   **`handler`**: Metodo en java que sera invocado al llegar el request HTTP

-   **`events.http`**: Datos del evento HTTP

    -   **path**: Ruta en la que se expondra el servicio se puede usar {} para declarar path params

    -   **method**: Metodo http

    -   **cors**: Para meticiones de otros dominios

    -   **authorizer**: Se debe configurar igual que en el ejemplo, es una funcion que actua como interceptor para validar las firmas digitales de los token

        -   Variables custom con el arn del autorizador, el valor siempre sera el mismo
    
```yaml
custom:
  authorizer:
    arn: arn:aws:lambda:#{AWS::Region}:#{AWS::AccountId}:function:identity-api-${self:provider.stage}-authorizator
```

```yaml
appointmentsDetail: #Nombre de la funció?n
  handler: src/controller/handler.appointmentsDetail
  events:
    - http:
        path: /person/{personid}
        method: get
        cors: true
        authorizer:
          arn: '${self:custom.authorizer.arn}'
          identitySource: method.request.header.id-token
          type: request
```

# Invocar servicios

Para poder invocar servicios usaremos la libreria axios.
Esto se realizara en lo repositories

## Importar libreria

```javascript
import axios, { AxiosError } from 'axios';
```
