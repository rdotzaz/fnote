package rdotzaz.footnote;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;

import rdotzaz.footnote.adapter.TeamAdapter;
import rdotzaz.footnote.database.DataBase;
import rdotzaz.footnote.database.Player;
import rdotzaz.footnote.database.Team;
import rdotzaz.footnote.dialog.AskDialog;
import rdotzaz.footnote.dialog.ErrorDialog;
import rdotzaz.footnote.dialog.IDialog;

public class MainActivity extends AppCompatActivity
{
    Button addTeam;
    RecyclerView rv;
    TeamAdapter teamAdapter;
    DataBase db;

    ArrayList<Team> teams;

    BottomSheetDialog bottomSheetDialog;
    View bottomSheet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomSheetDialog = new BottomSheetDialog(this,R.style.BottomSheetDialog);
        bottomSheet = LayoutInflater.from(this).inflate(R.layout.team_dialog, (RelativeLayout) findViewById(R.id.team_layout_container));
    }

    @Override
    protected void onResume() {
        super.onResume();

        db = new DataBase(this);
        teams = db.getTeams();

        rv = (RecyclerView) findViewById(R.id.teamsRV_main);
        addTeam = (Button) findViewById(R.id.add_main);

        teamAdapter = new TeamAdapter(this,teams);
        rv.setAdapter(teamAdapter);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setHasFixedSize(true);


        addTeam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogT();
            }
        });
    }

    public void showDialogT(final Integer position)
    {
        final EditText name = (EditText) bottomSheet.findViewById(R.id.team_name_dialog);
        final EditText coach = (EditText) bottomSheet.findViewById(R.id.team_coach_dialog);
        final Button approve = (Button) bottomSheet.findViewById(R.id.approve_team_dialog);

        name.setText(teams.get(position).getName());
        coach.setText(teams.get(position).getCoach());

        approve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ContentValues values = new ContentValues();
                Boolean isChanging = false;
                if(!name.getText().toString().equals(teams.get(position).getName()))
                {
                    values.put(DataBase.TEAM_NAME,name.getText().toString());
                    teams.get(position).setName(name.getText().toString());
                    isChanging = true;
                }
                if(!coach.getText().toString().equals(teams.get(position).getCoach()))
                {
                    values.put(DataBase.TEAM_COACH,coach.getText().toString());
                    teams.get(position).setCoach(coach.getText().toString());
                    isChanging = true;
                }

                if(isChanging)
                {
                    Long id = teams.get(position).getId();
                    db.updateTeam(id,values);
                    teamAdapter.notifyItemChanged(position);
                    rv.requestLayout();
                }
                bottomSheetDialog.dismiss();
            }
        });

        bottomSheetDialog.setContentView(bottomSheet);
        bottomSheetDialog.show();
    }

    public void deleteTeam(final Integer position)
    {
        AskDialog dialog = new AskDialog(
                "Delete team",
                "Do you accept?",
                new IDialog() {
                    @Override
                    public void clickYes() {
                        db.deleteTeam(teams.get(position).getId());
                        for(Player player : db.getPlayers(teams.get(position).getId()))
                        {
                            db.deletePlayer(player.getId());
                        }
                        teams.remove(teams.get(position));
                        teamAdapter.notifyItemRemoved(position);
                        rv.requestLayout();
                        bottomSheetDialog.dismiss();
                    }

                    @Override
                    public void clickNo() {

                    }
                }
        );
        dialog.show(getSupportFragmentManager(),"delete dialog");
    }

    public void showDialogT()
    {
        final EditText name = (EditText) bottomSheet.findViewById(R.id.team_name_dialog);
        final EditText coach = (EditText) bottomSheet.findViewById(R.id.team_coach_dialog);
        final Button approve = (Button) bottomSheet.findViewById(R.id.approve_team_dialog);

        approve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(name.getText().toString().isEmpty() || coach.getText().toString().isEmpty())
                {
                    ErrorDialog dialog = new ErrorDialog(
                            "Error",
                            "Fields cannot be emtpy!"
                    );
                    dialog.show(getSupportFragmentManager(),"error dialog");
                }
                else
                {
                    Team team = new Team(
                            name.getText().toString(),
                            coach.getText().toString()
                    );
                    Long id = db.addTeam(team);
                    team.setId(id);
                    teams.add(team);
                    rv.requestLayout();
                    bottomSheetDialog.dismiss();

                }
            }
        });

        bottomSheetDialog.setContentView(bottomSheet);
        bottomSheetDialog.show();
    }

    public void openTeam(final Long id)
    {
        Intent intent = new Intent(this,TeamActivity.class);
        intent.putExtra("team_id",id);
        startActivity(intent);
    }

}