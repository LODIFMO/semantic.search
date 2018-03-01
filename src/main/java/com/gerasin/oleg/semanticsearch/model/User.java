package com.gerasin.oleg.semanticsearch.model;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

/**
 *
 * @author geras
 */
@Entity(User.ENTITY_NAME)
public class User
{
    public static final String ENTITY_NAME = "User";

    public static final String NAME = "name";
    public static final String PASSWORD = "password";

    @Id
    private ObjectId id;
    private String name;
    private String password;

    public User()
    {
    }

    public User(String name, String password)
    {
        this.name = name;
        this.password = password;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }
}