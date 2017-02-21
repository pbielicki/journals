1. Prerequisites:
 * Launch ActiveMQ e.g. using Docker: docker run --name='activemq' -it --rm -p 8161:8161 -p 61616:61616 -p 61613:61613 webcenter/activemq:latest
 * Install local SMTP server or use existing one. In src/main/resources/application.properties you can set it up for mailService. Supported options are:
   - java.mail.host - SMTP host (defaults to localhost)
   - java.mail.port - defaults to 25
   - java.mail.username - SMTP user
   - java.mail.password - SMTP password
   
2. Database is initialized automatically created and populated by Hibernate, so there is nothing to do there.
I added email column to user table:

alter table user add column email varchar(255) not null;

Modified data is in data.sql - added email addresses for all users;

3. Nothing special for the source code: mvn spring-boot:run runs the application

4. I added three standard Spring components in com.crossover.trial.journals.config.MailConfig
  * JavaMailSender - used to send raw emails
  * TaskExecutor - used to asynchronously send emails - otherwise it could block until SMTP server replies
  * NotificationManagerImpl - used to send notifications to users for both use cases i.e. after new publication in category for which users are subscribed
    and periodically (once a day at midnight) new publications for last 24 hours. One method in this manager is annotated with @Scheduled(cron = "0 0 * * * *")
    which will be executed by Spring once a day
    
    NotificationManagerImpl will be also executed by JournalServiceImpl.publish method.
    
5. Assumptions:
  * I did not apply any of the fixes suggested in my reviews 
  * I did not fix the project structure - my changes will be adapted to the current status quo
  * I did not fix the DB structure
  * I assumed that no changes in the UI are needed
  * I assumed that SMTP server is available and I will not retry in case it fails - I will only log error
  * I did not internationalize the emails
  * I did not externalize or templatize email messages  
  * I did not create a nice HTML email template
  * Because of lack of time for this assignment I sacrified quality of unit tests by duplicating few lines of code - specifically messages
  
6. Issues completing:
  * Failing unit test
  * 24 hours is very challenging - not enough time to check existing issues and to test my new features
  * Small project but uses a lot of Spring and it took couple of minutes to understand what's going on
  * I'm 100% sure I did not notice all design and code issues
  * Lack of SMTP server for testing
  
7. Feedback
  * Initial database should be much bigger for ease of testing i.e. more users, publishers and journals
  * You could provide SMTP server for testing - I lost quite some time installing my own 

