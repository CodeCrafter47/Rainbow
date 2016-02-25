Changes in 1.9 by CodeCrafter47:

 1. plugins may include a plugin.properties file in their jar:
    ```
    # set a different main class from the default, prevents issues if users rename the file
    main: myPackage.MyPlugin
    ```