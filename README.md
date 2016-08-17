# restful-jersey

### CH00: Jersey on Http vs Servlet Container
* Http-only: JDK HTTP container, Simple HTTP container, Jetty HTTP Container, Grizzly
    
    source code (with main) -> Jar -> Java Command Line
  
* Servlet Container (like Tomcat): Grizzly servlet, Jetty Servlet, Generic Servlet
    
    source code (with web.xml) -> War -> Servlet Container
#### 0.1 Invoking an Endpoint
 WebTarget
 * path("")
 * request()
 
 Invocation.Builder
 * accept("application/json")
 * cookie("name", "value")
 * header("someHeader","someHeader")
 * get()
 
#### 0.2 Evaluating the Results
* Return an entity object
    ```java
    Book b = target("books").path("1").request().get(Book.class);
    
    assertNotNull(b.getTitle())
    ```

* Return a Response object
    ```java
    Response r = target("books").path("1").request().get();
    
    assertEquals(200, r.getStatus());
    Book b = r.readEntity(Book.class);
    assertNotNull(b.getTitle);
```

#### 0.3 JerseyTest
```
Use JerseyTest and im-memory grizzly container to run unit testing, so you don't have to
deploy the war file to tomcat.
```

#### 0.4 Injection with HK2 and @Context
* Jersey is bundled with HK2(no other DI framework necessary, like spring or Guice)
* Requires a "binder" registered with ResourceConfig
```
SomeClass anInstance = new SomeClass();
register(new AbstractBinder(){
        configure() {
            bind(anInstance).to(SomeClass.class);
        }
})
```

```
// In resource
@Context
SomeClass someInstance;
```



### CH01: Resources and sub-resources
#### 1.1 Root Resources
* @Path
```
1) @Path is a relative URL path
2) @Path("users/{username: [a‐zA‐Z][a‐zA‐Z_0‐9]*}")
3) A @Path value may or may not begin with a '/', it makes no difference.
4) @Path is for both the class and method levels
```

* @GET, @PUT, @POST, @DELETE and @HEAD are resource method designator annotations 
  defined by JAX‐RS and which correspond to the similarly named HTTP methods

* @Produces: is used to specify the MIME (multipurpose internet mail extension) media types of 
  representations a resource can produce and send back to the client.
```
1) @Produces can be applied at both the class and method levels. Method's annotation overrides the class-level setting.
2) @Produces can declare multiple media types. @Produces({"application/xml", "application/json"})
3) Higher quality factor one will be selected when client accepts all produce types. 
   @Produces({"application/xml; qs=0.9", "application/json"}) // "application/json" will be selected.
```

* @Consumes: is used to specify the MIME media types of representations that can be consumed by a resource.
    ```java
    1) it can be applied at both the class and the method levels 
    2) it can declare more than one media type
    ```

    ```
    @POST
    @Consumes("text/plain")
    public void postClichedMessage(String message) { // Store the message }
    
    Note: the resource method returns void. This means no representation is returned and response 
          with a status code of 204 (No Content) will be returned to the client.
    ```


#### 1.2 Parameter Annotations
1.2.1 @PathParam: is used to extract a path parameter from the path component of the request URL 
      that matched the path declared in @Path.   

1.2.2 @MatrixParam extracts information from URL path segments. 

1.2.3 @HeaderParam extracts information from the HTTP headers. 

1.2.4 @CookieParam extracts information from the cookies declared in cookie related HTTP headers.   

1.2.5 @QueryParam: is used to extract query parameters from the Query component of the request URL.

    ```java
    @Path("smooth")
    @GET
    public Response smooth(
    @DefaultValue("2") @QueryParam("step") int step,
    @DefaultValue("true") @QueryParam("min‐m") boolean hasMin,
    @DefaultValue("true") @QueryParam("max‐m") boolean hasMax,
    @DefaultValue("true") @QueryParam("last‐m") boolean hasLast,
    @DefaultValue("blue") @QueryParam("min‐color") ColorParam minColor,
    @DefaultValue("green") @QueryParam("max‐color") ColorParam maxColor,
    @DefaultValue("red") @QueryParam("last‐color") ColorParam lastColor) { ... }
    
    public class ColorParam extends Color {
        public ColorParam(String s) {
        super(getRGB(s));
        }
        private static int getRGB(String s) {
            if (s.charAt(0) == '#') {
                try {
                    Color c = Color.decode("0x" + s.substring(1));
                    return c.getRGB();
                } catch (NumberFormatException e) {
                    throw new WebApplicationException(400);
                }
            } else {
                try {
                    Field f = Color.class.getField(s);
                    return ((Color)f.get(null)).getRGB();
                } catch (Exception e) {
                    throw new WebApplicationException(400);
                }
        }
        }
    }
    ```


1.2.6 @FormParam extracts information from a request representation that is of the MIME media type "application/x‐www‐form‐urlencoded" 
   and conforms to the encoding specified by HTML forms, as described here. 
   This parameter is very useful for extracting information that is POSTed by HTML forms
    
    ```java
    @POST
    @Consumes("application/x‐www‐form‐urlencoded")
    public void post(@FormParam("name") String name) {// Store the message}
    ```

1.2.7 @Context can be used to obtain contextual Java types related to the request or response.
      * Application
      * UriInfo
      * Request
      * HttpHeaders
      * SecurityContext
      * Providers
      * ServletConfig* (available in servlet container)
      * ServletContext* (available in servlet container)
      * HttpServletRequest* (available in servlet container)
      * HttpServletResponse* (available in servlet container)
    
    ```java
    // Obtaining general map of URI path and/or query parameters
    @GET
    public String get(@Context UriInfo ui) {
    MultivaluedMap<String, String> queryParams = ui.getQueryParameters();
    MultivaluedMap<String, String> pathParams = ui.getPathParameters();
    }
    // Obtaining general map of header parameters
    @GET
    public String get(@Context HttpHeaders hh) {
    MultivaluedMap<String, String> headerParams = hh.getRequestHeaders();
    Map<String, Cookie> pathParams = hh.getCookies();
    }
    ```
    
1.2.8 @BeanParam is used this way to aggregate more request parameters into a single bean.

    ```java
    //Example of the bean which will be used as @BeanParam
        public class MyBeanParam {
            @PathParam("p")
            private String pathParam;
            
            @MatrixParam("m")
            @Encoded
            @DefaultValue("default")
            private String matrixParam;   
                
            @HeaderParam("header")
            private String headerParam;
            
            private String queryParam;
            
            public MyBeanParam(@QueryParam("q") String queryParam) {
                this.queryParam = queryParam;
            }
            public String getPathParam() {
                return pathParam;
            }
            ...
        }

    // Injection of MyBeanParam as a method parameter:
      @POST
      public void post(@BeanParam MyBeanParam beanParam, String entity) {
      final String pathParam = beanParam.getPathParam(); // contains injected path parameter "p"
      ...
      }  
      
      // Injection of more beans into one resource methods:
      @POST
      public void post(@BeanParam MyBeanParam beanParam, @BeanParam AnotherBean anotherBean, @PathParam("p") pathParam,
      String entity) {
      // beanParam.getPathParam() == pathParam
      ...
      }
    ```    
    
    
#### 1.3 Sub-resources
* Sub-resource methods: hierarchical matching on the path of the request URL is performed.

```java
   @Singleton
   @Path("/printers")
   public class PrintersResource {
      @GET
      @Produces({"application/json", "application/xml"})
      public WebResourceList getMyResources() { ... }
      
      @GET @Path("/list")
      @Produces({"application/json", "application/xml"})
      public WebResourceList getListOfPrinters() { ... }
      
      @GET @Path("/jMakiTable")
      @Produces("application/json")
      public PrinterTableModel getTable() { ... }
      
      @GET @Path("/jMakiTree")
      @Produces("application/json")
      public TreeModel getTree() { ... }
      
      @GET @Path("/ids/{printerid}")
      @Produces({"application/json", "application/xml"})
      public Printer getPrinter(@PathParam("printerid") String printerId) { ... }
      
      @PUT @Path("/ids/{printerid}")
      @Consumes({"application/json", "application/xml"})
      public void putPrinter(@PathParam("printerid") String printerId, Printer printer) { ... }
      
      @DELETE @Path("/ids/{printerid}")
      public void deletePrinter(@PathParam("printerid") String printerId) { ... }
   }
```

* Sub-resource locators: same url but with different request method

```java
@Path("/item")
public class ItemResource {
    @Context UriInfo uriInfo;
    @Path("content")
    public ItemContentResource getItemContentResource() {
        return new ItemContentResource();
    }
    @GET
    @Produces("application/xml")
    public Item get() { ... }
    }
}
public class ItemContentResource {
    @GET
    public Response get() { ... }
    
    @PUT
    @Path("{version}")
    public void put(@PathParam("version") int version,
    @Context HttpHeaders headers,
    byte[] in) {
    ...
    }
}
```

* Sub-resource locators with empty path

```java
// It is performed at runtime thus it is possible to support polymorphism.
@Path("/item")
public class ItemResource {
    @Path("/")
    public ItemContentResource getItemContentResource() {
        return new ItemContentResource();
    }
}

```

* Sub-resource locators returning sub-type

```java
@Path("/item")
public class ItemResource {
    @Path("/")
    public Object getItemContentResource() {
        return new AnyResource();
    }
}

```

* Sub‐resource locators created from classes

```java
import javax.inject.Singleton;
@Path("/item")
public class ItemResource {
    @Path("content")
    public Class<ItemContentSingletonResource> getItemContentResource() {
    return ItemContentSingletonResource.class;
    }
}
@Singleton
public class ItemContentSingletonResource {
// this class is managed in the singleton life cycle
}
```

* Sub‐resource locators returning resource model

```java
import org.glassfish.jersey.server.model.Resource;
@Path("/item")
public class ItemResource {
    @Path("content")
    public Resource getItemContentResource() {
    return Resource.from(ItemContentSingletonResource.class);
    }
}
```

#### 1.4 Life-cycle of Root Resource Classes
By default the life-cycle of root resource classes is per-request which, namely that a new instance 
of a root resource class is created every time the request URI path matches the root resource.

* Request scope: @RequestScoped (or none)
  Default lifecycle (applied when no annotation is present). In this scope the resource instance is
  created for each new request and used for processing of this request. If the resource is used
  more than one time in the request processing, always the same instance will be used. This can
  happen when a resource is a sub resource is returned more times during the matching. In this
  situation only one instance will server the requests.
  
* Per-lookup scope: @PerLookup
  In this scope the resource instance is created every time it is needed for the processing even it
  handles the same request.
  
* Singleton: @Singleton
  In this scope there is only one instance per jax-rs application. Singleton resource can be either
  annotated with @Singleton and its class can be registered using the instance of Application.
  You can also create singletons by registering singleton instances into Application.

#### 1.5 Rule of Injection

* Class fields: The field can be private and must not be final.
 Cannot be used in Singleton scope except proxible types.

* Constructor parameters: If more constructors exists the one with the most injectable parameters will be invoked. 
  Cannot be used in Singleton scope except proxible types.

* Resource methods: The resource methods (these annotated with @GET, @POST, ...) can contain parameters that can be injected 
  when the resource method is executed. Can be used in any scope.

* Sub resource locators: The sub resource locators (methods annotated with @Path but not @GET, @POST, ...) 
  can contain parameters that can be injected when the resource method is executed. Can be used in any scope.

* Setter methods: Instead of injecting values directly into field the value can be injected into the setter method 
  which will initialize the field. This injection can be used only with @Context annotation. 
  This means it cannot be used for example for injecting of query params 
  but it can be used for injections of request. The setters will be called after the object creation and only once.
  The name of the method does not necessary have a setter pattern. 
  Cannot be used in Singleton scope except proxiable types.

```java
//Injection can be performed on fields, constructor parameters, resource/sub‐resource/sub‐resource
  locator method parameters and bean setter methods.

@Path("{id:\\d+}")
public class InjectedResource {
    // Injection onto field
    @DefaultValue("q") @QueryParam("p")
    private String p;
    
    // Injection onto constructor parameter
    public InjectedResource(@PathParam("id") int id) { ... }
    
    // Injection onto resource method parameter
    @GET
    public String get(@Context UriInfo ui) { ... }
    
    // Injection onto sub‐resource resource method parameter
    @Path("sub‐id")
    @GET
    public String get(@PathParam("sub‐id") String id) { ... }
    
    // Injection onto sub‐resource locator method parameter
    @Path("sub‐id")
    public SubResource getSubResource(@PathParam("sub‐id") String id) { ... }
    
    // Injection using bean setter method
    @HeaderParam("X‐header")
    public void setHeader(String header) { ... }
}
```

```java
// all possible injections
@Path("resource")
public static class SummaryOfInjectionsResource {
    @QueryParam("query")
    String param; // injection into a class field
    
    @GET
    public String get(@QueryParam("query") String methodQueryParam) {
        // injection into a resource method parameter
        return "query param: " + param;
    }
    
    @Path("sub‐resource‐locator")
    public Class<SubResource> subResourceLocator(@QueryParam("query") String subResourceQueryParam) {
        // injection into a sub resource locator parameter
        return SubResource.class;
    }
    public SummaryOfInjectionsResource(@QueryParam("query") String constructorQueryParam) {
        // injection into a constructor parameter
    }

    @Context
    public void setRequest(Request request) {
        // injection into a setter method
        System.out.println(request != null);
    }
}
public static class SubResource {
    @GET
    public String get() {
        return "sub resource";
    }
}
```



The example below will cause validation failure during application initialization 
as singleton resources cannot inject request specific parameters. The same example would fail if
the query parameter would be injected into constructor parameter of such a singleton. 
In other words, if you wish one resource instance to server more requests (in the same
time) it cannot be bound to a specific request parameter.
```java
// Wrong injection into a singleton scope
@Path("resource")
@Singleton
public static class MySingletonResource {
    @QueryParam("query")
    String param; // WRONG: initialization of application will fail as you cannot
                  // inject request specific parameters into a singleton resource.
    @GET
    public String get() {
        return "query param: " + param;
    }
}
```


The exception exists for specific request objects which can injected even into constructor or class fields. 
For these objects the runtime will inject proxies which are able to simultaneously server more request. 
These request objects are HttpHeaders, Request, UriInfo, SecurityContext. 
These proxies can be injected using the @Context annotation. 
```java
@Path("resource")
@Singleton
public static class MySingletonResource {
    @Context
    Request request; // this is ok: the proxy of Request will be injected into this singleton
    public MySingletonResource(@Context SecurityContext securityContext) {
    // this is ok too: the proxy of SecurityContext will be injected
    }
    @GET
    public String get() {
        return "query param: " + param;
    }
}
```

### CH02: Application Deployment and Runtime Environments
#### 2.1 













































