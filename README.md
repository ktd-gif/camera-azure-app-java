# camera-azure-app-java

[![CodSpeed](https://img.shields.io/endpoint?url=https://codspeed.io/badge.json)](https://app.codspeed.io/ktd-gif/camera-azure-app-java?utm_source=badge)

The app works like a stand alone camera app on your machine.  
It lets you click pictures and uploads it to pre-configured storage account on Azure. It also lets you list and delete the pictures from the storage account. 
It uses keyvault to hide storage account credentials, and your service principal needs to be given access to keyvault for you to be able to use the app. 

App Design:

![](https://github.com/g2vinay/camera-azure-app-java/blob/master/design.png)

1. App bootstraps and uses your service principal to get storage account details from the key vault. 
2. Camera Starts displaying the Camera UI (Swing UI) 
3. Using the UI pictures are clicked, uploaded, listed or deleted. 
4. The storage credentials fetched in Step 1 are used to perform storage operations in Step 3. 

 
Libraries/Tools used: 
Jackson 
RxJava (will migrate to Reactor) 
Spotbugs 
azure-mgmt-vault 
azure-storage-blob 


Setup Instructions:
a. Configure Following Environment Variables: <br />
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;1. "CAM_VAULT_URL" <br />
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;2. "CAM_CLIENT" <br />
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;3. "CAM_CLIENT_KEY" <br />
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;4. "CAM_TENANT" <br />

b. Get your Service Principal added to camera vault's access policies. <br />
c. Compile and run the app 


Benchmarks:

Performance of the capture path (frame conversion and JPEG encoding) is tracked with
[CodSpeed](https://codspeed.io) using [JMH](https://github.com/openjdk/jmh) benchmarks living in
`benchmarks/`. To run them locally (JDK 21+ required):

```bash
git submodule update --init third-party/codspeed-jvm
git -C third-party/codspeed-jvm submodule update --init --recursive \
  jmh-fork/jmh-core/native-instrument-hooks/instrument-hooks
cd third-party/codspeed-jvm && ./gradlew -p jmh-fork publishToMavenLocal && cd -
mvn -f benchmarks/pom.xml package
java -jar benchmarks/target/benchmarks.jar -gc true
```


ScreenShots: 
