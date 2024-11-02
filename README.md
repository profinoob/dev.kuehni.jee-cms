# JEE CMS

> [!WARNING]
> Currently, the app is configured to drop all tables and recreate them on deployment.

## Project

### Facelets

Views are written
as [Facelets](https://jakarta.ee/learn/docs/jakartaee-tutorial/current/web/faces-facelets/faces-facelets.html).
Backing beans are declared as `ViewModel`s.

### UI Component Library

For UI components and for theming purposes, this project uses [PrimeFaces](https://www.primefaces.org/showcase).
The XML namespace `xmlns:p="http://primefaces.org/ui"` is used in facelets.

### URL Rewriting

To get nicely looking URLs, this project uses [prettyfaces](https://www.ocpsoft.org/prettyfaces/).
The rewriting rules are defined in [WEB-INF/pretty-config.xml](/src/main/webapp/WEB-INF/pretty-config.xml).

The mapping ids defined in [WEB-INF/pretty-config.xml](/src/main/webapp/WEB-INF/pretty-config.xml) can then be used
for navigation outcomes (e.g. `<p:link outcome="pretty:logout">Logout</p:link>`). For programmatic redirects,
`PrettyFacesRedirector` can be used.

### Auth

I didn't get Jakarta Security to work (see branch [not-working-auth](https://github.com/profinoob/dev.kuehni.jee-cms/tree/not-working-auth)).
Therefore, I implemented my own auth&hellip;

## Deployment

Deploy this on a GlassFish 7 server (Jakarta 10).

You have to create a JDBC Resource with the JNDI name `jdbc/cms`.
The user used for this connection pool has to have access to DDL, DQL, DML, and transactions.
