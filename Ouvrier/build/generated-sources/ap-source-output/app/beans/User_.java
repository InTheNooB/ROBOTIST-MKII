package app.beans;

import app.beans.Log;
import app.beans.Sequence;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2021-05-07T14:13:50")
@StaticMetamodel(User.class)
public class User_ { 

    public static volatile SingularAttribute<User, String> password;
    public static volatile ListAttribute<User, Log> logList;
    public static volatile SingularAttribute<User, Integer> pkUser;
    public static volatile SingularAttribute<User, String> username;
    public static volatile ListAttribute<User, Sequence> sequenceList;

}