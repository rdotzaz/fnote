package rdotzaz.footnote.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class DataBase implements IDatabase
{
    private final String DB_NAME = "dbase.db";
    private Helper helper;

    //////////////////////////////////////////////////
    //////////////////////////////////////////////////

    public static final String PLAYER_TABLE_NAME = "ps";
    public static final String PLAYER_ID = "id";
    public static final String PLAYER_NAME = "nm";
    public static final String PLAYER_TEAMID = "tID";
    public static final String PLAYER_AGE = "ag";
    public static final String PLAYER_OVR = "ovr";
    public static final String PLAYER_HEIGHT = "hgt";
    public static final String PLAYER_NUMBER = "num";
    public static final String PLAYER_POSITION = "pos";
    public static final String PLAYER_FIELD = "fd";

    public static final String TEAM_TABLE_NAME = "tms";
    public static final String TEAM_ID = "id";
    public static final String TEAM_NAME = "nm";
    public static final String TEAM_COACH = "ch";



    //////////////////////////////////////////////////
    //////////////////////////////////////////////////

    private class Helper extends SQLiteOpenHelper
    {

        public Helper(@Nullable Context context) {
            super(context, DB_NAME, null, 1);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("create table " + PLAYER_TABLE_NAME +
                    " (" + PLAYER_ID + " integer primary key, "
                        + PLAYER_NAME + " string not null, "
                        + PLAYER_TEAMID + " string not null, "
                        + PLAYER_AGE + " integer not null, "
                        + PLAYER_OVR + " integer not null, "
                        + PLAYER_HEIGHT + " integer not null, "
                        + PLAYER_NUMBER + " integer not null, "
                        + PLAYER_POSITION + " integer not null, "
                        + PLAYER_FIELD + " integer not null)");
            db.execSQL("create table " + TEAM_TABLE_NAME +
                    " (" + TEAM_ID + " integer primary key, "
                        + TEAM_NAME + " string not null, "
                        + TEAM_COACH + " string not null)");
        }

        private void deleteDB(SQLiteDatabase db)
        {
            db.execSQL("DROP TABLE IF EXISTS " + PLAYER_TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS " + TEAM_TABLE_NAME);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int i, int l) {
            deleteDB(db);
            onCreate(db);
        }
    }

    public DataBase(Context context)
    {
        if(helper==null) helper = new Helper(context);
    }


    /////////////////////////////////////////////////
    //  Methods /////////////////////////////////////
    /////////////////////////////////////////////////

    @Override
    public Long addPlayer(Player p) {
        Long id;
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(PLAYER_NAME,p.getName());
        values.put(PLAYER_TEAMID,p.getTeamID());
        values.put(PLAYER_AGE,p.getAge());
        values.put(PLAYER_OVR,p.getOvr());
        values.put(PLAYER_HEIGHT,p.getHeight());
        values.put(PLAYER_NUMBER,p.getNumber());
        values.put(PLAYER_POSITION,p.getPosition());
        values.put(PLAYER_FIELD,p.getField());
        id = db.insert(PLAYER_TABLE_NAME, null, values);
        db.close();
        return id;
    }

    @Override
    public void updatePlayer(Long id, ContentValues values) {
        SQLiteDatabase db = helper.getWritableDatabase();
        db.update(PLAYER_TABLE_NAME,values,
                PLAYER_ID + " = ?",new String[]{id.toString()});
        db.close();
    }

    @Override
    public void deletePlayer(Long id) {
        SQLiteDatabase db = helper.getWritableDatabase();
        db.delete(PLAYER_TABLE_NAME,
                PLAYER_ID + " = ?",new String[]{id.toString()});
        db.close();
    }

    @Override
    public ArrayList<Player> getPlayers(Long teamID) {
        ArrayList<Player> players = new ArrayList<>();
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from "
                + PLAYER_TABLE_NAME,null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast())
        {
            Player player = new Player(
                    cursor.getString(cursor.getColumnIndex(PLAYER_NAME)),
                    cursor.getLong(cursor.getColumnIndex(PLAYER_TEAMID)),
                    cursor.getInt(cursor.getColumnIndex(PLAYER_AGE)),
                    cursor.getInt(cursor.getColumnIndex(PLAYER_OVR)),
                    cursor.getInt(cursor.getColumnIndex(PLAYER_HEIGHT)),
                    cursor.getInt(cursor.getColumnIndex(PLAYER_NUMBER)),
                    cursor.getInt(cursor.getColumnIndex(PLAYER_POSITION)),
                    cursor.getInt(cursor.getColumnIndex(PLAYER_FIELD))
            );
            player.setId(cursor.getLong(cursor.getColumnIndex(PLAYER_ID)));
            players.add(player);
            cursor.moveToNext();
        }
        cursor.close();
        db.close();
        return players;
    }

    @Override
    public  Player getPlayer(Long id) {
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from "
                + PLAYER_TABLE_NAME + " where " + PLAYER_ID
                + " = ?",new String[]{id.toString()});
        cursor.moveToFirst();
        Player player = new Player(
                cursor.getString(cursor.getColumnIndex(PLAYER_NAME)),
                cursor.getLong(cursor.getColumnIndex(PLAYER_TEAMID)),
                cursor.getInt(cursor.getColumnIndex(PLAYER_AGE)),
                cursor.getInt(cursor.getColumnIndex(PLAYER_OVR)),
                cursor.getInt(cursor.getColumnIndex(PLAYER_HEIGHT)),
                cursor.getInt(cursor.getColumnIndex(PLAYER_NUMBER)),
                cursor.getInt(cursor.getColumnIndex(PLAYER_POSITION)),
                cursor.getInt(cursor.getColumnIndex(PLAYER_FIELD))
        );
        player.setId(cursor.getLong(cursor.getColumnIndex(PLAYER_ID)));
        cursor.close();
        db.close();
        return player;
    }

    @Override
    public Long addTeam(Team t) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TEAM_NAME,t.getName());
        values.put(TEAM_COACH,t.getCoach());
        Long id = db.insert(TEAM_TABLE_NAME, null, values);
        db.close();
        return id;
    }

    @Override
    public void updateTeam(Long id, ContentValues values) {
        SQLiteDatabase db = helper.getWritableDatabase();
        db.update(TEAM_TABLE_NAME,values,
                TEAM_ID + " = ?",new String[]{id.toString()});
        db.close();
    }

    @Override
    public void deleteTeam(Long id) {
        SQLiteDatabase db = helper.getWritableDatabase();
        db.delete(TEAM_TABLE_NAME,
                TEAM_ID + " = ?",new String[]{id.toString()});
        db.close();
    }

    @Override
    public ArrayList<Team> getTeams() {
        ArrayList<Team> teams = new ArrayList<>();
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from "
                + TEAM_TABLE_NAME,null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast())
        {
            Team team = new Team(
                    cursor.getString(cursor.getColumnIndex(TEAM_NAME)),
                    cursor.getString(cursor.getColumnIndex(TEAM_COACH))
            );
            team.setId(cursor.getLong(cursor.getColumnIndex(TEAM_ID)));
            teams.add(team);
            cursor.moveToNext();
        }
        cursor.close();
        db.close();
        return teams;
    }

    @Override
    public Team getTeam(Long id) {
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from "
                + TEAM_TABLE_NAME + " where " + TEAM_ID
                + " = ?",new String[]{id.toString()});
        cursor.moveToFirst();
        Team team = new Team(
                cursor.getString(cursor.getColumnIndex(TEAM_NAME)),
                cursor.getString(cursor.getColumnIndex(TEAM_COACH))
        );
        team.setId(cursor.getLong(cursor.getColumnIndex(TEAM_ID)));
        cursor.close();
        db.close();
        return team;
    }


}
