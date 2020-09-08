package rdotzaz.footnote.database;

import android.content.ContentValues;

import java.util.ArrayList;

public interface IDatabase
{
    Long addPlayer(Player p);
    void updatePlayer(Long id, ContentValues values);
    void deletePlayer(Long id);
    ArrayList<Player> getPlayers(Long teamID);
    Player getPlayer(Long id);

    Long addTeam(Team t);
    void updateTeam(Long id, ContentValues values);
    void deleteTeam(Long id);
    ArrayList<Team> getTeams();
    Team getTeam(Long id);
}
