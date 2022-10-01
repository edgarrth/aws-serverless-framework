# IDE

# GIT

Si usas windows y se tiene problemas con el retorno de carro (LF/CRLF) configurar este comando:

```shell script
    $ git config --global core.autocrlf input
```

# Clonado del proyecto

```shell script
$ git clone --branch develop https://gitlab.com/axiz-innovation-services/axiz-aws-template.git
```

# Instalación de dependencias

Se debe tener instalado el software base:
- nodejs
- npm
- java
- maven

Luego instalar globalmente el framework serverless y modo offline

```shell script
$ npm install -g serverless
$ npm install -g serverless-offline
$ npm install -g serverless-prune-plugin
```

# Configuración de cuenta AWS

Para este punto se debera tener un usuario de aws (**Programmatic access**)
Ejecutar el comando:

```shell
$ AWS configure
```

El comando preguntara por los siguientes valores

-   `AWS Access Key ID:` Usuario programatico de aws, ejm: EDIAXVWRRT5YIDPLED42, no es el correo.

-   `AWS Secret Access Key:` Clave del usuario

-   `Default region name:` us-east-1

-   `Default output format:` json

Esto guardara las crendenciales del usuario como default, cada que vez que se ejecute un comando con el cli de aws se utilizara este usuario,
en el caso de tener mas de un usuario agregar el argumento profile y el alias que quiere darle al nuevo usuario.

```shell script
$ AWS configure --profile user2
```

Para poder usar el user2 se tendra que poner el profile al final de cada comando del cli de aws

# Comandos para ejecutar el proyecto

## Ejecucion OffLine / Local


``` shell script
$ serverles offline
```


# Despliegues
``` shell script
$ serverles deploy 
ó
$ serverles deploy dev
```
