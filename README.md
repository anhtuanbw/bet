# ngaythobet - Launch Program 4 project

Spring framework with AngularJS implementation version for famous [ngaythobet](http://kmsbet.appspot.com/) web application

## How to Run

### Development mode

    $ mvn spring-boot:run

    $ cd src/main/webui

    $ npm install

    $ bower install

    $ gulp

    # Sonar analyze for Java code (required sonar profile in mvn conf)
    $ mvn org.jacoco:jacoco-maven-plugin:prepare-agent clean package -Dmaven.test.failure.ignore=true sonar:sonar

    # Sonar analyze for Javascript (required build.xml and sonar-ant-task in src\main\webui)
    $ ant

Visit [http://localhost:8888](http://localhost:8888) and enjoy!

### Production mode
    # Go to source folder
    $ cd /opt/ngaythobet2
     
    # Checkout a release tag
    $ git fetch -–tags

    # Save current config change into stash
    $ git stash

    # Checkout the tag build
    $ git checkout tag/ngaythobet-[VERSION]

    # Apply the config change back to the tag
    $ git stash apply

    # Build source code
    $ mvn clean package –Pprod -DskipTests

    # Run application
    $ nohup java -jar target/ngaythobet-[VERSION].jar > /dev/null 2>&1 &

    # Check log server
    $ tail -f logs/ngaythobet.log


Visit [http://localhost:8080](http://localhost:8080) and enjoy!
