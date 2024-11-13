package kz.bitlab.portal.exception;

public class LessonNotFoundException extends RuntimeException{

    public LessonNotFoundException(String message){
        super(message);
    }

}
