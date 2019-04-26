package mx.edu.ittepic.themickyebmo.tpdm_u3_practica2maciasurzuadelia;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button platillo,bebida;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        platillo = findViewById(R.id.platillo);
        bebida = findViewById(R.id.bebida);

        platillo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent platillo = new Intent(MainActivity.this,Main2Activity.class);
                startActivity(platillo);
            }
        });
        bebida.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent bebida = new Intent(MainActivity.this,Main3Activity.class);
                bebida.putExtra("tipo",1);
                startActivity(bebida);
            }
        });

    }
}
