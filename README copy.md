## Perfecto + Gradle + Testng Sample</br>

### Dependencies</br>
There are several prerequisite dependencies you should install on your machine prior to starting to work with this project:</br>

* [Java 8](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)</br>

* An IDE to write your tests on - [Eclipse](http://www.eclipse.org/downloads/packages/eclipse-ide-java-developers/marsr) or [IntelliJ](https://www.jetbrains.com/idea/download/#)

</br>

Eclipse users should also install:</br>

1. [TestNG Plugin](http://testng.org/doc/download.html)</br>

IntelliJ IDEA users:</br>

TestNG Plugin is built-in in the IntelliJ IDEA, from version 7 onwards.</br>

### Steps to integrate with Perfecto </br>

1. Clone/ Download this project.</br>
2. [Eclipse] : Go to Window->Preferences->Gradle (Windows) / Eclipse->Preferences (Mac) and then pass the following arguments into "Program Arguments" section.</br>

	`-PcloudName={Perfecto cloud name only. E.g. demo} -PsecurityToken={Perfecto security token}`</br>
3. Run gradle tasks: clean build test</br>

### Jenkins CI Dashboard integration:
1. Setup a job in any CI like Jenkins.</br>
2. Create a build task -> Execute shell.</br>
3. Enter the below shell command and run your job :</br>

	`gradle clean build test -PcloudName={Perfecto cloud name only. E.g. demo} -PsecurityToken={Perfecto security token} -PjobName=${JOB_NAME} -PjobNumber=${BUILD_NUMBER}` </br>


#### Note: 

1. Substitute your cloudName and securityToken respectively (without flower brackets)/ pass them as job parameters.</br>

2. ${JOB_NAME} & ${BUILD_NUMBER} are Jenkins internal environment variables which will return the job name and current job number.</br>

3. This [link](https://developers.perfectomobile.com/display/PD/Generate+security+tokens) will showcase how to generate Perfecto security token.</br>

4. Note: Enter the physical location of gradle if it wasnt identified by Jenkins while executing.</br>
