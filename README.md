# Graphene2Gephi

A simple pipeline that allows us to parse .NT files, specifically the ones that returns Graphene (https://github.com/Lambda-3/Graphene).
The parsed file is in a .gexf format, this xml format file can be used in the open graph viz platform named Gephi (https://gephi.org/) so that you're able to visualize your knowledge graphs.

## Requirements

* Java 8 (OpenJDK or Oracle)
* Apache Ant version 1.9.6

### Setup
	ant

### Get it to work
	java -jar graphene2gephi.jar inputFile.nt outputFile.gexf

Let's say you want your graph to only have some specific subjects, objects or even predicate. Graphene2Gephi offers an option to filter the results you get from your .nt file, so that the resultant .gexf file will only have the searched entities.
For this you'll need a file with entities you're searching:

```
	Arthur
	Lancelot
	Merlin
	Sword
	...
```

Once you have the file, Graphene2Gephi offers two options. You can choose whether to only search for this entities in subjects or objects using the `-d`, or you can search for this entities also in the predicate using `-D`.

### Usage
	java -jar graphene2gephi.jar inputFile.nt outputFile.gexf [-d|-D entitiesFile]
