# Selenium-HybridFramework-Demo
This is a sample framework created using Selenium Webdriver with Java and maven.
The Framework is Hybrid in nature which uses Keywords and data from XLS files to automate a web application. 

This Framework have "com.automationlearning.test" as main package. This is a Maven Project to handel all the jar's dependecies like POI, Loging, jar files in the project. POM.xml can be found in the root folder. All the Code, Properties and XLS files are kept in a structured manner. 

#src
1. config --> This package contains All the configurations needed beforehand to automate a website.

2. logs   --> All the test execution logs are maintained in this package.

3. test   --> This package contains all the files containing code logic written in JAVA.

4. xls    --> This package contains all the XLS files(Test Suites and TestCases) and file containing code logic for Reading And writing in xls files.




#results
After completing the test execution results are saved in an HTML report "index.html" which is Saved/ Updated in this folder.

