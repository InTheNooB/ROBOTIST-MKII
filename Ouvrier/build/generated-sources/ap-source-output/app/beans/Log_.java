package app.beans;

import app.beans.User;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2021-05-07T14:13:50")
@StaticMetamodel(Log.class)
public class Log_ { 

    public static volatile SingularAttribute<Log, Integer> pkLog;
    public static volatile SingularAttribute<Log, Date> startTime;
    public static volatile SingularAttribute<Log, User> fkUser;
    public static volatile SingularAttribute<Log, Date> endTime;
    public static volatile SingularAttribute<Log, byte[]> picture;

}