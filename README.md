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
    # Checkout a release tag
    $ git fetch --tags
    $ git checkout tag/ngaythobet-[VERSION]

    # Build source code
    $ mvn clean package -Pprod

    # Run application
    $ nohup java -jar target/ngaythobet-[VERSION].jar &

Visit [http://localhost:8080](http://localhost:8080) and enjoy!
