# asyncpp
asyncpp - Asynchronous Processing Pipeline

Lightweight asynchronous library

Pipeline of the data processing is a chain of objects going through computation entities

## Getting Started
Computation entities are the object that computes incoming data

For example, if you need to compute String, you need just simply extend ComputationEntity

```java
import io.cat.ai.asyncpp.core.computation.ComputationEntity;

class StrComputationEntity extends ComputationEntity<String> { 
    
    @Override
    public void startComputing(String data) throws Exception {
        // do something with incoming data
    }
}
```

Then you need an instance of Asyncpp that represents whole processing pipeline, and put the computation entity to the pipeline 

```java
import io.cat.ai.asyncpp.app.App;
import io.cat.ai.asyncpp.app.internal.Asyncpp;

Asyncpp<String> asyncpp = App.Dsl.
              <String>asyncpp() // creates instance of Asyncpp with executor
              .createPipeline() // creates instance of AbstractPipeline for Asyncpp
              .withEntity("strComputationEntity", StrComputationEntity::new) // adds to pipeline named entity and entity instance
             
```

If you wanna compute string with defined pipeline, you just need to use process() method of Asyncpp

```java
asyncpp
.process("String")
.process("Another string")
.process("STRING")
```

You need a delayed processing? Use the processWithDelay() method of Asyncpp
```java
asyncpp
.processWithDelay("String", Duration.ofMillis(10))
.processWithDelay("Another string", Duration.ofMillis(30))
.processWithDelay("STRING", Duration.ofMillis(50))
```

You wanna see the state of processing?

```java
asyncpp.pipeline() // pipeline of Asyncpp 
       .computationManager() // manager of pipeline
       .getAll(ComputationType.DELAYED_COMPUTING) // returns all delayed computations 
       .forEach(System.out::println);

```


## Authors

* **cat-ai@github.com** - *Initial work*
