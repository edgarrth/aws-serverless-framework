# Creación de tablas

## Nomenclatura y variables

Primero se debe configurar el nombre de la tabla, para ello, se generara una variable custom en el archivo principal (serverless.yml)
con la siguiente nomenclatura:

**`/[company]-[entidad]-[ambiente]`**
Donde:

- `[company]`: Empresa o Modulo para el cual es creado, se debe usar la expresion: ${self:empresa}
- `[entidad]`: Entidad principal a la que hace referencia la tabla
- `[ambiente]`: Nombre del ambiente, se debe usar la expresion: ${self:provider.stage}

La variables debera estar ubicada en la seccion custom.table.

```yaml
custom:
  bucket:
    base-serverless: axiz-serverless-template
    project: axiz-test
  stage: ${opt:stage, self:provider.stage}
  table:
    credentials: ${self:service}-credentials-${self:provider.stage}
```

## Propiedades de la tabla

### Campos

Las tablas con las que trabaja el proyecto son dynamoDB y sus propiedades se configuraran en el archivo serverless.yml

```yaml
resources:
  Resources:
    CredentialsTable:
      Type: AWS::DynamoDB::Table #Valor fijo del tipo de tabla
      Properties:
        TableName: ${self:custom.table.credentials} #Nombre de la tabla, referencia a la variable custom
        AttributeDefinitions: # Seccion de campos
          - AttributeName: email  #Nombre del campo
            AttributeType: S  # Tipo de campo, puede ser S=String, N=Number y B=Binario
          - AttributeName: code
            AttributeType: S
        KeySchema: # Secció?n de la llave primaria de la tabla
          - AttributeName: email #Nombre del campo, solo se pueden usar nombres de la seccion AttributeDefinitions
            KeyType: HASH # Tipo de llave HASH = Partition Key, sera la llave para realizar busquedas
          - AttributeName: code #Nombre del campo, solo se pueden usar nombres de la seccion AttributeDefinitions
            KeyType: RANGE #Tipo de llave RANGE = Sort Key, servira solo para filtrar los resultados del hash, no se podra hacer busquedas directas por este valor
        BillingMode: PAY_PER_REQUEST
```

# Despliegue de las tablas

Las tablas se desplegaran por el proceso de integración continua.

# Uso de tablas

Cuando ya se tiene las tablas configuradas y desplegadas en los ambientes, se podran referencia en su proyecto api.

## Configuración de variable de nombre

Al igual que en el proyecto resources, en el proyecto api se generará? una variable custom con el nombre de la tabla:

```yaml
custom:
  database: #Nombre de la tabla
    appointment: ${self:custom.company}-credentials-${self:provider.stage}
```

## Configuración de permisos

Se debera configurar los permisos hacia la tabla usando la seccion iamRolesSTatements del serverless.yml

```yaml
iamRoleStatements:
    - Effect: Allow
      Action: #Accion a realizar sobre la tabla, dejar solo la que corresponda
        - dynamodb:Query
        - dynamodb:Scan
        - dynamodb:GetItem
        - dynamodb:PutItem
        - dynamodb:UpdateItem
        - dynamodb:DeleteItem
      Resource: #Lista de tablas a las que se quiere dar el permiso de la seccion action
        - 'arn:aws:dynamodb:${self:provider.region}:*:table/${self:custom.database.credential}'
        - 'arn:aws:dynamodb:${self:provider.region}:*:table/${self:custom.database.credential}/index/*' # Los index tambien deben estar en esta seccion
```

## Configuración de variable de entorno

En el codigo, para poder conectarnos a las tablas, es necesario hacer referencia a sus nombres, para ello se generaran variables de entorno.
Con la siguiente nomenclatura:

**`DATABASE_[ENTIDAD]`**
Donde:

-   `DATABASE`: Texto fijo

-   `[entidad]`: Entidad principal a la que hace referencia la tabla (upcase)

    ```yaml
    environment:
      DATABASE_APPOINTMENT: ${self:custom.database.person} # Referencia al nombre de variable de la seccion custom
      DATABASE_CREDENTIALS: ${self:custom.database.credential}
    ```
