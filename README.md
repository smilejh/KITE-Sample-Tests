# KITE-Sample-Tests

## Requirements
In order to run the test, you'll need the KITE Engine (KITE 2.0) from webrtc.org's [KITE Github repository](https://github.com/webrtc/KITE/tree/kite-2.0)

Please follow the instructions to configure and compile the Engine.

## Compiling and running tests


### Compiling

#### If you have already follow the instructions for the KITE Engine, you will have:

* `KITE_HOME` is set as the path to where you configured KITE Engine
* The appropriate path (depends on your OS) is included to you `PATH`, so that you will have access to these commands:

	* `c` : compile with maven in your current directory.
	* `r` : run kite test with a config file.
	* `a` : open Allure report from the `kite-allure-reports` folder from your current directory.
	

#### If not, you might need to do that first. But you can still compile the project with:

```
mvn -DskipTests clean install
```

However, this might give you error, since some of the dependencies for this project, which need to be install to Maven local
repository, come from the compilation of the KITE Engine (namely, kite-framework).

### Run KITE-Janus-Test Example

Edit the file `./KITE-Janus-Test/configs/local.janus.config.json` with your favorite text editor.  
You might need to change __`version`__ and __`platform`__ according to what is installed on your local grid (if your grid is running on localhost, it's fine to keep "localhost" as the platform).


To run the Janus test:
```
cd KITE-Janus-Test
r local.janus.config.json
```

Alternatively, you can launch the test with the full command.
On Windows:  
```
-Dkite.firefox.profile="%KITE_HOME%"/third_party/ -cp "%KITE_HOME%/KITE-Engine/target/kite-jar-with-dependencies.jar;target/*" org.webrtc.kite.Engine configs/local.janus.config.json
```
On Linux/Mac:  
```
-Dkite.firefox.profile="$KITE_HOME"/third_party/ -cp "$KITE_HOME/KITE-Engine/target/kite-jar-with-dependencies.jar:target/*" org.webrtc.kite.Engine configs/local.janus.config.json
```

### Open the dashboard (Allure Report)

When the tests is launched using the above script, it will generate Allure reports in the folder `kite-allure-reports`.

After running the test, you can open the Allure dashboard with the command `a`.
```
cd KITE-Janus-Test
a
```

or:
```
allure serve kite-allure-reports\
```

Your default browser should open automatically on the Allure dashboard page.
