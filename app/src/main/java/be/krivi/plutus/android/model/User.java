package be.krivi.plutus.android.model;

import java.util.Date;

/**
 * Created by Jan on 27/11/2015.
 */
public class User{

    private String studentId;
    private String password;

    private String firstName;
    private String lastName;

    private double credit;
    private Date fetchDate;

    public User(){}

    public User( String studentId, String password ){
        setStudentId( studentId );
        setPassword( password );
    }

    public User( String studentId, String password, String firstName, String lastName ) {
        this(studentId, password );
        setFirstName( firstName );
        setLastName( lastName );
        setCredit( credit );
    }

    public User( String studentId, String password, String firstName, String lastName, double credit, Date fetchDate ) {
        this(studentId, password, firstName, lastName );
        setCredit( credit );
        setFetchDate(fetchDate);
    }

    public User( String studentId, String password, double credit, Date fetchDate ){
        this(studentId, password );
        setCredit( credit );
        setFetchDate(fetchDate);
    }

    public double getCredit(){
        return credit;
    }

    public void setFetchDate( Date fetchDate ){
        this.fetchDate = fetchDate;
    }

    public Date getFetchDate(){
        return fetchDate;
    }

    public void setCredit( double credit ){
        this.credit = credit;
    }

    public String getStudentId(){
        return studentId;
    }

    public void setStudentId( String id ){
        this.studentId = id;
    }

    public String getPassword(){
        return password;
    }

    public void setPassword( String password ){
        this.password = password;
    }

    public String getLastName(){
        return lastName;
    }

    public void setLastName( String lastName ){
        this.lastName = lastName;
    }

    public String getFirstName(){
        return firstName;
    }

    public void setFirstName( String firstName ){
        this.firstName = firstName;
    }
}
