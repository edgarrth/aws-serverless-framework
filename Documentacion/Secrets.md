Todas las variables que contentan usuarios, contraseñas o datos sensibles, deberan ser almacenados
en el secret manager de AWS.

# Creación de variables

Al ser informacion sensible, los lideres tecnicos seran los encargados de generar esta variables.

Se debera crear una variable por cada ambiente.

## Nomenclatura

Tanto los parametros como los secretos siguen un estadar de arbol por ella la estructura para nombrarlos sera la siguiente

-   /Ambiente

-   /Nombre de la empresa

-   /Entidad principal y/o sistema invocado

-   /Tipo de secret (credenciales, variables, etc)

Ejemplo:

``` {.yaml}
/dev/empresa/person/credentials
```

## Generación por linea de comandos

``` {.shell script}
$ aws secretsmanager create-secret --name miSecret --description "Data super secreta" --secret-string file://mycreds.json
```

Donde el archivo mycreds.json deberá? tener la siguiente estructura:

```json
[
    {
        "Key": "user",
        "Value": "user@axiz.pe"
    },
    {
        "Key": "clave",
        "Value": "axiz123."
    },
    {
        "Key": "apiSecret",
        "Value": "21212asdsd12312"
    }
]
```

# Usar variables secret en codigo
