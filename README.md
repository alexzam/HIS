HIS
===

Ok, you found it. "HIS" actually means "Home Information System" It`s kind of beta so download and try if you want, 
but I don`t intend to provide a beautiful project. For now.
So. How to install this piece of soft.

## Installation
If you know what it means: clone then "mvn package" then install to your Tomcat and setup DB connection with context.xml in META-INF.

If you doesn`t... Hmm...
Ok, well, you have to do this:
- Download and install [MySQL Community Server](http://dev.mysql.com/downloads/mysql/).
- Find in your Programs menu "MySQL Command Line Client" and start it.
- Enter root password, you should have remembered it during installation.
- When you see prompt "mysql>" enter this (press enter after each line):

    CREATE DATABASE his;
    CREATE USER his@localhost IDENTIFIED BY 'his';
    GRANT ALL ON his.* TO his@localhost;
    
- Ok, that's enough, close the window.
- Download project package. Version 0.1.5 in [ZIP](https://docs.google.com/file/d/0B1jW-d2yqWkVcU84bkNIWVFHT3c/edit?usp=sharing) or [tar.gz](https://docs.google.com/file/d/0B1jW-d2yqWkVeURva2M1SEF0V1E/edit?usp=sharing)
- Unpack it to where you want it to be unpacked. Do I really need to write this?..
- Don`t run it yet! If you for some reason created DB name or user other than "his", then go and tell application about that. Use file webapp/META-INF/context.xml
- Final step! Open webapp/WEB-INF/classes/import.sql and in first 2 lines change usernames to whatever you want. also you may add more lines like this.
- Ok, now you are allowed to run it. Just run his.cmd and behold huge messy output. If the 3rd line from bottom is "Servlet 'his' configured successfully" then you`re ok.
- Open in browser this: [http://localhost:8080/his](http://localhost:8080/his). Enjoy it if you can.
- Each time you find some bug please report it [here](https://github.com/alexzam/HIS/issues).

