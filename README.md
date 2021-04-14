# Endpoint Annotation Processor

## Annotation Producer

It provides endpoints annotation processor which reads
`@RolesAllowed` and `@RequestMapping` annotation. Based on these annotation metadata, it creates `endpoint.json` file.

Sample:

```json
{
  "/api": {
    "rolesByMethodType": {
      "POST": [
        "post"
      ],
      "GET": [
        "request"
      ]
    }
  }
}
```

## Annotation Consumer

It defines rest api along with its role. Now, it uses endpoints annotation processor to create `endpoint.json` file.
Which can be used at runtime to check for endpoint accessibility.

