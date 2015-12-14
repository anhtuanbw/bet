# ngaythobet - Launch Program 3 project

Spring framework with AngularJS implementation version for famous [ngaythobet](http://kmsbet.appspot.com/) web application

## How to Run

### Development mode

    $ mvn spring-boot:run
    
    $ cd src/main/webui
    
    $ npm install
    
    $ bower install
    
    $ gulp run-dev

Visit [http://localhost:8888](http://localhost:8888) and enjoy!

### Production mode
    
    $ mvn clean package -Pprod
    
    $ java -jar target/ngaythobet-[VERSION].jar
    
Visit [http://localhost:8080](http://localhost:8080) and enjoy!