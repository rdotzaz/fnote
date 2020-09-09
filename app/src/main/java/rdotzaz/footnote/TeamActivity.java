package rdotzaz.footnote;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import rdotzaz.footnote.adapter.PlayerAdapter;
import rdotzaz.footnote.database.DataBase;
import rdotzaz.footnote.database.Player;
import rdotzaz.footnote.database.Position;
import rdotzaz.footnote.database.Team;
import rdotzaz.footnote.dialog.AskDialog;
import rdotzaz.footnote.dialog.ErrorDialog;
import rdotzaz.footnote.dialog.IDialog;

public class TeamActivity extends AppCompatActivity
{
    TextView nameTeam;
    Button getOther, sortButton;
    TextView coach;

    Button gk_add, df_add, mf_add, st_add;
    RecyclerView[] RVs;
    PlayerAdapter[] playerAdapter;

    Team team;
    Long teamID;
    DataBase db;

    BottomSheetDialog bottomSheetDialog;
    View bottomSheet;
    BottomSheetDialog bottomSheetOtherDialog;
    View bottomSheetOther;

    Integer pos;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team);

        db = new DataBase(this);

        if(getIntent().hasExtra("team_id"))
        {
            teamID = getIntent().getLongExtra("team_id",-1);
        }

        if(teamID!=-1)
        {
            team = db.getTeam(teamID);
            setAll();
        }
        else
        {
            ErrorDialog dialog = new ErrorDialog("Error","Failed to load team");
            dialog.show(getSupportFragmentManager(),"error team");
        }
    }

    private void setAll()
    {
        nameTeam = findViewById(R.id.name_team);
        getOther = findViewById(R.id.getother_main);
        coach = findViewById(R.id.coach_team);
        sortButton = findViewById(R.id.sort_main);

        gk_add = findViewById(R.id.gk_add);
        df_add = findViewById(R.id.df_add);
        mf_add = findViewById(R.id.mf_add);
        st_add = findViewById(R.id.st_add);

        RVs = new RecyclerView[4];
        RVs[0] = findViewById(R.id.gkRV);
        RVs[1] = findViewById(R.id.dfRV);
        RVs[2] = findViewById(R.id.mfRV);
        RVs[3] = findViewById(R.id.stRV);

        playerAdapter = new PlayerAdapter[4];
        for(int i = 0; i < 4; i++)
        {
            playerAdapter[i] = new PlayerAdapter(this,db.getPlayers(teamID),i);
            RVs[i].setAdapter(playerAdapter[i]);
            RVs[i].setLayoutManager(new LinearLayoutManager(this));
            RVs[i].setHasFixedSize(true);
        }


        bottomSheetDialog = new BottomSheetDialog(this,R.style.BottomSheetDialog);
        bottomSheet = LayoutInflater.from(this).inflate(R.layout.player_dialog, (RelativeLayout) findViewById(R.id.player_layout_container));

        bottomSheetOtherDialog = new BottomSheetDialog(this,R.style.BottomSheetDialog);
        bottomSheetOther = LayoutInflater.from(this).inflate(R.layout.get_dialog, (RelativeLayout) findViewById(R.id.getdialog_layout));

        /////////////////////////////////////////////

        nameTeam.setText(team.getName());
        coach.setText(team.getCoach());

        gk_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogPadd(0);
            }
        });

        df_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogPadd(1);
            }
        });

        mf_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogPadd(2);
            }
        });

        st_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogPadd(3);
            }
        });

        sortButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for(int i = 0 ; i< 4; i++)
                {
                    sortByNumber(playerAdapter[i].getPlayers());
                    playerAdapter[i].notifyDataSetChanged();
                }
            }
        });

        getOther.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showOtherDialog();
            }
        });

    }

    private void showOtherDialog()
    {
        final Spinner spinner = bottomSheetOther.findViewById(R.id.team_spinner_getdialog);
        final Button approve = bottomSheetOther.findViewById(R.id.approve_getdialog);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        final ArrayList<Team> teams = db.getTeams();
        String[] teamNames = getTeamNames(teams);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,teamNames);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);
        spinner.setSelection(0);

        approve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Long teamOtherID = teams.get(spinner.getSelectedItemPosition()).getId();

                ArrayList<Player> players = db.getPlayers(teamOtherID);
                for(Player p : players)
                {
                    Player newP = new Player(
                            p.getName(),
                            teamID,
                            p.getAge(),
                            p.getOvr(),
                            p.getHeight(),
                            p.getNumber(),
                            p.getPosition(),
                            p.getField()
                    );
                    Long idP = db.addPlayer(newP);
                    newP.setId(idP);
                    playerAdapter[p.getField()].getPlayers().add(newP);
                }
                for(int i = 0; i< 4; i++) RVs[i].requestLayout();
                bottomSheetOtherDialog.dismiss();
            }
        });
        bottomSheetOtherDialog.setContentView(bottomSheetOther);
        bottomSheetOtherDialog.show();
    }

    private String[] getTeamNames(ArrayList<Team> teams)
    {
        String[] tab = new String[teams.size()];
        for(int i = 0; i < teams.size(); i++)
        {
            tab[i] = teams.get(i).getName();
        }
        return tab;
    }

    public void showDialogP(final Player player, final Integer position)
    {
        final EditText name = bottomSheet.findViewById(R.id.player_name_dialog);
        final Spinner positionSpinner = bottomSheet.findViewById(R.id.position_dialog);
        final EditText age = bottomSheet.findViewById(R.id.age_player_dialog);
        final EditText ovr = bottomSheet.findViewById(R.id.ovr_player_dialog);
        final EditText height = bottomSheet.findViewById(R.id.height_player_dialog);
        final EditText number = bottomSheet.findViewById(R.id.number_dialog);
        final Button approve = bottomSheet.findViewById(R.id.button_player_dialog);
        final Button delete = bottomSheet.findViewById(R.id.delete_player_dialog);

        delete.setVisibility(View.VISIBLE);


        name.setText(player.getName());
        age.setText(player.getAge().toString());
        ovr.setText(player.getOvr().toString());
        height.setText(player.getHeight().toString());
        number.setText(player.getNumber().toString());

        positionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                pos = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,Position.arShort);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        positionSpinner.setAdapter(dataAdapter);
        positionSpinner.setSelection(player.getPosition());

        approve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(name.getText().toString().isEmpty() || age.getText().toString().isEmpty() || ovr.getText().toString().isEmpty() ||
                        height.getText().toString().isEmpty() || number.getText().toString().isEmpty())
                {
                    ErrorDialog dialog = new ErrorDialog("Error", "Fields cannot be empty");
                    dialog.show(getSupportFragmentManager(),"error player");
                }
                else
                {
                    try {
                        Integer ageNum = Integer.parseInt(age.getText().toString());
                        Integer ovrNum = Integer.parseInt(ovr.getText().toString());
                        Integer heightNum = Integer.parseInt(height.getText().toString());
                        Integer numberNum = Integer.parseInt(number.getText().toString());

                        if(ageNum < 15 || ageNum > 50) throw new IllegalArgumentException("Failed to read age");
                        if(ovrNum < 40 || ovrNum > 101) throw new IllegalArgumentException("Failed to read ovr");
                        if(heightNum < 150 || heightNum > 210) throw new IllegalArgumentException("Failed to read height");
                        if(numberNum < 1 || numberNum > 99) throw new IllegalArgumentException("Failed to read number");

                        ////////////////////

                        Integer field = getFieldBySelected(positionSpinner.getSelectedItemPosition());

                        ContentValues values = new ContentValues();
                        Boolean isChanging = false;

                        if(!name.getText().toString().equals(player.getName()))
                        {
                            values.put(DataBase.PLAYER_NAME,name.getText().toString());
                            player.setName(name.getText().toString());
                            isChanging = true;
                        }

                        if(!age.getText().toString().equals(player.getAge()))
                        {
                            values.put(DataBase.PLAYER_AGE,ageNum);
                            player.setAge(ageNum);
                            isChanging = true;
                        }

                        if(!ovr.getText().toString().equals(player.getOvr()))
                        {
                            values.put(DataBase.PLAYER_OVR,ovrNum);
                            player.setOvr(ovrNum);
                            isChanging = true;
                        }

                        if(!height.getText().toString().equals(player.getHeight()))
                        {
                            values.put(DataBase.PLAYER_HEIGHT,heightNum);
                            player.setHeight(heightNum);
                            isChanging = true;
                        }

                        if(!number.getText().toString().equals(player.getNumber()))
                        {
                            values.put(DataBase.PLAYER_NUMBER,numberNum);
                            player.setNumber(numberNum);
                            isChanging = true;
                        }

                        if(!field.equals(player.getField()))
                        {
                            playerAdapter[player.getField()].notifyItemRemoved(position);
                            playerAdapter[player.getField()].getPlayers().remove(position);
                            RVs[player.getField()].requestLayout();

                            playerAdapter[field].getPlayers().add(player);
                            playerAdapter[field].notifyDataSetChanged();
                            RVs[field].requestLayout();

                            values.put(DataBase.PLAYER_POSITION,positionSpinner.getSelectedItemPosition());
                            values.put(DataBase.PLAYER_FIELD,field);

                            player.setPosition(positionSpinner.getSelectedItemPosition());
                            player.setField(field);
                            isChanging = true;

                        }
                        else if(positionSpinner.getSelectedItemPosition()!=player.getPosition())
                        {
                            values.put(DataBase.PLAYER_POSITION,positionSpinner.getSelectedItemPosition());
                            player.setPosition(positionSpinner.getSelectedItemPosition());
                            isChanging = true;

                        }

                        if(isChanging)
                        {
                            db.updatePlayer(player.getId(),values);
                            playerAdapter[field].notifyItemChanged(position);
                            //sortByNumber(playerAdapter[field].getPlayers());
                            RVs[field].requestLayout();
                        }

                        bottomSheetDialog.dismiss();

                    }
                    catch (NumberFormatException e)
                    {
                        ErrorDialog dialog = new ErrorDialog("Error", "Cannot convert fields to number");
                        dialog.show(getSupportFragmentManager(),"error numbers");
                        return;
                    }
                    catch (IllegalArgumentException e)
                    {
                        ErrorDialog dialog = new ErrorDialog("Error", e.getMessage());
                        dialog.show(getSupportFragmentManager(),"error logic player");
                        return;
                    }


                }
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AskDialog dialog = new AskDialog(
                        "Delete player",
                        "Do you accept?",
                        new IDialog() {
                            @Override
                            public void clickYes() {
                                db.deletePlayer(player.getId());
                                playerAdapter[player.getField()].notifyItemRemoved(position);
                                playerAdapter[player.getField()].getPlayers().remove(player);
                                RVs[player.getField()].requestLayout();
                                bottomSheetDialog.dismiss();

                            }

                            @Override
                            public void clickNo() {

                            }
                        }
                );
                dialog.show(getSupportFragmentManager(),"delete player");
            }
        });
        bottomSheetDialog.setContentView(bottomSheet);
        bottomSheetDialog.show();

    }

    private void sortByNumber(ArrayList<Player> players)
    {
        Collections.sort(players, new Comparator<Player>() {
            @Override
            public int compare(Player player, Player player1) {
                return player.getNumber().compareTo(player1.getNumber());
            }
        });
    }

    public void showDialogPadd(final Integer field)
    {
        final EditText name = bottomSheet.findViewById(R.id.player_name_dialog);
        final Spinner positionSpinner = bottomSheet.findViewById(R.id.position_dialog);
        final EditText age = bottomSheet.findViewById(R.id.age_player_dialog);
        final EditText ovr = bottomSheet.findViewById(R.id.ovr_player_dialog);
        final EditText height = bottomSheet.findViewById(R.id.height_player_dialog);
        final EditText number = bottomSheet.findViewById(R.id.number_dialog);
        final Button approve = bottomSheet.findViewById(R.id.button_player_dialog);
        final Button delete = bottomSheet.findViewById(R.id.delete_player_dialog);

        delete.setVisibility(View.GONE);

        positionSpinner.setSelection(getSinglePosition(field));
        positionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                pos = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,getPositions(field));
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        positionSpinner.setAdapter(dataAdapter);

        approve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(name.getText().toString().trim().isEmpty() || age.getText().toString().trim().isEmpty() || ovr.getText().toString().trim().isEmpty() ||
                    height.getText().toString().trim().isEmpty() || number.getText().toString().trim().isEmpty())
                {
                    ErrorDialog dialog = new ErrorDialog("Error", "Fields cannot be empty");
                    dialog.show(getSupportFragmentManager(),"error player");
                }
                else
                {
                    try {
                        Integer ageNum = Integer.parseInt(age.getText().toString());
                        Integer ovrNum = Integer.parseInt(ovr.getText().toString());
                        Integer heightNum = Integer.parseInt(height.getText().toString());
                        Integer numberNum = Integer.parseInt(number.getText().toString());

                        if(ageNum < 15 || ageNum > 50) throw new IllegalArgumentException("Failed to read age");
                        if(ovrNum < 40 || ovrNum > 101) throw new IllegalArgumentException("Failed to read ovr");
                        if(heightNum < 150 || heightNum > 210) throw new IllegalArgumentException("Failed to read height");
                        if(numberNum < 1 || numberNum > 99) throw new IllegalArgumentException("Failed to read number");

                        Player player = new Player(
                                name.getText().toString(),
                                teamID,
                                ageNum,
                                ovrNum,
                                heightNum,
                                numberNum,
                                getSinglePosition(field)+positionSpinner.getSelectedItemPosition(),
                                field
                        );
                        Long playerID = db.addPlayer(player);
                        player.setId(playerID);

                        playerAdapter[field].getPlayers().add(player);
                        //sortByNumber(playerAdapter[field].getPlayers());
                        RVs[field].requestLayout();
                        bottomSheetDialog.dismiss();

                    }
                    catch (NumberFormatException e)
                    {
                        ErrorDialog dialog = new ErrorDialog("Error", "Cannot convert fields to number");
                        dialog.show(getSupportFragmentManager(),"error numbers");
                        return;
                    }
                    catch (IllegalArgumentException e)
                    {
                        ErrorDialog dialog = new ErrorDialog("Error", e.getMessage());
                        dialog.show(getSupportFragmentManager(),"error logic player");
                        return;
                    }


                }
            }
        });
        bottomSheetDialog.setContentView(bottomSheet);
        bottomSheetDialog.show();
    }


    private String[] getPositions(final Integer field)
    {
        String[] positions = null;
        switch (field)
        {
            case 0:
                positions = new String[1];
                positions[0] = Position.arShort[0];
                break;

            case 1:
                positions = new String[3];
                for(int i = 1; i<= 3; i++) positions[i-1]=Position.arShort[i];
                break;

            case 2:
                positions = new String[5];
                for(int i = 4;i<=8; i++) positions[i-4] = Position.arShort[i];
                break;

            case 3:
                positions = new String[4];
                for(int i = 9;i<=12;i++) positions[i-9] = Position.arShort[i];
                break;
        }
        return positions;
    }

    private Integer getSinglePosition(final Integer field)
    {
        Integer position = 0;
        switch (field)
        {
            case 1:
                position  = 1;
                break;

            case 2:
                position = 4;
                break;

            case 3:
                position = 9;
                break;
        }
        return position;
    }

    private Integer getFieldBySelected(final Integer selected)
    {
        if(selected==0) return 0;
        else if(selected>=1 && selected<=3) return 1;
        else if(selected>=4 && selected<=8) return 2;
        else return 3;
    }

    public void openPlayer(final Long userID, final Long teamID)
    {
        Intent intent = new Intent(this,PlayerActivity.class);
        intent.putExtra("user_id",userID);
        intent.putExtra("team_id",teamID);
        startActivity(intent);
    }

}