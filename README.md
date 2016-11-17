# GraphQL Error Handler

This is just a simple project to modify the behaviour of the HTTP Response body of GraphQL specification (see below)
to add an extra field per each error to contain a code. That should be compatible with any GraphQL client as
this is only adding an extra field.

For example:


```json
{
  "data": null,
  "errors": [
    {
      "message": "random message error",
      "code": "random code error"
    }
  ]
}
```
(The new field is the code)

If there is an error the HTTP Header Status would be:

- InvalidSyntax, ValidationError: 404;
- DataFetchingException: 500;

### Technology Stack
* java 8
* spring Boot 1.4.2.RELEASE
* graphql-java 2.2.0

### Links
[Facebook GraphQL Error specification](http://facebook.github.io/graphql/#sec-Errors)


  