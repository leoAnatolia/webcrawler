# webcrawler

This project is implemention of a simple web crawler. Basically it is taking arguments as two html files.
Find the element with attribute id="make-everything-ok-button" in the first html file and looks for the element which has the same mission in the second html file. Basically, it compares the title attibute of element(with attribute id="make-everything-ok-button").
If it finds more than one element with the same title, then it is returning the first element with the same "class" attribute.

To build and create the artifact, run command

#mvn package

under main project directory. It will generate a fat jar under target directory named as webcrawler-ozgur-jar-with-dependencies.jar



To run the program, use command
#java -jar webcrawler-ozgur-jar-with-dependencies.jar <input_html_file_1> <input_html_file_2>

e.g.

#java -jar webcrawler-ozgur-jar-with-dependencies.jar samples/sample-0-origin.html samples/sample-1-evil-gemini.html


