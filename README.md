# Graphene2Gephi

A simple pipeline that allows us to parse .NT files, specifically the ones that returns Graphene (https://github.com/Lambda-3/Graphene).
The parsed file is in a .gexf format, this xml format file can be used in the open graph viz platform named Gephi (https://gephi.org/) so that you're able to visualize your knowledge graphs.

## Requirements

* Java 8 (OpenJDK or Oracle)
* Apache Ant version 1.9.6

### Setup
	ant

### Get it to work
	java -jar Graphene2Gephi.jar inputFile.nt outputFile.gexf

