package edu.uw.exemplar.di;

public class Greeting {
    private String message;

   /**
    * @return the message
    */
   public String getMessage() {
       return message;
   }

   /**
    * @param messgae the message to set
    */
   public void setMessage(final String message) {
       this.message = message;
   }

   public String toString() {
       return "Greeter - message: " + message;
   }
}
