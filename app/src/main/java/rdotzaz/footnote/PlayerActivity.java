package rdotzaz.footnote;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import rdotzaz.footnote.database.DataBase;
import rdotzaz.footnote.database.Player;
import rdotzaz.footnote.database.Position;
import rdotzaz.footnote.database.Team;
import rdotzaz.footnote.dialog.ErrorDialog;

public class PlayerActivity extends AppCompatActivity
{
    DataBase db;
    Player player;
    Long id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        if(getIntent().hasExtra("user_id"))
        {
            id = getIntent().getLongExtra("user_id",1);
        }


        db = new DataBase(this);

        final TextView name = findViewById(R.id.name_player);
        final TextView number = findViewById(R.id.number_player);
        final TextView age = findViewById(R.id.year_player);
        final TextView ovr = findViewById(R.id.ovr_player);
        final TextView position = findViewById(R.id.pos_player);
        final TextView height = findViewById(R.id.height_player);

        if(id!=-1)
        {
            player = db.getPlayer(id);

            name.setText(player.getName());
            number.setText(player.getNumber().toString());
            age.setText(player.getAge().toString() + " years");
            ovr.setText(player.getOvr().toString());
            position.setText(Position.arLong[player.getPosition()]);
            height.setText(player.getHeight().toString() + " cm");
        }
        else
        {
            ErrorDialog dialog = new ErrorDialog("Error","Cannot load player");
            dialog.show(getSupportFragmentManager(),"player error");
        }


    }
}