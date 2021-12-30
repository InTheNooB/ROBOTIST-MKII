package app.beans;

import app.beans.User;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2021-05-07T14:13:50")
@StaticMetamodel(Sequence.class)
public class Sequence_ { 

    public static volatile SingularAttribute<Sequence, byte[]> actionFile;
    public static volatile SingularAttribute<Sequence, Integer> pkSequence;
    public static volatile SingularAttribute<Sequence, User> fkUser;
    public static volatile SingularAttribute<Sequence, String> sequenceName;

}