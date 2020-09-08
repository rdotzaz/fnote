package rdotzaz.footnote.database;

public class Team
{
    private Long id;
    private String name;
    private String coach;

    public Team(String name, String coach) {
        this.name = name;
        this.coach = coach;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCoach() {
        return coach;
    }

    public void setCoach(String coach) {
        this.coach = coach;
    }
}
