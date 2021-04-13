package edu.uw.exemplar.di;

public class GreeterFactory {
    public static Greeter newGreeter(String message) {
        Greeting greeting = new Greeting();
        greeting.setMessage(message);
        Greeter greeter = new Greeter();
        greeter.setGreeting(greeting);
        return greeter;
    }
    
    public Greeter createGreeter(String message) {
        Greeting greeting = new Greeting();
        greeting.setMessage(message);
        Greeter greeter = new Greeter();
        greeter.setGreeting(greeting);
        return greeter;
    }

}
