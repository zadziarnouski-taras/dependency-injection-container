# Dependency-Injection Container

## About
**DI container** is a library written using the Java Reflection API and classes from the java.lang.reflect package to implement the simplest version of the Dependency Injection container (Spring Framework). 

Implemented the ability to use Singleton (creating an object on the first call) and Prototype beans.

Injection only through constructors is supported. Getting providers must be thread safe.

To perform binding,```@Inject``` annotation is added to the constructor of the implementation class. For example:
```Java
   public class EventServiceImpl implements EventService {
   private EventDao eventDao;
   
   @Inject
   public EventServiceImpl(EventDao eventDao) {
   this.eventDao = eventDao;
       }
   }
   ```
If there are multiple ```@Inject```annotated constructors in a class, a ```TooManyConstructorsException``` is thrown.

If there are no constructors with ```@Inject```, the default constructor is used. At
   if not, a ```ConstructorNotFoundException``` is thrown.

If the container uses a constructor with ```@Inject``` and for any argument the container
   cannot find binding, a ```BindingNotFoundException``` is thrown.

If we request a Provider for a class and there is no corresponding binding, ```null``` is returned.

## How to Use Library
### Step 1. Install and setup Maven
In case you don’t have maven already installed on your system, Please follow Maven [Download & Installation](http://websystique.com/maven/maven-installation-and-setup-windows-unix "Download & Installation") containing step-by-step instruction to setup maven.

### Step 2: Use Maven template to generate project structure and artifacts
Following is the syntax of maven template:
```
mvn archetype:generate 
-DgroupId=YourProjectGroupId 
-DartifactId=YourProjectName 
-DarchetypeArtifactId=maven-archetype-quickstart 
-DinteractiveMode=false
```
Description :

* groupId : Refers to project packaging and identifies your project uniquely across all projects.
* artifactId : Name of jar of your project without version.
* version : Version number of your choice.
* archetypeArtifactId : Template type.Several templates are available to choose from.
* interactiveMode : If set to true, maven will ask confirmation on each step of project generation.
### Step 3. Add dependency of library to ```pom.xml``` (with [jitpack](https://jitpack.io/ "jitpack"))

Add these dependencies to your ```pom.xml```:
```XML
<repositories>
   <repository>
      <id>jitpack.io</id>
      <url>https://jitpack.io</url>
   </repository>
</repositories>
```
and 
```XML
<dependency>
   <groupId>com.github.zadziarnouski-taras</groupId>
   <artifactId>dependency-injection-container</artifactId>
   <version>v1.0</version>
</dependency>
```
That's it! Good luck!

## Example of Use

```Java
import by.zadziarnouski.service.Injector;
import by.zadziarnouski.service.InjectorImpl;
import by.zadziarnouski.service.Provider;
import testClass.*;

public class Main {
    public static void main(String[] args) {
        Injector injector = new InjectorImpl();

        injector.bind(EventDao.class, EventDaoImpl.class);
        injector.bind(EventDao2.class, EventDao2Impl.class);
        injector.bind(EventDao3.class, EventDao3Impl.class);
        injector.bindSingleton(EventService.class, EventServiceImpl.class);

        Provider<EventService> provider = injector.getProvider(EventService.class);
        EventService instance = provider.getInstance();
        instance.doSomething();
    }
}
```

## Author
**Taras Zadziarnouski**. Send me message to [Gmail](mailto:taras.zadziarnouski@gmail.com "Gmail"). Add me to [LinkedIn](https://www.linkedin.com/in/taras-zadziarnouski-b6205a206/ "LinkedIn"). Follow me to [Instagram](https://t.me/taraszadziarnouski "Instagram").

## Customer:
Test task for the [Elinext.com](https://www.elinext.com/ "elinext") before the interview.