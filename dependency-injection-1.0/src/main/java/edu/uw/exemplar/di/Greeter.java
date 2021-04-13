package edu.uw.exemplar.di;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Greeter {
    private Greeting greeting;
    
    public Greeter() {
    }

    public Greeter(final String message) {
        greeting = new Greeting();
        greeting.setMessage(message);
    }

    /**
     * @param greeting the greeting to set
     */
    public void setGreeting(Greeting greeting) {
        this.greeting = greeting;
    }

    public void greet() {
        System.out.println(greeting.getMessage());
    }
    
    public static final void main(final String[] args) {
    	ClassPathXmlApplicationContext beanfactory = new ClassPathXmlApplicationContext("context.xml");
        Greeter greeter = beanfactory.getBean("Welcomer", Greeter.class);
        greeter.greet();
        
        greeter = (Greeter)beanfactory.getBean("Constructed");
        greeter.greet();
        greeter = (Greeter)beanfactory.getBean("Constructed", "Dude");
        greeter.greet();

        greeter = (Greeter)beanfactory.getBean("FactoryDirect");
        greeter.greet();
        greeter = (Greeter)beanfactory.getBean("FactoryDirect", "G'day mate");
        greeter.greet();

        greeter = (Greeter)beanfactory.getBean("FactoryDirect2");
        greeter.greet();
        greeter = (Greeter)beanfactory.getBean("FactoryDirect2", "Hey");
        greeter.greet();

        Collector col = (Collector)beanfactory.getBean("Collector");
        System.out.println(col);
        beanfactory.close();
    }         

}
