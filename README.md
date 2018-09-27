# allele
`allele` is purely functional highly flexible scala library for genetic algorithms 

## Manifesto
#### This library _is_ and _always should be_:
1. **Easy in use**
	
    You don't have to know how genetic algorithms work, if you know what you can use one for.
    `allele` allows you to call `GeneticAlgorithm.evolve` directly with standard or custom set of operators
    * `GeneticAlgorithm.evolve`	- for simple sequential evolution of generation for certain number of iterations 
    * `GeneticAlogrithm.par.evolve`- for parallel sequential evolution of generation for certain number of iterations
    * `GeneticAlgorithm.stream.evolve` - for evolving initial population as continuous stream of evolved ones
    
2. **Elastic for user needs**

	You are no longer restricted with byte arrays or sequence of doubles. With support of type classes `allele` allows you to use genetic algorithm with any data type you want: Books, Shapes, Colors as well as any already existing model from your domain without need of nasty adapters or modifications.
3. **Purely functional and highly expandable**

 	`allele` enforces immutability which increases code readability and allows efficient caching. It is also built with modular and extensible architecture, so users can easily extend any genetic algorithm or operator for particular needs.
4. **Fast and optimized**
	
    Using scala parallel collections and streaming libraries , like `akka-streams` it is possible to use many genetic operators in parallel and asynchronous way with no-cost transition between them. 
