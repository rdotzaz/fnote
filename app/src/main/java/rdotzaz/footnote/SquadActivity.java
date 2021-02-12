package rdotzaz.footnote;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

import rdotzaz.footnote.adapter.PlayerSquadAdapter;
import rdotzaz.footnote.database.DataBase;
import rdotzaz.footnote.database.Player;
import rdotzaz.footnote.database.Team;
import rdotzaz.footnote.dialog.ErrorDialog;

public class SquadActivity extends AppCompatActivity
{
    DataBase db;
    Team team;
    Long id;
    Integer current, ICurrent=-1;
    ArrayList<Player> players;

    Boolean isChoosed = false;

    private final String[] formations = {
            "4-4-2",
            "4-3-3",
            "3-4-3",
            "4-2-3-1"
    };

    private final int[] kitLayout = {
            R.id.p1_layout,
            R.id.p2_layout,
            R.id.p3_layout,
            R.id.p4_layout,
            R.id.p5_layout,
            R.id.p6_layout,
            R.id.p7_layout,
            R.id.p8_layout,
            R.id.p9_layout,
            R.id.p10_layout,
            R.id.p11_layout
    };

    private final int[] kitNames = {
            R.id.p1_name,
            R.id.p2_name,
            R.id.p3_name,
            R.id.p4_name,
            R.id.p5_name,
            R.id.p6_name,
            R.id.p7_name,
            R.id.p8_name,
            R.id.p9_name,
            R.id.p10_name,
            R.id.p11_name
    };

    private final int[] kitNumber = {
            R.id.p1_num,
            R.id.p2_num,
            R.id.p3_num,
            R.id.p4_num,
            R.id.p5_num,
            R.id.p6_num,
            R.id.p7_num,
            R.id.p8_num,
            R.id.p9_num,
            R.id.p10_num,
            R.id.p11_num
    };


    // 0 - empty
    // 1 - player


    //////////////////////////////////////////////

    TextView teamName;
    Spinner form_spinner;
    FrameLayout pitch_layout;
    RecyclerView playersRV;
    Button delete;
    NestedScrollView scrollView;

    ConstraintLayout[] kits;
    TextView[] kit_names;
    TextView[] kit_number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_squad);

        db = new DataBase(this);
        if(getIntent().hasExtra("team_id"))
        {
            id = getIntent().getLongExtra("team_id",-1);
            setAll();
        }
        else
        {
            ErrorDialog dialog = new ErrorDialog("Error","Cannot find or load team by id");
            dialog.show(getSupportFragmentManager(),"error squad");
        }

    }

    private void setAll()
    {
        scrollView = findViewById(R.id.scroll_squad);
        teamName = findViewById(R.id.team_name_squad);
        form_spinner = findViewById(R.id.formation_spinner);
        pitch_layout = findViewById(R.id.pitch_layout);
        playersRV = findViewById(R.id.playersRV_squad);
        delete = findViewById(R.id.delete_player_squad);
        delete.setVisibility(View.GONE);

        kits = new ConstraintLayout[11];
        kit_names = new TextView[11];
        kit_number = new TextView[11];
        for(int i = 0; i<11; i++)
        {
            kits[i] = findViewById(kitLayout[i]);
            kit_names[i] = findViewById(kitNames[i]);
            kit_number[i] = findViewById(kitNumber[i]);

            final int finalI = i;
            kits[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickKit(finalI);
                }
            });
        }

        form_spinner.setSelection(0);
        current = 0;
        set442();
        form_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                changeFormation(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,formations);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        form_spinner.setAdapter(arrayAdapter);

        players = db.getPlayers(id);
        team = db.getTeam(id);

        playersRV.setLayoutManager(new LinearLayoutManager(this));
        playersRV.setAdapter(new PlayerSquadAdapter(this,players));

        teamName.setText(team.getName());

    }

    private void clickKit(int i)
    {
        if(!isChoosed)
        {
            kits[i].setBackground(ContextCompat.getDrawable(this,R.drawable.button_green));
            ICurrent = i;
            isChoosed = true;
        }
        else
        {
            kits[i].setBackground(null);
            kit_names[i].setText("-");
            kit_number[i].setText("-");
            isChoosed = false;
        }
    }

    private void changeFormation(int i)
    {
        if(current==i) return;
        switch (i)
        {
            case 0:
                set442();
                break;

            case 1:
                set433();
                break;

            case 2:
                set343();
                break;

            case 3:
                set4231();
                break;
        }
        current = i;
    }

    private void set4231()
    {
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) pitch_layout.getLayoutParams();
        layoutParams.height=dpToPx(380);
        pitch_layout.setLayoutParams(layoutParams);

        ConstraintLayout.MarginLayoutParams params = (ConstraintLayout.MarginLayoutParams) kits[0].getLayoutParams();
        params.bottomMargin=dpToPx(15);
        kits[0].setLayoutParams(params);

        ///////////

        params = (ConstraintLayout.MarginLayoutParams) kits[1].getLayoutParams();
        params.bottomMargin=dpToPx(85);
        params.setMarginEnd(dpToPx(135));
        params.setMarginStart(0);
        kits[1].setLayoutParams(params);

        params = (ConstraintLayout.MarginLayoutParams) kits[2].getLayoutParams();
        params.bottomMargin=dpToPx(85);
        params.setMarginStart(dpToPx(135));
        params.setMarginEnd(0);
        kits[2].setLayoutParams(params);

        params = (ConstraintLayout.MarginLayoutParams) kits[3].getLayoutParams();
        params.bottomMargin=dpToPx(85);
        params.setMarginEnd(dpToPx(45));
        params.setMarginStart(0);
        kits[3].setLayoutParams(params);

        params = (ConstraintLayout.MarginLayoutParams) kits[4].getLayoutParams();
        params.bottomMargin=dpToPx(85);
        params.setMarginStart(dpToPx(45));
        params.setMarginEnd(0);
        kits[4].setLayoutParams(params);

        ////////////

        params = (ConstraintLayout.MarginLayoutParams) kits[5].getLayoutParams();
        params.bottomMargin=dpToPx(155);
        params.setMarginEnd(dpToPx(45));
        params.setMarginStart(0);
        kits[5].setLayoutParams(params);

        params = (ConstraintLayout.MarginLayoutParams) kits[6].getLayoutParams();
        params.bottomMargin=dpToPx(155);
        params.setMarginStart(dpToPx(45));
        params.setMarginEnd(0);
        kits[6].setLayoutParams(params);

        //////////////

        params = (ConstraintLayout.MarginLayoutParams) kits[7].getLayoutParams();
        params.bottomMargin=dpToPx(225);
        params.setMarginEnd(dpToPx(100));
        params.setMarginStart(0);
        kits[7].setLayoutParams(params);

        params = (ConstraintLayout.MarginLayoutParams) kits[8].getLayoutParams();
        params.bottomMargin=dpToPx(225);
        params.setMarginStart(0);
        params.setMarginEnd(0);
        kits[8].setLayoutParams(params);

        params = (ConstraintLayout.MarginLayoutParams) kits[9].getLayoutParams();
        params.bottomMargin=dpToPx(225);
        params.setMarginStart(dpToPx(100));
        params.setMarginEnd(0);
        kits[9].setLayoutParams(params);

        ////////////////

        params = (ConstraintLayout.MarginLayoutParams) kits[10].getLayoutParams();
        params.bottomMargin=dpToPx(305);
        params.setMarginStart(0);
        params.setMarginEnd(0);
        kits[10].setLayoutParams(params);
    }

    private void set343()
    {
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) pitch_layout.getLayoutParams();
        layoutParams.height=dpToPx(340);
        pitch_layout.setLayoutParams(layoutParams);

        ConstraintLayout.MarginLayoutParams params = (ConstraintLayout.MarginLayoutParams) kits[0].getLayoutParams();
        params.bottomMargin=dpToPx(20);
        kits[0].setLayoutParams(params);

        /////////////

        params = (ConstraintLayout.MarginLayoutParams) kits[1].getLayoutParams();
        params.bottomMargin=dpToPx(95);
        params.setMarginEnd(dpToPx(100));
        params.setMarginStart(0);
        kits[1].setLayoutParams(params);

        params = (ConstraintLayout.MarginLayoutParams) kits[2].getLayoutParams();
        params.bottomMargin=dpToPx(95);
        params.setMarginStart(0);
        params.setMarginEnd(0);
        kits[2].setLayoutParams(params);

        params = (ConstraintLayout.MarginLayoutParams) kits[3].getLayoutParams();
        params.bottomMargin=dpToPx(95);
        params.setMarginStart(dpToPx(100));
        params.setMarginEnd(0);
        kits[3].setLayoutParams(params);

        /////////////////

        params = (ConstraintLayout.MarginLayoutParams) kits[4].getLayoutParams();
        params.bottomMargin=dpToPx(180);
        params.setMarginStart(dpToPx(135));
        params.setMarginEnd(0);
        kits[4].setLayoutParams(params);

        params = (ConstraintLayout.MarginLayoutParams) kits[5].getLayoutParams();
        params.bottomMargin=dpToPx(180);
        params.setMarginEnd(dpToPx(45));
        params.setMarginStart(0);
        kits[5].setLayoutParams(params);

        params = (ConstraintLayout.MarginLayoutParams) kits[6].getLayoutParams();
        params.bottomMargin=dpToPx(180);
        params.setMarginStart(dpToPx(45));
        params.setMarginEnd(0);
        kits[6].setLayoutParams(params);

        params = (ConstraintLayout.MarginLayoutParams) kits[7].getLayoutParams();
        params.bottomMargin=dpToPx(180);
        params.setMarginEnd(dpToPx(135));
        params.setMarginStart(0);
        kits[7].setLayoutParams(params);

        ///////////////////////

        params = (ConstraintLayout.MarginLayoutParams) kits[8].getLayoutParams();
        params.bottomMargin=dpToPx(270);
        params.setMarginStart(dpToPx(100));
        params.setMarginEnd(0);
        kits[8].setLayoutParams(params);

        params = (ConstraintLayout.MarginLayoutParams) kits[9].getLayoutParams();
        params.bottomMargin=dpToPx(270);
        params.setMarginEnd(dpToPx(100));
        params.setMarginStart(0);
        kits[9].setLayoutParams(params);

        params = (ConstraintLayout.MarginLayoutParams) kits[10].getLayoutParams();
        params.bottomMargin=dpToPx(270);
        params.setMarginStart(0);
        params.setMarginEnd(0);
        kits[10].setLayoutParams(params);

    }

    private void set433()
    {
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) pitch_layout.getLayoutParams();
        layoutParams.height=dpToPx(340);
        pitch_layout.setLayoutParams(layoutParams);

        ConstraintLayout.MarginLayoutParams params = (ConstraintLayout.MarginLayoutParams) kits[0].getLayoutParams();
        params.bottomMargin=dpToPx(20);
        kits[0].setLayoutParams(params);

        params = (ConstraintLayout.MarginLayoutParams) kits[1].getLayoutParams();
        params.bottomMargin=dpToPx(95);
        params.setMarginEnd(dpToPx(135));
        params.setMarginStart(0);
        kits[1].setLayoutParams(params);

        params = (ConstraintLayout.MarginLayoutParams) kits[2].getLayoutParams();
        params.bottomMargin=dpToPx(95);
        params.setMarginStart(dpToPx(135));
        params.setMarginEnd(0);
        kits[2].setLayoutParams(params);

        params = (ConstraintLayout.MarginLayoutParams) kits[3].getLayoutParams();
        params.bottomMargin=dpToPx(95);
        params.setMarginEnd(dpToPx(45));
        params.setMarginStart(0);
        kits[3].setLayoutParams(params);

        params = (ConstraintLayout.MarginLayoutParams) kits[4].getLayoutParams();
        params.bottomMargin=dpToPx(95);
        params.setMarginStart(dpToPx(45));
        params.setMarginEnd(0);
        kits[4].setLayoutParams(params);

        params = (ConstraintLayout.MarginLayoutParams) kits[5].getLayoutParams();
        params.bottomMargin=dpToPx(180);
        params.setMarginEnd(dpToPx(100));
        params.setMarginStart(0);
        kits[5].setLayoutParams(params);

        params = (ConstraintLayout.MarginLayoutParams) kits[6].getLayoutParams();
        params.bottomMargin=dpToPx(180);
        params.setMarginStart(dpToPx(100));
        params.setMarginEnd(0);
        kits[6].setLayoutParams(params);

        params = (ConstraintLayout.MarginLayoutParams) kits[7].getLayoutParams();
        params.bottomMargin=dpToPx(180);
        params.setMarginEnd(0);
        params.setMarginStart(0);
        kits[7].setLayoutParams(params);

        params = (ConstraintLayout.MarginLayoutParams) kits[8].getLayoutParams();
        params.bottomMargin=dpToPx(270);
        params.setMarginStart(dpToPx(100));
        params.setMarginEnd(0);
        kits[8].setLayoutParams(params);

        params = (ConstraintLayout.MarginLayoutParams) kits[9].getLayoutParams();
        params.bottomMargin=dpToPx(270);
        params.setMarginEnd(dpToPx(100));
        params.setMarginStart(0);
        kits[9].setLayoutParams(params);

        params = (ConstraintLayout.MarginLayoutParams) kits[10].getLayoutParams();
        params.bottomMargin=dpToPx(270);
        params.setMarginStart(0);
        params.setMarginEnd(0);
        kits[10].setLayoutParams(params);
    }

    private void set442()
    {
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) pitch_layout.getLayoutParams();
        layoutParams.height=dpToPx(340);
        pitch_layout.setLayoutParams(layoutParams);

        ConstraintLayout.MarginLayoutParams params = (ConstraintLayout.MarginLayoutParams) kits[0].getLayoutParams();
        params.bottomMargin=dpToPx(20);
        kits[0].setLayoutParams(params);

        params = (ConstraintLayout.MarginLayoutParams) kits[1].getLayoutParams();
        params.bottomMargin=dpToPx(95);
        params.setMarginEnd(dpToPx(135));
        params.setMarginStart(0);
        kits[1].setLayoutParams(params);

        params = (ConstraintLayout.MarginLayoutParams) kits[2].getLayoutParams();
        params.bottomMargin=dpToPx(95);
        params.setMarginStart(dpToPx(135));
        params.setMarginEnd(0);
        kits[2].setLayoutParams(params);

        params = (ConstraintLayout.MarginLayoutParams) kits[3].getLayoutParams();
        params.bottomMargin=dpToPx(95);
        params.setMarginEnd(dpToPx(45));
        params.setMarginStart(0);
        kits[3].setLayoutParams(params);

        params = (ConstraintLayout.MarginLayoutParams) kits[4].getLayoutParams();
        params.bottomMargin=dpToPx(95);
        params.setMarginStart(dpToPx(45));
        params.setMarginEnd(0);
        kits[4].setLayoutParams(params);

        params = (ConstraintLayout.MarginLayoutParams) kits[5].getLayoutParams();
        params.bottomMargin=dpToPx(180);
        params.setMarginEnd(dpToPx(135));
        params.setMarginStart(0);
        kits[5].setLayoutParams(params);

        params = (ConstraintLayout.MarginLayoutParams) kits[6].getLayoutParams();
        params.bottomMargin=dpToPx(180);
        params.setMarginStart(dpToPx(135));
        params.setMarginEnd(0);
        kits[6].setLayoutParams(params);

        params = (ConstraintLayout.MarginLayoutParams) kits[7].getLayoutParams();
        params.bottomMargin=dpToPx(180);
        params.setMarginEnd(dpToPx(45));
        params.setMarginStart(0);
        kits[7].setLayoutParams(params);

        params = (ConstraintLayout.MarginLayoutParams) kits[8].getLayoutParams();
        params.bottomMargin=dpToPx(180);
        params.setMarginStart(dpToPx(45));
        params.setMarginEnd(0);
        kits[8].setLayoutParams(params);

        params = (ConstraintLayout.MarginLayoutParams) kits[9].getLayoutParams();
        params.bottomMargin=dpToPx(270);
        params.setMarginEnd(dpToPx(80));
        params.setMarginStart(0);
        kits[9].setLayoutParams(params);

        params = (ConstraintLayout.MarginLayoutParams) kits[10].getLayoutParams();
        params.bottomMargin=dpToPx(270);
        params.setMarginStart(dpToPx(80));
        params.setMarginEnd(0);
        kits[10].setLayoutParams(params);
    }


    private int dpToPx(int dp) {
        float density = getResources().getDisplayMetrics().density;
        return Math.round((float) dp * density);
    }


    public void clickPlayerCard(final Player player)
    {
        if(ICurrent==-1) return;
        if(isChoosed)
        {
            kits[ICurrent].setBackground(null);
            kit_names[ICurrent].setText(player.getName());
            kit_number[ICurrent].setText(player.getNumber().toString());
            isChoosed = false;
            scrollView.scrollTo(0,0);
        }
    }

}