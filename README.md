zebra-puzzle
============

<a href="http://en.wikipedia.org/wiki/Zebra_Puzzle">Zebra puzzle</a> solving program.

Usage
-----

Note, to execute the instructions below you should have Git, Maven and Java installed.

    git clone https://github.com/MATPOCOB/zebra-puzzle.git
    cd zebra-puzzle
    mvn clean install
    cd target/classes
    java org.kklenski.zebra.PuzzleSolver < ../../samples/puzzle.csv > solutions.xml
    
PuzzleSolver reads puzzle definition in CSV format from standard input and streams solutions in the form of XML to standard output.

Take a look at the samples:
* samples/puzzle.csv - classic version definition in CSV format
* samples/solutions.xsl - XSL that can be used to render result XML into HTML (works only for classic property set)
* samples/solutions.xml - classic version solution XML with reference to XSL that will be rendered automatically to HTML by most browsers