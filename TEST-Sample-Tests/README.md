#  TEST Module for KITE-Sample-Tests

This is a test modules that enable running all the Sample Tests in one run.

## Compile

Just type `c` (which will execute `mvn clean install -DskipTests`). 
    
    ```
    c
    ```

If you are within a test folder, for example in KITE-Janus-Test, you can type __`c`__ to compile the test module
only or __`c all`__ to recompile the entire project:

    ```
    cd TEST-Sample-Tests 
    c all
    ```  
    


## Run

```
cd TEST-Sample-Tests
r configs\all.config.json
```


## Test output

Each will generate allure report found in `kite-allure-report/` folder.  
After running the test, you can open the Allure dashboard with the command `a`.

```
cd TEST-Sample-Tests
a
```


Alternatively, the full command to launch the Allure dashboard is:  
```
allure serve kite-allure-reports
```
