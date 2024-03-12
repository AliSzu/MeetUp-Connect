package meetup.connect.common.exception;

import lombok.Getter;

@Getter
public class MeetUpException extends RuntimeException{
    private MeetUpError meetUpError;
    public MeetUpException() {
        super();
    }

    public MeetUpException(MeetUpError meetUpError) {
        this.meetUpError = meetUpError;
    }
}
