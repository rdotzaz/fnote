package rdotzaz.footnote.database;

public class Player
{
    private Long id;
    private String name;
    private Long teamID;
    private Integer age;
    private Integer ovr;
    private Integer height;
    private Integer number;
    private Integer position;
    private Integer field;

    public Player(String name, Long teamID, Integer age, Integer ovr, Integer height, Integer number, Integer position, Integer field) {
        this.name = name;
        this.teamID = teamID;
        this.age = age;
        this.ovr = ovr;
        this.height = height;
        this.number = number;
        this.position = position;
        this.field=field;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTeamID() {
        return teamID;
    }

    public void setTeamID(Long teamID) {
        this.teamID = teamID;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Integer getOvr() {
        return ovr;
    }

    public void setOvr(Integer ovr) {
        this.ovr = ovr;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public Integer getField() {
        return field;
    }

    public void setField(Integer field) {
        this.field = field;
    }
}
